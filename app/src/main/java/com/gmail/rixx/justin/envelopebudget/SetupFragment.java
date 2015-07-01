package com.gmail.rixx.justin.envelopebudget;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.rixx.justin.envelopebudget.Adapter.HomeRecyclerViewAdapter;
import com.gmail.rixx.justin.envelopebudget.Adapter.SetupRecyclerViewAdapter;
import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;
import com.gmail.rixx.justin.envelopebudget.SQLite.BudgetSQLiteHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class SetupFragment extends Fragment {

    private List<Category> categories;
    private RecyclerView mRecyclerView;
    private SetupRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public SetupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setup, container, false);

        populateCategories();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.category_recyclerview);
        setUpRecyclerView();

        return rootView;
    }

    /**
     * Get the recyclerview taken care of
     */
    private void setUpRecyclerView() {
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create the adapter
        mAdapter = new SetupRecyclerViewAdapter(categories);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void populateCategories() {

        BudgetSQLiteHelper helper = new BudgetSQLiteHelper(getActivity());
        categories = helper.getCategories();

    }
}
