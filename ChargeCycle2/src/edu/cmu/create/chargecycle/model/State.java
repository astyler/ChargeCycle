/**
 * 
 */
package edu.cmu.create.chargecycle.model;

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
	private volatile double ambientTemp;
	
	public State(){
		this.vehicle = new VehicleState(0,0,0,0,0,0,0,0,0,0,0);
		this.ambientTemp = 0;
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
		this.vehicle = new VehicleState(newState);
		
	}
}
