package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.swixml.*;
import org.swixml.SwingEngine;

import java.util.Calendar;


public class PrintBDayGUI extends AbstractGUI {

    public XGridBagConstraints gbc, gbc1, gbc2;
    public JPanel panel;
    public JComboBox month, missing;
    public JButton create, cancel, choose;
    public JTextField filePath;

    public PrintBDayGUI(ApplicationFrame af) {
	super(af);
	loadComboBoxes();
    }

    public RecordManager getRecordManager() {
	//return manager;
	return getExecutioner().getPatientRecordManager();
    }

    public void loadComboBoxes() {
	month.addItem("Month");
	month.addItem("January");
	month.addItem("February");
	month.addItem("March");
	month.addItem("April");
	month.addItem("May");
	month.addItem("June");
	month.addItem("July");
	month.addItem("August");
	month.addItem("September");
	month.addItem("October");
	month.addItem("November");
	month.addItem("December");
	for (int i = 0; i < 30; i++) {
	    missing.addItem(new Integer(i));
	}

	Calendar cal = Calendar.getInstance();
	int currentMonth = cal.get(Calendar.MONTH) + 1;
	month.setSelectedIndex(currentMonth);

	if (JDentPro.staticRunningOnMacOSX()) {
	    filePath.setText(System.getProperty("user.home")+"/Desktop");
	}
    }

    protected String getGUIxml() {
	return "/xml/PrintBDay.xml";
    }

    public String getMonth() {
	if (month.getSelectedIndex() == 0) {
	    new ErrorMessage("Invalid Month Selection");
	}
	if (month.getSelectedIndex() == 1) {
	    return "01";
	}
	if (month.getSelectedIndex() == 2) {
	    return "02";
	}
	if (month.getSelectedIndex() == 3) {
	    return "03";
	}
	if (month.getSelectedIndex() == 4) {
	    return "04";
	}
	if (month.getSelectedIndex() == 5) {
	    return "05";
	}
	if (month.getSelectedIndex() == 6) {
	    return "06";
	}
	if (month.getSelectedIndex() == 7) {
	    return "07";
	}
	if (month.getSelectedIndex() == 8) {
	    return "08";
	}
	if (month.getSelectedIndex() == 9) {
	    return "09";
	}
	if (month.getSelectedIndex() == 10) {
	    return "10";
	}
	if (month.getSelectedIndex() == 11) {
	    return "11";
	}
	if (month.getSelectedIndex() == 12) {
	    return "12";
	}
	return null;
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
	PrintBDayListener listener = new PrintBDayListener(this);
    }

}