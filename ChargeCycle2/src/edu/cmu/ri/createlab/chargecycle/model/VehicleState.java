package edu.cmu.ri.createlab.chargecycle.model;

/**
 * @author astyler
 * Note: VehicleState is immutable.  This allows us to pass the
 * reference around to many threads that will simultaneously read
 * it without worrying about thread safety/synchronization/or locking.
 * 
 * This should speed up overall performance as many threads use vehicle
 * state.
 * 
 * The updating will replace the reference with a new object.
 */
public class VehicleState {
	
	
	private final double GPSLat;
	private final double GPSLon;
	private final double speed;
	private final double acceleration;
	private final double batteryCharge;
	private final double capacitorCharge;
	private final double throttlePosition;
	private final double batteryTemp;
	private final double powerDemand;
	private final double throttleScalar;
	private final double throttleMax;
	
	public VehicleState(double gPSLat, double gPSLon, double speed,
			double acceleration, double batteryCharge, double capacitorCharge,
			double throttlePosition, double batteryTemp, double powerDemand,
			double throttleScalar, double throttleMax) {
		super();
		this.GPSLat = gPSLat;
		this.GPSLon = gPSLon;
		this.speed = speed;
		this.acceleration = acceleration;
		this.batteryCharge = batteryCharge;
		this.capacitorCharge = capacitorCharge;
		this.throttlePosition = throttlePosition;
		this.batteryTemp = batteryTemp;
		this.powerDemand = powerDemand;
		this.throttleScalar = throttleScalar;
		this.throttleMax = throttleMax;
	}
	
	public VehicleState(VehicleState source){
		super();
		GPSLat = source.GPSLat;
		GPSLon = source.GPSLon;
		this.speed = source.speed;
		this.acceleration = source.acceleration;
		this.batteryCharge = source.batteryCharge;
		this.capacitorCharge = source.capacitorCharge;
		this.throttlePosition = source.throttlePosition;
		this.batteryTemp = source.batteryTemp;
		this.powerDemand = source.powerDemand;
		this.throttleScalar = source.throttleScalar;
		this.throttleMax = source.throttleMax;
	}

	public double getGPSLat() {
		return GPSLat;
	}

	public double getGPSLon() {
		return GPSLon;
	}


	public double getSpeed() {
		return speed;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public double getBatteryCharge() {
		return batteryCharge;
	}

	public double getCapacitorCharge() {
		return capacitorCharge;
	}


	public double getThrottlePosition() {
		return throttlePosition;
	}

	public double getBatteryTemp() {
		return batteryTemp;
	}

	public double getPowerDemand() {
		return powerDemand;
	}

	public double getThrottleScalar() {
		return throttleScalar;
	}

	public double getThrottleMax() {
		return throttleMax;
	}
}
