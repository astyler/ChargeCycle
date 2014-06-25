package edu.cmu.create.chargecycle;

import javax.swing.SwingUtilities;

import edu.cmu.create.chargecycle.model.State;
import edu.cmu.create.chargecycle.view.ViewThread;

public class ChargeCycle {

	
	
	public static void main(String[] args) {
		 State state = new State();
		SwingUtilities.invokeLater(new ViewThread(state));
		
	}

}
