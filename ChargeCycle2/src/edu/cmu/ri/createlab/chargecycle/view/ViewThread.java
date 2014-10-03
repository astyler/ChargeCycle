/**
 * 
 */
package edu.cmu.ri.createlab.chargecycle.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import edu.cmu.ri.createlab.chargecycle.logging.EventLogger;
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
	private final JLabel prechargeLabel = new JLabel("<HTML>Capacitor<br>Precharge");
	private final JLabel gpsLockLabel = new JLabel("<HTML><center>No GPS<br>Lock");
	private final JLabel dischargeLabel = new JLabel("Discharge");
	private final JLabel chargingLabel = new JLabel("<html><center>Battery<br>Charging");
	private final JLabel voltageLabel = new JLabel("<HTML><center>Cap Over<br>Voltage");
	private final JLabel keyLabel = new JLabel("Key");
	private final JLabel capacitorLabel = new JLabel("Capacitor");
	private final JLabel batteryLabel = new JLabel("Battery");
	
	//private final JLabel battery0Voltage = new JLabel("B1: 0.00 V");
	private final JLabel battery1Voltage = new JLabel("B: 00.00 V");
	private final JLabel mphLabel = new JLabel("V: 00.00 MPH");
	private final JLabel motorCurrent = new JLabel("MC: 000.00 A");
	
	private final JTextArea logText = new JTextArea(10,57);
	private final Color trueWarningColor = Color.RED;
	private final Color falseWarningColor = new Color(80,0,0);
	private final Color trueIndicatorColor = new Color(60,255,120);
	private final Color falseIndicatorColor = new Color(20,85,40);
	
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
	private void updateWarning(JLabel label, boolean value){
		label.setBackground(value ? trueWarningColor : falseWarningColor);		
	}
	
	private void updateIndicator(JLabel label, boolean value){
		label.setBackground(value ? trueIndicatorColor : falseIndicatorColor);		
	}
	
	protected void updateDisplay() {
		VehicleState vState = state.getVehicleState();
		logText.setText(logger.getRecentLogText(10));
		if(vState != null){
			updateIndicator(keyLabel, vState.isKey());
			updateWarning(gpsLockLabel, vState.isGPSWarning());
			updateIndicator(chargingLabel, vState.isBatteryCharging());
			updateIndicator(prechargeLabel,vState.isPrechargeEnable());
			updateWarning(voltageLabel, vState.isCapOverVoltage());
			updateIndicator(dischargeLabel, vState.isDischargeEnable());
			updateIndicator(capacitorLabel, vState.isSourceSelector());
			updateIndicator(batteryLabel, !vState.isSourceSelector());

			//battery0Voltage.setText("B1: "+df.format(vState.getBattery0Voltage()) +" V");
			battery1Voltage.setText("B: "+df.format(vState.getBattery1Voltage()) +" V");
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
	     
	    
	     initLabelIndicator(keyLabel);
	     initLabelIndicator(prechargeLabel);
	     initLabelIndicator(chargingLabel);
	     initLabelIndicator(dischargeLabel);
	     initLabelIndicator(voltageLabel);
	     initLabelIndicator(batteryLabel);
	     initLabelIndicator(capacitorLabel);
	     initLabelIndicator(gpsLockLabel);
	 
	     
	     JPanel indicatorsPanel = new JPanel();
	     indicatorsPanel.setLayout(new FlowLayout());
		 indicatorsPanel.add(keyLabel);	     
	     indicatorsPanel.add(chargingLabel);
	     indicatorsPanel.add(dischargeLabel);
	     indicatorsPanel.add(prechargeLabel);     
	     indicatorsPanel.setBorder(BorderFactory.createTitledBorder("Indicators"));
	     
	     JPanel warningsPanel = new JPanel();
	     warningsPanel.setLayout(new FlowLayout());
	     warningsPanel.add(gpsLockLabel);
	     warningsPanel.add(voltageLabel);
	     warningsPanel.setBorder(BorderFactory.createTitledBorder("Warnings"));
	     
	     JPanel sourcePanel = new JPanel();
//	     sourcePanel.setLayout(new BoxLayout(sourcePanel, BoxLayout.Y_AXIS));
	     sourcePanel.setLayout(new FlowLayout());
	     sourcePanel.setBorder(BorderFactory.createTitledBorder("Source"));
	     sourcePanel.add(batteryLabel);
	     sourcePanel.add(capacitorLabel);

	     JPanel gaugesPanel = new JPanel();
	     initLabelGauge(motorCurrent);
	     initLabelGauge(mphLabel);
	     //initLabelGauge(battery0Voltage);
	     initLabelGauge(battery1Voltage);
	       
	     gaugesPanel.setLayout(new BoxLayout(gaugesPanel, BoxLayout.Y_AXIS));
	     gaugesPanel.setBorder(BorderFactory.createTitledBorder("Gauges"));
	     gaugesPanel.add(motorCurrent);
	     gaugesPanel.add(mphLabel);
	//   gaugesPanel.add(battery0Voltage);
	     gaugesPanel.add(battery1Voltage);
	     
	     JPanel logPanel = new JPanel();
	     logPanel.add(logText);
	     logText.setFont(logText.getFont().deriveFont(18f));
	     logPanel.setBorder(BorderFactory.createTitledBorder("Event Log"));
	     
	     f.add(indicatorsPanel);
	     f.add(warningsPanel);
	    
	     f.add(sourcePanel);
	     f.add(gaugesPanel);
	 	
	     f.add(logPanel);
	     f.pack();
	     f.setExtendedState(JFrame.MAXIMIZED_BOTH);
	     f.setVisible(true);
	}

	private void initLabelIndicator(JLabel label){
		label.setOpaque(true);
		label.setPreferredSize(new Dimension(95,70));
		label.setBorder(BorderFactory.createBevelBorder(1));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(label.getFont().deriveFont(18f));
	}
	
	private void initLabelGauge(JLabel label){
		label.setFont(label.getFont().deriveFont(18f));
	}

	
}
