package com.winningsmiledental;

import java.util.*;
import java.sql.*;

public interface UserAccountManager {

    public final static int ACCESS_DENIED = 0;
    public final static int ACCESS_GRANTED = 1;

    public final static int ACCESS_NONE = 3;
    public final static int ACCESS_USER = 2;
    public final static int ACCESS_MANAGER = 1;
    public final static int ACCESS_ADMIN = 0;

    //public void establishConnection();

    //public Connection getConnection();

    public ResultSet getAllUsers();

    public ResultSet getUser(String username);

    public int getEmployeeNum(String username);

    public boolean userAlreadyExists(String username);

    public void addUser(String username, String password, int accessLevel);

    public void editUser(String username, String password, int accessLevel);

    public void removeUser(String username);

    public void setPassword(String username, String password);

    public void setAccessLevel(String username, int accessLevel);

    //public String getPassword(String username);

    public int getAccessLevel(String username);

    public int authenticateUser(String username, String password);

}