package com.winningsmiledental;

import java.io.*;
import java.util.*;
import java.sql.*;

public abstract class AbstractUserAccountManager implements UserAccountManager {

    protected static Connection connection;
    protected String databaseName;

    public AbstractUserAccountManager (String databaseName) {
	this.databaseName = databaseName;
	establishConnection();
    }

    public void establishConnection() {
	connection = JDentPro.getConnection(databaseName);
    }

    public Connection getConnection() {
	return connection;
    }

    public ResultSet getAllUsers() {
	try {
	    Statement stmt = getConnection().createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT e.username, u.permissionnum FROM employee e, userpermission u WHERE e.employeenum = u.employeenum");
	    return rs;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    public ResultSet getUser(String username) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("SELECT e.username, u.permissionnum FROM employee e, userpermission u WHERE e.username=?");
	    stmt.setString(1, username);
	    ResultSet rs = stmt.executeQuery();
	    return rs;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    public boolean userAlreadyExists(String username) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("SELECT employeenum FROM employee WHERE username=?");
	    stmt.setString(1, username);
	    ResultSet rs = stmt.executeQuery();
	    return rs.next();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return true;
    }

    public int getEmployeeNum(String username) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("SELECT employeenum FROM employee WHERE username=?");
	    stmt.setString(1, username);
	    ResultSet rs = stmt.executeQuery();
	    rs.first();
	    return rs.getInt(1);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return -1;
    }

    public void addUser(String username, String password, int accessLevel) {
	try {
		PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO employee (UserName, Password) VALUES (?,Password(?))");
		stmt.setString(1, username);
		stmt.setString(2, password);
		stmt.executeUpdate();
		
		stmt = getConnection().prepareStatement("INSERT INTO userpermission (PermissionNum, EmployeeNum) VALUES (?, ?)");
		stmt.setInt(1, accessLevel);
		stmt.setInt(2, getEmployeeNum(username));
		stmt.executeUpdate();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void editUser(String username, String password, int accessLevel) {
	setPassword(username, password);
	setAccessLevel(username, accessLevel);
    }

    public void removeUser(String username) {
	try {
	    int employeeNum = getEmployeeNum(username);
	    PreparedStatement stmt1 = getConnection().prepareStatement("DELETE FROM employee WHERE employeenum=?");
	    PreparedStatement stmt2 = getConnection().prepareStatement("DELETE FROM userpermission WHERE employeenum=?");
	    stmt1.setInt(1, employeeNum);
	    stmt2.setInt(1, employeeNum);
	    stmt1.executeUpdate();
	    stmt2.executeUpdate();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }


    public void setPassword(String username, String password) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE employee SET password=password(?) WHERE username=?");
	    stmt.setString(1, password);
	    stmt.setString(2, username);
	    stmt.executeUpdate();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void setAccessLevel(String username, int accessLevel) {
	try {
	    int employeeNum = getEmployeeNum(username);
	    PreparedStatement stmt = getConnection().prepareStatement("UPDATE userpermission SET permissionnum=? WHERE employeenum=?");
	    stmt.setInt(1, accessLevel);
	    stmt.setInt(2, employeeNum);
	    stmt.executeUpdate();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /*
    public String getPassword(String username) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("SELECT password FROM employee WHERE username=?");
	    stmt.setString(1, username);
	    ResultSet rs = stmt.executeQuery();
	    rs.next();
	    return rs.getString(1);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }
    */

    public int getAccessLevel(String username) {
	try {
	    int employeeNum = getEmployeeNum(username);
	    PreparedStatement stmt = getConnection().prepareStatement("SELECT permissionnum FROM userpermission WHERE employeenum=?");
	    stmt.setInt(1, employeeNum);
	    ResultSet rs = stmt.executeQuery();
	    if (rs.next()) {
		return rs.getInt(1);
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return -1;
    }

    /**
     * 
     * @return access level, currently just ACCESS_DENIED or ACCESS_GRANTED
     */
    public int authenticateUser(String username, String password) {
	try {
	    PreparedStatement stmt = getConnection().prepareStatement("SELECT employeenum FROM employee WHERE username=? and password=password(?);");
	    stmt.setString(1, username);
	    stmt.setString(2, password);
	    ResultSet rs = stmt.executeQuery();
	    if (rs.next()) {// && password.equals(rs.getString(2))) {
		return rs.getInt(1);
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return -1;
    }

}
