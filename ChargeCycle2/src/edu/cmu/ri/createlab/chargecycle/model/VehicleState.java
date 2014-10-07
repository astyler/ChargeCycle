package edu.cmu.ri.createlab.chargecycle.model;

/**
 * @author astyler Note: VehicleState is immutable. This allows us to pass the
 *         reference around to many threads that will simultaneously read it
 *         without worrying about thread safety/synchronization/or locking.
 * 
 *         This should speed up overall performance as many threads use vehicle
 *         state.
 * 
 *         The updating will replace the reference with a new object.
 */
public class VehicleState {
	private final String protocolVersion;
	private final String firmwareVersion;
	private final String hardwareVersion;
	private final boolean dischargeEnable;
	private final boolean prechargeEnable;
	private final boolean sourceSelector; // cap==true, batt==false
	private final boolean batteryCharging;
	private final boolean key;
	private final boolean capOverVoltage;
	private final double requestedCapChargingCurrentAmps;
	private final double capChargingCurrentAmps;
	private final double altitude;// meters
	private final double ambientTemp;// celsius
	private final double accelerometerX;// 1000*Gs
	private final double accelerometerY;// 1000*Gs
	private final double accelerometerZ;// 1000*Gs
	private final double compassX;// 1000*Gauss
	private final double compassY;// 1000*Gauss
	private final double compassZ;// 1000*Gauss
	private final double gyroX;// 10*degrees/second
	private final double gyroY;// 10*degrees/second
	private final double gyroZ;// 10*degrees/second
	private final int throttlePot;// 0-4095
	private final int throttleScale;// 0-255
	private final int throttleMax;// 0-65536
	private final int throttleOuputToMotor;// 0-65536
	private final int brakePot;// 0-4095
	private final int brakeScale;// 0-255
	private final int brakeMax;// 0-65536
	private final int brakeOutputToMotor;// 0-65536
	private final double GPSLatitude;// decimal degrees
	private final char GPSLatHemisphere;// N,S,' '
	private final double GPSLongitude;// decimal degrees
	private final char GPSLonHemisphere;// E,W,' '
	private final boolean GPSWarning;// true == no fix on satellites
	private final double motorCurrent;// amps, positive drives, negative regens
	private final double batteryCurrent;// amps, positive discharges, negative
										// charges
	private final double capacitorCurrent;// amps, positive discharges, negative
											// charges
	private final double battery1Voltage;// volts
	private final double battery0Voltage;// volts
	private final double capacitor1Voltage;// volts
	private final double capacitor0Voltage;// volets
	private final double battery1Temperature;// celsius
	private final double battery0Temperature;// celsius
	private final double capacitor1Temperature;// celsius
	private final double capacitor0Temperature;// celsius
	private final int bikeSpeedEncoderTicksAbsolute;// 32bit
	private final double bikeSpeedFPS;// feet per second
	private final double bikeSpeedMPH;// miles per hour

