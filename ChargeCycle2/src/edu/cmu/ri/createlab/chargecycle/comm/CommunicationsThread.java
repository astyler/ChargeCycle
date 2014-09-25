package edu.cmu.ri.createlab.chargecycle.comm;

import java.util.List;

import javax.swing.SwingWorker;

import edi.cmu.ri.createlab.chargecycle.logging.Logger;
import edu.cmu.ri.createlab.chargecycle.model.State;
import edu.cmu.ri.createlab.chargecycle.model.VehicleState;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class CommunicationsThread extends SwingWorker<Void, String> implements SerialPortEventListener {

	private final State state;
	private final Logger logger;
	private final Communicator comms;
	
	public CommunicationsThread(State state, Logger logger){
		this.state = state;
		this.logger = logger;
		this.comms = new Communicator(logger);
	}
			
	public String getExampleSerialMessage(){
		String exampleMsg = "!!!,V1,V1,V1,0,0,0,0,1,0,0,0,1000,20,10,10,10,20,20,20,0,0,0,4095,255,65535,65535,4095,255,65535,65535,40,N,100,E,0,0,0,0,12,12,12,12,30,30,30,30,1000,7,10,\r\n";
		return exampleMsg;
	}

	@Override
	protected Void doInBackground() throws Exception {
		// TODO Auto-generated method stub
		List<CommPortIdentifier> ports = comms.searchForPorts();
		for(CommPortIdentifier port : ports){
			if(comms.connect(port)){
				
				
			}
			else{
				continue;
			}
			
			
			
		}
		return null;
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
