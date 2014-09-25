package edu.cmu.ri.createlab.chargecycle;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import edi.cmu.ri.createlab.chargecycle.logging.Logger;
import edu.cmu.ri.createlab.chargecycle.comm.CommunicationsThread;
import edu.cmu.ri.createlab.chargecycle.comm.Communicator;
import edu.cmu.ri.createlab.chargecycle.model.State;
import edu.cmu.ri.createlab.chargecycle.view.ViewThread;

public class ChargeCycle{	
	public static void main(String[] args) {
		File logFile = new File(args[0]);		
		
		State state = new State();
		Logger logger = new Logger(logFile, true);
		Communicator comms = new Communicator(logger, state);
		
		SwingUtilities.invokeLater(new ViewThread(state, logger));
		
		SwingWorker<Boolean, String> commThread = new CommunicationsThread(comms, logger);
		commThread.execute();
		
		//state will be alive until window is closed, comms fail to establish,
		//or comms establish and then the key is turned off
		while(state.isAlive()){	
			try {
				//do main thread stuff
				
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.err.println("Main thread interrupted");
				e.printStackTrace();
			}
			
		}	
		logger.logEvent("Killing communications...");		
		commThread.cancel(true);
		if(comms.getConnected())
			comms.disconnect();		
		try {
			logger.logEvent("Writing log.");
			logger.flushLog();
			System.out.println("Log successfully written");
		} catch (IOException e) {
			System.err.println("Error writing event log file");
			e.printStackTrace();
		}
	}

}
