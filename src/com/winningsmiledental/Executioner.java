package com.winningsmiledental;

public interface Executioner {

    public ApplicationFrame getAppFrame();

    public void setDatabase(String database);

    public String getDatabase();

    public void setCurrentEmployee(int id);

    public int getCurrentEmployeeID();

    public void loadLoginScreen();
    
    public void loadMainMenu();

    public void loadPatientInfo();

    public void loadPatientRecord(RecordManager manager);

    public void loadPatientRecord(RecordManager manager, int rcn);

    public void loadGuarantorInfo(int rcn);

    public void loadGuarantorRecord(int ptRCN, RecordManager manager);

    public void loadGuarantorRecord(int ptRCN, RecordManager manager, int gPatNum);

    public void loadRecall(RecordManager manager, int patnum);

    public void loadSpecialFunctions();

    public void loadPrintOptions();

    public void loadPrintBDay();

    public void loadPrintRecall();

    public void loadUserTable();

    public void loadUserAdmin(UserAccountManager manager);

    public void loadUserAdmin(UserAccountManager manager, String user);

    public void exit(int i);

}