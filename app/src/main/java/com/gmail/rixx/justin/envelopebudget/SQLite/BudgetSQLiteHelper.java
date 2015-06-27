package com.gmail.rixx.justin.envelopebudget.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;

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
    private static final String CATEGORY_KEY_REFRESHCODE = "refreshCode";

    private static final String[] CATEGORY_COLUMNS = {CATEGORY_KEY_ID, CATEGORY_KEY_NAME,
            CATEGORY_KEY_AMOUNT, CATEGORY_KEY_NEXTREFRESH, CATEGORY_KEY_REFRESHCODE};

    // transaction table column names
    private static final String TRANSACTION_KEY_ID       = "id";
    private static final String TRANSACTION_KEY_AMOUNT   = "amount";
    private static final String TRANSACTION_KEY_COMMENT  = "comment";
    private static final String TRANSACTION_KEY_DATE     = "date";

    private static final String[] TRANSACTION_COLUMNS = {TRANSACTION_KEY_ID, TRANSACTION_KEY_AMOUNT,
            TRANSACTION_KEY_COMMENT, TRANSACTION_KEY_DATE};

    public BudgetSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL to create category table
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + CATEGORY_TABLE_NAME +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, amount REAL, nextRefresh TEXT);";

        // SQL to create transaction table
        String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TRANSACTION_TABLE_NAME +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL, comment TEXT, date TEXT);";

        // create the tables
        db.execSQL(CREATE_CATEGORY_TABLE + CREATE_TRANSACTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // drop old tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME);

        this.onCreate(db);
    }

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
                " id = ?", new String[] { String.valueOf(id) }, null, null, null, null);

        // only get the first one
        if (cursor != null) {
            cursor.moveToFirst();
        }

        // build a category object
        Category category = new Category(cursor.getString(1), cursor.getDouble(2),
                cursor.getString(3), getRefreshcode(cursor.getString(4)));
        category.setId(cursor.getInt(0));

        db.close();

        return category;
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
        if (refreshString.equals("biweekly")) {
            return Category.RefreshCode.BIWEEKLY;
        } else if (refreshString.equals("monthly")) {
            return Category.RefreshCode.MONTHLY;
        } else {
            Log.wtf("SQLite Helper", "Unknown refresh string!");
            return Category.RefreshCode.MONTHLY;
        }
    }
}
