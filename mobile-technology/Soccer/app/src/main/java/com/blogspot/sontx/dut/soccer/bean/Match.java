package com.blogspot.sontx.dut.soccer.bean;

import com.blogspot.sontx.dut.soccer.bo.DatabaseManager;
import com.blogspot.sontx.dut.soccer.utils.DateTime;

import java.util.Date;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 23/04/2016.
 */
public class Match {
    private int fieldId;
    private int matchId;
    private int hostId;
    private Date createdTime;
    private Date startTime;
    private Date endTime;
    private int numberOfSlots;
    private int numberOfAvailableSlots;
    private int moneyPerSlot;
    private boolean isVerified;

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getNumberOfSlots() {
        return numberOfSlots;
    }

    public void setNumberOfSlots(int numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }

    public int getNumberOfAvailableSlots() {
        return numberOfAvailableSlots;
    }

    public void setNumberOfAvailableSlots(int numberOfAvailableSlots) {
        this.numberOfAvailableSlots = numberOfAvailableSlots;
    }

    public int getMoneyPerSlot() {
        return moneyPerSlot;
    }

    public void setMoneyPerSlot(int moneyPerSlot) {
        this.moneyPerSlot = moneyPerSlot;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    @Override
    public String toString() {
        Field field = DatabaseManager.getInstance().getField(fieldId);
        return String.format("%s at %s", field.getName(), DateTime.getFriendlyString(startTime));
    }
}
