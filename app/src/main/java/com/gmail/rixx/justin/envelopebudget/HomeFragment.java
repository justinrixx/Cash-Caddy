package com.gmail.rixx.justin.envelopebudget;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.rixx.justin.envelopebudget.Adapter.HomeRecyclerViewAdapter;
import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;
import com.gmail.rixx.justin.envelopebudget.SQLite.BudgetSQLiteHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {

    private List<Category> categories;
    private RecyclerView mRecyclerView;
    private HomeRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

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
        mAdapter = new HomeRecyclerViewAdapter(categories);

        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Populate the list of categories to prepare to display them in the RecyclerView
     */
    private void populateCategories() {

        // get all the categories from the database
        BudgetSQLiteHelper helper = new BudgetSQLiteHelper(getActivity());
        categories = helper.getCategories();

        // make sure the categories are up to date
        helper.updateCategories();

        // update the current costs
        for (Category c : categories) {
            c.setAmount(c.getAmount() - helper.getTotalCost(c.getCategory(), c.getDateLastRefresh()));
        }
    }

    private ArrayList<Category> generateFakeData() {
        ArrayList <Category> list = new ArrayList<>();

        list.add(new Category("Groceries", 76.56));
        list.add(new Category("Gas", -19.43));
        list.add(new Category("Entertainment", 17.57));
        list.add(new Category("Haircuts", -505.5));
        list.add(new Category("Emergency", 0.78));
        list.add(new Category("Wardrobe", 98.40));
        list.add(new Category("Harry Potter", -12.00));
        list.add(new Category("Other Stuff", 23.54));

        return list;
    }
}
