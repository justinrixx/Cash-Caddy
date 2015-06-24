package com.gmail.rixx.justin.envelopebudget.DataObjects;

/**
 * Created by justin on 6/20/15.
 */
public class Category {

    public enum RefreshCode {
        BIWEEKLY, MONTHLY
    }

    private String category;
    private double amount;
    private String dateLastRefresh;
    private RefreshCode refreshCode;

    public Category(String category, double amount, String dateLastRefresh, RefreshCode refreshCode) {
        this.category = category;
        this.amount = amount;
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

    public String getDateLastRefresh() {
        return dateLastRefresh;
    }

    public void setDateLastRefresh(String dateLastRefresh) {
        this.dateLastRefresh = dateLastRefresh;
    }

    public RefreshCode getRefreshCode() {
        return refreshCode;
    }

    public void setRefreshCode(RefreshCode refreshCode) {
        this.refreshCode = refreshCode;
    }
}
