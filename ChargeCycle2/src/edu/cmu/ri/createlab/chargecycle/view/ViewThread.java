/**
 * 
 */
package edu.cmu.ri.createlab.chargecycle.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
	private final DecimalFormat df = new DecimalFormat("0.00");
	private final JLabel prechargeLabel = new JLabel("Precharging");
	private final JLabel gpsLockLabel = new JLabel("GPS Lock");
	private final JLabel dischargeLabel = new JLabel("Discharging");
	private final JLabel chargingLabel = new JLabel("B Charging");
	private final JLabel voltageLabel = new JLabel("Over Voltage");
	private final JLabel keyLabel = new JLabel("Key");
	private final JLabel capacitorLabel = new JLabel("Capacitor");
	private final JLabel batteryLabel = new JLabel("Battery");
	
	private final JLabel battery0Voltage = new JLabel("B1: 0.00 V");
	private final JLabel battery1Voltage = new JLabel("B2: 0.00 V");
	private final JLabel mphLabel = new JLabel("V: 0.00 MPH");
	private final JLabel motorCurrent = new JLabel("MC: 0.00 A");
	
	private final JTextArea logText = new JTextArea(10,60);
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
	private void updateLabel(JLabel label, boolean value){
		label.setBackground(value ? trueColor : falseColor);		
	}
	
	protected void updateDisplay() {
		VehicleState vState = state.getVehicleState();
		logText.setText(logger.getRecentLogText(10));
		if(vState != null){
			updateLabel(keyLabel, vState.isKey());
			updateLabel(gpsLockLabel, !vState.isGPSWarning());
			updateLabel(chargingLabel, vState.isBatteryCharging());
			updateLabel(prechargeLabel,vState.isPrechargeEnable());
			updateLabel(voltageLabel, vState.isCapOverVoltage());
			updateLabel(dischargeLabel, vState.isDischargeEnable());
			updateLabel(capacitorLabel, vState.isSourceSelector());
			updateLabel(batteryLabel, !vState.isSourceSelector());

			battery0Voltage.setText("B1: "+df.format(vState.getBattery0Voltage()) +" V");
			battery1Voltage.setText("B2: "+df.format(vState.getBattery1Voltage()) +" V");
			mphLabel.setText("V: "+df.format(vState.getBikeSpeedMPH())+" MPH");
			motorCurrent.setText("MC: "+ df.format(vState.getMotorCurrent()) +" A");

		}
	}

	private void constructGui(){
		 JFrame f = new JFrame("ChargeCycle Information");
	     // Sets the behavior for when the window is closed
	     f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	     f.setLayout(new FlowLayout());
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
	     batteryLabel.setOpaque(true);
	     capacitorLabel.setOpaque(true);
	     gpsLockLabel.setOpaque(true);
	     
	     JPanel indicatorsPanel = new JPanel();
	     indicatorsPanel.setLayout(new GridBagLayout());
	     GridBagConstraints gbc = new GridBagConstraints(); 
	     gbc.gridy = 0;
	     gbc.gridx = 0;
	     indicatorsPanel.add(keyLabel,gbc);
	     gbc.gridx = 1;
	     indicatorsPanel.add(gpsLockLabel, gbc);
	     gbc.gridx = 2;
	     indicatorsPanel.add(prechargeLabel, gbc);
	     gbc.gridy = 1;
	     gbc.gridx = 0;
	     indicatorsPanel.add(chargingLabel, gbc);
	     gbc.gridx = 1;
	     indicatorsPanel.add(dischargeLabel, gbc);
	     gbc.gridx = 2;
	     indicatorsPanel.add(voltageLabel, gbc);
	     indicatorsPanel.setBorder(BorderFactory.createTitledBorder("Indicators"));
	     
	     JPanel sourcePanel = new JPanel();
	     sourcePanel.setLayout(new BoxLayout(sourcePanel, BoxLayout.Y_AXIS));
	     sourcePanel.setBorder(BorderFactory.createTitledBorder("Source"));
	     sourcePanel.add(batteryLabel);
	     sourcePanel.add(capacitorLabel);

	     JPanel gaugesPanel = new JPanel();
	     gaugesPanel.setLayout(new BoxLayout(gaugesPanel, BoxLayout.Y_AXIS));
	     gaugesPanel.setBorder(BorderFactory.createTitledBorder("Gauges"));
	     gaugesPanel.add(motorCurrent);
	     gaugesPanel.add(mphLabel);
	     gaugesPanel.add(battery0Voltage);
	     gaugesPanel.add(battery1Voltage);
	     
	     JPanel logPanel = new JPanel();
	     logPanel.add(logText);
	     logPanel.setBorder(BorderFactory.createTitledBorder("Event Log"));
	     
	     f.add(indicatorsPanel);
	     f.add(gaugesPanel);
	     f.add(sourcePanel);
	 	
	     f.add(logPanel);
	     f.pack();
	     f.setVisible(true);
	}


	
}
