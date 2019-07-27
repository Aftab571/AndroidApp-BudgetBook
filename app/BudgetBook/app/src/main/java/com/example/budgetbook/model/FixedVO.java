package com.example.budgetbook.model;

import java.io.Serializable;
import java.util.Date;

public class FixedVO implements Serializable {
    private int ID;
    private String component;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    private float fxd_amt;
    private float lim_amt;
    private float unfxd_amt;
    private float income;
    private String comment;
    private String payMode;

    public String getiDate() {
        return iDate;
    }

    public void setiDate(String iDate) {
        this.iDate = iDate;
    }

    private int pin;
    private String payDate;
    private String iDate;

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    private String freq;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }



    public float getUnfxd_amt() {
        return unfxd_amt;
    }

    public void setUnfxd_amt(float unfxd_amt) {
        this.unfxd_amt = unfxd_amt;
    }



    public float getLim_amt() {
        return lim_amt;
    }

    public void setLim_amt(float lim_amt) {
        this.lim_amt = lim_amt;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public float getFxd_amt() {
        return fxd_amt;
    }

    public void setFxd_amt(float fxd_amt) {
        this.fxd_amt = fxd_amt;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }


    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
}
