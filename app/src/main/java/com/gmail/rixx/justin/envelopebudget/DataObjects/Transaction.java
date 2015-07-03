package com.gmail.rixx.justin.envelopebudget.DataObjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Transaction class used throughout the app. Each transaction represents a row in the transactions
 * table in the database
 */
public class Transaction implements Parcelable {

    private int id;
    private String category;
    private long date;
    private double cost;
    private String comment;

    public Transaction(int id, String category, long date, double cost, String comment) {
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
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

    protected Transaction(Parcel in) {
        id = in.readInt();
        category = in.readString();
        date = in.readLong();
        cost = in.readDouble();
        comment = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(category);
        dest.writeLong(date);
        dest.writeDouble(cost);
        dest.writeString(comment);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
}