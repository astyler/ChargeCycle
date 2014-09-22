package edu.cmu.ri.createlab.chargecycle.comm;

import edi.cmu.ri.createlab.chargecycle.logging.Logger;
import edu.cmu.ri.createlab.chargecycle.model.State;
import edu.cmu.ri.createlab.chargecycle.model.VehicleState;

public class CommunicationsThread implements Runnable {

	private final State state;
	private final Logger logger;
	
	public CommunicationsThread(State state, Logger logger){
		this.state = state;
		this.logger = logger;
	}
	
	@Override
	public void run() {
		String commMsg;
		while(true){			
			//state.setVehicleState(new VehicleState("V1", "V1", "V1", false, false, false, false, true, false, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, i, 'N', 0.0, 'E', false, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0.0, 0.0));
			commMsg = getSerialMessage();
			try {
				state.setVehicleState(CommParser.Parse(commMsg));
			} catch (ParseException e) {
				logger.logException(e);				
				e.printStackTrace();				
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.logException(e);
			}
		}

	}
	
	private String getSerialMessage(){
		String exampleMsg = "!!!,V1,V1,V1,0,0,0,0,1,0,0,0,1000,20,10,10,10,20,20,20,0,0,0,4095,255,65535,65535,4095,255,65535,65535,40,N,100,E,0,0,0,0,12,12,12,12,30,30,30,30,1000,7,10,\r\n";
		return exampleMsg;
	}

}
