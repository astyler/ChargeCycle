package edu.cmu.ri.createlab.chargecycle.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import edu.cmu.ri.createlab.chargecycle.model.State;
import edu.cmu.ri.createlab.chargecycle.model.VehicleState;

public class SensorPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	
	private final Color trueWarningColor = Color.RED;
	private final Color falseWarningColor = new Color(80, 0, 0);
	private final Color trueIndicatorColor = new Color(60, 255, 120);
	private final Color falseIndicatorColor = new Color(20, 85, 40);

	private final DecimalFormat df = new DecimalFormat("0.00");
	
	public SensorPanel(){

	}
	
	public void initPanel(){
		this.setLayout(new FlowLayout());		
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
		
		this.add(indicatorsPanel);
		this.add(warningsPanel);
		this.add(sourcePanel);
		this.add(gaugesPanel);
	}
	
	public void updatePanel(State state){
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
	
	private void updateWarning(JLabel label, boolean value) {
		label.setBackground(value ? this.trueWarningColor : this.falseWarningColor);
	}

	private void updateIndicator(JLabel label, boolean value) {
		label.setBackground(value ? this.trueIndicatorColor : this.falseIndicatorColor);
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
