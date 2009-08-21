package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.swixml.*;
import org.swixml.SwingEngine;

public class PrintOptionsGUI extends AbstractGUI {

    public JButton bday, recall, main;
    public XGridBagConstraints gbc1;
    public JPanel panel;

    public PrintOptionsGUI(ApplicationFrame af) {
	super(af);
    }

    protected String getGUIxml() {
	return "/xml/PrintOptions.xml";
    }

    protected void connectActionListeners() {
	actionableItems.add(bday);
	actionableItems.add(recall);
	actionableItems.add(main);
	PrintOptionsListener listener = new PrintOptionsListener(this);
    }

}