package com.winningsmiledental.test;

import com.winningsmiledental.*;

import java.util.*;
import java.net.*;
import java.io.*;

import junit.framework.*;

public class PDFLabelsTest extends TestCase {

    public static Test suite() { 
	TestSuite suite = new TestSuite(); 
	suite.addTest(new PDFLabelsTest("generatePDF"));
	suite.addTest(new PDFLabelsTest("generatePDFwithMissingLabels"));
	suite.addTest(new PDFLabelsTest("generatePDFwithMultiplePages"));

	return suite;
    }
    
    public PDFLabelsTest(String test) {
	super(test);
    }

    public void generatePDF() {
	String[] bogus = {"jenn pan \n279 w naomi ave \narcadia, ca 91007",
			  "jenn pan \n279 w naomi ave \narcadia, ca 91007",
			  "jenn pan \n279 w naomi ave \narcadia, ca 91007",
			  "jenn pan \n279 w naomi ave \narcadia, ca 91007",
			  "jenn pan \n279 w naomi ave \narcadia, ca 91007"};
	PDFLabels pdfLabels = new PDFLabels(bogus, "");
    }

    public void generatePDFwithMissingLabels() {
	String[] bogus = {"jenn pan \n279 w naomi ave \narcadia, ca91007", "blah \nblah \nblah",
			  "theodore slowik \nc/o blah blah \n08860 castelldefels \nspain",
			  "amy teachout \npobox12345 \nlake forest, ca 12345",
			  "maddy black \nsupercamp \nsan marco, ca 12345", "matt tsai \nhawaii"};
	int missing = 5;
	PDFLabels pdfLabels = new PDFLabels(bogus, "", missing);
    }

    public void generatePDFwithMultiplePages() {
	String[] bogus = new String[40];
	for (int i = 0; i < 40; i++) {
	    bogus[i] = "jenn pan \n279 w naomi ave \narcadia, ca 91007";
	}
	PDFLabels pdfLabels = new PDFLabels(bogus, "", 7);
    }

   public static void main(String args[]) { 
	junit.textui.TestRunner.run(suite());
    }

}