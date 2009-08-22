package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.swixml.*;
import org.swixml.SwingEngine;

public class PrintRecallGUI extends AbstractGUI {

    public XGridBagConstraints gbc, gbc1, gbc2;
    public JPanel panel;
    public JComboBox Smonth, Sday, Syear, Emonth, Eday, Eyear, missing;
    public JButton create, cancel, choose;
    public JTextField filePath;
    public RecordManager manager;

    public PrintRecallGUI(ApplicationFrame af) {
	super(af);
	//manager = new PatientRecordManager();
	loadComboBoxes();
    }

    public RecordManager getRecordManager() {
	//return manager;
	return getExecutioner().getPatientRecordManager();
    }

    public void loadComboBoxes() {
	Smonth.addItem("Month");
	Sday.addItem("Day");
	Syear.addItem("Year");
	Emonth.addItem("Month");
	Eday.addItem("Day");
	Eyear.addItem("Year");
	for (int i = 1; i <= 12; i++) {
	    Smonth.addItem(new Integer(i));
	    Emonth.addItem(new Integer(i));	    
	}
	for (int i = 1; i <= 31; i++) {
	    Sday.addItem(new Integer(i));
	    Eday.addItem(new Integer(i));
	}
	/* should be this year as the last idx */
	for (int i = 2000; i <= 2020; i++) {
	    Syear.addItem(new Integer(i));
	    Eyear.addItem(new Integer(i));
	}
	for (int i = 0; i <= 30; i++) {
	    missing.addItem(new Integer(i));
	}
    }

    protected String getGUIxml() {
	return "/xml/PrintRecall.xml";
    }

    public String getStartDate() {
	String yr = (Syear.getSelectedIndex()+1999)+"";
	String mo = Smonth.getSelectedIndex()+"";
	String da = Sday.getSelectedIndex()+"";

	String dateString = yr + "-" + mo + "-" + da;
	if (dateString.equals("1999-0-0")) {
	    dateString = "0001-1-1";
	}

	return dateString;
    }

   public String getEndDate() {
	String yr = (Eyear.getSelectedIndex()+1999)+"";
	String mo = Emonth.getSelectedIndex()+"";
	String da = Eday.getSelectedIndex()+"";

	String dateString = yr + "-" + mo + "-" + da;
	if (dateString.equals("1999-0-0")) {
	    dateString = "0001-1-1";
	}

	return dateString;
    }

    public int getNumOfMissingLabels() {
	return missing.getSelectedIndex();
    }

    public String getFilePath() {
	return filePath.getText();
    }

    public void setFilePath(String path) {
	filePath.setText(path);
    }

    protected void connectActionListeners() {
	actionableItems.add(create);
	actionableItems.add(cancel);
	actionableItems.add(choose);
	PrintRecallListener listener = new PrintRecallListener(this);
    }

}