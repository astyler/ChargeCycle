/**
 * 
 */
package edu.cmu.ri.createlab.chargecycle.view;

import java.awt.Color;
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
	
	private final JLabel prechargeLabel = new JLabel("Precharging");
	private final JLabel dischargeLabel = new JLabel("Discharging");
	private final JLabel chargingLabel = new JLabel("B Charging");
	private final JLabel voltageLabel = new JLabel("Over Voltage");
	private final JLabel keyLabel = new JLabel("Key");
	private final JLabel sourceLabel = new JLabel("Source     :");
	private final Color trueColor = Color.CYAN;
	private final Color falseColor = Color.RED;
	
	public ViewThread(State state){
		this.state = state;
	}
	
	@Override
	public void run() {
        // Create the window
       constructGui();
       
       Timer updateDisplayTimer = new Timer(100, new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent e) {
        	updateDisplay(); 
         }
       });
       
       updateDisplayTimer.start();

	}
	
	protected void updateDisplay() {
		VehicleState vState = state.getVehicleState(); 
		if(vState != null){
			if(vState.isKey()){
				keyLabel.setBackground(trueColor);
			}
			else{
				keyLabel.setBackground(falseColor);				
			}
			if(vState.isBatteryCharging()){
				chargingLabel.setBackground(trueColor);
			}
			else{
				chargingLabel.setBackground(falseColor);				
			}
			if(vState.isPrechargeEnable()){
				prechargeLabel.setBackground(trueColor);
			}
			else{
				prechargeLabel.setBackground(falseColor);				
			}
			if(vState.isCapOverVoltage()){
				voltageLabel.setBackground(trueColor);
			}
			else{
				voltageLabel.setBackground(falseColor);				
			}
			if(vState.isDischargeEnable()){
				dischargeLabel.setBackground(trueColor);
			}
			else{
				dischargeLabel.setBackground(falseColor);				
			}
			if(vState.isSourceSelector()){
				sourceLabel.setBackground(trueColor);
				sourceLabel.setText("S: Capacitor");
				
			}
			else{
				sourceLabel.setBackground(falseColor);
				sourceLabel.setText("S: Battery");
			}
		}
		
	}

	private void constructGui(){
		 JFrame f = new JFrame("ChargeCycle Information");
	     // Sets the behavior for when the window is closed
	     f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     // Add a layout manager so that the button is not placed on top of the label
	     f.setLayout(new FlowLayout());
	     // Add a label and a button
	     keyLabel.setOpaque(true);
	     prechargeLabel.setOpaque(true);
	     chargingLabel.setOpaque(true);
	     dischargeLabel.setOpaque(true);
	     voltageLabel.setOpaque(true);
	     sourceLabel.setOpaque(true);
	     
	     f.add(keyLabel);
	     f.add(prechargeLabel);
	     f.add(chargingLabel);
	     f.add(dischargeLabel);
	     f.add(voltageLabel);
	     f.add(sourceLabel);
	    // f.add(new JButton("Press me!"));
	        // Arrange the components inside the window
	        f.pack();
	        // By default, the window is not visible. Make it visible.
	        f.setVisible(true);
	}


	
}
