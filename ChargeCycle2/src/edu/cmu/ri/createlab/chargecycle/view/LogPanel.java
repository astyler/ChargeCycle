package edu.cmu.ri.createlab.chargecycle.view;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import edu.cmu.ri.createlab.chargecycle.logging.EventLogger;

public class LogPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JTextArea logText;
	private final EventLogger logger;
	
	public LogPanel(EventLogger logger){
		this.logText = new JTextArea(10,57);
		this.logger = logger;
	}
	
	public void initPanel(){
		this.add(this.logText);		
		this.logText.setFont(this.logText.getFont().deriveFont(18f));
		this.logText.setBorder(BorderFactory.createTitledBorder("Event Log"));

	}
	
	public void updatePanel(){
		this.logText.setText(this.logger.getRecentLogText(10));
	}

}
