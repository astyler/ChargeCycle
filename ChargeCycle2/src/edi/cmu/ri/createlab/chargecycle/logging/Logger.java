package edi.cmu.ri.createlab.chargecycle.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Logger {
	private final List<String> logBuffer;
	private final boolean debug;
	private final DateFormat df = DateFormat.getDateTimeInstance();
	private final File log;
	
	public Logger(File logFile, boolean debug){
		this.logBuffer = new ArrayList<String>();
		this.debug = debug;
		this.log = logFile;
	}
	
	public synchronized void logException(Exception x){
		logBuffer.add("ERR: " + df.format(Calendar.getInstance().getTime()) + " : " + x.toString());
		if(!debug) return;
		for(StackTraceElement e : x.getStackTrace()){
			logBuffer.add(e.toString());
		}
	}
	
	public synchronized void logEvent(String msg){
		logBuffer.add(df.format(Calendar.getInstance().getTime()) + " : " + msg);
	}
	
	public synchronized void flushLog() throws IOException{
		log.getParentFile().mkdirs();
		log.createNewFile();
		FileWriter fw = new FileWriter(log, true);
		for(String s : logBuffer){
			fw.write(s);
			fw.write(System.lineSeparator());
		}
		fw.close();		
	}
	
	public synchronized String getRecentLogText(int lines){
		int endIndex = logBuffer.size(); // exclusive
		int fromIndex = endIndex - lines;
		fromIndex = fromIndex < 0 ? 0 : fromIndex;
		StringBuilder sb = new StringBuilder();
		for(String s: logBuffer.subList(fromIndex, endIndex)){
			sb.append(s).append("\n");
		}
		return sb.toString();
	}
}
