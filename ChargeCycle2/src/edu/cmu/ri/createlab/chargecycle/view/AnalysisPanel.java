package edu.cmu.ri.createlab.chargecycle.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class AnalysisPanel extends JPanel{
	private final File logFileDirectory;
	private final JComboBox<LogFile> logSelectorBox = new JComboBox<LogFile>();
	
	public AnalysisPanel(File logFileDirectory){
		this.logFileDirectory = logFileDirectory;
	}
	
	
	public void initPanel(){
		for(File f : this.logFileDirectory.listFiles()){
			if(f.getName().contains("CCLog")){
				this.logSelectorBox.addItem(new LogFile(f));
			}
		}
		
		//remove last item, the current log
		this.logSelectorBox.removeItemAt(this.logSelectorBox.getItemCount()-1);
		
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
