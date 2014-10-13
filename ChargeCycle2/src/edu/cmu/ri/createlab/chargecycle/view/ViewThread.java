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
import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
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

	private final JTabbedPane mainWindow = new JTabbedPane();
	
	//panels for main window
	private final SensorPanel sensorPanel;
	private final LogPanel logPanel;
	private final AnalysisPanel analysisPanel;
	
	private Timer updateDisplayTimer;

	//for automatic tab switching when vehicle state changes
	private boolean priorKey = false;
	private boolean priorCharge = false;
	
	public ViewThread(State state, EventLogger logger, File logFileDirectory) {
		this.state = state;
		this.logger = logger;
		this.sensorPanel = new SensorPanel();
		this.logPanel = new LogPanel(logger);
		this.analysisPanel = new AnalysisPanel(logFileDirectory);
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
			this.sensorPanel.updatePanel(state);
		}
		else if(selected  == logPanel){
			this.logPanel.updatePanel();
		}
		else if(selected == analysisPanel){
			this.analysisPanel.updatePanel();
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
		
		sensorPanel.initPanel();
		logPanel.initPanel();
		analysisPanel.initPanel();
		
		mainWindow.setSelectedComponent(logPanel);
		f.add(mainWindow);

		f.pack();
		f.setExtendedState(Frame.MAXIMIZED_BOTH);
		f.setVisible(true);
	}


}
