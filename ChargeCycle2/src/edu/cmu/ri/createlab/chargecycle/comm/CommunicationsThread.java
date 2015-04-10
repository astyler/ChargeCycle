package edu.cmu.ri.createlab.chargecycle.comm;

import java.util.List;

import javax.swing.SwingWorker;

import edu.cmu.ri.createlab.chargecycle.logging.EventLogger;
import edu.cmu.ri.createlab.chargecycle.model.State;
import gnu.io.CommPortIdentifier;

public class CommunicationsThread extends SwingWorker<Boolean, Void> {

	private final Communicator comms;
	private final EventLogger logger;
	private final State state;

	public CommunicationsThread(State state, Communicator comms, EventLogger logger) {
		this.comms = comms;
		this.logger = logger;
		this.state = state;
	}

/*	public String getExampleSerialMessage() {
		String exampleMsg = "!!!,V1,V1,V1,0,0,0,0,1,0,0,0,1000,20,10,10,10,20,20,20,0,0,0,4095,255,65535,65535,4095,255,65535,65535,40,N,100,E,0,0,0,0,12,12,12,12,30,30,30,30,1000,7,10,\r\n";
		return exampleMsg;
	}
*/
	@Override
	protected Boolean doInBackground() throws Exception {
		// Connect to Serial Port
		this.logger.logEvent("Searching for serial ports...");
		List<CommPortIdentifier> ports = this.comms.searchForPorts();
		for (CommPortIdentifier port : ports) {
			this.logger.logEvent("Attempting to connect to port: " + port.getName());

			if (this.comms.connect(port)) {
				this.logger.logEvent("Port Connected");
			} else {
				this.logger.logEvent("Port Connect Attempt Failed");
				continue;
			}

			this.comms.initIOStream();
			this.comms.initListener();

			Thread.sleep(1000);
			if (this.state.getVehicleState() != null) {
				this.logger.logEvent("Bike found, logging data");
				return true;
			}
			this.logger.logEvent("Not the bike, disconnecting");
			this.comms.disconnect();
		}

		if (ports.isEmpty()) {
			this.logger.logEvent("No Ports Found! No communication established with bike");
		}

		if (!this.comms.getConnected()) {
			this.logger.logEvent("No bike found.  Will retry in 10s.  Contact Josh if problem persists.");
		}
		// failed to connect
		return false;
	}
}
