/**
 * 
 */
package edu.cmu.ri.createlab.chargecycle.model;

/**
 * @author astyler
 *
 */
public class State {
	//vehicle state here is immutable, so no synchronization locking is needed
	//volatile is used to guarantee visibility across multiple threads
	private volatile VehicleState vehicle; 
	//volatility of 64 bit  long/double values is used to guarantee that their
	//writes are atomic and thread safe
	//note, any reads and then writes based on the read value is not atomic
	//as it is 2 operations
	private volatile double ambientTemp;
	
	private volatile boolean isAlive;
	
	public State(){
		this.vehicle = null;//new VehicleState(0,0,0,0,0,0,0,0,0,0,0);
		this.ambientTemp = 0;
		this.isAlive = true;
	}
	
	public VehicleState getVehicleState(){
		return vehicle;
	}
	
	public double getAmbientTemp(){
		return this.ambientTemp;
	}
	
	public void setAmbientTemp(double ambientTemp){
		this.ambientTemp = ambientTemp;
	}
	
	public void setVehicleState(VehicleState newState){
		//updating references is atomic and need not be synchronized
		this.vehicle = newState;
		
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean alive) {
		this.isAlive = alive;
	}
}
