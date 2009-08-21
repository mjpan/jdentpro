package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.swixml.*;
import org.swixml.SwingEngine;

public class SpecialFunctionsGUI extends AbstractGUI {

    public JPanel panel;
    public XGridBagConstraints gbc1;
    public JButton manageUsers, mainMenu;

    public SpecialFunctionsGUI(ApplicationFrame af) {
	super(af);
    }

    protected String getGUIxml() {
	return "/xml/SpecialFunctions.xml";
    }

    protected void connectActionListeners() {
	actionableItems.add(manageUsers);
	actionableItems.add(mainMenu);
	SpecialFunctionsListener listener = new SpecialFunctionsListener(this);
    }

}