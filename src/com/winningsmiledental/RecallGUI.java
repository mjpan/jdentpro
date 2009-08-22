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
    public JComboBox recallTypeComboBox1, recallInterval1;
    public JComboBox recallTypeComboBox2, recallInterval2;
    public JComboBox recallTypeComboBox3, recallInterval3;
    public JComboBox Rday1, Rmonth1, Ryear1;
    public JComboBox Rday2, Rmonth2, Ryear2;
    public JComboBox Rday3, Rmonth3, Ryear3;
    public JComboBox LRday1, LRmonth1, LRyear1;
    public JComboBox LRday2, LRmonth2, LRyear2;
    public JComboBox LRday3, LRmonth3, LRyear3;

    public JCheckBox recallActive1, recallActive2, recallActive3;

    public JComboBox[] recallTypeComboBox, recallInterval;
    public JComboBox[] Rday, Rmonth, Ryear;
    public JComboBox[] LRday, LRmonth, LRyear;

    public JCheckBox[] recallActive;
   
    //private RecordManager manager;
    private int patnum;

    protected Collection recalls;

    public RecallGUI(ApplicationFrame af, int patnum) {
	super(af);
	//this.manager = manager;
	recallTypeComboBox = new JComboBox[] {recallTypeComboBox1, recallTypeComboBox2, recallTypeComboBox3};
	recallInterval = new JComboBox[] {recallInterval1, recallInterval2, recallInterval3};
	recallActive = 
	    new JCheckBox[] {recallActive1, recallActive2, recallActive3 };
	Rday = new JComboBox[] {Rday1, Rday2, Rday3};
	Rmonth = new JComboBox[] {Rmonth1, Rmonth2, Rmonth3};
	Ryear = new JComboBox[] {Ryear1, Ryear2, Ryear3};
	LRday = new JComboBox[] {LRday1, LRday2, LRday3};
	LRmonth = new JComboBox[] {LRmonth1, LRmonth2, LRmonth3};
	LRyear = new JComboBox[] {LRyear1, LRyear2, LRyear3};
	loadComboBoxes();
	setRecall(patnum);
    }

    public void setRecall(int patnum) {
	emptyValues();
	this.patnum = patnum;
	loadValues();
    }

    protected String getGUIxml() {
	return "/xml/Recall.xml";
    }

    public void loadComboBoxes() {
	for (int i = 0; i <= 2; i++) {

	    Rmonth[i].addItem("Month");
	    LRmonth[i].addItem("Month");
	    Rday[i].addItem("Day");
	    LRday[i].addItem("Day");
	    Ryear[i].addItem("Year");
	    LRyear[i].addItem("Year");

	    for (int j = 1; j <= 12; j++) {
		Rmonth[i].addItem(new Integer(j));
		LRmonth[i].addItem(new Integer(j));
	    }

	    for (int j = 1; j <= 31; j++) {
		Rday[i].addItem(new Integer(j));
		LRday[i].addItem(new Integer(j));
	    }

	    for (int j = 1992; j <= 2020; j++) {
		Ryear[i].addItem(new Integer(j));
		LRyear[i].addItem(new Integer(j));
	    }

	    recallTypeComboBox[i].addItem("");
	    /*
	    recallTypeComboBox[i].addItem("Prophylaxis");
	    recallTypeComboBox[i].addItem("OfficeReline");
	    recallTypeComboBox[i].addItem("RootPlanning");
	    */
	    for (int j=0; j<PatientRecall.RECALL_TYPE.length; j++) {
		recallTypeComboBox[i].addItem(PatientRecall.RECALL_TYPE[j]);
	    }

	    recallInterval[i].addItem("");
	    recallInterval[i].addItem("3 Months / 92 Days");
	    recallInterval[i].addItem("6 Months / 184 Days");
	    recallInterval[i].addItem("12 Months / 365 Days");
	    recallInterval[i].addItem("24 Months / 730 Days");
	    recallInterval[i].addItem("36 Months / 1095 Days");

	}
    }

    public void emptyValues() {
	for (int i = 0; i <= 2; i++) {
	    LRyear[i].setSelectedIndex(0);
	    LRmonth[i].setSelectedIndex(0);
	    LRday[i].setSelectedIndex(0);
	    Ryear[i].setSelectedIndex(0);
	    Rmonth[i].setSelectedIndex(0);
	    Rday[i].setSelectedIndex(0);
	    recallInterval[i].setSelectedIndex(0);
	    recallTypeComboBox[i].setSelectedIndex(0);
	    recallActive[i].setSelected(false);
	}

	recalls = new HashSet();
    }


    public Collection getRecalls() {
	//return recalls;
	Collection c = new ArrayList();
	c.addAll(recalls);
	return c;
    }

    public void addRecall(PatientRecall recall) {
	recalls.add(recall);
    }

    public void loadValues() {
	RecordManager manager = getRecordManager();
	int patnum = getPatNum();

	for (int i=0; i<PatientRecall.RECALL_TYPE.length; i++) {
	    String recallType = PatientRecall.RECALL_TYPE[i];


	    PatientRecall patientRecall = null; 
	    try {
		patientRecall =
		    getExecutioner().getPatientRecordManager().getRecall(patnum, recallType);
	    }
	    catch (Exception e) {
		e.printStackTrace();

		/* does not current exist, 
		   so create new recall and set to disabled */
		patientRecall =
		    new PatientRecall(patnum,
				      "0001-01-01",
				      "0001-01-01",
				      92,
				      recallType,
				      true);
	    }

		/* process recall type */
		recallTypeComboBox[i].setSelectedItem(recallType);
		
		/* process previous recall date */
		LRyear[i].setSelectedItem
		    (patientRecall.getPreviousDate(PatientRecall.DATEINDEX_YEAR));
		LRmonth[i].setSelectedItem
		    (patientRecall.getPreviousDate(PatientRecall.DATEINDEX_MONTH));
		LRday[i].setSelectedItem
		    (patientRecall.getPreviousDate(PatientRecall.DATEINDEX_DAY));


		/* process next recall date */
		Ryear[i].setSelectedItem
		    (patientRecall.getDueDate(PatientRecall.DATEINDEX_YEAR));
		Rmonth[i].setSelectedItem
		    (patientRecall.getDueDate(PatientRecall.DATEINDEX_MONTH));
		Rday[i].setSelectedItem
		    (patientRecall.getDueDate(PatientRecall.DATEINDEX_DAY));



		/* process recall interval */
		int interval = patientRecall.getInterval();
		if (interval <= 150) {
		    recallInterval[i].setSelectedIndex(1);
		}
		else if (interval > 150 && interval <= 250 ) {
		    recallInterval[i].setSelectedIndex(2);
		}
		else if (interval > 250 && interval <= 500) {
		    recallInterval[i].setSelectedIndex(3);
		}
		else if (interval > 500 && interval <= 1000) {
		    recallInterval[i].setSelectedIndex(4);
		}
		else {
		    recallInterval[i].setSelectedIndex(5);
		}

		/* process recall active */
		if (!patientRecall.isDisabled()) {
		    System.out.println("setting recall to active");
		    recallActive[i].setSelected(true);
		}
		else {
		    System.out.println("setting recall to disabled");
		    recallActive[i].setSelected(false);
		}

		patientRecall.setGUIIndex(i);
		//getRecalls().add(patientRecall);
		addRecall(patientRecall);
	}

	/*
	try {
	    if (manager.recallAlreadyExists(patnum)) {
		//ResultSet rs = manager.getRecallWithPatNum(patnum);
		ResultSet rs = manager.getRecallsForPatient(patnum);

		//add recall type and last visit later
		int i = 0;
		while (rs.next()) {
		    /* process recall type 
		    recallType[i].setSelectedItem(rs.getString(4));

		    /* process recall id 
		    int recallID = rs.getInt(1);
		    recallIDs.add(new Integer(recallID));

		    /* process last recall date 
		    String[] lastR = rs.getString(2).split("-");
		    LRyear[i].setSelectedItem(new Integer(lastR[0]));
		    LRmonth[i].setSelectedItem(new Integer(lastR[1]));
		    LRday[i].setSelectedItem(new Integer(lastR[2]));

		    /* process next recall date 
		    String[] nextR = rs.getString(3).split("-");
		    Ryear[i].setSelectedItem(new Integer(nextR[0]));
		    Rmonth[i].setSelectedItem(new Integer(nextR[1]));
		    Rday[i].setSelectedItem(new Integer(nextR[2]));

		    /* process recall interval 
		    int interval = rs.getInt(4);
		    if (interval <= 150) {
			recallInterval[i].setSelectedIndex(1);
		    }
		    else if (interval > 150 && interval <= 250 ) {
			recallInterval[i].setSelectedIndex(2);
		    }
		    else if (interval > 250 && interval <= 500) {
			recallInterval[i].setSelectedIndex(3);
		    }
		    else if (interval > 500 && interval <= 1000) {
			recallInterval[i].setSelectedIndex(4);
		    }
		    else {
			recallInterval[i].setSelectedIndex(5);
		    }

		    /* process recall active 
		    if (rs.getInt(6) == 0) {
			recallActive[i].setSelected(true);
		    }

		    i++;
		}
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	}

	*/
    }

    public RecordManager getRecordManager() {
	//return manager;
	return getExecutioner().getPatientRecordManager();
    }

    public int getPatNum() {
	return patnum;
    }
    
    /*
    public String getRecallType1() {
	int rt = recallType1.getSelectedIndex();
	return getType(rt);
    }

   public String getRecallType2() {
	int rt = recallType2.getSelectedIndex();
	return getType(rt);

    }

   public String getRecallType3() {
	int rt = recallType3.getSelectedIndex();
	return getType(rt);
    }
    */
    /**
     * @return the recall type of the recall at index (index starts at 0)
     */
    public String getRecallType(int index) {
	//int rt = recallType[index].getSelectedIndex();
	//return RECALL_TYPE[rt];
	return (String) recallTypeComboBox[index].getSelectedItem();
    }

    /*
    public String getType(int rt) {
	if (rt == 1) {
	    return "Prophylaxis";
	}
	if (rt == 2) {
	    return "OfficeReline";
	}
	if (rt == 3) {
	    return "RootPlanning";
	}
	return null;
    }
    */

    /*
    public String getNextRecallDate1() {
	String yr = (Ryear1.getSelectedIndex()+1999)+"";
	String mo = Rmonth1.getSelectedIndex()+"";
	String da = Rday1.getSelectedIndex()+"";

	return getDateString(yr, mo, da);
    }

    public String getNextRecallDate2() {
	String yr = (Ryear2.getSelectedIndex()+1999)+"";
	String mo = Rmonth2.getSelectedIndex()+"";
	String da = Rday2.getSelectedIndex()+"";

	return getDateString(yr, mo, da);
    }

    public String getNextRecallDate3() {
	String yr = (Ryear3.getSelectedIndex()+1999)+"";
	String mo = Rmonth3.getSelectedIndex()+"";
	String da = Rday3.getSelectedIndex()+"";

	return getDateString(yr, mo, da);
    }
    */
    /**
     * @return the next recall date for recall at index (index starts from 0)
     */
    /*
    public String getNextRecallDate(int index) {
	String yr = (Ryear[index].getSelectedIndex()+
		     ((Integer)Ryear[index].getItemAt(1)).intValue()-1)+"";
	String mo = Rmonth[index].getSelectedIndex()+"";
	String da = Rday[index].getSelectedIndex()+"";

	return getDateString(yr, mo, da);
    } 
    */
    public String getNextRecallDate(int recallIndex) {
	return getDateString(Ryear[recallIndex].getSelectedItem(),
			     Rmonth[recallIndex].getSelectedItem(),
			     Rday[recallIndex].getSelectedItem());
    }

    /*
    public String getLastRecallDate1() {
	String yr = (LRyear1.getSelectedIndex()+1999)+"";
	String mo = LRmonth1.getSelectedIndex()+"";
	String da = LRday1.getSelectedIndex()+"";

	return getDateString(yr, mo, da);
    }

    public String getLastRecallDate2() {
	String yr = (LRyear2.getSelectedIndex()+1999)+"";
	String mo = LRmonth2.getSelectedIndex()+"";
	String da = LRday2.getSelectedIndex()+"";

	return getDateString(yr, mo, da);
    }

    public String getLastRecallDate3() {
	String yr = (LRyear3.getSelectedIndex()+1999)+"";
	String mo = LRmonth3.getSelectedIndex()+"";
	String da = LRday3.getSelectedIndex()+"";

	return getDateString(yr, mo, da);
    }
    */
    /**
     * @return last recall date of recall at index (index starts at 0)
     */
    /*
    public String getLastRecallDate(int index) {
	//String yr = (LRyear[index].getSelectedIndex()+1999)+"";

	String yr = (LRyear[index].getSelectedIndex()+
		     ((Integer)LRyear[index].getItemAt(1)).intValue()-1)+"";

	String mo = LRmonth[index].getSelectedIndex()+"";
	String da = LRday[index].getSelectedIndex()+"";

	return getDateString(yr, mo, da);
    }
    */
    public String getLastRecallDate(int recallIndex) {
	return getDateString(LRyear[recallIndex].getSelectedItem(),
			     LRmonth[recallIndex].getSelectedItem(),
			     LRday[recallIndex].getSelectedItem());
    }

    public String getDateString(Object year,
				Object month,
				Object day) {
	if (!(year instanceof Integer)) {
	    year = new Integer(1);
	}
	if (!(month instanceof Integer)) {
	    month = new Integer(1);
	}
	if (!(day instanceof Integer)) {
	    day = new Integer(1);
	}
	return year+"-"+month+"-"+day;
    }

    /*
    public int getRecallInterval1() {
	int interval = recallInterval1.getSelectedIndex();
	return getInterval(interval);
    }

    public int getRecallInterval2() {
	int interval = recallInterval2.getSelectedIndex();
	return getInterval(interval);
    }

    public int getRecallInterval3() {
	int interval = recallInterval3.getSelectedIndex();
	return getInterval(interval);
    }
    */
    /**
     * @return recall interval of recall at index (index starts at 0)
     */
    public int getRecallInterval(int index) throws Exception {
	int intervalIndex = recallInterval[index].getSelectedIndex();
	return PatientRecall.getInterval(intervalIndex);
    }


    /*
    public int getInterval(int interval) {
	if (interval == 1) {
	    return 92;
	}
	if (interval == 2) {
	    return 184;
	}
	if (interval == 3) {
	    return 365;
	}
	if (interval == 4) {
	    return 730;
	}
	if (interval == 5) {
	    return 1095;
	}
	return -1;
    }
    */
    /**
     * @return the recall interval at index 
     */
    /*
    public int getInterval(int index) {
	return PatientRecall.RECALL_INTERVAL[index];
    }
    */

    public boolean recallActive(int index) {
	return recallActive[index].isSelected();
    }

    /*
    public int getNumOfRecalls() {
	int numOfRecalls = 0;
	if (getRecallType1() != null) {
	    numOfRecalls++;
	}
	if (getRecallType2() != null) {
	    numOfRecalls++;
	}
	if (getRecallType3() != null) {
	    numOfRecalls++;
	}
	return numOfRecalls;
    }
    */
    /**
     * @return Collection of Booleans corresponding to the indices of active recalls
     */
    /*
    public Collection getActiveRecalls() {
	Collection c = new HashSet();
	for (int i=0; i<recallType.length; i++) {
	    boolean b = false;
	    if (getRecallType(i) != null &&
		recallActive[i].isSelected()) {
		b = true;
	    }
	    c.add(new Boolean(b));
	}

	return c;
    }
    */

    protected void connectActionListeners() {
	actionableItems.add(save);
	actionableItems.add(cancel);
	RecallListener listener = new RecallListener(this);
    }

}