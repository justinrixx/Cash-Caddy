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
        data.add(new Transaction(1l, "Gas", "12-18-1992", 57.43, "Gas in Laramie" ));
        data.add(new Transaction(2l, "Gas", "08-11-2014", 47.13, "Gas in Laramie" ));
        data.add(new Transaction(3l, "Gas", "08-14-2014", 27.43, "Gas in Laramie" ));
        data.add(new Transaction(4l, "Gas", "08-17-2014", 28.47, "Gas in Laramie" ));
        data.add(new Transaction(5l, "Gas", "08-14-2014", 23.65, "Gas in Laramie" ));
    }
}
