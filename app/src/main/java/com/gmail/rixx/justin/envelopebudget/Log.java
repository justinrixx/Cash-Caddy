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

import com.gmail.rixx.justin.envelopebudget.Adapter.TransactionRecyclerViewAdapter;
import com.gmail.rixx.justin.envelopebudget.DataObjects.Transaction;
import com.gmail.rixx.justin.envelopebudget.WorkerFragment.GetAllTransactionsFragment;
import com.gmail.rixx.justin.envelopebudget.WorkerFragment.TaskCallbacks;

import java.util.ArrayList;

public class Log extends AppCompatActivity implements TaskCallbacks<ArrayList<Transaction>> {

    private static final String TRANSACTION_ARRAYLIST = "TRANSACTION_ARRAYLIST";
    private static final String TAG_WORKER_FRAGMENT = "WORKER_FRAGMENT";
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private Context mContext = this;
    private NavigationView mNavigationView;
    private ArrayList<Transaction> transactions;
    private RecyclerView mRecyclerView;
    private TransactionRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private GetAllTransactionsFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpToolbar();
        setUpNavDrawer();

        if (savedInstanceState != null) {
            transactions = (ArrayList<Transaction>) savedInstanceState.getParcelable(TRANSACTION_ARRAYLIST);
        }

        FragmentManager fm = getSupportFragmentManager();
        mFragment = (GetAllTransactionsFragment) fm.findFragmentByTag(TAG_WORKER_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mFragment == null) {
            mFragment = new GetAllTransactionsFragment();

            fm.beginTransaction().add(mFragment, TAG_WORKER_FRAGMENT).commit();

            // empty until we get the real stuff from the database
            transactions = new ArrayList<>();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.category_recyclerview);
        setUpRecyclerView();
    }

    private void setUpToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);

            getSupportActionBar().setTitle("Log");

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
    }

    /**
     * Get the recyclerview taken care of
     */
    private void setUpRecyclerView() {
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create the adapter
        mAdapter = new TransactionRecyclerViewAdapter(transactions);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onProgressUpdate(int percent) {

    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onPostExecute(ArrayList<Transaction>... params) {

        android.util.Log.d("Log Activity", "In onPostExecute");

        transactions = params[0];
        mAdapter = new TransactionRecyclerViewAdapter(transactions);
        mRecyclerView.setAdapter(mAdapter);
    }
}
