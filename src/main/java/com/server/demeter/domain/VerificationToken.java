package com.server.demeter.domain;

import java.util.Date;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;

@Document
public class VerificationToken {

    private final static int EXPIRATION = 60 * 24;

    @Id
    private String id;

    @DBRef(lazy = true)
    private User user;

    private String token;
    private Date expiryDate;

    public VerificationToken() {
    }

    public VerificationToken(final String token) {
        this.token = token;
        this.expiryDate = CalculateExpiryDate(EXPIRATION);
    }

    public VerificationToken(final String token, final User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = CalculateExpiryDate(EXPIRATION);
    }

    private Date CalculateExpiryDate(final int expiryTimeinMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeinMinutes);
        return new Date(cal.getTime().getTime());
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof VerificationToken)) {
            return false;
        }
        VerificationToken verificationToken = (VerificationToken) o;
        return Objects.equals(id, verificationToken.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void UpdateToken(final String token)
    {
        this.token = token;
        this.expiryDate = CalculateExpiryDate(EXPIRATION);
    }

}