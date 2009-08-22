package com.winningsmiledental.test;

import com.winningsmiledental.*;

import java.util.*;
import java.net.*;
import java.io.*;

import junit.framework.*;

public class PDFListTest extends TestCase {

    public static Test suite() { 
	TestSuite suite = new TestSuite(); 
	suite.addTest(new PDFListTest("generatePDF"));

	return suite;
    }
    
    public PDFListTest(String test) {
	super(test);
    }


    public void generatePDF() {
	String[] rcn = new String[40];
	String[] name = new String[40];
	String[] hmPhone = new String[40];
	String[] wkPhone = new String[40];
	String[] mobile = new String[40];
	String[] recallDate = new String[40];
	String[] recallType = new String[40];
	for (int i = 0; i < 40; i++) {
	    rcn[i] = "4686";
	    name[i] = "jenn pan \n279 w naomi ave \narcadia, ca 91007";
	    hmPhone[i] = "626.447.1706";
	    wkPhone[i] = "";
	    mobile[i] = "626.215.5496";
	    recallDate[i] = "2005-04-06";
	    recallType[i] = "Prophylaxis";
	}
	//PDFList pdfList = new PDFList("", rcn, name, hmPhone, wkPhone, mobile, recallDate, recallType);
    }

   public static void main(String args[]) { 
	junit.textui.TestRunner.run(suite());
    }

}