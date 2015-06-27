package com.gmail.rixx.justin.envelopebudget;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.rixx.justin.envelopebudget.Adapter.TransactionRecyclerViewAdapter;
import com.gmail.rixx.justin.envelopebudget.DataObjects.Transaction;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class CategoryActivityFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TransactionRecyclerViewAdapter mAdapter;
    private ArrayList<Transaction> data;

    public CategoryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        data = new ArrayList<>();
        populateData();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.transaction_recyclerview);
        if (mRecyclerView != null) {
            setUpRecyclerView();
        }

        return rootView;
    }

    private void setUpRecyclerView() {
        mAdapter = new TransactionRecyclerViewAdapter(data);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void populateData() {
        data.add(new Transaction(1, "Gas", 1435415823, 12.37, "oops"));
        data.add(new Transaction(1, "Gas", 1435415813, 12.37, "comment"));
        data.add(new Transaction(1, "Gas", 1435411223, 14.16, "comment here"));
    }
}
