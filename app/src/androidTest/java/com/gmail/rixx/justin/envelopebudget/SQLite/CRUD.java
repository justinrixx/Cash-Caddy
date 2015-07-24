package com.gmail.rixx.justin.envelopebudget.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * Created by justin on 7/24/15.
 */
public class CRUD extends AndroidTestCase {

    private static final String NAME = "name";
    private static final double AMOUNT = 12.12;
    private static final long LASTREFRESH = 123;
    private static final long NEXTREFRESH = 321;
    private static final String REFRESHCODE = "refresh";

    private long result;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testWriteAndReadCategory() throws Exception {

        // get a reference to the database
        BudgetSQLiteHelperNew helper = new BudgetSQLiteHelperNew(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();

        // construct the object to insert
        ContentValues values = new ContentValues();
        values.put(BudgetContract.CategoryEntry.COLUMN_NAME, NAME);
        values.put(BudgetContract.CategoryEntry.COLUMN_AMOUNT, AMOUNT);
        values.put(BudgetContract.CategoryEntry.COLUMN_LASTREFRESH, LASTREFRESH);
        values.put(BudgetContract.CategoryEntry.COLUMN_NEXTREFRESH, NEXTREFRESH);
        values.put(BudgetContract.CategoryEntry.COLUMN_REFRESHCODE, REFRESHCODE);

        result = db.insert(BudgetContract.CategoryEntry.TABLE_NAME, null, values);

        if (result == -1) {
            fail("Insert returned -1. Oops.");
        }

        Cursor c = db.query(BudgetContract.CategoryEntry.TABLE_NAME, null,
                BudgetContract.CategoryEntry._ID + " =?",
                new String[]{String.valueOf(result)},
                null, null, null);

        assertTrue("Error: no result found", c.moveToFirst());

        // verify the name came back ok
        String name = c.getString(c.getColumnIndex(BudgetContract.CategoryEntry.COLUMN_NAME));
        assertEquals("Error: name doesn't match", name, NAME);

        double amount = c.getDouble(c.getColumnIndex(BudgetContract.CategoryEntry.COLUMN_AMOUNT));
        assertEquals("Error: amount doesn't match", amount, AMOUNT);

        long lastrefresh = c.getLong(c.getColumnIndex(BudgetContract.CategoryEntry.COLUMN_LASTREFRESH));
        assertEquals("Error: lastRefresh doesn't match", lastrefresh, LASTREFRESH);

        long nextrefresh = c.getLong(c.getColumnIndex(BudgetContract.CategoryEntry.COLUMN_NEXTREFRESH));
        assertEquals("Error: nextRefresh doesn't match", nextrefresh, NEXTREFRESH);

        String refreshcode = c.getString(c.getColumnIndex(BudgetContract.CategoryEntry.COLUMN_REFRESHCODE));
        assertEquals("Error: refresh code doesn't match", refreshcode, REFRESHCODE);

        c.close();

        db.close();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
