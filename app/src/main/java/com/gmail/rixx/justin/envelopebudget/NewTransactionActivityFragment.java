package com.gmail.rixx.justin.envelopebudget;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;
import com.gmail.rixx.justin.envelopebudget.SQLite.BudgetSQLiteHelperOld;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewTransactionActivityFragment extends Fragment {

    private ArrayList<String> categories;
    private TextView popup;

    public NewTransactionActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_transaction, container, false);

        // populate data
        categories = new ArrayList<>();
        populateData();

        // set up popup
        popup = (TextView) rootView.findViewById(R.id.dropdown);
        setUpPopup();

        return rootView;
    }

    private void setUpPopup() {

        if (categories.size() > 0) {
            popup.setText(categories.get(0));
        } else {
            popup.setText("No category");
        }

        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a list fragment
                ListDialog dialog = new ListDialog();

                String[] data = new String[categories.size()];
                data = categories.toArray(data);

                Bundle args = new Bundle();
                args.putStringArray("DATA", data);
                args.putSerializable("TITLE", "Category");

                dialog.setArguments(args);

                dialog.show(getFragmentManager(), null);
            }
        });
    }

    private void populateData() {

        BudgetSQLiteHelperOld helper = new BudgetSQLiteHelperOld(getActivity());
        List<Category> list = helper.getCategoriesForDisplay();

        for (int i = 0; i < list.size(); ++i) {
            categories.add(list.get(i).getCategory());
        }
    }
}