	public VehicleState(String protocolVersion, String firmwareVersion, String hardwareVersion, boolean dischargeEnable, boolean prechargeEnable,
			boolean sourceSelector, boolean batteryCharging, boolean key, boolean capOverVoltage, double requestedCapChargingCurrentAmps,
			double capChargingCurrentAmps, double altitude, double ambientTemp, double accelerometerX, double accelerometerY, double accelerometerZ,
			double compassX, double compassY, double compassZ, double gyroX, double gyroY, double gyroZ, int throttlePot, int throttleScale, int throttleMax,
			int throttleOuputToMotor, int brakePot, int brakeScale, int brakeMax, int brakeOutputToMotor, double gPSLatitude, char gPSLatHemisphere,
			double gPSLongitude, char gPSLonHemisphere, boolean gPSWarning, double motorCurrent, double batteryCurrent, double capacitorCurrent,
			double battery1Voltage, double battery0Voltage, double capacitor1Voltage, double capacitor0Voltage, double battery1Temperature,
			double battery0Temperature, double capacitor1Temperature, double capacitor0Temperature, int bikeSpeedEncoderTicksAbsolute, double bikeSpeedFPS,
			double bikeSpeedMPH) {
		super();
		this.protocolVersion = protocolVersion;
		this.firmwareVersion = firmwareVersion;
		this.hardwareVersion = hardwareVersion;
		this.dischargeEnable = dischargeEnable;
		this.prechargeEnable = prechargeEnable;
		this.sourceSelector = sourceSelector;
		this.batteryCharging = batteryCharging;
		this.key = key;
		this.capOverVoltage = capOverVoltage;
		this.requestedCapChargingCurrentAmps = requestedCapChargingCurrentAmps;
		this.capChargingCurrentAmps = capChargingCurrentAmps;
		this.altitude = altitude;
		this.ambientTemp = ambientTemp;
		this.accelerometerX = accelerometerX;
		this.accelerometerY = accelerometerY;
		this.accelerometerZ = accelerometerZ;
		this.compassX = compassX;
		this.compassY = compassY;
		this.compassZ = compassZ;
		this.gyroX = gyroX;
		this.gyroY = gyroY;
		this.gyroZ = gyroZ;
		this.throttlePot = throttlePot;
		this.throttleScale = throttleScale;
		this.throttleMax = throttleMax;
		this.throttleOuputToMotor = throttleOuputToMotor;
		this.brakePot = brakePot;
		this.brakeScale = brakeScale;
		this.brakeMax = brakeMax;
		this.brakeOutputToMotor = brakeOutputToMotor;
		this.GPSLatitude = gPSLatitude;
		this.GPSLatHemisphere = gPSLatHemisphere;
		this.GPSLongitude = gPSLongitude;
		this.GPSLonHemisphere = gPSLonHemisphere;
		this.GPSWarning = gPSWarning;
		this.motorCurrent = motorCurrent;
		this.batteryCurrent = batteryCurrent;
		this.capacitorCurrent = capacitorCurrent;
		this.battery1Voltage = battery1Voltage;
		this.battery0Voltage = battery0Voltage;
		this.capacitor1Voltage = capacitor1Voltage;
		this.capacitor0Voltage = capacitor0Voltage;
		this.battery1Temperature = battery1Temperature;
		this.battery0Temperature = battery0Temperature;
		this.capacitor1Temperature = capacitor1Temperature;
		this.capacitor0Temperature = capacitor0Temperature;
		this.bikeSpeedEncoderTicksAbsolute = bikeSpeedEncoderTicksAbsolute;
		this.bikeSpeedFPS = bikeSpeedFPS;
		this.bikeSpeedMPH = bikeSpeedMPH;
	}

	public String getProtocolVersion() {
		return this.protocolVersion;
	}

	public String getFirmwareVersion() {
		return this.firmwareVersion;
	}

	public String getHardwareVersion() {
		return this.hardwareVersion;
	}

	public boolean isDischargeEnable() {
		return this.dischargeEnable;
	}

	public boolean isPrechargeEnable() {
		return this.prechargeEnable;
	}

	public boolean isSourceSelector() {
		return this.sourceSelector;
	}

	public boolean isBatteryCharging() {
		return this.batteryCharging;
	}

	public boolean isKey() {
		return this.key;
	}

	public boolean isCapOverVoltage() {
		return this.capOverVoltage;
	}

	public double getRequestedCapChargingCurrentAmps() {
		return this.requestedCapChargingCurrentAmps;
	}

	public double getCapChargingCurrentAmps() {
		return this.capChargingCurrentAmps;
	}

	public double getAltitude() {
		return this.altitude;
	}

	public double getAmbientTemp() {
		return this.ambientTemp;
	}

	public double getAccelerometerX() {
		return this.accelerometerX;
	}

	public double getAccelerometerY() {
		return this.accelerometerY;
	}

	public double getAccelerometerZ() {
		return this.accelerometerZ;
	}

	public double getCompassX() {
		return this.compassX;
	}

	public double getCompassY() {
		return this.compassY;
	}

	public double getCompassZ() {
		return this.compassZ;
	}

	public double getGyroX() {
		return this.gyroX;
	}

	public double getGyroY() {
		return this.gyroY;
	}

	public double getGyroZ() {
		return this.gyroZ;
	}

	public int getThrottlePot() {
		return this.throttlePot;
	}

