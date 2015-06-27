package com.gmail.rixx.justin.envelopebudget.DataObjects;

/**
 * Created by justin on 6/20/15.
 */
public class Category {

    public enum RefreshCode {
        BIWEEKLY, MONTHLY
    }

    private int id;
    private String category;
    private double amount;
    private int dateNextRefresh;
    private int dateLastRefresh;

    private RefreshCode refreshCode;

    public Category(int id, String category, double amount, int dateNextRefresh, int dateLastRefresh, RefreshCode refreshCode) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.dateNextRefresh = dateNextRefresh;
        this.dateLastRefresh = dateLastRefresh;
        this.refreshCode = refreshCode;
    }

    // TODO remove this once HomeFragment no longer uses it
    public Category(String category, double amount) {
        this.category = category;
        this.amount = amount;
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

    public int getDateNextRefresh() {
        return dateNextRefresh;
    }

    public void setDateNextRefresh(int dateNextRefresh) {
        this.dateNextRefresh = dateNextRefresh;
    }

    public int getDateLastRefresh() {
        return dateLastRefresh;
    }

    public void setDateLastRefresh(int dateLastRefresh) {
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
}
