package com.winningsmiledental;

public interface Executioner {

    public ApplicationFrame getAppFrame();

    public void setDatabase(String database);

    public String getDatabase();

    public void setCurrentEmployee(int id);

    public int getCurrentEmployeeID();

    public PatientRecordManager getPatientRecordManager();
    public GuarantorRecordManager getGuarantorRecordManager();

    public void loadLoginScreen();
    
    public void loadMainMenu();

    public void loadPatientInfo();

    public void loadPatientRecord();

    public void loadPatientRecord(int rcn);

    public void loadGuarantorInfo(int rcn);

    public void loadGuarantorRecord(int ptRCN);

    public void loadGuarantorRecord(int ptRCN, int gPatNum);

    public void loadRecall(int patnum);

    public void loadSpecialFunctions();

    public void loadPrintOptions();

    public void loadPrintBDay();

    public void loadPrintRecall();

    public void loadUserTable();

    public void loadUserAdmin(UserAccountManager manager);

    public void loadUserAdmin(UserAccountManager manager, String user);

    public void exit(int i);

}