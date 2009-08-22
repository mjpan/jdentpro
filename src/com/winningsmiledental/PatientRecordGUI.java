package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.swixml.*;
import org.swixml.SwingEngine;
import java.sql.*;


public class PatientRecordGUI extends AbstractGUI {

    public JPanel panel;
    public XGridBagConstraints gbc, gbc1, gbc2;
    public JTextField RCN, LastName, FirstName, MiddleName, title, nickname, SSN, guarName;
    public JTextField Address, Line2, city, state, ZipCode,  homePhone, wkPhone, mobilePhone;
    public JComboBox Sex, month, day, year;

    public JButton guarantor, recall, cancel, save, self;
    public boolean isNewPatient;
    //public RecordManager manager;
    public int patnum, gPatNum;

    //this shows whether the patient is active
    //ie being actively recalled for dental work
    public JCheckBox active;

    private PatientRecord record;

    /*
      Creates GUI for a new patient.
    */
    public PatientRecordGUI(ApplicationFrame af) {
	super(af);
	//this.manager = manager;
	loadComboBoxes();
	setPatient();

    }

    /*
      Creates GUI for existing patient with RCN rcn.
    */
    public PatientRecordGUI(ApplicationFrame af,  int rcn) {
	super(af);
	//this.manager = manager;
	loadComboBoxes();
	setPatient(rcn);
    }

    public void setPatient() {
	emptyValues();
	isNewPatient = true;
	setRCN();

	//new Exception("called PatientRecordGUI() for new patient").printStackTrace();
    }

    public void setPatient(int rcn) {
	emptyValues();
	isNewPatient = false;
	loadValues(rcn);

	//new Exception("called PatientRecordGUI() for existing patient having rcn "+rcn).printStackTrace();
    }

    /*
      Returns true if patient does not exist in database.
    */
    public boolean isNewPatient() {
	return isNewPatient;
    }

    /*
      Returns the RecordManager.
    */
    public RecordManager getRecordManager() {
	//return manager;
	return getExecutioner().getPatientRecordManager();
    }

    /*
      Sets the texfield to next available RCN.
    */
    public void setRCN() {
	try {
	    int s = ((PatientRecordManager)getRecordManager()).getNewRCN();
	    RCN.setText(Integer.toString(s));
	}
	catch (Exception e) {
	    new ErrorMessage("error on getting rcn for new patient >> "+e.getMessage());
	}
    }

    public void emptyValues() {
	    RCN.setText("");
	    LastName.setText("");
	    FirstName.setText("");
	    MiddleName.setText("");
	    title.setText("");
	    nickname.setText("");
	    Address.setText("");
	    Line2.setText("");
	    city.setText("");
	    state.setText("");
	    ZipCode.setText("");
	    homePhone.setText("");
	    wkPhone.setText("");
	    mobilePhone.setText("");

	    //SSN.setText("");
	    generateSSN();

	    Sex.setSelectedIndex(0);
	    year.setSelectedIndex(0);
	    month.setSelectedIndex(0);
	    day.setSelectedIndex(0);
	    guarName.setText("");

	    getRecord();
    }

    public Record getRecord() {
	if (record == null) {
	    record = new PatientRecord(getLastName(),
				       getFirstName(),
				       getMiddleName(),
				       getTitle(),
				       getNickname(),
				       getAddress(),
				       getAddress2(),
				       getCity(),
				       getState(),
				       getZipCode(),
				       getHmPhone(),
				       getMobile(),
				       getWkPhone(),
				       getSSN(),
				       getGender(),
				       getBirthdate(),
				       getExecutioner().getCurrentEmployeeID());
	}
	else {
	    record.setLastName(getLastName());
	    record.setFirstName(getFirstName());
	    record.setMiddleName(getMiddleName());
	    record.setSalutation(getTitle());
	    record.setNickname(getNickname());
	    record.setAddressStreet(getAddress());
	    record.setAddressStreet2(getAddress2());
	    record.setAddressCity(getCity());
	    record.setAddressState(getState());
	    record.setAddressZip(getZipCode());
	    record.setHomePhone(getHmPhone());
	    record.setMobilePhone(getMobile());
	    record.setWorkPhone(getWkPhone());
	    record.setSsn(getSSN());
	    record.setGender(getGender());
	    record.setBirthday(getBirthdate());
	}


	return record;
    }

