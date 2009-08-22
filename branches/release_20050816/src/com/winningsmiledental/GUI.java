package com.winningsmiledental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.swixml.*;
import org.swixml.SwingEngine;
import java.util.*;

public interface GUI {
    
    public void setAppFrame(ApplicationFrame appFrame);

    public ApplicationFrame getAppFrame();

    public void configure();

    public Collection getActionableItems();

    public JPanel getPane();

    public Executioner getExecutioner();
}