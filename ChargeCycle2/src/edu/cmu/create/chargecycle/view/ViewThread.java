/**
 * 
 */
package edu.cmu.create.chargecycle.view;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import edu.cmu.create.chargecycle.model.State;

/**
 * @author astyler
 * THe thread controlling the view
 */
public class ViewThread implements Runnable {

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	private final State state;
	private final JLabel latLab = new JLabel("-1");
	
	public ViewThread(State state){
		this.state = state;
	}
	
	@Override
	public void run() {
        // Create the window
       constructGui();
       
       while(true){
    	   latLab.setText("Temp: "+state.getAmbientTemp());
       }

	}
	
	private void constructGui(){
		 JFrame f = new JFrame("Hello, World!");
	     // Sets the behavior for when the window is closed
	     f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     // Add a layout manager so that the button is not placed on top of the label
	     f.setLayout(new FlowLayout());
	     // Add a label and a button
	     f.add(new JLabel("Hello, world!"));
	     f.add(new JButton("Press me!"));
	        // Arrange the components inside the window
	        f.pack();
	        // By default, the window is not visible. Make it visible.
	        f.setVisible(true);
	}

}
