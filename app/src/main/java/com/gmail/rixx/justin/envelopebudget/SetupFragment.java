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

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class SetupFragment extends Fragment {

    private ArrayList<Category> categories;
    private RecyclerView mRecyclerView;
    private SetupRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public SetupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setup, container, false);

        categories = new ArrayList<>();
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
        categories.add(new Category("Gas", 120.00, "06-01-2015", Category.RefreshCode.BIWEEKLY));
        categories.add(new Category("Groceries", 230.00, "06-01-2015", Category.RefreshCode.MONTHLY));
        categories.add(new Category("Eating Out", 50.00, "06-01-2015", Category.RefreshCode.BIWEEKLY));
        categories.add(new Category("Entertainment", 30.00, "06-01-2015", Category.RefreshCode.BIWEEKLY));
    }
}
