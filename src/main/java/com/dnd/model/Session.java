package com.dnd.model;

import java.time.OffsetDateTime;

public class Session {
    private int idSession;
    private int nameCompanyFk;
    private String content;
    private String address;
    private OffsetDateTime date;

    public Session() {}

    public Session(int idSession, int nameCompanyFk, String content, String address, OffsetDateTime date) {
        this.idSession = idSession;
        this.nameCompanyFk = nameCompanyFk;
        this.content = content;
        this.address = address;
        this.date = date;
    }

    public int getIdSession() { return idSession; }
    public void setIdSession(int idSession) { this.idSession = idSession; }

    public int getNameCompanyFk() { return nameCompanyFk; }
    public void setNameCompanyFk(int nameCompanyFk) { this.nameCompanyFk = nameCompanyFk; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public OffsetDateTime getDate() { return date; }
    public void setDate(OffsetDateTime date) { this.date = date; }
}