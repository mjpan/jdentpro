package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.swixml.*;
import org.swixml.SwingEngine;

import java.util.Calendar;

public class PrintRecallGUI extends AbstractGUI {

    public static int YEAR_DEFAULT = 1999;

    public XGridBagConstraints gbc, gbc1, gbc2;
    public JPanel panel;
    public JComboBox Smonth, Sday, Syear, Emonth, Eday, Eyear, missing;
    public JButton create, cancel, choose;
    public JTextField filePath;
    public RecordManager manager;

    public PrintRecallGUI(ApplicationFrame af) {
	super(af);
	loadComboBoxes();
    }

    public RecordManager getRecordManager() {
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
	int startYear = 2000;
	int numYears = 30;
	for (int i = startYear; i <= startYear+numYears; i++) {
	    Syear.addItem(new Integer(i));
	    Eyear.addItem(new Integer(i));
	}
	for (int i = 0; i <= 30; i++) {
	    missing.addItem(new Integer(i));
	}

	// now set the selected indices to to the current year and month
	Calendar cal = Calendar.getInstance();
	int currentYear = cal.get(Calendar.YEAR);
	int currentMonth = cal.get(Calendar.MONTH) + 1;
	Syear.setSelectedIndex(currentYear-YEAR_DEFAULT);
	Eyear.setSelectedIndex(currentYear-YEAR_DEFAULT);
	Smonth.setSelectedIndex(currentMonth);
	Emonth.setSelectedIndex(currentMonth);
	Sday.setSelectedIndex(1);
    }

    protected String getGUIxml() {
	return "/xml/PrintRecall.xml";
    }

    public String getStartDate() {
	String yr = (Syear.getSelectedIndex()+YEAR_DEFAULT)+"";
	String mo = Smonth.getSelectedIndex()+"";
	String da = Sday.getSelectedIndex()+"";

	String dateString = yr + "-" + mo + "-" + da;
	if (dateString.equals(YEAR_DEFAULT + "-0-0")) {
	    dateString = "0001-1-1";
	}

	return dateString;
    }

   public String getEndDate() {
	String yr = (Eyear.getSelectedIndex()+YEAR_DEFAULT)+"";
	String mo = Emonth.getSelectedIndex()+"";
	String da = Eday.getSelectedIndex()+"";

	String dateString = yr + "-" + mo + "-" + da;
	if (dateString.equals(YEAR_DEFAULT+"-0-0")) {
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