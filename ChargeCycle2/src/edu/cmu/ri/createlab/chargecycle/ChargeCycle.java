package edu.cmu.ri.createlab.chargecycle;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import edi.cmu.ri.createlab.chargecycle.logging.EventLogger;
import edi.cmu.ri.createlab.chargecycle.logging.StateLogger;
import edu.cmu.ri.createlab.chargecycle.comm.CommunicationsThread;
import edu.cmu.ri.createlab.chargecycle.comm.Communicator;
import edu.cmu.ri.createlab.chargecycle.model.State;
import edu.cmu.ri.createlab.chargecycle.model.VehicleState;
import edu.cmu.ri.createlab.chargecycle.view.ViewThread;

public class ChargeCycle{	
	public static void main(String[] args) throws IOException {
		File logFileDirectory = new File(args[0]);		
		
		
		State state = new State();
		EventLogger eventLogger = new EventLogger(new File(logFileDirectory, "CCEventLog.txt"), false);
		Communicator comms = new Communicator(eventLogger, state);
		StateLogger stateLogger = new StateLogger(logFileDirectory);
		
		SwingUtilities.invokeLater(new ViewThread(state, eventLogger));
		
		SwingWorker<Boolean, Void> commThread = new CommunicationsThread(state, comms, eventLogger);
		commThread.execute();
		
		//state will be alive until window is closed, comms fail to establish,
		//or comms establish and then the key is turned off
		VehicleState prevState = state.getVehicleState();
		try {
			stateLogger.startLogging();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(state.isAlive()){	
			try {
				VehicleState currState = state.getVehicleState();
				
				if(currState != prevState && currState != null){
					stateLogger.writeState(currState);
				}
				//eventLogger.flushLog();
				//do main thread stuff
				Thread.sleep(100);
				prevState = currState;
			} catch (InterruptedException e) {
				System.err.println("Main thread interrupted");
				e.printStackTrace();
			} catch (IOException e){
				eventLogger.logEvent("Problem writing vehicle state");
				eventLogger.logException(e);
				e.printStackTrace();
			}
			
		}	
		prevState = null;
		eventLogger.logEvent("Killing communications...");		
		commThread.cancel(true);
		if(comms.getConnected())
			comms.disconnect();		
		try {
			eventLogger.logEvent("Writing log.");
			eventLogger.flushLog();
			stateLogger.stopLogging();
			System.out.println("Log successfully written");
		} catch (IOException e) {
			System.err.println("Error writing event log file");
			e.printStackTrace();
		}
		
		VehicleState vState = state.getVehicleState();
		if(vState != null && vState.isKey() == false && vState.isBatteryCharging() == false)
		{
			Runtime.getRuntime().exec("pmset sleepnow");
		}
		System.exit(0);
	}

}
