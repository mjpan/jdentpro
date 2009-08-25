package com.winningsmiledental;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.awt.Color;
import java.io.FileOutputStream;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


public class PDFLabels {

    private Document document;
    private Object[] data;
    private int labelsMissing;
    private int labelsAppend;
    private String name;
    private String path;

    public PDFLabels (Object[] data, String name) {
	this(data, name, "");
    }

    public PDFLabels (Object[] data, String name, String path) {
	this(data, name, path, 0);
    }

    public PDFLabels(Object[] data, String name, int missing) {
	this(data, name, "", missing);
    }

    public PDFLabels(Object[] data, String name, 
		     String path, int missingBefore) {
	this.data = data;
	this.path = path + name;
	this.labelsMissing = missingBefore;
	setUpDocument();
    }

    public void setUpDocument() {
	document = new Document(PageSize.LETTER, 13.5f, 13.5f, 36f, 36f);
	try {
	    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
	    document.open();
	    PdfPTable table = new PdfPTable(3);
	    table.setWidthPercentage(100f);
	    Font gry = FontFactory.getFont(FontFactory.TIMES, Font.DEFAULTSIZE, Font.NORMAL, new Color(0x80, 0x80, 0x80));
	    float[] columnWidths = {198f, 198f, 189f};
	    table.setTotalWidth(columnWidths);
	    table.getDefaultCell().setFixedHeight(72f);
	    table.getDefaultCell().setBorderWidth(0f);
	    table.getDefaultCell().setPaddingLeft(12f);
	    table.getDefaultCell().setPaddingTop(12f);

	    for (int i = 0; i < this.labelsMissing; i++ ) {
		table.addCell("");
	    }

	    int numLabels = this.labelsMissing;
	    for (int i = 0; i < data.length; i++) {
		table.addCell(new Phrase((String)data[i], gry));
		numLabels ++;
	    }

	    // fill out the rest of the row
	    int labelsAppend = 3-(numLabels%3);
	    for (int i = 0; i < labelsAppend; i++ ) {
		table.addCell("");
	    }

	    document.add(table);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	document.close();
    }

}