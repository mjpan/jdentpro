package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.swixml.*;
import org.swixml.SwingEngine;

public class MainMenuGUI extends AbstractGUI {

    public JPanel panel;
    public XGridBagConstraints gbc1;
    public JButton patientInfo, printOptions, loginScreen, specialFunctions, quit;

    public MainMenuGUI(ApplicationFrame af) {
	super(af);
    }

    protected String getGUIxml() {
	return "/xml/MainMenu.xml";
    }

    protected void connectActionListeners() {
	actionableItems.add(patientInfo);
	actionableItems.add(printOptions);
	actionableItems.add(loginScreen);
	actionableItems.add(specialFunctions);
	actionableItems.add(quit);
	MainMenuListener listener = new MainMenuListener(this);
    }

}