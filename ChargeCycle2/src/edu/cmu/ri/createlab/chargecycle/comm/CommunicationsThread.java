package edu.cmu.ri.createlab.chargecycle.comm;

import java.util.List;

import javax.swing.SwingWorker;

import edi.cmu.ri.createlab.chargecycle.logging.Logger;
import gnu.io.CommPortIdentifier;


public class CommunicationsThread extends SwingWorker<Boolean, String>  {

	private final Communicator comms;
	private final Logger logger;
	
	public CommunicationsThread(Communicator comms, Logger logger){
		this.comms = comms;
		this.logger = logger;
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
		}
		if(ports.isEmpty())
			logger.logEvent("No Ports Found!  No communication established with bike");
		//failed to connect
		return false;
	}
}
