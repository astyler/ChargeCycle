package edu.cmu.ri.createlab.chargecycle.comm;

import edu.cmu.ri.createlab.chargecycle.model.VehicleState;

public class CommParser {
	public static VehicleState Parse(String comm) throws ParseException{
		//Electric Bike MCU Data Protocol V1
		String[] parts = comm.split(",");
		if(parts.length != 50)
			throw new ParseException("Badly formatted input string: expecting 50 parts, found "+parts.length);
		if(!parts[0].equals("!!!"))
			throw new ParseException("Missing starting sync sequence '!!!' in input string: "+comm);
		if(!parts[1].equals("1"))
			throw new ParseException("Incompatable protocol version; expecting V1, received "+parts[1]+" in input string: "+comm);
		
		return new VehicleState(parts[1], 		//String protocolVersion
				parts[2],						//String firmwareVersion
				parts[3],						//String hardwareVersion
				Integer.parseInt(parts[4]) == 1,//boolean dischargeEnable
				Integer.parseInt(parts[5]) == 1,//boolean prechargeEnable
				Integer.parseInt(parts[6]) == 1,//boolean sourceSelector
				Integer.parseInt(parts[7]) == 1,//boolean batteryCharging
				Integer.parseInt(parts[8]) == 1,//boolean key
				Integer.parseInt(parts[9]) == 1,//boolean capOverVoltage
				Double.parseDouble(parts[10]),	//double requestedCapChargingCurrentAmps
				Double.parseDouble(parts[11]),	//double capChargingCurrentAmps
				Double.parseDouble(parts[12]),	//double altitude
				Double.parseDouble(parts[13]),	//double ambientTemp
				Double.parseDouble(parts[14]),	//double accelerometerX
				Double.parseDouble(parts[15]),	//double accelerometerY
				Double.parseDouble(parts[16]),	//double accelerometerZ
				Double.parseDouble(parts[17]),	//double compassX 
				Double.parseDouble(parts[18]),	//double compassY
				Double.parseDouble(parts[19]),	//double compassZ 
				Double.parseDouble(parts[20]),	//double gyroX
				Double.parseDouble(parts[21]),	//double gyroY
				Double.parseDouble(parts[22]),	//double gyroZ
				Integer.parseInt(parts[23]),	//int throttlePot
				Integer.parseInt(parts[24]),	//int throttleScale
				Integer.parseInt(parts[25]),	//int throttleMax
				Integer.parseInt(parts[26]),	//int throttleOuputToMotor
				Integer.parseInt(parts[27]),	//int brakePot
				Integer.parseInt(parts[28]),	//int brakeScale
				Integer.parseInt(parts[29]),	//int brakeMax
				Integer.parseInt(parts[30]),	//int brakeOutputToMotor, 
				Double.parseDouble(parts[31]),	//double gPSLatitude,
				parts[32].charAt(0),			//char gPSLatHemisphere, 
				Double.parseDouble(parts[33]),	//double gPSLongitude
				parts[34].charAt(0),			//char gPSLonHemisphere,
				Integer.parseInt(parts[35]) == 1,//boolean gPSWarning, 
				Double.parseDouble(parts[36]),	//double motorCurrent,
				Double.parseDouble(parts[37]),	//double batteryCurrent,
				Double.parseDouble(parts[38]),	//double capacitorCurrent, 
				Double.parseDouble(parts[39]),	//double battery1Voltage,
				Double.parseDouble(parts[40]),	//double battery0Voltage, 
				Double.parseDouble(parts[41]),	//double capacitor1Voltage,
				Double.parseDouble(parts[42]),	//double capacitor0Voltage, 
				Double.parseDouble(parts[43]),	//double battery1Temperature,
				Double.parseDouble(parts[44]),	//double battery0Temperature, 
				Double.parseDouble(parts[45]),	//double capacitor1Temperature,
				Double.parseDouble(parts[46]),	//double capacitor0Temperature, 
				Integer.parseInt(parts[47]),	//int bikeSpeedEncoderTicksAbsolute,
				Double.parseDouble(parts[48]),	//double bikeSpeedFPS, 
				Double.parseDouble(parts[49])	//double bikeSpeedMPH
				);
	}
}


