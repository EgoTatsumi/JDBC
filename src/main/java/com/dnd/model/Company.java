package com.dnd.model;

public class Company {
    private int idCompany;
    private String title;
    private String setting;

    public Company() {}

    public Company(int idCompany, String title, String setting) {
        this.idCompany = idCompany;
        this.title = title;
        this.setting = setting;
    }

    public int getIdCompany() { return idCompany; }
    public void setIdCompany(int idCompany) { this.idCompany = idCompany; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSetting() { return setting; }
    public void setSetting(String setting) { this.setting = setting; }
}