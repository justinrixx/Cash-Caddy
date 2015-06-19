package com.gmail.rixx.justin.envelopebudget;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;


/**
 * A placeholder fragment containing a simple view.
 */
public class NewTransactionActivityFragment extends Fragment {

    private ArrayList<String> categories;
    private MaterialSpinner mSpinner;

    public NewTransactionActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_transaction, container, false);

        // populate data
        categories = new ArrayList<>();
        populateData();

        // set up spinner
        mSpinner = (MaterialSpinner) rootView.findViewById(R.id.category_spinner);
        setUpSpinner();

        return rootView;
    }

    private void setUpSpinner() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, categories);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
    }

    private void populateData() {
        categories.add("Groceries");
        categories.add("Gas");
        categories.add("Entertainment");
        categories.add("Haircuts");
        categories.add("Pets");
    }
}
