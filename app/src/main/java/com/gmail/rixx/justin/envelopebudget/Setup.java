package com.gmail.rixx.justin.envelopebudget;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.gmail.rixx.justin.envelopebudget.Adapter.SetupRecyclerViewAdapter;
import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;
import com.gmail.rixx.justin.envelopebudget.SQLite.BudgetSQLiteHelperOld;

import java.util.ArrayList;
import java.util.List;


public class Setup extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private Context mContext = this;
    private NavigationView mNavigationView;
    private List<Category> categories;
    private RecyclerView mRecyclerView;
    private SetupRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpToolbar();
        setUpNavDrawer();

        categories = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.category_recyclerview);
        setUpRecyclerView();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, EditCategoryActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateCategories();
        mAdapter = new SetupRecyclerViewAdapter(categories);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setUpToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);

            getSupportActionBar().setTitle("Setup");

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
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
        mNavigationView.getMenu().getItem(1).setChecked(true);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home: {
                        Intent i = new Intent(mContext, Home.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(i);
                        return true;
                    }
                    case R.id.action_log: {
                        Intent i = new Intent(mContext, Log.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(i);
                        return true;
                    }
                    default:
                        return true;
                }
            }
        });
    }

    /**
     * Get the recyclerview taken care of
     */
    private void setUpRecyclerView() {
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create the adapter
        mAdapter = new SetupRecyclerViewAdapter(categories);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void populateCategories() {

        BudgetSQLiteHelperOld helper = new BudgetSQLiteHelperOld(this);
        categories = helper.getCategories();
    }
}
