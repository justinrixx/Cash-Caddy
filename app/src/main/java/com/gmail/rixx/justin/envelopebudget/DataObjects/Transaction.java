package com.gmail.rixx.justin.envelopebudget.DataObjects;

/**
 * Transaction class used throughout the app. Each transaction represents a row in the transactions
 * table in the database
 */
public class Transaction {

    private int id;
    private String category;
    private String date;
    private double cost;
    private String comment;

    public Transaction(int id, String category, String date, double cost, String comment) {
        this.id = id;
        this.category = category;
        this.date = date;
        this.cost = cost;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
