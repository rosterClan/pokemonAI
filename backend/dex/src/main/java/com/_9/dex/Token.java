package com._9.dex;
import java.util.Date;

public class Token {
    private int SessionID; 
    private int UserID; 
    private String SessionToken; 
    private Date ExpiryDate;
    private boolean IsActive; 

    public Token(int SessionID, int UserID, String SessionToken, Date ExpiryDate, boolean IsActive) {
        this.SessionID = SessionID;
        this.UserID = UserID;
        this.SessionToken = SessionToken;
        this.ExpiryDate = ExpiryDate;
        this.IsActive = IsActive;
    }

    public int getSessionID() {
        return this.SessionID;
    }

    public int getUserID() {
        return this.UserID;
    }

    public String getSessionToken() {
        return this.SessionToken;
    }

    public Date getExpiryDate() {
        return this.ExpiryDate;
    }

    public boolean getIsActive() {
        return this.IsActive;
    }

}
