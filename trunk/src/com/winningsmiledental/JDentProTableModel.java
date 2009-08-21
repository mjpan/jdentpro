package com.winningsmiledental;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class JDentProTableModel extends DefaultTableModel {

    public JDentProTableModel() {
	super();
    }

    public boolean isCellEditable(int row, int column) {
	return false;
    }

}