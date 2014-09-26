/**
 * 
 */
package edu.cmu.ri.createlab.chargecycle.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import edi.cmu.ri.createlab.chargecycle.logging.EventLogger;
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
	private final EventLogger logger;
	
	private final JLabel prechargeLabel = new JLabel("Precharging");
	private final JLabel dischargeLabel = new JLabel("Discharging");
	private final JLabel chargingLabel = new JLabel("B Charging");
	private final JLabel voltageLabel = new JLabel("Over Voltage");
	private final JLabel keyLabel = new JLabel("Key");
	private final JLabel sourceLabel = new JLabel("Source     :");
	private final JTextArea logText = new JTextArea(10,80);
	private final Color trueColor = Color.CYAN;
	private final Color falseColor = Color.RED;
	private Timer updateDisplayTimer;
	
	public ViewThread(State state, EventLogger logger){
		this.state = state;
		this.logger = logger;
	}
	
	@Override
	public void run() {       
       constructGui();
       logger.logEvent("GUI Built.");
       
       updateDisplayTimer = new Timer(100, new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent e) {
        	updateDisplay(); 
         }
       });
       
       updateDisplayTimer.start();

	}
	
	protected void updateDisplay() {
		VehicleState vState = state.getVehicleState();
		logText.setText(logger.getRecentLogText(10));
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
	     f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	     // Add a layout manager so that the button is not placed on top of the label
	     f.setLayout(new FlowLayout());
	     // Add a label and a button
	     f.addWindowListener(new WindowListener(){
			@Override
			public void windowActivated(WindowEvent arg0) {
			}
			@Override
			public void windowClosed(WindowEvent arg0) {
				updateDisplayTimer.stop();
				state.setAlive(false);
				logger.logEvent("View window closed.");
				//window closed, kill state which will kill comms, flush log, and exit		
			}

			@Override
			public void windowClosing(WindowEvent arg0) {		
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowOpened(WindowEvent e) {
			}


	    	 
	     });
	     
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
	     f.add(logText);
	    // f.add(new JButton("Press me!"));
	        // Arrange the components inside the window
	        f.pack();
	        // By default, the window is not visible. Make it visible.
	        f.setVisible(true);
	}


	
}
