package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.swixml.*;
import org.swixml.SwingEngine;
import java.io.*;
import java.util.*;
import java.net.*;
import org.jdom.*;
import java.sql.*;

public class RecallGUI extends AbstractGUI {

    public JButton save, cancel;
    public JComboBox recallType, recallInterval;
    public JComboBox Rday, Rmonth, Ryear;
    public JComboBox LRday, LRmonth, LRyear;
   
    private RecordManager manager;
    private int patnum;

    public RecallGUI(ApplicationFrame af, RecordManager manager, int patnum) {
	super(af);
	this.manager = manager;
	this.patnum = patnum;
	loadComboBoxes();
	loadValues();
    }

    protected String getGUIxml() {
	return "/xml/Recall.xml";
    }

    public void loadComboBoxes() {
	Rmonth.addItem("Month");
	LRmonth.addItem("Month");
	Rday.addItem("Day");
	LRday.addItem("Day");
	Ryear.addItem("Year");
	LRyear.addItem("Year");
	for (int i = 1; i <= 12; i++) {
	    Rmonth.addItem(new Integer(i));
	    LRmonth.addItem(new Integer(i));
	}
	for (int i = 1; i <= 31; i++) {
	    Rday.addItem(new Integer(i));
	    LRday.addItem(new Integer(i));
	}
	/* should be this year as the last idx */
	for (int i = 2000; i <= 2020; i++) {
	    Ryear.addItem(new Integer(i));
	    LRyear.addItem(new Integer(i));
	}
	recallType.addItem("");
	recallType.addItem("Prophylaxis");
	recallType.addItem("Office Reline");
	recallInterval.addItem("");
	recallInterval.addItem("3 Months / 92 Days");
	recallInterval.addItem("6 Months / 184 Days");
	recallInterval.addItem("12 Months / 365 Days");
    }

    public void loadValues() {
	if (getRecordManager().recallAlreadyExists(getPatNum())) {
	    ResultSet rs = getRecordManager().getRecallWithPatNum(getPatNum());
	    try {
		//add recall type and last visit later
		rs.first();
		String[] lastR = rs.getString(1).split("-");
		LRyear.setSelectedItem(new Integer(lastR[0]));
		LRmonth.setSelectedItem(new Integer(lastR[1]));
		LRday.setSelectedItem(new Integer(lastR[2]));
		String[] nextR = rs.getString(2).split("-");
		Ryear.setSelectedItem(new Integer(nextR[0]));
		Rmonth.setSelectedItem(new Integer(nextR[1]));
		Rday.setSelectedItem(new Integer(nextR[2]));
		int interval = rs.getInt(3);
		if (interval < 150
) {
		    recallInterval.setSelectedIndex(1);
		}
		else if (interval >= 150 && interval <= 250 ) {
		    recallInterval.setSelectedIndex(2);
		}
		else {
		    recallInterval.setSelectedIndex(3);
		}
		recallType.setSelectedItem(rs.getString(4));
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    public RecordManager getRecordManager() {
	return manager;
    }

    public int getPatNum() {
	return patnum;
    }
    
    public String getRecallType() {
	int rt = recallType.getSelectedIndex();
	if (rt == 1) {
	    System.out.println("prophy");
	    return "Prophylaxis";
	}
	if (rt == 2) {
	    System.out.println("reline");
	    return "Office Reline";
	}
	return null;
    }
    
    public String getNextRecallDate() {
	String yr = (Ryear.getSelectedIndex()+1999)+"";
	String mo = Rmonth.getSelectedIndex()+"";
	String da = Rday.getSelectedIndex()+"";

	String recallString = yr + "-" + mo + "-" + da;
	if (recallString.equals("1999-0-0")) {
	    recallString = "0001-01-01";
	}

	return recallString;
    }

    public String getLastRecallDate() {
	String yr = (LRyear.getSelectedIndex()+1999)+"";
	String mo = LRmonth.getSelectedIndex()+"";
	String da = LRday.getSelectedIndex()+"";

	String recallString = yr + "-" + mo + "-" + da;
	if (recallString.equals("1999-0-0")) {
	    recallString = "0001-01-01";
	}

	return recallString;
    }


    public int getRecallInterval() {
	int interval = recallInterval.getSelectedIndex();
	if (interval == 1) {
	    return 92;
	}
	if (interval == 2) {
	    return 184;
	}
	if (interval == 3) {
	    return 365;
	}
	return -1;
    }

    protected void connectActionListeners() {
	actionableItems.add(save);
	actionableItems.add(cancel);
	RecallListener listener = new RecallListener(this);
    }

}