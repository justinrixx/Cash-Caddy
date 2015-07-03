package com.gmail.rixx.justin.envelopebudget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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
import com.gmail.rixx.justin.envelopebudget.WorkerFragment.PopulateCategoriesFragment;
import com.gmail.rixx.justin.envelopebudget.WorkerFragment.TaskCallbacks;

import java.util.ArrayList;


public class Home extends AppCompatActivity implements TaskCallbacks< ArrayList<Category> >{

    /**
     * Fields related to the headless fragment used to pull database info
     */
    private static final String TAG_WORKER_FRAGMENT = "worker_fragment";
    private PopulateCategoriesFragment mFragment;

    private static final String KEY_CATEGORIES = "key_categories";

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private Context mContext = this;
    private NavigationView mNavigationView;
    private RecyclerView mRecyclerView;
    private HomeRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                    case R.id.action_home:

                        return true;
                    case R.id.action_setup:
                        Intent i = new Intent(mContext, Setup.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(i);
                        return true;

                    case R.id.action_log:

                        return true;

                    default:
                        return true;
                }
            }
        });
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
}
