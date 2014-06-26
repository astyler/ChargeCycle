package edu.cmu.ri.createlab.chargecycle;

import javax.swing.SwingUtilities;

import edu.cmu.ri.createlab.chargecycle.comm.CommunicationsThread;
import edu.cmu.ri.createlab.chargecycle.model.State;
import edu.cmu.ri.createlab.chargecycle.view.ViewThread;

public class ChargeCycle{	
	public static void main(String[] args) {
		State state = new State();
		SwingUtilities.invokeLater(new ViewThread(state));
		(new Thread(new CommunicationsThread(state))).start();
	}

}
