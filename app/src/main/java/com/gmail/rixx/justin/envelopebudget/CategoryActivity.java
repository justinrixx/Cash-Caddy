package com.gmail.rixx.justin.envelopebudget;

import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gmail.rixx.justin.envelopebudget.Adapter.TransactionRecyclerViewAdapter;
import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;
import com.gmail.rixx.justin.envelopebudget.DataObjects.Transaction;
import com.gmail.rixx.justin.envelopebudget.WorkerFragment.GetTransactionsFragment;
import com.gmail.rixx.justin.envelopebudget.WorkerFragment.PopulateCategoriesFragment;
import com.gmail.rixx.justin.envelopebudget.WorkerFragment.TaskCallbacks;

import java.util.ArrayList;
import java.util.List;


public class CategoryActivity extends AppCompatActivity implements TaskCallbacks<ArrayList<Transaction>>{

    private static final String DATA_ARRAYLIST = "DATA_ARRAYLIST";
    private static final String TAG_WORKER_FRAGMENT = "WORKER_FRAGMENT";
    private Toolbar mToolbar;
    private Category category;
    private RecyclerView mRecyclerView;
    private TransactionRecyclerViewAdapter mAdapter;
    private ArrayList<Transaction> data;

    private GetTransactionsFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // get the data out
        category = (Category) getIntent().getParcelableExtra(getString(R.string.intent_extra_category));

        setUpToolbar();

        if (savedInstanceState != null) {
            data = (ArrayList<Transaction>) savedInstanceState.getSerializable(DATA_ARRAYLIST);
        }

        FragmentManager fm = getSupportFragmentManager();
        mFragment = (GetTransactionsFragment) fm.findFragmentByTag(TAG_WORKER_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mFragment == null) {
            mFragment = new GetTransactionsFragment();

            // set the arguments for the worker thread
            Bundle args = new Bundle();
            args.putString("CATEGORY_NAME", category.getCategory());
            args.putLong("LAST_REFRESH", category.getDateLastRefresh());
            mFragment.setArguments(args);

            fm.beginTransaction().add(mFragment, TAG_WORKER_FRAGMENT).commit();

            // empty until we get the real stuff from the database
            data = new ArrayList<>();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.transaction_recyclerview);
        if (mRecyclerView != null) {
            setUpRecyclerView();
        }
    }

    private void setUpRecyclerView() {
        mAdapter = new TransactionRecyclerViewAdapter(data);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // set the title
            getSupportActionBar().setTitle(category.getCategory());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(DATA_ARRAYLIST, data);
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

        data = params[0];


        mAdapter = new TransactionRecyclerViewAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
    }
}
