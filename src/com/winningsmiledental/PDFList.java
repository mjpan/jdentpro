package com.winningsmiledental;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.awt.*;

import java.io.FileOutputStream;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


public class PDFList {

    private Document document;
    private Object[] rcn;
    private Object[] nameAndAddress;
    private Object[] hmPhone;
    private Object[] wkPhone;
    private Object[] mobile;
    private Object[] dateDue;
    private Object[] recallType;
    private String path;
    private Phrase head;


    public PDFList(String path, Object[] rcn, Object[] nameAndAddress, Object[] hmPhone,
		   Object[] wkPhone, Object[] mobile, Object[] dateDue, Object[] recallType,
		   String startDate, String endDate) {
	this.path = path + "RecallList.pdf";
	this.rcn = rcn;
	this.nameAndAddress = nameAndAddress;
	this.hmPhone = hmPhone;
	this.wkPhone =  wkPhone;
	this.mobile = mobile;
	this.dateDue = dateDue;
	this.recallType = recallType;
	head = new Phrase("Frank J. Pan, D.D.S. Winning Smile Dental Center. Recall List from " + startDate + " to " + endDate + ". Page ");
	setUpDocument();
    }

    public PdfPCell headerCell(String label, Font gry) {
	PdfPCell temp = new PdfPCell(new Phrase(label, gry));
	temp.setFixedHeight(25f);
	return temp;
    }

    public void setUpDocument() {
	document = new Document(PageSize.LETTER, 36f, 36f, 36f, 36f);
	try {
	    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
	    HeaderFooter header = new HeaderFooter(head, true);
	    header.setBottom(36f);
	    document.setHeader(header);
	    document.open();
	    PdfPTable table = new PdfPTable(7);
	    table.setWidthPercentage(100f);
	    Font gry = FontFactory.getFont(FontFactory.TIMES, Font.DEFAULTSIZE, Font.NORMAL, new Color(0x80, 0x80, 0x80));
	    float[] columnWidths = {66f, 144f, 66f, 66f, 66f, 66f, 66f};
	    table.setTotalWidth(columnWidths);
	    table.getDefaultCell().setFixedHeight(51f);
	    table.getDefaultCell().setBorderWidth(0f);
	    table.getDefaultCell().setPaddingLeft(0f);
	    table.getDefaultCell().setPaddingTop(0f);
	    PdfPCell rcn1 = headerCell("RCN/Age", gry);
	    PdfPCell nameAddress = headerCell("Patient Address", gry);
	    PdfPCell home = headerCell("Home Phone", gry);
	    PdfPCell work = headerCell("Work Phone", gry);
	    PdfPCell cell = headerCell("Mobile Phone", gry);
	    PdfPCell date = headerCell("Recall Date", gry);
	    PdfPCell type = headerCell("Recall Type", gry);
	    for (int i = 0; i < rcn.length; i++ ) {
		if (i%13 == 0) {
		    table.addCell(rcn1);
		    table.addCell(nameAddress);
		    table.addCell(home);
		    table.addCell(work);
		    table.addCell(cell);
		    table.addCell(date);
		    table.addCell(type);
		}
		table.addCell(new Phrase((String)rcn[i], gry));
		table.addCell(new Phrase((String)nameAndAddress[i], gry));
		table.addCell(new Phrase((String)hmPhone[i], gry));
		table.addCell(new Phrase((String)wkPhone[i], gry));
		table.addCell(new Phrase((String)mobile[i], gry));
		table.addCell(new Phrase((String)dateDue[i], gry));
		table.addCell(new Phrase((String)recallType[i], gry));
	    }
	    document.add(table);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	document.close();
    }

}