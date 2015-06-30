package com.gmail.rixx.justin.envelopebudget;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.rixx.justin.envelopebudget.Adapter.TransactionRecyclerViewAdapter;
import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;
import com.gmail.rixx.justin.envelopebudget.DataObjects.Transaction;
import com.gmail.rixx.justin.envelopebudget.SQLite.BudgetSQLiteHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class CategoryActivityFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TransactionRecyclerViewAdapter mAdapter;
    private List<Transaction> data;
    private Category category;

    public CategoryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        category = ((Category) getActivity().getIntent()
                .getSerializableExtra(getString(R.string.intent_extra_category)));

        populateData();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.transaction_recyclerview);
        if (mRecyclerView != null) {
            setUpRecyclerView();
        }

        // set the remaining amount at the bottom
        TextView remaining = (TextView) rootView.findViewById(R.id.net_amount_textview);
        BudgetSQLiteHelper helper = new BudgetSQLiteHelper(getActivity());
        double net = category.getAmount() - helper.getTotalCost(category.getCategory(), category.getDateLastRefresh());

        if (net >= 0.0) {
            remaining.setTextColor(getResources().getColor(R.color.green));
            remaining.setText("+$" + String.valueOf(net));
        } else {
            remaining.setTextColor(getResources().getColor(R.color.red));
            remaining.setText("-$" + String.valueOf(Math.abs(net)));
        }

        return rootView;
    }

    private void setUpRecyclerView() {
        mAdapter = new TransactionRecyclerViewAdapter(data);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void populateData() {

        BudgetSQLiteHelper helper = new BudgetSQLiteHelper(getActivity());
        data = helper.getTransactions(category.getCategory(), category.getDateLastRefresh());

        /*
        data = new ArrayList<>();
        data.add(new Transaction(1, "Gas", 1435415823, 12.37, "oops"));
        data.add(new Transaction(1, "Gas", 1435415813, 12.37, "comment"));
        data.add(new Transaction(1, "Gas", 1435411223, 14.16, "comment here"));
        */
    }
}
