package com.gmail.rixx.justin.envelopebudget;

import android.os.Build;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.rixx.justin.envelopebudget.DataObjects.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewTransactionActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        setUpToolbar();

        setUpOnClickListeners();
    }

    private void setUpOnClickListeners() {
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((TextView) findViewById(R.id.dropdown)).getText().toString().equals("No category")
                        || ((EditText) findViewById(R.id.amount_edittext)).getText().toString().equals("")) {
                    Snackbar.make(v, "Please enter a valid category and amount", Snackbar.LENGTH_LONG).show();

                    return;
                }

                // get the save the date with the data
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
                String formattedDate = df.format(c.getTime());

                // get the data
                String category = ((TextView) findViewById(R.id.dropdown)).getText().toString();
                double amount = Double.valueOf(((EditText) findViewById(R.id.amount_edittext)).getText().toString());
                String comment = ((EditText) findViewById(R.id.comments_edittext)).getText().toString();

                Snackbar.make(v, "Got the data!", Snackbar.LENGTH_LONG).show();

                Log.i("Data", "Date: " + formattedDate);
                Log.i("Data", "Category: " + category);
                Log.i("Data", "Amount: " + String.valueOf(amount));
                Log.i("Data", "Comment: " + comment);

                Transaction transaction = new Transaction(0, category, formattedDate, amount, comment);
            }
        });
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_clear_mtrl_alpha);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
            }
        }
    }
}
