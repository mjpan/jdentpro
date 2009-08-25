package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.filechooser.*;
import org.swixml.*;
import org.swixml.SwingEngine;
import java.io.*;
import java.util.*;
import java.net.*;
import org.jdom.*;
import java.sql.*;

public class PrintBDayListener extends AbstractListener {
    public SwingEngine swix = null;
    private boolean CONTINUE_RUNNING;
    private ArrayList pdfLabels;
    private ArrayList pdfListRCN;
    private ArrayList pdfListName;
    private ArrayList pdfListHmPhone;
    private ArrayList pdfListWkPhone;
    private ArrayList pdfListMobile;
    private ArrayList pdfListBirthdate;
    private String filePath;


    public PrintBDayListener(GUI gui) {
	super(gui);
	resetArrays();
    }

    private void resetArrays() {
	pdfLabels = new ArrayList();
	pdfListRCN = new ArrayList();
	pdfListName = new ArrayList();
	pdfListHmPhone = new ArrayList();
	pdfListWkPhone = new ArrayList();
	pdfListMobile = new ArrayList();
	pdfListBirthdate = new ArrayList();
    }

    public void actionPerformed (ActionEvent ae) {
        String command = ae.getActionCommand();
	PrintBDayGUI temp = (PrintBDayGUI)gui;

	//RecordManager manager = ((PrintBDayGUI)gui).getRecordManager();
	if (command.equals("AC_CANCEL")) {
	    ((JDentProExecutioner)getExecutioner()).loadPrintOptions();
	}
	if (command.equals("AC_CHOOSE")) {
	    File file = null;
	    JFileChooser fc = new JFileChooser();
	    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    int returnVal = fc.showOpenDialog(getExecutioner().getAppFrame().getFrame());
	    
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		file = fc.getSelectedFile();
		filePath = file.getPath() + "/";
		temp.setFilePath(filePath);
		//dialog.dispose();
	    } 
	}
	if (command.equals("AC_RECALL_CREATE")) {
	    try {
		if (temp.getFilePath().equals("")) {
		    new ErrorMessage("Please Choose File to Save PDFs in.");
		}
		else {
		    filePath = temp.getFilePath() + "/";

		    resetArrays();

		    String month = temp.getMonth();
		    ResultSet rs = 
			getExecutioner().getPatientRecordManager().getRecordsWithBirthdateInMonth(month);

		    int recordsProcessed = 0;
		    while (rs.next()) {
			String rcn = rs.getString(1) + "\n\n" + rs.getInt(13);
			String lname = rs.getString(2);
			String fname = rs.getString(3);
			String address = rs.getString(4);
			String address2 = rs.getString(5);
			String city = rs.getString(6);
			String state = rs.getString(7);
			String zip = rs.getString(8);
			
			String nameAndAddress = (lname + ", " + fname + "\n" + address + " " + address2
						 + "\n" + city + ", " + state + " " + zip);
			
			String hmPhone = rs.getString(9);
			String wkPhone = rs.getString(10);
			String mobile = rs.getString(11);
			String birthdate = rs.getString(12);
			
			pdfLabels.add(nameAndAddress);
			
			pdfListRCN.add(rcn);
			pdfListName.add(nameAndAddress);
			pdfListHmPhone.add(hmPhone);
			pdfListWkPhone.add(wkPhone);
			pdfListMobile.add(mobile);
			pdfListBirthdate.add(birthdate);

			recordsProcessed ++;
		    }
		    int missing = temp.getNumOfMissingLabels();

		    new PDFLabels(pdfLabels.toArray(), "BirthdayLabels.pdf", filePath, missing);
		    new BirthdayList(filePath, pdfListRCN.toArray(), pdfListName.toArray(),
				     pdfListHmPhone.toArray(), pdfListWkPhone.toArray(),
				     pdfListMobile.toArray(), pdfListBirthdate.toArray(), month);

		    String os = System.getProperty("os.name");
		    String[] commandArrayLabels;
		    String[] commandArrayList;
		    String labels = filePath + "BirthdayLabels.pdf";
		    String list = filePath + "BirthdayList.pdf";
		    if (os.startsWith("Mac OS X")) {
			//operating system is tiger
			commandArrayLabels = new String[] {"open", labels};
			commandArrayList =  new String[]{"open", list};
		    }
		    else {
		    //operating system is windows
			commandArrayLabels =  new String[]{"rundll32.exe", "url.dll,FileProtocolHandler", labels};
			commandArrayList =  new String[]{"rundll32.exe", "url.dll,FileProtocolHandler", list};
		    }
		    Runtime.getRuntime().exec(commandArrayLabels);
		    Runtime.getRuntime().exec(commandArrayList);

		    //verify all fields valid
		    //print recall list/labels depending on data
		    //load/return to print options screen
		    ((JDentProExecutioner)getExecutioner()).loadPrintOptions();
		}
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }

	}
    }

}

