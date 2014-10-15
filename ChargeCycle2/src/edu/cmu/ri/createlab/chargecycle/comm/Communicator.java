package edu.cmu.ri.createlab.chargecycle.comm;

/*
 Adapted from code from http://henrypoon.wordpress.com/
 */

import edu.cmu.ri.createlab.chargecycle.logging.EventLogger;
import edu.cmu.ri.createlab.chargecycle.model.State;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

public class Communicator implements SerialPortEventListener {
	private final EventLogger logger;
	private final State state;

	// for containing the ports that will be found
	private Enumeration ports = null;

	// map the port names to CommPortIdentifiers
	// private List<CommPortIdentifier> portList = new
	// ArrayList<CommPortIdentifier>();

	// this is the object that contains the opened port
	private CommPortIdentifier selectedPortIdentifier = null;
	private SerialPort serialPort = null;
	private String portName = null;

	// input and output streams for sending and receiving data
	private InputStream input = null;
	private OutputStream output = null;

	private boolean connected = false;

	// the timeout value for connecting with the port
	final static int TIMEOUT = 2000;

	// some ascii values for for certain things
	final static int SPACE_ASCII = 32;
	final static int DASH_ASCII = 45;
	final static int NEW_LINE_ASCII = 10;

	// a string for recording what goes on in the program
	// this string is written to the GUI
	String logText = "";

	public Communicator(EventLogger logger, State state) {
		this.logger = logger;
		this.state = state;
	}

	// search for all the serial ports
	// pre: none
	// post: adds all the found ports to a combo box on the GUI
	public List<CommPortIdentifier> searchForPorts() {
		List<CommPortIdentifier> portList = new ArrayList<CommPortIdentifier>();

		this.ports = CommPortIdentifier.getPortIdentifiers();

		while (this.ports.hasMoreElements()) {
			CommPortIdentifier curPort = (CommPortIdentifier) this.ports.nextElement();

			// get only serial ports
			if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL && curPort.getName().contains("Bluetooth") == false) {
				// window.cboxPorts.addItem(curPort.getName());
				// portMap.put(curPort.getName(), curPort);
				portList.add(curPort);
			}
		}

		return portList;
	}

	// connect to the selected port in the combo box
	// pre: ports are already found by using the searchForPorts method
	// post: the connected comm port is stored in commPort, otherwise,
	// an exception is generated
	public boolean connect(CommPortIdentifier port) {
		this.selectedPortIdentifier = port;
		CommPort commPort = null;
		this.portName = this.selectedPortIdentifier.getName();

		try {
			// the method below returns an object of type CommPort
			commPort = this.selectedPortIdentifier.open("ChargeCycleCommunicator", TIMEOUT);
			// the CommPort object can be casted to a SerialPort object
			this.serialPort = (SerialPort) commPort;

			this.setConnected(true);

			// logging
			this.logText = this.portName + " opened successfully.";
			this.logger.logEvent(this.logText);

			this.serialPort.setSerialPortParams(230400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// enables the controls on the GUI if a successful connection is
			// made
			// window.keybindingController.toggleControls();
			return true;

		} catch (PortInUseException e) {
			this.logText = this.portName + " is in use.";
			this.logger.logEvent(this.logText);
			this.logger.logException(e);

		} catch (Exception e) {
			this.logText = "Failed to open " + this.portName;
			this.logger.logEvent(this.logText);
			this.logger.logException(e);
		}
		return false;
	}

	// open the input and output streams
	// pre: an open port
	// post: initialized input and output streams for use to communicate data
	public boolean initIOStream() {
		// return value for whether opening the streams is successful or not
		try {
			//
			this.input = this.serialPort.getInputStream();
			this.output = this.serialPort.getOutputStream();
			return true;
		} catch (IOException e) {
			this.logText = "I/O Streams failed to open.";
			this.logger.logEvent(this.logText);
			this.logger.logException(e);
			return false;
		}
	}

	// starts the event listener that knows whenever data is available to be
	// read
	// pre: an open serial port
	// post: an event listener for the serial port that knows when data is
	// recieved
	public void initListener() {
		try {
			this.serialPort.addEventListener(this);
			this.serialPort.notifyOnDataAvailable(true);
		} catch (TooManyListenersException e) {
			this.logText = "Too many listeners.";
			this.logger.logEvent(this.logText);
			this.logger.logException(e);
		}
	}

	// disconnect the serial port
	public void disconnect() {
		// close the serial port
		try {
			this.serialPort.removeEventListener();
			this.serialPort.close();
			this.input.close();
			this.output.close();
			this.setConnected(false);
			// window.keybindingController.toggleControls();

			this.logText = "Disconnected.";
			this.logger.logEvent(this.logText);
		} catch (Exception e) {
			this.logText = "Failed to close " + this.portName;
			this.logger.logEvent(this.logText);
			this.logger.logException(e);
		}
	}

	final public boolean getConnected() {
		return this.connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	// what happens when data is received
	// pre: serial event is triggered
	// post: processing on the data it reads
	public String readData() throws IOException {
		int dataLength = this.input.available();
		byte[] data = new byte[dataLength];
		this.input.read(data);
		String msg = new String(data);
		// logger.logEvent("R: "+msg);
		return msg;

	}

	// method that can be called to send data
	// pre: open serial port
	// post: data sent to the other device
	public void writeData(String msg) {
		try {
			this.output.write(msg.getBytes());
			this.output.flush();
		} catch (Exception e) {
			this.logText = "Failed to write data.";
			this.logger.logEvent(this.logText);
			this.logger.logException(e);
		}
	}

	private String inputBuffer;

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		if (arg0.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			// data came in, put into buffer
			// parse rolling buffer for complete sentences
			// if complete sentence, update state and remove
			// all prior data from buffer;
			try {
				this.inputBuffer = this.inputBuffer + this.readData();
			} catch (IOException x) {
				this.logger.logEvent("Problem reading data from bike, disconnecting");
				this.logger.logException(x);
				this.disconnect();
			}
			int sentenceEndIndex = this.inputBuffer.lastIndexOf("\r\n");
			if (sentenceEndIndex == -1) {
				return;
			}
			int sentenceBeginIndex = this.inputBuffer.lastIndexOf("!!!", sentenceEndIndex);
			if (sentenceBeginIndex == -1) {
				return;
			}

			String inputSentence = this.inputBuffer.substring(sentenceBeginIndex, sentenceEndIndex);
			this.inputBuffer = this.inputBuffer.substring(sentenceEndIndex);

			try {
				this.state.setVehicleState(CommParser.Parse(Calendar.getInstance(), inputSentence));
				// logger.logEvent("S: "+inputSentence);
			} catch (ParseException e) {
				this.logger.logEvent("Parsing of sentence failed");
				this.logger.logException(e);
			}
		}

	}
}
