package com.gmail.rixx.justin.envelopebudget;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.gmail.rixx.justin.envelopebudget.Adapter.HomeRecyclerViewAdapter;
import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;
import com.gmail.rixx.justin.envelopebudget.SQLite.BudgetContract;
import com.gmail.rixx.justin.envelopebudget.WorkerFragment.PopulateCategoriesFragment;
import com.gmail.rixx.justin.envelopebudget.WorkerFragment.TaskCallbacks;

import java.util.ArrayList;


public class Home extends AppCompatActivity implements TaskCallbacks< ArrayList<Category> > {


    private static final String TAG_WORKER_FRAGMENT = "worker_fragment";
    private PopulateCategoriesFragment mFragment;

    private static final String PREFERENCES_FILE = "cashcaddy_settings";
    private static final String PREF_USED_DRAWER = "USED_DRAWER";

    private static final int TOKEN_CATEGORIES   = 0;
    private static final int TOKEN_TRANSACTIONS = 1;

    private static final String KEY_CATEGORIES = "key_categories";

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private Context mContext = this;
    private NavigationView mNavigationView;
    private RecyclerView mRecyclerView;
    private HomeRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Category> categories;
    private AsyncQueryHandler mQueryHandler;

    boolean mUserLearnedDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* Set up AsyncQueryHandler */
        mQueryHandler = new HomeQueryHandler(getContentResolver());

        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(this, PREF_USED_DRAWER, "false"));

        if (savedInstanceState != null) {
            categories = (ArrayList<Category>) savedInstanceState.getSerializable(KEY_CATEGORIES);
        }

        FragmentManager fm = getSupportFragmentManager();
        mFragment = (PopulateCategoriesFragment) fm.findFragmentByTag(TAG_WORKER_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mFragment == null) {
            mFragment = new PopulateCategoriesFragment();
            fm.beginTransaction().add(mFragment, TAG_WORKER_FRAGMENT).commit();

            // empty until we get the real stuff from the database
            categories = new ArrayList<>();
        }

        /* ASYNC QUERY TO DB */
        String[] projection = { BudgetContract.CategoryEntry._ID, BudgetContract.CategoryEntry.COLUMN_NAME,
        BudgetContract.CategoryEntry.COLUMN_AMOUNT, BudgetContract.CategoryEntry.COLUMN_NEXTREFRESH,
        BudgetContract.CategoryEntry.COLUMN_LASTREFRESH, BudgetContract.CategoryEntry.COLUMN_REFRESHCODE };

        mQueryHandler.startQuery(TOKEN_CATEGORIES, null, BudgetContract.CategoryEntry.CONTENT_URI,
                projection, null, null, null);
        /* /ASYNC QUERY TO DB */

        setUpToolbar();
        setUpNavDrawer();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, NewTransactionActivity.class));
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.category_recyclerview);
        setUpRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //                     If we're coming back to the activity
        if (mFragment == null || !mFragment.isRunning()) {
            mFragment = new PopulateCategoriesFragment();
            getSupportFragmentManager().beginTransaction().add(mFragment, TAG_WORKER_FRAGMENT).commit();
        }
    }

    private void setUpRecyclerView() {
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create the adapter
        mAdapter = new HomeRecyclerViewAdapter(categories);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(KEY_CATEGORIES, categories);
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        mNavigationView = (NavigationView) findViewById(R.id.nav_drawer);
        mNavigationView.getMenu().getItem(0).setChecked(true);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_log: {
                        Intent i = new Intent(mContext, Log.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(i);
                        return true;
                    }
                    case R.id.action_setup: {
                        Intent i = new Intent(mContext, Setup.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(i);
                        return true;
                    }
                    default:
                        return true;
                }
            }
        });

        if (!mUserLearnedDrawer) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            mUserLearnedDrawer = true;
            saveSharedSetting(this, PREF_USED_DRAWER, "true");
        }
    }

    @Override
    public void onPreExecute() {
        // TODO add a progress thing
    }

    @Override
    public void onProgressUpdate(int percent) {
        // do nothing
    }

    @Override
    public void onCancelled() {
        // eventually show an error message
    }

    /**
     * Add the category
     * @param params
     */
    @Override
    public void onPostExecute(ArrayList<Category>... params) {
        categories = params[0];
        mAdapter = new HomeRecyclerViewAdapter(categories);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Used to make the nav drawer open the first time only
     * @param context
     * @param settingName What to name the preference
     * @param settingValue The value to save (true or false)
     */
    public static void saveSharedSetting(Context context, String settingName, String settingValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }
    public static String readSharedSetting(Context context, String settingName, String defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    private static class HomeQueryHandler extends AsyncQueryHandler {

        public HomeQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

            switch (token) {

                case TOKEN_CATEGORIES: {

                    if (null == cursor) {
                        android.util.Log.e("CASH CADDY", "Cursor is null");
                    }

                    int iID          = cursor.getColumnIndex(BudgetContract.CategoryEntry._ID);
                    int iName        = cursor.getColumnIndex(BudgetContract.CategoryEntry.COLUMN_NAME);
                    int iAmount      = cursor.getColumnIndex(BudgetContract.CategoryEntry.COLUMN_AMOUNT);
                    int iNextRefresh = cursor.getColumnIndex(BudgetContract.CategoryEntry.COLUMN_NEXTREFRESH);
                    int iLastRefresh = cursor.getColumnIndex(BudgetContract.CategoryEntry.COLUMN_LASTREFRESH);
                    int iRefreshCode = cursor.getColumnIndex(BudgetContract.CategoryEntry.COLUMN_REFRESHCODE);

                    if (cursor.moveToFirst()) {
                        do {

                            android.util.Log.e("CASH CADDY", "Category: " + cursor.getString(iName));

                                /*
                                categories.add(new Category(
                                        cursor.getInt(iID),
                                        cursor.getString(iName),
                                        cursor.getDouble(iAmount),
                                        cursor.getInt(iNextRefresh),
                                        cursor.getInt(iLastRefresh),
                                        BudgetContract.getRefreshcode(cursor.getString(iRefreshCode))));
                                */
                        } while (cursor.moveToNext());

                        cursor.close();
                    }
                }
            }

        }
    }
}