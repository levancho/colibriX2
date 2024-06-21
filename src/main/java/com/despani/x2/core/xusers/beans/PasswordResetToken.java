package com.despani.x2.core.xusers.beans;

import java.util.Date;

public class PasswordResetToken {

    private int oid;
    private int userOid;
    private String username;
    private String token;
    private Date expirationDate;

    public PasswordResetToken() {
    }


    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getUserOid() {
        return userOid;
    }

    public void setUserOid(int userOid) {
        this.userOid = userOid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "PasswordResetToken{" +
                "oid=" + oid +
                ", userName='" + username + '\'' +
                ", token='" + token + '\'' +
                ", expirationDate=" + expirationDate +
                '}';
    }
}



