package edu.cmu.ri.createlab.chargecycle.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.cmu.ri.createlab.chargecycle.comm.CommParser;
import edu.cmu.ri.createlab.chargecycle.comm.ParseException;
import edu.cmu.ri.createlab.chargecycle.model.State;
import edu.cmu.ri.createlab.chargecycle.model.VehicleState;

public class AnalysisPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final File logFileDirectory;
	private final JComboBox<LogFile> logSelectorBox = new JComboBox<LogFile>();
	private final JLabel efficiencyLabel = new JLabel("Efficiency: 0.000 miles / AmpHour");
	private final JLabel ampHourLabel = new JLabel("AH Spent: 00.000 AmpHours");
	private final JLabel distanceLabel = new JLabel("Distance Traveled: 00.000 Miles");
	private final JLabel timeLabel = new JLabel("Time Elapsed: 00000 seconds");
	private final DecimalFormat df = new DecimalFormat("0.000");
	
	//private final double ENCODER_TICKS_TO_MILES = 36490.6666667;
	
	public AnalysisPanel(File logFileDirectory){
		this.logFileDirectory = logFileDirectory;
	}
	
	
	public void initPanel(){
		this.setLayout(new FlowLayout());
		JPanel resultsPanel = new JPanel();
		resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
		
		
		List<File> fileList = Arrays.asList(this.logFileDirectory.listFiles());
	
		Collections.sort(fileList);		
		
		for(File f : fileList){
			if(f.getName().contains("CCLog")){
				this.logSelectorBox.addItem(new LogFile(f));
			}
		}
		
		//remove last item, the current log
		if(this.logSelectorBox.getItemCount() > 0){
			this.logSelectorBox.removeItemAt(this.logSelectorBox.getItemCount()-1);
		}
		
		this.logSelectorBox.addActionListener(new ActionListener() {
            
			@Override
            public void actionPerformed(ActionEvent e) {    
                LogFile s = (LogFile) logSelectorBox.getSelectedItem();
                analyzeAndDisplay(s);
            }
			
		});
		resultsPanel.add(this.efficiencyLabel);
		resultsPanel.add(this.ampHourLabel);
		resultsPanel.add(this.distanceLabel);
		resultsPanel.add(this.timeLabel);
		resultsPanel.setBorder(BorderFactory.createTitledBorder("Analysis"));
		this.logSelectorBox.setBorder(BorderFactory.createTitledBorder("Log Selection"));
		//this.logSelectorBox.setMaximumSize(this.logSelectorBox.getPreferredSize());
		this.add(this.logSelectorBox);
		this.add(resultsPanel);
		//select most recent log
		logSelectorBox.setSelectedIndex(logSelectorBox.getItemCount()-1);
	}
	
	protected void analyzeAndDisplay(LogFile lf){
		BufferedReader br;
	
		State fakeState = new State();
		
		try {
			br = new BufferedReader(new FileReader(lf.getFile()));
			String line;	
			while ((line = br.readLine()) != null) {
				try {
					VehicleState current = parseLogLine(line);					
					fakeState.setVehicleState(current);					
				} catch (ParseException e) {
					//e.printStackTrace();
					//ignore null state parse exceptions... for now.
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		this.efficiencyLabel.setText("Efficiency: " + df.format(fakeState.getEfficiency()) +  " miles / amphour");
		this.ampHourLabel.setText("AH Spent: "+df.format(fakeState.getAmpHoursSpent()) +" amp hours");
		this.distanceLabel.setText("Distance Traveled: "+df.format(fakeState.getDistanceTraveled())+ " miles");
		this.timeLabel.setText("Time Elapsed "+(int)fakeState.getSecondsElapsed() + " seconds");
		
	}


	private VehicleState parseLogLine(String line) throws ParseException {
		if(line.contains("Null")) throw new ParseException("Null state");
		//yyyy.MM.dd, HH:mm:ss, SSS, REST
		String[] parts = line.split(", ",4);
		String vstate = "!!!,"+parts[3];
		
		//yyyy.MM.dd, HH:mm:ss, SSS					
		String date = parts[0];
		String time = parts[1];
		int millis = Integer.parseInt(parts[2]);
		
		//yyyy.MM.dd
		parts = date.split("\\.");
		int year = Integer.parseInt(parts[0]);
		int month = Integer.parseInt(parts[1]) - 1;//month is zero based
		int day = Integer.parseInt(parts[2]);
		
		//HH:mm:ss
		parts = time.split(":");
		int hour = Integer.parseInt(parts[0]);
		int minutes = Integer.parseInt(parts[1]);
		int seconds = Integer.parseInt(parts[2]);
		
		Calendar cTime = Calendar.getInstance();
		cTime.clear();
		cTime.set(Calendar.MILLISECOND, millis);
		cTime.set(year, month, day, hour, minutes, seconds);
		
		VehicleState current = CommParser.Parse(cTime, vstate);
		return current;
	}
	
	public void updatePanel(){
		
	}
		
	protected class LogFile{
		private final File f;
		private final String id;
		
		LogFile(File f){
			this.f = f;
			this.id = createName(f);
		}
		
		private String createName(File f){
			String fileName = f.getName();
			String datetime = fileName.substring(fileName.indexOf('_')+1, fileName.indexOf('.'));
			String date = datetime.substring(0, datetime.indexOf('_')).replace('-', '/');
			String time = datetime.substring(datetime.indexOf('_')+2).replace('-', ':');
			return date+" "+time;
		}
		
		public String toString(){
			return id;
		}
		
		public File getFile(){
			return this.f;
		}
	}
}
