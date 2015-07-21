package com.gmail.rixx.justin.envelopebudget;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.rixx.justin.envelopebudget.Adapter.TransactionRecyclerViewAdapter;
import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;
import com.gmail.rixx.justin.envelopebudget.DataObjects.Transaction;
import com.gmail.rixx.justin.envelopebudget.SQLite.BudgetSQLiteHelper;

import java.text.DecimalFormat;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class CategoryActivityFragment extends Fragment {

    private Category category;

    public CategoryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        category = getActivity().getIntent()
                .getParcelableExtra(getString(R.string.intent_extra_category));

        // set the remaining amount at the bottom
        TextView remaining = (TextView) rootView.findViewById(R.id.net_amount_textview);
        double net = category.getAmount();

        DecimalFormat df = new DecimalFormat("#0.00");

        if (net >= 0.0) {
            remaining.setTextColor(getResources().getColor(R.color.green));
            remaining.setText("+$" + String.valueOf(df.format(net)));
        } else {
            remaining.setTextColor(getResources().getColor(R.color.red));
            remaining.setText("-$" + String.valueOf(df.format(Math.abs(net))));
        }

        return rootView;
    }
}
