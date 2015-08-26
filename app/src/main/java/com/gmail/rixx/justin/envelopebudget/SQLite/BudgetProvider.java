package com.gmail.rixx.justin.envelopebudget.SQLite;

import android.content.ContentProvider;
import android.content.UriMatcher;

/**
 * Created by justin on 7/25/15.
 * https://github.com/udacity/Sunshine-Version-2/compare/4.06_test_weather_table...4.07_start_code_content_provider#diff-7337a5db0b2f4215d477e506a8507035R50
 */
public class BudgetProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = null; // TODO
    private BudgetSQLiteHelperNew mOpenHelper;

    static final int TRANSACTION = 100;
    static final int TRANSACTIONS_WITH_CATEGORY = 101;
    static final int TRANSACTIONS_WITH_CATEGORY_AND_START = 102;
    static final int TRANSACTIONS_WITH_CATEGORY_AND_START_AND_END = 103;
    static final int CATEGORY = 200;
    static final int CATEGORIES = 201;

    private static final String sCategorySetting =
            BudgetContract.CategoryEntry.TABLE_NAME +
                    "." + BudgetContract.CategoryEntry._ID + " = ? ";

    // TODO all categories


}
