package com.gmail.rixx.justin.envelopebudget.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;
import com.gmail.rixx.justin.envelopebudget.DataObjects.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by justin on 6/26/15.
 */
public class BudgetSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BudgetDB";

    // table names
    private static final String CATEGORY_TABLE_NAME = "categories";
    private static final String TRANSACTION_TABLE_NAME = "transactions";

    // category table column names
    private static final String CATEGORY_KEY_ID          = "id";
    private static final String CATEGORY_KEY_NAME        = "name";
    private static final String CATEGORY_KEY_AMOUNT      = "amount";
    private static final String CATEGORY_KEY_NEXTREFRESH = "nextRefresh";
    private static final String CATEGORY_KEY_LASTREFRESH = "lastRefresh";
    private static final String CATEGORY_KEY_REFRESHCODE = "refreshCode";

    private static final String[] CATEGORY_COLUMNS = {CATEGORY_KEY_ID, CATEGORY_KEY_NAME,
            CATEGORY_KEY_AMOUNT, CATEGORY_KEY_NEXTREFRESH, CATEGORY_KEY_LASTREFRESH, CATEGORY_KEY_REFRESHCODE};

    // transaction table column names
    private static final String TRANSACTION_KEY_ID       = "id";
    private static final String TRANSACTION_KEY_CATEGORY = "category";
    private static final String TRANSACTION_KEY_AMOUNT   = "amount";
    private static final String TRANSACTION_KEY_COMMENT  = "comment";
    private static final String TRANSACTION_KEY_DATE     = "date";

    private static final String[] TRANSACTION_COLUMNS = {TRANSACTION_KEY_ID, TRANSACTION_KEY_CATEGORY,
            TRANSACTION_KEY_DATE, TRANSACTION_KEY_AMOUNT, TRANSACTION_KEY_COMMENT};

    public BudgetSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL to create category table
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + CATEGORY_TABLE_NAME +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, amount REAL, "
        + "nextRefresh INTEGER, lastRefresh INTEGER, refreshCode TEXT);";

        // SQL to create transaction table
        String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TRANSACTION_TABLE_NAME +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, category TEXT, "
        + "amount REAL, comment TEXT, date INTEGER);";

        // create the tables
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_TRANSACTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // drop old tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME);

        this.onCreate(db);
    }

    /**
     * Dump the database
     */
    public void dumpDb() {

        SQLiteDatabase db = getWritableDatabase();

        // drop old tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME);

        this.onCreate(db);
    }

    /*****************************************************************************
     * CATEGORY DATABASE OPERATIONS
     *****************************************************************************/
    /**
     * Add a category to the category table
     * @param category the category object to put into the table
     */
    public void addCategory(Category category) {
        // get reference to writable database
        SQLiteDatabase db = getWritableDatabase();

        // create content values for the data
        ContentValues values = new ContentValues();
        values.put(CATEGORY_KEY_NAME, category.getCategory());
        values.put(CATEGORY_KEY_AMOUNT, category.getAmount());
        values.put(CATEGORY_KEY_NEXTREFRESH, category.getDateNextRefresh());
        values.put(CATEGORY_KEY_LASTREFRESH, category.getDateLastRefresh());
        values.put(CATEGORY_KEY_REFRESHCODE, getRefreshString(category.getRefreshCode()));

        // insert and close
        db.insert(CATEGORY_TABLE_NAME, null, values);
        db.close();
    }

    /**
     * Get a category from the database based on its id
     * @param id The id of the category
     * @return A category from the database
     */
    public Category getCategory(int id) {
        // get readable database
        SQLiteDatabase db = getReadableDatabase();

        // build the query
        Cursor cursor = db.query(CATEGORY_TABLE_NAME, CATEGORY_COLUMNS,
                " id = ?", new String[]{String.valueOf(id)}, null, null, null, null);

        Category category = null;
        // only get the first one
        if (cursor != null) {
            cursor.moveToFirst();

            // build a category object
            category = new Category(
                    cursor.getInt(0),                     // id
                    cursor.getString(1),                  // name
                    cursor.getDouble(2),                  // amount
                    cursor.getInt(3),                     // nextrefresh
                    cursor.getInt(4),                     // lastrefresh
                    getRefreshcode(cursor.getString(5))); // refreshcode

            cursor.close();
        }

        db.close();

        return category;
    }

    /**
     * Get all categories in the database
     * @return A list of all category objects currently in the database
     */
    public List<Category> getCategories() {

        List<Category> data = new ArrayList<>();

        // build the query
        String query = "SELECT * FROM " + CATEGORY_TABLE_NAME;

        // execute the query
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // build a category object
                Category category = new Category(
                        cursor.getInt(0),                     // id
                        cursor.getString(1),                  // name
                        cursor.getDouble(2),                  // amount
                        cursor.getInt(3),                     // nextrefresh
                        cursor.getInt(4),                     // lastrefresh
                        getRefreshcode(cursor.getString(5))); // refreshcode

                data.add(category);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return data;
    }

    public List<Category> getCategoriesForDisplay() {

        List<Category> data = new ArrayList<>();

        // execute the query
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(CATEGORY_TABLE_NAME, CATEGORY_COLUMNS,
                CATEGORY_KEY_LASTREFRESH + " != ?", new String[] {String.valueOf(0)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                // build a category object
                Category category = new Category(
                        cursor.getInt(0),                     // id
                        cursor.getString(1),                  // name
                        cursor.getDouble(2),                  // amount
                        cursor.getInt(3),                     // nextrefresh
                        cursor.getInt(4),                     // lastrefresh
                        getRefreshcode(cursor.getString(5))); // refreshcode

                data.add(category);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return data;
    }

    /**
     * Refresh the categories that need refreshin'
     */
    public void updateCategories() {

        List<Category> data = new ArrayList<>();

        // build the query
        String query = "SELECT * FROM " + CATEGORY_TABLE_NAME;

        // execute the query
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // build a category object
                Category category = new Category(
                        cursor.getInt(0),                     // id
                        cursor.getString(1),                  // name
                        cursor.getDouble(2),                  // amount
                        cursor.getInt(3),                     // nextrefresh
                        cursor.getInt(4),                     // lastrefresh
                        getRefreshcode(cursor.getString(5))); // refreshcode

                data.add(category);
            } while (cursor.moveToNext());

            cursor.close();
        }

        // loop through and update the categories
        for (Category c : data) {
            Calendar calendar = Calendar.getInstance();
            if (c.getDateNextRefresh() <= (int)(calendar.getTimeInMillis() / 1000)) {
                c.refresh();
                updateCategory(c);
            }
        }
    }

    /**
     * Update a category object in the database
     * @param category The categoyr to update
     * @return The number of rows affected in the database
     */
    public int updateCategory(Category category) {

        SQLiteDatabase db = getWritableDatabase();

        // store all the values
        ContentValues values = new ContentValues();
        values.put(CATEGORY_KEY_NAME, category.getCategory());
        values.put(CATEGORY_KEY_AMOUNT, category.getAmount());
        values.put(CATEGORY_KEY_NEXTREFRESH, category.getDateNextRefresh());
        values.put(CATEGORY_KEY_LASTREFRESH, category.getDateLastRefresh());
        values.put(CATEGORY_KEY_REFRESHCODE, getRefreshString(category.getRefreshCode()));

        // update the row
        int i = db.update(CATEGORY_TABLE_NAME,
                values,
                CATEGORY_KEY_ID + " = ?",
                new String[]{String.valueOf(category.getId())});

        db.close();

        // return the number of rows affected
        return i;
    }

    /**
     * Delete a category from the database
     * @param category The category to delete
     */
    public void deleteCategory(Category category) {

        SQLiteDatabase db = getWritableDatabase();

        // delete the row
        db.delete(CATEGORY_TABLE_NAME, CATEGORY_KEY_ID + " = ?",
                new String[]{String.valueOf(category.getId())});

        db.close();
    }

    /**
     * Get a string from a refresh code
     * @param code The refresh code to change to a string
     * @return A string corresponding with the refresh code
     */
    private String getRefreshString (Category.RefreshCode code) {
        if (code == Category.RefreshCode.BIWEEKLY) {
            return "biweekly";
        } else {
            return "monthly";
        }
    }

    /**
     * Get a refresh code from a string
     * @param refreshString The string representing a refresh code
     * @return A refresh code corresponding to the string given
     */
    private Category.RefreshCode getRefreshcode(String refreshString) {
        switch (refreshString) {
            case "biweekly":
                return Category.RefreshCode.BIWEEKLY;
            case "monthly":
                return Category.RefreshCode.MONTHLY;
            default:
                Log.wtf("SQLite Helper", "Unknown refresh string!");
                return Category.RefreshCode.MONTHLY;
        }
    }


    /*****************************************************************************
     * TRANSACTION DATABASE OPERATIONS
     *****************************************************************************/
    /**
     * Add a transaction to the database
     * @param transaction The transaction to add to the database
     */
    public void addTransaction(Transaction transaction) {

        SQLiteDatabase db = getWritableDatabase();

        // create key/value pairs
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_KEY_CATEGORY, transaction.getCategory());
        values.put(TRANSACTION_KEY_DATE, transaction.getDate());
        values.put(TRANSACTION_KEY_AMOUNT, transaction.getCost());
        values.put(TRANSACTION_KEY_COMMENT, transaction.getComment());

        // insert them into the database
        db.insert(TRANSACTION_TABLE_NAME, null, values);

        db.close();
    }

    /**
     * Get a transaction by its ID
     * @param id The ID of the transaction
     * @return The transaction corresponding to the ID. Returns null if no transaction is found
     */
    public Transaction getTransaciton(int id) {

        SQLiteDatabase db = getReadableDatabase();

        // build query
        Cursor cursor =
                db.query(TRANSACTION_TABLE_NAME, TRANSACTION_COLUMNS, " id = ?",
                        new String[] {String.valueOf(id)}, null, null, null, null);

        Transaction transaction = null;

        if (cursor != null) {
            cursor.moveToFirst();

            transaction = new Transaction(cursor.getInt(0), cursor.getString(1),
                    cursor.getInt(2), cursor.getDouble(3), cursor.getString(4));

            cursor.close();
        }

        db.close();

        return transaction;
    }

    /**
     * Get all the transactions from the database
     * @return A list of all transactions
     */
    public List<Transaction> getTransactions() {
        return null; // TODO
    }

    /**
     * Get all the transactions from the database corresponding to a given category
     * @param category The name of the category
     * @return A list containing the results
     */
    public List<Transaction> getTransactions(String category) {
        return null; // TODO
    }

    /**
     * Get all the transactions from the database corresponding to a given category, but which <p/>
     * have timestamps after a specific date. This allows for searching for all rows with the <p/>
     * category "foo", but only those which were added after the date given.
     * @param category The name of the category
     * @param minDate The minimum date of the results. This is an integer representing a UNIX <p/>
     *                timestamp.
     * @return A list containing the results
     */
    public List<Transaction> getTransactions(String category, long minDate) {

        List<Transaction> list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        // build the query string
        Cursor cursor = db.query(TRANSACTION_TABLE_NAME, TRANSACTION_COLUMNS,
                null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {

                do {
                    list.add(new Transaction(cursor.getInt(0), cursor.getString(1),
                            cursor.getInt(2), cursor.getDouble(3), cursor.getString(4)));
                } while (cursor.moveToNext());

                cursor.close();
            }
        }

        return list;
    }

    /**
     * Get the cost for all transactions in a given category that occurred after a given date
     * @param category The category to query
     * @param minDate The date to start looking at, formatted as a UNIX timestamp
     * @return The total cost for a category
     */
    public double getTotalCost(String category, long minDate) {
        double result = 0.0;

        SQLiteDatabase db = getReadableDatabase();

        // build the query string
        Cursor cursor = db.query(TRANSACTION_TABLE_NAME,
                new String[] { TRANSACTION_KEY_AMOUNT }, // only get the amount column
                TRANSACTION_KEY_CATEGORY + " = ? and " + TRANSACTION_KEY_DATE + " >= ? ",
                new String[] { category, String.valueOf(minDate) },
                null, null, null, null);

        // add up all the costs
        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    result += cursor.getDouble(0);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        return result;
    }

    /**
     * Get the total cost for a category, in the entire database
     * @param category The category to query for
     * @return The total cost of all the transactions in dollars
     */
    public double getTotalCost(String category) {
        double result = 0.0;

        SQLiteDatabase db = getReadableDatabase();

        // build the query string
        Cursor cursor = db.query(TRANSACTION_TABLE_NAME,
                new String[] { TRANSACTION_KEY_AMOUNT }, // only get the amount column
                TRANSACTION_KEY_CATEGORY + " = ? ",
                new String[] { category },
                null, null, null, null);

        // add up all the costs
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    result += cursor.getDouble(0);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return result;
    }

    /**
     * Update a transaction in the database
     * @param transaction The transaction to update
     * @return The number of rows affected in the update
     */
    public int updateTransaction(Transaction transaction) {

        SQLiteDatabase db = getWritableDatabase();

        // create key/value pairs
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_KEY_CATEGORY, transaction.getCategory());
        values.put(TRANSACTION_KEY_DATE, transaction.getDate());
        values.put(TRANSACTION_KEY_AMOUNT, transaction.getCost());
        values.put(TRANSACTION_KEY_COMMENT, transaction.getComment());

        int i = db.update(TRANSACTION_TABLE_NAME, values, TRANSACTION_KEY_ID + " = ?",
                new String[] { String.valueOf(transaction.getId()) });

        db.close();

        return i;
    }

    /**
     * Delete a transaction from the database
     * @param transaction The transaction to delete
     */
    public void deleteTransaction(Transaction transaction) {

        SQLiteDatabase db = getWritableDatabase();

        db.delete(TRANSACTION_TABLE_NAME, TRANSACTION_KEY_ID + " = ?",
                new String[] { String.valueOf(transaction.getId()) });

        db.close();
    }
}
