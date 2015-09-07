package com.gmail.rixx.justin.envelopebudget.SQLite;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Created by justin on 7/24/15.
 */
public class BudgetContract {

    /**
     * Content authority for the content provider. This is the package name
     */
    public static final String CONTENT_AUTHORITY = "com.gmail.rixx.justin.envelopebudget";

    /**
     * The base URI to fetch content from the content provider
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible paths from the base URI. These refer to their respective database tables
     */
    public static final String PATH_CATEGORIES = "category";
    public static final String PATH_TRANSACTIONS = "transaction";


    /**
     * All the constants defined for the Category table in the database
     */
    public static final class CategoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "." + PATH_CATEGORIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "." + PATH_CATEGORIES;

        public static Uri buildCategoryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String TABLE_NAME = "categories";

        // column storing the name of the category as a string
        public static final String COLUMN_NAME        = "name";

        // column storing the amount allocated to the category
        public static final String COLUMN_AMOUNT      = "amount";

        // column storing the date of next refresh
        public static final String COLUMN_NEXTREFRESH = "nextRefresh";

        // column storing the date of last refresh
        public static final String COLUMN_LASTREFRESH = "lastRefresh";

        // column storing the refresh code
        public static final String COLUMN_REFRESHCODE = "refreshCode";
    }

    /**
     * All the constants defined for the Transaction table in the database
     */
    public static final class TransactionEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRANSACTIONS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRANSACTIONS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRANSACTIONS;

        public static Uri buildTransactionsForCategory(long id) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_CATEGORY_KEY, String.valueOf(id)).build();
        }

        // TODO all the other ones

        public static final String TABLE_NAME = "transactions";

        // foreign key into the category table
        public static final String COLUMN_CATEGORY_KEY = "category_id";

        // name of the column storing the amount used in the transaction
        public static final String COLUMN_AMOUNT       = "amount";

        // column storing a comment on the transaction
        public static final String COLUMN_COMMENT      = "comment";

        // column storing the date as a unix timestamp
        public static final String COLUMN_DATE         = "date";
    }

}
