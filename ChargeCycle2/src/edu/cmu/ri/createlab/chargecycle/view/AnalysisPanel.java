package edu.cmu.ri.createlab.chargecycle.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import edu.cmu.ri.createlab.chargecycle.comm.CommParser;
import edu.cmu.ri.createlab.chargecycle.comm.ParseException;
import edu.cmu.ri.createlab.chargecycle.model.VehicleState;

public class AnalysisPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final File logFileDirectory;
	private final JComboBox<LogFile> logSelectorBox = new JComboBox<LogFile>();
	
	public AnalysisPanel(File logFileDirectory){
		this.logFileDirectory = logFileDirectory;
	}
	
	
	public void initPanel(){
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
		
		this.add(this.logSelectorBox);
		
		//select most recent log
		logSelectorBox.setSelectedIndex(logSelectorBox.getItemCount()-1);
	}
	
	protected void analyzeAndDisplay(LogFile lf){
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(lf.getFile()));
			String line;
			while ((line = br.readLine()) != null) {
				try {
					//TODO edit to read in custom timestamps
					String[] parts = line.split(",",2);
					String datetime = parts[0];
					String vstate = "!!!,"+parts[1];
					VehicleState s = CommParser.Parse(vstate);
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
