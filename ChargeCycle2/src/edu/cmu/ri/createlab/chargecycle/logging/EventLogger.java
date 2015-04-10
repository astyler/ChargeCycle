package edu.cmu.ri.createlab.chargecycle.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventLogger {
	private final List<String> logBuffer;
	private final boolean debug;
	private final DateFormat df = DateFormat.getDateTimeInstance();
	private final File log;

	public EventLogger(File logFile, boolean debug) {
		this.logBuffer = new ArrayList<String>();
		this.debug = debug;
		this.log = logFile;
	}

	public synchronized void logException(Exception x) {
		this.logBuffer.add("ERR: " + this.df.format(Calendar.getInstance().getTime()) + " : " + x.toString());
		if (!this.debug) {
			return;
		}
		for (StackTraceElement e : x.getStackTrace()) {
			this.logBuffer.add(e.toString());
		}
	}

	public synchronized void logEvent(String msg) {
		this.logBuffer.add(this.df.format(Calendar.getInstance().getTime()) + " : " + msg);
	}

	public synchronized void flushLog() throws IOException {
		this.log.getParentFile().mkdirs();
		this.log.createNewFile();
		FileWriter fw = new FileWriter(this.log, true);
		for (String s : this.logBuffer) {
			fw.write(s);			
			// fw.write(System.lineSeparator());
			fw.write(System.getProperty("line.separator"));
		}
		fw.close();
		this.logBuffer.clear();
	}

	public synchronized String getRecentLogText(int lines) {
		int endIndex = this.logBuffer.size(); // exclusive
		int fromIndex = endIndex - lines;
		fromIndex = fromIndex < 0 ? 0 : fromIndex;
		StringBuilder sb = new StringBuilder();
		for (String s : this.logBuffer.subList(fromIndex, endIndex)) {
			sb.append(s).append("\n");
		}
		return sb.toString();
	}
}
