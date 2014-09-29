package edu.cmu.ri.createlab.chargecycle.comm;

import java.util.List;
import javax.swing.SwingWorker;
import edi.cmu.ri.createlab.chargecycle.logging.EventLogger;
import gnu.io.CommPortIdentifier;
import edu.cmu.ri.createlab.chargecycle.model.State;


public class CommunicationsThread extends SwingWorker<Boolean, String>  {

	private final Communicator comms;
	private final EventLogger logger;
	private final State state;
	
	public CommunicationsThread(State state, Communicator comms, EventLogger logger){
		this.comms = comms;
		this.logger = logger;
		this.state = state;
	}
			
	public String getExampleSerialMessage(){
		String exampleMsg = "!!!,V1,V1,V1,0,0,0,0,1,0,0,0,1000,20,10,10,10,20,20,20,0,0,0,4095,255,65535,65535,4095,255,65535,65535,40,N,100,E,0,0,0,0,12,12,12,12,30,30,30,30,1000,7,10,\r\n";
		return exampleMsg;
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		// Connect to Serial Port
		logger.logEvent("Searching for serial ports...");
		List<CommPortIdentifier> ports = comms.searchForPorts();
		for(CommPortIdentifier port : ports){
			logger.logEvent("Attempting to connect to port: "+port.getName());
			
			if(comms.connect(port)){
				logger.logEvent("Port Connected");
			}
			else{
				logger.logEvent("Port Connect Attempt Failed");
				continue;
			}		
			
			comms.initIOStream();
			comms.initListener();
			
			Thread.sleep(1000);
			if(this.state.getVehicleState() != null){
				logger.logEvent("Bike found");
				return true;
			}
			logger.logEvent("Disconnecting");
			comms.disconnect();	
		}
			
		if(ports.isEmpty())
			logger.logEvent("No Ports Found! No communication established with bike");
		//failed to connect
		return false;
	}
}
