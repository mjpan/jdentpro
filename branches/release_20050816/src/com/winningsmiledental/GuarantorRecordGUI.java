
package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.swixml.*;
import org.swixml.SwingEngine;
import java.sql.*;

public class GuarantorRecordGUI extends AbstractGUI {

    public JPanel panel;
    public XGridBagConstraints gbc, gbc1, gbc2;
    public JTextField RCN, LastName, FirstName, SSN, Telephone;
    public JTextField Address, City, State, ZipCode;
    public JComboBox Sex, month, day, year;
    public JButton updateFamily, recall, save, cancel, change;
    public JButton self;

    //private RecordManager rManager;
    private boolean isNew;
    private int ptRCN;
    private int gPatNum;

    private GuarantorRecord record;

    public GuarantorRecordGUI(ApplicationFrame af, int ptRCN) {
	super(af);
	loadComboBoxes();
	isNew = true;
	config(ptRCN);
    }

    public GuarantorRecordGUI(ApplicationFrame af, int ptRCN, int gPatNum) {
	super(af);
	loadComboBoxes();
	isNew = false;
	config(ptRCN,gPatNum);
    }

    // looks like this should be somewhere else
    protected void setSelfAsGuarantor() throws Exception {
	int patnum = getExecutioner().getPatientRecordManager().getPatNumOfPatientWithRCN(getPatientRCN());
	isNew = false;
	setGuarantor(patnum);

    }

    public void config(int ptRCN) {
	this.ptRCN = ptRCN;
	emptyValues();
    }

    public void config(int ptRCN, int gPatNum) {
	this.ptRCN = ptRCN;
	setGuarantor(gPatNum);
    }

    protected void setGuarantor(int guarantorID) {
	gPatNum = guarantorID;
	//if (!isNewGuarantor()) {
	loadValues(guarantorID);
	//}
    }

    public int getPatientRCN() {
	return ptRCN;
    }

    public int getGuarantorPatNum() {
	return gPatNum;
    }

    public boolean isNewGuarantor() {
	return isNew;
    }

    public GuarantorRecord getRecord() throws Exception {
	if (record == null) {
	    throw new Exception("GuarantorRecordGUI.getRecord() needs to be implemented");
	}
	return record;
    }

    public void emptyValues() {
	RCN.setText("");
	LastName.setText("");
	FirstName.setText("");
	Address.setText("");
	City.setText("");
	State.setText("");
	ZipCode.setText("");
	Telephone.setText("");

	//SSN.setText("");
	generateSSN();
	
	Sex.setSelectedIndex(0);
	year.setSelectedIndex(0);
	month.setSelectedIndex(0);
	day.setSelectedIndex(0);
    }

    public void loadValues(int patnum) {
	RCN.setText(Integer.toString(patnum));
	/*
	ResultSet rs = getRecordManager().getRecordWithPatNum(patnum);
	try {
	    rs.next();
	    LastName.setText(rs.getString(1));
	    FirstName.setText(rs.getString(2));
	    Address.setText(rs.getString(3));
	    City.setText(rs.getString(4));
	    State.setText(rs.getString(5));
	    ZipCode.setText(rs.getString(6));
	    Telephone.setText(rs.getString(7));
	    SSN.setText(rs.getString(8));
	    Sex.setSelectedIndex(rs.getInt(10)+1);
	    String[] bday = rs.getString(9).split("-");
	    year.setSelectedItem(new Integer(bday[0]));
	    month.setSelectedItem(new Integer(bday[1]));
	    day.setSelectedItem(new Integer(bday[2]));
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	*/
	try {
	 record = 
	    (GuarantorRecord) getExecutioner().getGuarantorRecordManager().getRecordUsingInternalID(patnum);
	LastName.setText(record.getLastName());
	FirstName.setText(record.getFirstName());
	Address.setText(record.getAddressStreet());
	City.setText(record.getAddressCity());
	State.setText(record.getAddressState());
	ZipCode.setText(record.getAddressZip());
	Telephone.setText(record.getHomePhone());
	SSN.setText(record.getSsn());
	Sex.setSelectedIndex(record.getGender()+1);
	String[] bday = record.getBirthday().split("-");
	year.setSelectedItem(new Integer(bday[0]));
	month.setSelectedItem(new Integer(bday[1]));
	day.setSelectedItem(new Integer(bday[2]));
	}
	catch (Exception e) {
	    new ErrorMessage("failed on loading patient record >> "+e.getMessage());
	}
    }

    public void loadComboBoxes() {
	Sex.addItem("");
	Sex.addItem("Male");
	Sex.addItem("Female");
	month.addItem("Month");
	day.addItem("Day");
	year.addItem("Year");
	for (int i = 1; i <= 12; i++) {
	    month.addItem(new Integer(i));
	}
	for (int i = 1; i <= 31; i++) {
	    day.addItem(new Integer(i));
	}
	/* should be this year as the last idx */
	for (int i = 1900; i <= 2020; i++) {
	    year.addItem(new Integer(i));
	}
    }

    public RecordManager getRecordManager() {
	//return rManager;
	return getExecutioner().getGuarantorRecordManager();
    }

    public String getLastName() {
	return LastName.getText();
    }

    public String getFirstName() {
	return FirstName.getText();
    }

    public String getAddress() {
	return Address.getText();
    }

    public String getCity() {
	return City.getText();
    }

    public String getState() {
	return State.getText();
    }

    public String getZipCode() {
	return ZipCode.getText();
    }

    public String getHmPhone() {
	return Telephone.getText();
    }

    public int getGender() {
	String gender = (String)Sex.getSelectedItem();
	if (gender.equals("Male")) {
	    return 0;
	}
	if (gender.equals("Female")) {
	    return 1;
	}
	return 0;
    }

    public void generateSSN() {
	SSN.setText(((JDentProExecutioner)getExecutioner()).generateSSN());
    }

    public String getSSN() {
	String ssn = SSN.getText();
	/*
	ssn = ssn.replaceAll("\\D", "");
	if (ssn.length() == 9) {
	    return ssn;
	}
	*/
	return ssn;
    }

    public String getBirthdate() {
	String yr = (year.getSelectedIndex()+1899)+"";
	String mo = month.getSelectedIndex()+"";
	String da = day.getSelectedIndex()+"";

	String bdayString = yr + "-" + mo + "-" + da;
	if (bdayString.equals("1900-0-0")) {
	    bdayString = "0001-1-1";
	}
	return bdayString;
    }

    protected String getGUIxml() {
	return "/xml/GuarantorRecord.xml";
    }

    protected void connectActionListeners() {
	actionableItems.add(updateFamily);
	actionableItems.add(recall);
	actionableItems.add(save);
	actionableItems.add(cancel);
	actionableItems.add(change);
	actionableItems.add(self);
	GuarantorRecordListener listener = new GuarantorRecordListener(this);
    }

}