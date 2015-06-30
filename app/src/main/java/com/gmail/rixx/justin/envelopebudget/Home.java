package com.gmail.rixx.justin.envelopebudget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.gmail.rixx.justin.envelopebudget.Adapter.HomeRecyclerViewAdapter;
import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;
import com.gmail.rixx.justin.envelopebudget.SQLite.BudgetSQLiteHelper;

import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private Context mContext = this;
    private NavigationView mNavigationView;
    private RecyclerView mRecyclerView;
    private HomeRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setUpToolbar();
        setUpNavDrawer();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, NewTransactionActivity.class));
            }
        });

        // empty until we get the real stuff from the database
        categories = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.category_recyclerview);
        setUpRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateCategories();
        mAdapter = new HomeRecyclerViewAdapter(categories);
        mRecyclerView.swapAdapter(mAdapter, false);
    }

    private void setUpRecyclerView() {
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create the adapter
        mAdapter = new HomeRecyclerViewAdapter(categories);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void populateCategories() {

        BudgetSQLiteHelper helper = new BudgetSQLiteHelper(this);

        // make sure the categories are up to date
        helper.updateCategories();

        categories = helper.getCategoriesForDisplay();

        // update the current costs
        for (Category c : categories) {
            c.setAmount(c.getAmount() - helper.getTotalCost(c.getCategory(), c.getDateLastRefresh()));
        }
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

                    // TODO remove this
                    case R.id.action_log:

                        BudgetSQLiteHelper helper = new BudgetSQLiteHelper(mContext);
                        helper.dumpDb();
                        return true;

                    default:
                        return true;
                }
            }
        });
    }
}
