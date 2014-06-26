/**
 * 
 */
package edu.cmu.ri.createlab.chargecycle.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import edu.cmu.ri.createlab.chargecycle.model.State;
import edu.cmu.ri.createlab.chargecycle.model.VehicleState;

/**
 * @author astyler
 * THe thread controlling the view
 */
public class ViewThread implements Runnable {

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	private final State state;
	private final JLabel latLab = new JLabel("Lat: -1");
	
	public ViewThread(State state){
		this.state = state;
	}
	
	@Override
	public void run() {
        // Create the window
       constructGui();
       
       Timer updateDisplayTimer = new Timer(1000, new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent e) {
        	updateDisplay(); 
         }
       });
       
       updateDisplayTimer.start();

	}
	
	protected void updateDisplay() {
		latLab.setText("Lat: "+ state.getVehicleState().getGPSLat());
		
	}

	private void constructGui(){
		 JFrame f = new JFrame("Hello, World!");
	     // Sets the behavior for when the window is closed
	     f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     // Add a layout manager so that the button is not placed on top of the label
	     f.setLayout(new FlowLayout());
	     // Add a label and a button
	     f.add(latLab);
	     f.add(new JButton("Press me!"));
	        // Arrange the components inside the window
	        f.pack();
	        // By default, the window is not visible. Make it visible.
	        f.setVisible(true);
	}


	
}