    /*
      Loads values of patient with RCN rcn into respective textfields & comboboxes.
    */
    public void loadValues(int rcn) {
	/*
	try {
	    ResultSet rs = getRecordManager().getRecordWithRCN(rcn);
	    rs.next();
	    patnum = rs.getInt(1);
	    RCN.setText(Integer.toString(rcn));
	    LastName.setText(rs.getString(2));
	    FirstName.setText(rs.getString(3));
	    MiddleName.setText(rs.getString(4));
	    title.setText(rs.getString(5));
	    nickname.setText(rs.getString(6));
	    Address.setText(rs.getString(7));
	    Line2.setText(rs.getString(8));
	    city.setText(rs.getString(9));
	    state.setText(rs.getString(10));
	    ZipCode.setText(rs.getString(11));
	    homePhone.setText(rs.getString(12));
	    wkPhone.setText(rs.getString(13));
	    mobilePhone.setText(rs.getString(14));
	    SSN.setText(rs.getString(15));
	    Sex.setSelectedIndex(rs.getInt(16)+1);
	    String[] bday = rs.getString(17).split("-");
	    year.setSelectedItem(new Integer(bday[0]));
	    month.setSelectedItem(new Integer(bday[1]));
	    day.setSelectedItem(new Integer(bday[2]));
	    gPatNum = rs.getInt(18);
	    if (patnum == gPatNum) {
		guarName.setText("Self");
	    }
	    else if (gPatNum > 0) {
		guarName.setText(manager.getNameOfPatientWithPatNum(gPatNum));
	    }

	    if (!getRecordManager().hasActiveRecall(patnum)) {
		active.setSelected(false);
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	*/
	try {
	    record = 
	    (PatientRecord) getExecutioner().getPatientRecordManager().getRecordUsingExternalID(rcn);
	patnum = record.getInternalID();
	RCN.setText(Integer.toString(record.getExternalID()));
	LastName.setText(record.getLastName());
	FirstName.setText(record.getFirstName());
	MiddleName.setText(record.getMiddleName());
	title.setText(record.getSalutation());
	nickname.setText(record.getNickname());
	Address.setText(record.getAddressStreet());
	Line2.setText(record.getAddressStreet2());
	city.setText(record.getAddressCity());
	state.setText(record.getAddressState());
	ZipCode.setText(record.getAddressZip());
	homePhone.setText(record.getHomePhone());
	wkPhone.setText(record.getWorkPhone());
	mobilePhone.setText(record.getMobilePhone());
	SSN.setText(record.getSsn());
	Sex.setSelectedIndex(record.getGender()+1);
	String[] bday = record.getBirthday().split("-");
	year.setSelectedItem(new Integer(bday[0]));
	month.setSelectedItem(new Integer(bday[1]));
	day.setSelectedItem(new Integer(bday[2]));
	gPatNum = record.getGuarantorID();
	if (patnum == gPatNum) {
	    guarName.setText("Self");
	}
	else if (gPatNum > 0) {
	    guarName.setText(getExecutioner().getPatientRecordManager().getNameOfPatientWithPatNum(gPatNum));
	}

	if (!getExecutioner().getPatientRecordManager().hasActiveRecall(patnum)) {
	    active.setSelected(false);
	}
	else {
	    active.setSelected(true);
	}

	
	}
	catch (Exception e) {
	    new ErrorMessage("failed on loading patient record >> "+e.getMessage());
	}
    }

    /*
      Loads selections into the birthday and gender comboboxes.
    */
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

    /*
      Returns this patient's PatNum.
    */
    public int getPatNum() {
	return patnum;
    }

    /*
      Returns this patient's guarantor PatNum.
    */
    public int getGPatNum() {
	return gPatNum;
    }

    /*
      Returns value in RCN textfield.
    */
    public int getRCN() {
	Integer x = new Integer(RCN.getText());
	return x.intValue();
    }

    /*
      Returns value in last name field.
    */
    public String getLastName() {
	return LastName.getText();
    }

    /*
      Returns value in first name field.
    */
    public String getFirstName() {
	return FirstName.getText();
    }

    /*
      Returns value in middle name field.
    */
    public String getMiddleName() {
	return MiddleName.getText();
    }

    /*
      Returns value in title/salutation field.
    */
    public String getTitle() {
	return title.getText();
    }

    /*
      Returns value in nickname field.
    */
    public String getNickname() {
	return nickname.getText();
    }

    /*
      Returns value entered in Address field.
     */
    public String getAddress() {
	return Address.getText();
    }

    /*
      Returns value entered in Address2 field.
    */
    public String getAddress2() {
	return Line2.getText();
    }

    /*
      Returns value entered in city field.

    */
    public String getCity() {
	return city.getText();
    }
    
    /*
      Returns value entered in state field.
    */
    public String getState() {
	return state.getText();
    }
    
    /*
      Returns value entered in zip code field.
    */
    public String getZipCode() {
	return ZipCode.getText();
    }

    /*
      Returns value entered in work phone textfield.
    */
    public String getWkPhone() {
	return wkPhone.getText();
    }

    /*
      Returns value entered in home phone textfield.
    */
    public String getHmPhone() {
	return homePhone.getText();
    }

    /*
      Uses values entered for birthdate and returns it as a string in the form yyyy-mm-dd.
    */
    public String getBirthdate() {
	String yr = (year.getSelectedIndex()+1899)+"";//((Integer)year.getSelectedItem()).toString();
	String mo = month.getSelectedIndex()+"";//((Integer)month.getSelectedItem()).toString();
	String da = day.getSelectedIndex()+"";//((Integer)day.getSelectedItem()).toString();

	String bdayString = yr + "-" + mo + "-" + da;
	if (bdayString.equals("1899-0-0")) {
	    bdayString = "0001-1-1";
	}

	return bdayString;
    }

    /*
      Returns value in mobile field.
    */
    public String getMobile() {
	return mobilePhone.getText();
    }
    
    /*
      Returns value in SSN textfield.
    */
    public String getSSN() {
	String ssn = SSN.getText();
	/*
	ssn = ssn.replaceAll("\\D", "");
	if (ssn.length() == 9) {
	    return ssn;
	}
	//return null;
	return "";
	*/
	return ssn;
    }
    
    public void generateSSN() {
	SSN.setText(((JDentProExecutioner)getExecutioner()).generateSSN());
    }

    /*
      Returns index of patient's selected gender.
    */
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

    /*
      Returns value in guarantor textfield.
    */
    public String getGuarantorName() {
	return guarName.getText();
    }

    /*
      Returns if 'active patient' checkbox is checked.
    */
    public boolean isActive() {
	return active.isSelected();
    }

    public JCheckBox getRecallActiveCheckbox() {
	return active;
    }

    protected String getGUIxml() {
	return "/xml/PatientRecord.xml";
    }

    protected void connectActionListeners() {
	actionableItems.add(guarantor);
	actionableItems.add(recall);
	actionableItems.add(cancel);
	actionableItems.add(save);
	actionableItems.add(self);
	PatientRecordListener listener = new PatientRecordListener(this);
    }

}