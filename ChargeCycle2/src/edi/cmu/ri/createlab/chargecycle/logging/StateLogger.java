package edi.cmu.ri.createlab.chargecycle.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.cmu.ri.createlab.chargecycle.model.VehicleState;

public class StateLogger {
	private final File logFileDirectory;
	private FileWriter fw;
	private final SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy.MM.dd, HH:mm:ss, SSS");
	private final SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd__HH-mm-ss");
	
	public StateLogger(File logFileDirectory){
		this.logFileDirectory = logFileDirectory;
	}
	
	public synchronized void startLogging() throws IOException{
		logFileDirectory.mkdirs();
		//StringBuilder sb = new StringBuilder();
		Calendar time = Calendar.getInstance();
		String logName = "CCLog_"+fileDateFormat.format(Calendar.getInstance().getTime())+".txt";
		File log = new File(logFileDirectory, logName);
		log.createNewFile();
		this.fw = new FileWriter(log, true);
	}
	
	public synchronized void writeState(VehicleState vState) throws IOException{
		fw.write(logDateFormat.format(Calendar.getInstance().getTime())+", ");
		fw.write(vState.toString());
		fw.write(System.lineSeparator());
	}
	
	public synchronized void writeString(String msg) throws IOException{
		fw.write(logDateFormat.format(Calendar.getInstance().getTime())+", ");
		fw.write(msg);
		fw.write(System.lineSeparator());
	}
	
	public synchronized void stopLogging() throws IOException{
		fw.flush();
		fw.close();
	}
	
}
