package edu.cmu.ri.createlab.chargecycle;

import java.io.File;

import javax.swing.SwingUtilities;

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
		(new Thread(new CommunicationsThread(state, logger))).start();
	}

}
