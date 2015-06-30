package com.gmail.rixx.justin.envelopebudget.DataObjects;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by justin on 6/20/15.
 */
public class Category implements Serializable {

    public enum RefreshCode {
        BIWEEKLY, MONTHLY
    }

    private int id;
    private String category;
    private double amount;
    private long dateNextRefresh;
    private long dateLastRefresh;

    private RefreshCode refreshCode;

    public Category(int id, String category, double amount, long dateNextRefresh, long dateLastRefresh, RefreshCode refreshCode) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.dateNextRefresh = dateNextRefresh;
        this.dateLastRefresh = dateLastRefresh;
        this.refreshCode = refreshCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getDateNextRefresh() {
        return dateNextRefresh;
    }

    public void setDateNextRefresh(long dateNextRefresh) {
        this.dateNextRefresh = dateNextRefresh;
    }

    public long getDateLastRefresh() {
        return dateLastRefresh;
    }

    public void setDateLastRefresh(long dateLastRefresh) {
        this.dateLastRefresh = dateLastRefresh;
    }

    public RefreshCode getRefreshCode() {
        return refreshCode;
    }

    public void setRefreshCode(RefreshCode refreshCode) {
        this.refreshCode = refreshCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Update the new refresh dates
     */
    public void refresh() {

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dateNextRefresh * 1000);

        // refresh in a month
        if (refreshCode == RefreshCode.MONTHLY) {
            c.add(Calendar.MONTH, 1);
        } else { // refresh in two weeks
            c.add(Calendar.DAY_OF_MONTH, 14);
        }

        dateLastRefresh = dateNextRefresh;

        dateNextRefresh = (c.getTimeInMillis() / 1000);
    }
}
