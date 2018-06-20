package com.pagasys.welcome.pegasysattendence.models;

/**
 * Created by pegasys on 10/5/2017.
 */

public class EmployeeInfo {
    private String Name;
    private String Biometric;
    private String Role;
    private String Password;

    public EmployeeInfo() {
    }

    public EmployeeInfo(String name, String biometric, String role, String password) {
        Name = name;
        Biometric = biometric;
        Role = role;
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBiometric() {
        return Biometric;
    }

    public void setBiometric(String biometric) {
        Biometric = biometric;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
