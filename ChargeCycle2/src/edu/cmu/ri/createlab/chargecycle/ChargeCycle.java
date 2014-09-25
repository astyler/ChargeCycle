package edu.cmu.ri.createlab.chargecycle;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import edi.cmu.ri.createlab.chargecycle.logging.Logger;
import edu.cmu.ri.createlab.chargecycle.comm.CommunicationsThread;
import edu.cmu.ri.createlab.chargecycle.model.State;
import edu.cmu.ri.createlab.chargecycle.view.ViewThread;

public class ChargeCycle{	
	public static void main(String[] args) {
		File logFile = new File(args[0]);		
		
		State state = new State();
		Logger logger = new Logger(logFile, true);
		
		SwingUtilities.invokeLater(new ViewThread(state));
		Thread commThread = new Thread(new CommunicationsThread(state,logger));
		commThread.start();
		//(new Thread(new CommunicationsThread(state, logger))).start();
		//remake
		//Communicator should take the State and logger and be the listener
		//the background thread should just be to establish comms unless that 
		//can go on the main thread (if NOTHING else can be done without comms)
		while(state.getVehicleState().isKey()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println("Main thread interrupted");
				e.printStackTrace();
			}
			
		}
		
		commThread.interrupt();
		try {
			logger.flushLog();
		} catch (IOException e) {
			System.err.println("Error writing event log file");
			e.printStackTrace();
		}
	}

}
