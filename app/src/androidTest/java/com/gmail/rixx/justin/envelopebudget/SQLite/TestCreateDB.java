package com.gmail.rixx.justin.envelopebudget.SQLite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by justin on 7/24/15.
 */
public class TestCreateDB extends AndroidTestCase {

    public void testCreateDB() {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(BudgetContract.CategoryEntry.TABLE_NAME);
        tableNameHashSet.add(BudgetContract.TransactionEntry.TABLE_NAME);

        mContext.deleteDatabase(BudgetSQLiteHelper.DATABASE_NAME);
        SQLiteDatabase db = new BudgetSQLiteHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: database was created without both the transaction entry and category entry tables",
                tableNameHashSet.isEmpty());
    }
}