	public int getThrottleScale() {
		return this.throttleScale;
	}

	public int getThrottleMax() {
		return this.throttleMax;
	}

	public int getThrottleOuputToMotor() {
		return this.throttleOuputToMotor;
	}

	public int getBrakePot() {
		return this.brakePot;
	}

	public int getBrakeScale() {
		return this.brakeScale;
	}

	public int getBrakeMax() {
		return this.brakeMax;
	}

	public int getBrakeOutputToMotor() {
		return this.brakeOutputToMotor;
	}

	public double getGPSLatitude() {
		return this.GPSLatitude;
	}

	public char getGPSLatHemisphere() {
		return this.GPSLatHemisphere;
	}

	public double getGPSLongitude() {
		return this.GPSLongitude;
	}

	public char getGPSLonHemisphere() {
		return this.GPSLonHemisphere;
	}

	public boolean isGPSWarning() {
		return this.GPSWarning;
	}

	public double getMotorCurrent() {
		return this.motorCurrent;
	}

	public double getBatteryCurrent() {
		return this.batteryCurrent;
	}

	public double getCapacitorCurrent() {
		return this.capacitorCurrent;
	}

	public double getBattery1Voltage() {
		return this.battery1Voltage;
	}

	public double getBattery0Voltage() {
		return this.battery0Voltage;
	}

	public double getCapacitor1Voltage() {
		return this.capacitor1Voltage;
	}

	public double getCapacitor0Voltage() {
		return this.capacitor0Voltage;
	}

	public double getBattery1Temperature() {
		return this.battery1Temperature;
	}

	public double getBattery0Temperature() {
		return this.battery0Temperature;
	}

	public double getCapacitor1Temperature() {
		return this.capacitor1Temperature;
	}

	public double getCapacitor0Temperature() {
		return this.capacitor0Temperature;
	}

	public int getBikeSpeedEncoderTicksAbsolute() {
		return this.bikeSpeedEncoderTicksAbsolute;
	}

	public double getBikeSpeedFPS() {
		return this.bikeSpeedFPS;
	}

	public double getBikeSpeedMPH() {
		return this.bikeSpeedMPH;
	}

	@Override
	public String toString() {
		return this.protocolVersion + "," + this.firmwareVersion + "," + this.hardwareVersion + "," + this.boolToString(this.dischargeEnable) + ","
				+ this.boolToString(this.prechargeEnable) + "," + this.boolToString(this.sourceSelector) + "," + this.boolToString(this.batteryCharging) + ","
				+ this.boolToString(this.key) + "," + this.boolToString(this.capOverVoltage) + "," + this.requestedCapChargingCurrentAmps + ","
				+ this.capChargingCurrentAmps + "," + this.altitude + "," + this.ambientTemp + "," + this.accelerometerX + "," + this.accelerometerY + ","
				+ this.accelerometerZ + "," + this.compassX + "," + this.compassY + "," + this.compassZ + "," + this.gyroX + "," + this.gyroY + ","
				+ this.gyroZ + "," + this.throttlePot + "," + this.throttleScale + "," + this.throttleMax + "," + this.throttleOuputToMotor + ","
				+ this.brakePot + "," + this.brakeScale + "," + this.brakeMax + "," + this.brakeOutputToMotor + "," + this.GPSLatitude + ","
				+ this.GPSLatHemisphere + "," + this.GPSLongitude + "," + this.GPSLonHemisphere + "," + this.boolToString(this.GPSWarning) + ","
				+ this.motorCurrent + "," + this.batteryCurrent + "," + this.capacitorCurrent + "," + this.battery1Voltage + "," + this.battery0Voltage + ","
				+ this.capacitor1Voltage + "," + this.capacitor0Voltage + "," + this.battery1Temperature + "," + this.battery0Temperature + ","
				+ this.capacitor1Temperature + "," + this.capacitor0Temperature + "," + this.bikeSpeedEncoderTicksAbsolute + "," + this.bikeSpeedFPS + ","
				+ this.bikeSpeedMPH;
	}

	private String boolToString(boolean b) {
		if (b) {
			return "1";
		} else {
			return "0";
		}
	}

}
