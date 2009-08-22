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

public class PrintRecallListener extends AbstractListener {
    public SwingEngine swix = null;
    private boolean CONTINUE_RUNNING;
    private ArrayList pdfLabels;
    private ArrayList pdfListRCN;
    private ArrayList pdfListName;
    private ArrayList pdfListHmPhone;
    private ArrayList pdfListWkPhone;
    private ArrayList pdfListMobile;
    private ArrayList pdfListDateDue;
    private ArrayList pdfListRecallType;
    private String filePath;


    public PrintRecallListener(GUI gui) {
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
	pdfListDateDue = new ArrayList();
	pdfListRecallType = new ArrayList();
    }

    public void actionPerformed (ActionEvent ae) {
	String command = ae.getActionCommand();
	PrintRecallGUI temp = (PrintRecallGUI)gui;
	RecordManager manager = ((PrintRecallGUI)gui).getRecordManager();
	if (command.equals("AC_CANCEL")) {
	    ((JDentProExecutioner)getExecutioner()).loadPrintOptions();
	}
	if (command.equals("AC_CHOOSE")) {
	    File file = null;
	    JFileChooser fc = new JFileChooser();
	    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    int returnVal = fc.showOpenDialog
		(getExecutioner().getAppFrame().getFrame());

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
		    resetArrays();
		    String startDate = temp.getStartDate();
		    String endDate = temp.getEndDate();
		    ResultSet rs = manager.getRecordsWithRecallBetweenDates(startDate, endDate);
		    while (rs.next()) {
			String rcn = rs.getString(1) + "\n\n" + rs.getInt(14);
			String lname = rs.getString(2);
			String fname = rs.getString(3);
			String address = rs.getString(4);
			String address2 = rs.getString(5);
			String city = rs.getString(6);
			String state = rs.getString(7);
			String zip = rs.getString(8);

			String nameAndAddress = (lname + ", " + fname + "\n" + address + " " + 
						 address2
						 + "\n" + city + ", " + state + " " + zip);

			String hmPhone = rs.getString(9);
			String wkPhone = rs.getString(10);
			String mobile = rs.getString(11);
			String dateDue = rs.getString(12);
			String recallType = rs.getString(13);

			pdfLabels.add(nameAndAddress);

			pdfListRCN.add(rcn);
			pdfListName.add(nameAndAddress);
			pdfListHmPhone.add(hmPhone);
			pdfListWkPhone.add(wkPhone);
			pdfListMobile.add(mobile);
			pdfListDateDue.add(dateDue);
			pdfListRecallType.add(recallType);
		    }
		    int missing = temp.getNumOfMissingLabels();
		    //create labels
		    new PDFLabels(pdfLabels.toArray(), "RecallLabels.pdf", filePath, missing);
		    //create list
		    new PDFList(filePath, pdfListRCN.toArray(), pdfListName.toArray(),
				pdfListHmPhone.toArray(), pdfListWkPhone.toArray(),
				pdfListMobile.toArray(), pdfListDateDue.toArray(),
				pdfListRecallType.toArray(), startDate, endDate);
		    //open two pdf files.

		    String os = System.getProperty("os.name");
		    String[] commandArrayLabels;
		    String[] commandArrayList;
		    String labels = filePath + "RecallLabels.pdf";
		    String list = filePath + "RecallList.pdf";
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

