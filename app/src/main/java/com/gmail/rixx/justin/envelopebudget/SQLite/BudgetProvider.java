package com.gmail.rixx.justin.envelopebudget.SQLite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by justin on 7/25/15.
 *
 * This class is the implementation of a ContentProvider for all the data in the app
 */
public class BudgetProvider extends ContentProvider {

    private BudgetSQLiteHelperNew mOpenHelper;

    static final int TRANSACTIONS = 100;
    static final int TRANSACTION_ID = 101;
    static final int CATEGORIES = 200;
    static final int CATEGORY_ID = 201;

    /* Set up the UriMatcher object */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        // for all the transactions in the table
        sUriMatcher.addURI(BudgetContract.CONTENT_AUTHORITY,
                BudgetContract.TransactionEntry.TABLE_NAME, TRANSACTIONS);

        // for one transaction by id
        sUriMatcher.addURI(BudgetContract.CONTENT_AUTHORITY,
                BudgetContract.TransactionEntry.TABLE_NAME + "/#", TRANSACTION_ID);

        // for all the categories in the table
        sUriMatcher.addURI(BudgetContract.CONTENT_AUTHORITY,
                BudgetContract.CategoryEntry.TABLE_NAME, CATEGORIES);

        // for one category by id
        sUriMatcher.addURI(BudgetContract.CONTENT_AUTHORITY,
                BudgetContract.CategoryEntry.TABLE_NAME + "/#", CATEGORY_ID);
    }


    /**
     * Create the ContentProvider. This just instantiates the needed member variable for the database
     * @return true when done
     */
    @Override
    public boolean onCreate() {

        mOpenHelper = new BudgetSQLiteHelperNew(getContext());

        return true;
    }

    /**
     * Query the content provider for some data
     * @param uri The uri specifying what type of data to query for
     * @param projection The columns to include in the result
     * @param selection SQLite selection statement (pass an empty string if you'd like an entry by its ID
     * @param selectionArgs SQLite selection arguments. Optional.
     * @param sortOrder SQLite sortOrder
     * @return A cursor pointing to the requested data
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // which table to look in
        String table;

        /* different actions for different URIs */
        switch (sUriMatcher.match(uri)) {

            case TRANSACTIONS:
                table = BudgetContract.TransactionEntry.TABLE_NAME;
                break;

            case TRANSACTION_ID:
                table = BudgetContract.TransactionEntry.TABLE_NAME;
                selection = selection + BudgetContract.TransactionEntry._ID + " = " + uri.getLastPathSegment();
                break;

            case CATEGORIES:
                table = BudgetContract.CategoryEntry.TABLE_NAME;
                break;

            case CATEGORY_ID:
                table = BudgetContract.CategoryEntry.TABLE_NAME;
                selection = selection + BudgetContract.CategoryEntry._ID + " = " + uri.getLastPathSegment();
                break;

            default:
                throw new IllegalArgumentException("Invalid URI for query: " + uri.toString());
        }

        // if we made it this far, it's a valid URI, so we can query the database with the info
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Cursor result = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);

        db.close();

        return result;
    }

    /**
     * Get the type of data the content provider will return given a URI
     * @param uri The URI to check
     * @return A string representing the content type returned. If the URI is invalid, the empty <p />
     * string will be returned instead
     */
    @Override
    public String getType(Uri uri) {

        switch (sUriMatcher.match(uri)) {

            case TRANSACTIONS:
                return BudgetContract.TransactionEntry.CONTENT_TYPE;

            case TRANSACTION_ID:
                return BudgetContract.TransactionEntry.CONTENT_ITEM_TYPE;

            case CATEGORIES:
                return BudgetContract.CategoryEntry.CONTENT_TYPE;

            case CATEGORY_ID:
                return BudgetContract.CategoryEntry.CONTENT_ITEM_TYPE;

            default:
                return "";
        }
    }

    /**
     * Insert an entry into the content provider
     * @param uri The URI specifying which table to insert into
     * @param values The values for the required columns
     * @return A URI pointing to the newly created item
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final String ERROR_VALUES = "Provided ContentValues does not contain all"
                + " the necessary columns. You must provide every column specified in the contract class.";

        switch (sUriMatcher.match(uri)) {

            case TRANSACTIONS: {

                // check for incomplete data
                if (!values.containsKey(BudgetContract.TransactionEntry.COLUMN_CATEGORY_KEY) ||
                        !values.containsKey(BudgetContract.TransactionEntry.COLUMN_AMOUNT) ||
                        !values.containsKey(BudgetContract.TransactionEntry.COLUMN_COMMENT) ||
                        !values.containsKey(BudgetContract.TransactionEntry.COLUMN_DATE)) {
                    throw new IllegalArgumentException(ERROR_VALUES);
                }

                // do the insertion
                SQLiteDatabase db = mOpenHelper.getWritableDatabase();

                long id = db.insert(BudgetContract.TransactionEntry.TABLE_NAME, null, values);

                db.close();

                return ContentUris.withAppendedId(uri, id);
            }

            case CATEGORIES: {

                // check for incomplete data
                if (!values.containsKey(BudgetContract.CategoryEntry.COLUMN_NAME) ||
                        !values.containsKey(BudgetContract.CategoryEntry.COLUMN_AMOUNT) ||
                        !values.containsKey(BudgetContract.CategoryEntry.COLUMN_NEXTREFRESH) ||
                        !values.containsKey(BudgetContract.CategoryEntry.COLUMN_LASTREFRESH) ||
                        !values.containsKey(BudgetContract.CategoryEntry.COLUMN_REFRESHCODE)) {
                    throw new IllegalArgumentException(ERROR_VALUES);
                }

                // do the insertion
                SQLiteDatabase db = mOpenHelper.getWritableDatabase();

                long id = db.insert(BudgetContract.CategoryEntry.TABLE_NAME, null, values);

                db.close();

                return ContentUris.withAppendedId(uri, id);
            }
        }

        throw new IllegalArgumentException("Invalid URI for insert method: " + uri.toString());
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
