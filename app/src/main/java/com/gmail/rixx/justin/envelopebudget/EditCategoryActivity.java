package com.gmail.rixx.justin.envelopebudget;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;


public class EditCategoryActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setUpToolbar();
        }

        setOnClickListeners();
    }

    /**
     * set an onclick on the FAB. Get all the data and get ready to save it
     */
    private void setOnClickListeners() {
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = ((EditText) findViewById(R.id.name_edittext)).getText().toString();
                String amountString = ((EditText) findViewById(R.id.amount_edittext)).getText().toString();
                String date = ((TextView) findViewById(R.id.date_textview)).getText().toString();

                // not sure what the choice was
                Category.RefreshCode refreshCode;

                // check the radio buttons
                if (((RadioButton) findViewById(R.id.month_radiobutton)).isChecked()) {
                    refreshCode = Category.RefreshCode.MONTHLY;
                } else {
                    refreshCode = Category.RefreshCode.BIWEEKLY;
                }


                TextInputLayout nameLayout = (TextInputLayout) findViewById(R.id.input_layout_name);
                TextInputLayout amountLayout = (TextInputLayout) findViewById(R.id.input_layout_dollar);

                // do some error checking
                if (name.equals("")) {
                    nameLayout.setErrorEnabled(true);
                    nameLayout.setError("Please enter a name for the category");

                    return;
                } else {
                    nameLayout.setErrorEnabled(false);
                }
                if (amountString.equals("")) {
                    amountLayout.setErrorEnabled(true);
                    amountLayout.setError("Please enter the allocated amount");

                    return;
                } else {
                    amountLayout.setErrorEnabled(false);
                }

                // get the double out
                double amount = Double.valueOf(amountString);

                Category c = new Category(name, amount, date, refreshCode);

                Snackbar.make(mToolbar, "Got the data", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Edit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationIcon(R.drawable.abc_ic_clear_mtrl_alpha);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
