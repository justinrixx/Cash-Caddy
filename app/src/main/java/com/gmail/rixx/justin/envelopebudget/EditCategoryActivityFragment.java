package com.gmail.rixx.justin.envelopebudget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A placeholder fragment containing a simple view.
 */
public class EditCategoryActivityFragment extends Fragment {

    private TextView date;
    private Category mCategory = null;
    private View root;

    public EditCategoryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_edit_category, container, false);

        // set the category if we got it
        if (getActivity().getIntent().hasExtra(getResources().getString(R.string.intent_extra_category))) {
            mCategory = (Category) getActivity().getIntent().getSerializableExtra(getResources().getString(R.string.intent_extra_category));
        }

        date = (TextView) root.findViewById(R.id.date_textview);
        setOnClickListeners();

        return root;
    }

    private void setOnClickListeners() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

        // set the textview to today
        if (mCategory == null) {
            String formattedDate = df.format(c.getTime());
            date.setText(formattedDate);
        } else {
            // or whenever the category object says to
            c.setTimeInMillis(mCategory.getDateNextRefresh() * 1000);

            String formattedDate = df.format(c.getTime());
            date.setText(formattedDate);

            // set the category name
            EditText name = (EditText) root.findViewById(R.id.name_edittext);
            name.setText(mCategory.getCategory());

            // set the amount
            EditText amount = (EditText) root.findViewById(R.id.amount_edittext);
            amount.setText(String.valueOf(mCategory.getAmount()));

            // set the radio button
            if (mCategory.getRefreshCode() == Category.RefreshCode.BIWEEKLY) {
                ((RadioButton) root.findViewById(R.id.two_week_radiobutton)).setChecked(true);
            }
        }

        // set an onclick listener
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "Date Picker");
            }
        });
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // save the user's choice
            ((TextView) getActivity().findViewById(R.id.date_textview))
                    .setText(String.format("%02d", month + 1) + "-" + String.format("%02d", day)
                            + "-" + String.valueOf(year));
        }
    }
}
