package com.gmail.rixx.justin.envelopebudget.SQLite;

import android.provider.BaseColumns;

/**
 * Created by justin on 7/24/15.
 */
public class BudgetContract {

    /**
     * Normalize date method?
     */
    // TODO

    public static final class CategoryEntry implements BaseColumns {

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

    public static final class TransactionEntry implements BaseColumns {

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
