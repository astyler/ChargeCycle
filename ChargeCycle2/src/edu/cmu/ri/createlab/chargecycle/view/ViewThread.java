/**
 * 
 */
package edu.cmu.ri.createlab.chargecycle.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import edu.cmu.ri.createlab.chargecycle.logging.EventLogger;
import edu.cmu.ri.createlab.chargecycle.model.State;
import edu.cmu.ri.createlab.chargecycle.model.VehicleState;

/**
 * @author astyler THe thread controlling the view
 */
public class ViewThread implements Runnable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	private final State state;
	private final EventLogger logger;
	private final DecimalFormat df = new DecimalFormat("0.00");
	
	private final JTabbedPane mainWindow = new JTabbedPane();
	
	//sensor panel and components
	private final JPanel sensorPanel = new JPanel();
	private final JLabel prechargeLabel = new JLabel("<HTML>Capacitor<br>Precharge");
	private final JLabel gpsLockLabel = new JLabel("<HTML><center>No GPS<br>Lock");
	private final JLabel dischargeLabel = new JLabel("Discharge");
	private final JLabel chargingLabel = new JLabel("<html><center>Battery<br>Charging");
	private final JLabel voltageLabel = new JLabel("<HTML><center>Cap Over<br>Voltage");
	private final JLabel keyLabel = new JLabel("Key");
	private final JLabel capacitorLabel = new JLabel("Capacitor");
	private final JLabel batteryLabel = new JLabel("Battery");
	// private final JLabel battery0Voltage = new JLabel("B1: 0.00 V");
	private final JLabel battery1Voltage = new JLabel("B: 00.00 V");
	private final JLabel mphLabel = new JLabel("V: 00.00 MPH");
	private final JLabel batteryCurrent = new JLabel("BC: 000.00 A");

	//logging panel and components
	private final JPanel logPanel = new JPanel();
	private final JTextArea logText = new JTextArea(10,57);
	
	//analysis panel and components
	private final JPanel analysisPanel = new JPanel();
	
	
	
	private final Color trueWarningColor = Color.RED;
	private final Color falseWarningColor = new Color(80, 0, 0);
	private final Color trueIndicatorColor = new Color(60, 255, 120);
	private final Color falseIndicatorColor = new Color(20, 85, 40);

	private Timer updateDisplayTimer;

	//for automatic tab switching when vehicle state changes
	private boolean priorKey = false;
	private boolean priorCharge = false;
	
	public ViewThread(State state, EventLogger logger) {
		this.state = state;
		this.logger = logger;
	}

	@Override
	public void run() {
		this.constructGui();
		this.logger.logEvent("GUI Built.");

		this.updateDisplayTimer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewThread.this.updateDisplay();
			}
		});

		this.updateDisplayTimer.start();

	}

	private void updateWarning(JLabel label, boolean value) {
		label.setBackground(value ? this.trueWarningColor : this.falseWarningColor);
	}

	private void updateIndicator(JLabel label, boolean value) {
		label.setBackground(value ? this.trueIndicatorColor : this.falseIndicatorColor);
	}

	protected void updateDisplay() {
		VehicleState vState = state.getVehicleState();
		
		if(vState != null){
			if( !this.priorCharge && vState.isBatteryCharging() ){
				//battery just started charging, change to analysis panel
				this.mainWindow.setSelectedComponent(this.analysisPanel);
			}
			else if( !this.priorKey && vState.isKey() ){
				//key just turned on, switch to sensors panel
				this.mainWindow.setSelectedComponent(this.sensorPanel);
			}
			else if( this.priorCharge && !vState.isBatteryCharging() && vState.isKey() ){
				//we just stopped charging, and key is on, switch to sensors panel
				this.mainWindow.setSelectedComponent(this.sensorPanel);
			}
			
			this.priorCharge = vState.isBatteryCharging();
			this.priorKey = vState.isKey();
		}
		
		JPanel selected = (JPanel) mainWindow.getSelectedComponent();
		
		if(selected == sensorPanel){
			updateSensorPanel();
		}
		else if(selected  == logPanel){
			updateLogPanel();
		}
		
	}

	private void updateLogPanel() {
		this.logText.setText(this.logger.getRecentLogText(10));
	}

	private void updateSensorPanel() {
		VehicleState vState = state.getVehicleState();
		if (vState != null) {
			this.updateIndicator(this.keyLabel, vState.isKey());
			this.updateWarning(this.gpsLockLabel, vState.isGPSWarning());
			this.updateIndicator(this.chargingLabel, vState.isBatteryCharging());
			this.updateIndicator(this.prechargeLabel, vState.isPrechargeEnable());
			this.updateWarning(this.voltageLabel, vState.isCapOverVoltage());
			this.updateIndicator(this.dischargeLabel, vState.isDischargeEnable());
			this.updateIndicator(this.capacitorLabel, vState.isSourceSelector());
			this.updateIndicator(this.batteryLabel, !vState.isSourceSelector());

			// battery0Voltage.setText("B1: "+df.format(vState.getBattery0Voltage())
			// +" V");
			this.battery1Voltage.setText("B: " + this.df.format(vState.getBattery1Voltage()) + " V");
			this.mphLabel.setText("V: " + this.df.format(vState.getBikeSpeedMPH()) + " MPH");
			this.batteryCurrent.setText("BC: " + this.df.format(vState.getBatteryCurrent()) + " A");

		}
	}

	private void constructGui() {
		JFrame f = new JFrame("ChargeCycle Information");
		// Sets the behavior for when the window is closed
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		f.addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				ViewThread.this.updateDisplayTimer.stop();
				ViewThread.this.state.setAlive(false);
				ViewThread.this.logger.logEvent("View window closed.");
				// window closed, kill state which will kill comms, flush log,
				// and exit
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

		mainWindow.add("Sensors", sensorPanel);
		mainWindow.add("Log", logPanel);
		mainWindow.add("Analysis", analysisPanel);
		
		initSensorPanel();
		initLogPanel();
		initAnalysisPanel();
		
		mainWindow.setSelectedComponent(logPanel);
		f.add(mainWindow);

		f.pack();
		f.setExtendedState(Frame.MAXIMIZED_BOTH);
		f.setVisible(true);
	}

	private void initAnalysisPanel(){
		//add components here
	}
	
	private void initLogPanel(){
		this.logPanel.add(this.logText);
		this.logText.setFont(this.logText.getFont().deriveFont(18f));
		this.logText.setBorder(BorderFactory.createTitledBorder("Event Log"));
	}
	
	private void initSensorPanel() {
		this.sensorPanel.setLayout(new FlowLayout());
		
		this.initLabelIndicator(this.keyLabel);
		this.initLabelIndicator(this.prechargeLabel);
		this.initLabelIndicator(this.chargingLabel);
		this.initLabelIndicator(this.dischargeLabel);
		this.initLabelIndicator(this.voltageLabel);
		this.initLabelIndicator(this.batteryLabel);
		this.initLabelIndicator(this.capacitorLabel);
		this.initLabelIndicator(this.gpsLockLabel);

		JPanel indicatorsPanel = new JPanel();
		indicatorsPanel.setLayout(new FlowLayout());
		indicatorsPanel.add(this.keyLabel);
		indicatorsPanel.add(this.chargingLabel);
		indicatorsPanel.add(this.dischargeLabel);
		indicatorsPanel.add(this.prechargeLabel);
		indicatorsPanel.setBorder(BorderFactory.createTitledBorder("Indicators"));

		JPanel warningsPanel = new JPanel();
		warningsPanel.setLayout(new FlowLayout());
		warningsPanel.add(this.gpsLockLabel);
		warningsPanel.add(this.voltageLabel);
		warningsPanel.setBorder(BorderFactory.createTitledBorder("Warnings"));

		JPanel sourcePanel = new JPanel();
		// sourcePanel.setLayout(new BoxLayout(sourcePanel, BoxLayout.Y_AXIS));
		sourcePanel.setLayout(new FlowLayout());
		sourcePanel.setBorder(BorderFactory.createTitledBorder("Source"));
		sourcePanel.add(this.batteryLabel);
		sourcePanel.add(this.capacitorLabel);

		JPanel gaugesPanel = new JPanel();
		this.initLabelGauge(this.batteryCurrent);
		this.initLabelGauge(this.mphLabel);
		// initLabelGauge(battery0Voltage);
		this.initLabelGauge(this.battery1Voltage);
		gaugesPanel.setLayout(new BoxLayout(gaugesPanel, BoxLayout.Y_AXIS));
		gaugesPanel.setBorder(BorderFactory.createTitledBorder("Gauges"));
		gaugesPanel.add(this.batteryCurrent);
		gaugesPanel.add(this.mphLabel);
		// gaugesPanel.add(battery0Voltage);
		gaugesPanel.add(this.battery1Voltage);
		
		sensorPanel.add(indicatorsPanel);
		sensorPanel.add(warningsPanel);
		sensorPanel.add(sourcePanel);
		sensorPanel.add(gaugesPanel);
	}

	private void initLabelIndicator(JLabel label) {
		label.setOpaque(true);
		label.setPreferredSize(new Dimension(95, 70));
		label.setBorder(BorderFactory.createBevelBorder(1));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(label.getFont().deriveFont(18f));
	}

	private void initLabelGauge(JLabel label) {
		label.setFont(label.getFont().deriveFont(18f));
	}

}
