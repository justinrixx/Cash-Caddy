package com.gmail.rixx.justin.envelopebudget.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by justin on 7/24/15.
 */
public class BudgetSQLiteHelperNew extends SQLiteOpenHelper {

    private static final int DB_VERSION = 2;

    static final String DATABASE_NAME = "budget.db";

    public BudgetSQLiteHelperNew(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_TRANSACTION_TABLE = "CREATE TABLE "
                + BudgetContract.TransactionEntry.TABLE_NAME + " ("
                + BudgetContract.TransactionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BudgetContract.TransactionEntry.COLUMN_AMOUNT + " REAL NOT NULL, "
                + BudgetContract.TransactionEntry.COLUMN_COMMENT + " TEXT NOT NULL, "
                + BudgetContract.TransactionEntry.COLUMN_DATE + " INTEGER NOT NULL, "

                // foreign key to the category table
                + BudgetContract.TransactionEntry.COLUMN_CATEGORY_KEY + " INTEGER, "
                + "FOREIGN KEY (" + BudgetContract.TransactionEntry.COLUMN_CATEGORY_KEY + ") REFERENCES "
                + BudgetContract.CategoryEntry.TABLE_NAME + " (" + BudgetContract.CategoryEntry._ID
                + "));";

        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE "
                + BudgetContract.CategoryEntry.TABLE_NAME + " ("
                + BudgetContract.CategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BudgetContract.CategoryEntry.COLUMN_NAME + " TEXT NOT NULL,"
                + BudgetContract.CategoryEntry.COLUMN_AMOUNT + " REAL NOT NULL,"
                + BudgetContract.CategoryEntry.COLUMN_LASTREFRESH + " INTEGER NOT NULL,"
                + BudgetContract.CategoryEntry.COLUMN_NEXTREFRESH + " INTEGER NOT NULL,"
                + BudgetContract.CategoryEntry.COLUMN_REFRESHCODE + " TEXT NOT NULL);";

        Log.i("SQLiteHelper", "Transaction: " + SQL_CREATE_TRANSACTION_TABLE);
        Log.i("SQLiteHelper", "Category: " + SQL_CREATE_CATEGORY_TABLE);

        db.execSQL(SQL_CREATE_CATEGORY_TABLE);
        db.execSQL(SQL_CREATE_TRANSACTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
