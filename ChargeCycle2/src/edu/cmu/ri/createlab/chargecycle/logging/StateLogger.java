package edu.cmu.ri.createlab.chargecycle.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.cmu.ri.createlab.chargecycle.model.VehicleState;

public class StateLogger {
	private final File logFileDirectory;
	private FileWriter fw;
	private final SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy.MM.dd, HH:mm:ss, SSS");
	private final SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd__HH-mm-ss");

	public StateLogger(File logFileDirectory) {
		this.logFileDirectory = logFileDirectory;
	}

	public synchronized void startLogging() throws IOException {
		this.logFileDirectory.mkdirs();
		String logName = "CCLog_" + this.fileDateFormat.format(Calendar.getInstance().getTime()) + ".txt";
		File log = new File(this.logFileDirectory, logName);
		log.createNewFile();
		this.fw = new FileWriter(log, true);
	}

	public synchronized void writeState(VehicleState vState) throws IOException {
		this.fw.write(this.logDateFormat.format(Calendar.getInstance().getTime()) + ", ");
		this.fw.write(vState.toString());
		// fw.write(System.lineSeparator());
		this.fw.write(System.getProperty("line.separator"));

	}

	public synchronized void writeString(String msg) throws IOException {
		this.fw.write(this.logDateFormat.format(Calendar.getInstance().getTime()) + ", ");
		this.fw.write(msg);
		// fw.write(System.lineSeparator());
		this.fw.write(System.getProperty("line.separator"));

	}

	public synchronized void stopLogging() throws IOException {
		this.fw.flush();
		this.fw.close();
	}

}
