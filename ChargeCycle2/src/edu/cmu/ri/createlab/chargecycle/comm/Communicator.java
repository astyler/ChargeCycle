package edu.cmu.ri.createlab.chargecycle.comm;

/*
Adapted from code from http://henrypoon.wordpress.com/
 */

import edi.cmu.ri.createlab.chargecycle.logging.Logger;
import edu.cmu.ri.createlab.chargecycle.model.State;
import gnu.io.*;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.TooManyListenersException;

public class Communicator implements SerialPortEventListener
{
	private final Logger logger;
	private final State state;
	
    //for containing the ports that will be found
    private Enumeration ports = null;
    
    //map the port names to CommPortIdentifiers
   // private List<CommPortIdentifier> portList = new ArrayList<CommPortIdentifier>();

    //this is the object that contains the opened port
    private CommPortIdentifier selectedPortIdentifier = null;
    private SerialPort serialPort = null;
    private String portName = null;

    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;

    //just a boolean flag that i use for enabling
    //and disabling buttons depending on whether the program
    //is connected to a serial port or not
    private boolean bConnected = false;

    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    //some ascii values for for certain things
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;

    //a string for recording what goes on in the program
    //this string is written to the GUI
    String logText = "";

    public Communicator(Logger logger, State state){
    	this.logger = logger;
    	this.state = state;
    }

    //search for all the serial ports
    //pre: none
    //post: adds all the found ports to a combo box on the GUI
    public List<CommPortIdentifier> searchForPorts()
    {
    	List<CommPortIdentifier> portList = new ArrayList<CommPortIdentifier>();
    	
        ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements())
        {
            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

            //get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
//                window.cboxPorts.addItem(curPort.getName());
                //portMap.put(curPort.getName(), curPort);
            	portList.add(curPort);
            }
        }
        
        return portList;
    }

    //connect to the selected port in the combo box
    //pre: ports are already found by using the searchForPorts method
    //post: the connected comm port is stored in commPort, otherwise,
    //an exception is generated
    public boolean connect(CommPortIdentifier port)
    {
    	this.selectedPortIdentifier = port;
        CommPort commPort = null;
        this.portName = this.selectedPortIdentifier.getName();
        
        try
        {
             //the method below returns an object of type CommPort
            commPort = selectedPortIdentifier.open("ChargeCycleCommunicator", TIMEOUT);
            //the CommPort object can be casted to a SerialPort object
            serialPort = (SerialPort)commPort;

            //for controlling GUI elements
 //           setConnected(true);
           
            //logging
            logText = portName + " opened successfully.";
            logger.logEvent(logText);

            serialPort.setSerialPortParams(230400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            //enables the controls on the GUI if a successful connection is made
//            window.keybindingController.toggleControls();
            return true;
            
        }
        catch (PortInUseException e)
        {
            logText = portName + " is in use. (" + e.toString() + ")";
            logger.logEvent(logText);
            logger.logException(e);
           
        }
        catch (Exception e)
        {
            logText = "Failed to open " + portName + "(" + e.toString() + ")";
            logger.logEvent(logText);
            logger.logException(e);
        }
        return false;
    }

    //open the input and output streams
    //pre: an open port
    //post: initialized input and output streams for use to communicate data
    public boolean initIOStream()
    {
        //return value for whether opening the streams is successful or not
        boolean successful = false;

        try {
            //
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
            writeData(0, 0);
            
            successful = true;
            return successful;
        }
        catch (IOException e) {
            logText = "I/O Streams failed to open. (" + e.toString() + ")";
            logger.logEvent(logText);
            logger.logException(e);
            return successful;
        }
    }

    //starts the event listener that knows whenever data is available to be read
    //pre: an open serial port
    //post: an event listener for the serial port that knows when data is recieved
    public void initListener(SerialPortEventListener listener)
    {
        try
        {
            serialPort.addEventListener(listener);
            serialPort.notifyOnDataAvailable(true);
        }
        catch (TooManyListenersException e)
        {
            logText = "Too many listeners. (" + e.toString() + ")";
            logger.logEvent(logText);
            logger.logException(e);
        }
    }

    //disconnect the serial port
    //pre: an open serial port
    //post: clsoed serial port
    public void disconnect()
    {
        //close the serial port
        try
        {
            writeData(0, 0);

            serialPort.removeEventListener();
            serialPort.close();
            input.close();
            output.close();
            setConnected(false);
 //           window.keybindingController.toggleControls();

            logText = "Disconnected.";
            logger.logEvent(logText);
        }
        catch (Exception e)
        {
            logText = "Failed to close " + portName + "(" + e.toString() + ")";
            logger.logEvent(logText);
            logger.logException(e);
        }
    }

    final public boolean getConnected()
    {
        return bConnected;
    }

    public void setConnected(boolean bConnected)
    {
        this.bConnected = bConnected;
    }

    //what happens when data is received
    //pre: serial event is triggered
    //post: processing on the data it reads
    public String readData() {     
    		try
            {
    			int dataLength = input.available();
    			byte[] data = new byte[dataLength];
    			input.read(data);
    			String msg = new String(data);
    			logger.logEvent("R: "+msg);
    			return msg;
            }
            catch (Exception e)
            {
                logText = "Failed to read data. (" + e.toString() + ")";
                logger.logEvent(logText);
                logger.logException(e);
                return null;
            }
        
    }

    //method that can be called to send data
    //pre: open serial port
    //post: data sent to the other device
    public void writeData(int leftThrottle, int rightThrottle)
    {
        try
        {
            output.write(leftThrottle);
            output.flush();
            //this is a delimiter for the data
            output.write(DASH_ASCII);
            output.flush();
            
            output.write(rightThrottle);
            output.flush();
            //will be read as a byte so it is a space key
            output.write(SPACE_ASCII);
            output.flush();
        }
        catch (Exception e)
        {
            logText = "Failed to write data. (" + e.toString() + ")";
            logger.logEvent(logText);
            logger.logException(e);
        }
    }

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		
		// data came in.  update rolling buffer
		// parse rolling buffer for complete sentences
		//if complete sentence, update state and remove from buffer;
		
	}
}
