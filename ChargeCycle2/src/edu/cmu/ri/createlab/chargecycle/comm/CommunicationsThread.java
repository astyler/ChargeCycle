package edu.cmu.ri.createlab.chargecycle.comm;

import edu.cmu.ri.createlab.chargecycle.model.State;
import edu.cmu.ri.createlab.chargecycle.model.VehicleState;

public class CommunicationsThread implements Runnable {

	private final State state;
	
	public CommunicationsThread(State state){
		this.state = state;
	}
	
	@Override
	public void run() {
		int i = 0;
		while(true){			
			state.setVehicleState(new VehicleState("V1", "V1", "V1", false, false, false, false, true, false, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, i, 'N', 0.0, 'E', false, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0.0, 0.0));
			i++;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
