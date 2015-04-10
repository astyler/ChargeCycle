/**
 * 
 */
package edu.cmu.ri.createlab.chargecycle.model;

/**
 * @author astyler
 * 
 */
public class State {
	// vehicle state here is immutable, so no synchronization locking is needed
	// volatile is used to guarantee visibility across multiple threads
	private volatile VehicleState vehicle;
	// volatility of 64 bit long/double values is used to guarantee that their
	// writes are atomic and thread safe
	// note, any reads and then writes based on the read value is not atomic
	// as it is 2 operations
	private volatile double ambientTemp;

	private volatile boolean isAlive;


	private volatile double ampHoursSpent;
	private volatile double secondsElapsed;
	private volatile double distanceTraveled;
	private volatile double efficiency;
	
	
	final static double ENCODER_TICKS_TO_MILES = 36490.6666667;
	
	//private volatile int deadMan;

	public State() {
		this.vehicle = null;// new VehicleState(0,0,0,0,0,0,0,0,0,0,0);
		this.ambientTemp = 0.0;
		this.isAlive = true;
		this.ampHoursSpent = 0.0;
		this.secondsElapsed = 0.0;
		this.distanceTraveled = 0.0;
		this.efficiency = 0.0;
	//	this.deadMan = 0;
	}

	public VehicleState getVehicleState() {
		return this.vehicle;
	}

	
	public double getAmpHoursSpent() {
		return ampHoursSpent;
	}

	public double getSecondsElapsed() {
		return secondsElapsed;
	}

	public double getDistanceTraveled() {
		return distanceTraveled;
	}

	public double getEfficiency() {
		return efficiency;
	}
	
	public double getAmbientTemp() {
		return this.ambientTemp;
	}

	public void setAmbientTemp(double ambientTemp) {
		this.ambientTemp = ambientTemp;
	}

	public synchronized void setVehicleState(VehicleState newState) {
		//update running sums... may be out of sync with vehicle state if read happens during this method
		//but external reads do not affect internal writes, so only a very rare 100ms delay can result from
		//this thread condition
		if(this.vehicle != null){
			long millisElapsed = newState.getTime().getTimeInMillis() - this.vehicle.getTime().getTimeInMillis();
			double secondDiff = (double)millisElapsed / 1000;						

			//synchronize this method because this += method is not atomic
			this.ampHoursSpent += (newState.getBatteryCurrent()*secondDiff/3600.0);
			this.distanceTraveled += (newState.getBikeSpeedEncoderTicksAbsolute() - this.vehicle.getBikeSpeedEncoderTicksAbsolute()) / ENCODER_TICKS_TO_MILES;
			this.secondsElapsed += secondDiff;
			if(this.ampHoursSpent > 0.0){
				this.efficiency = this.distanceTraveled / this.ampHoursSpent;
			}
		}
		// updating references is atomic and need not be synchronized with reads
		// but we dont want this method running two times at once
		this.vehicle = newState;
		
		if (newState.isKey() == false && newState.isBatteryCharging() == false) {
	//		this.deadMan ++;
	//		if(this.deadMan > 5){
				this.setAlive(false);
	//		}
		}
	//	else{
	//		this.deadMan = 0;
	//	}
	}

	public boolean isAlive() {
		return this.isAlive;
	}

	public void setAlive(boolean alive) {
		this.isAlive = alive;
	}
}
