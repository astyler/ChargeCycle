package edu.cmu.ri.createlab.chargecycle.comm;

import edu.cmu.create.chargecycle.model.State;
import edu.cmu.create.chargecycle.model.VehicleState;

public class CommunicationsThread implements Runnable {

	private final State state;
	
	public CommunicationsThread(State state){
		this.state = state;
	}
	
	@Override
	public void run() {
		int i = 0;
		while(true){			
			state.setVehicleState(new VehicleState(i,0,0,0,0,0,0,0,0, 0, 0));
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
