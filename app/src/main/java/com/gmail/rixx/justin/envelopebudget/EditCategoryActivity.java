package com.gmail.rixx.justin.envelopebudget;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;
import com.gmail.rixx.justin.envelopebudget.SQLite.BudgetSQLiteHelperOld;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EditCategoryActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Context mContext = this;

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
     * Add the delete ONLY IF a category was passed in
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().hasExtra(getString(R.string.intent_extra_category))) {
            getMenuInflater().inflate(R.menu.menu_edit_category, menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    /**
     * Should never EVER be called if no category was passed in through the intent
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?");
            builder.setMessage(getString(R.string.delete_category_warning));
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    new BudgetSQLiteHelperOld(mContext).deleteCategory((Category) getIntent()
                            .getParcelableExtra(getString(R.string.intent_extra_category)));

                    finish();
                }
            }).setNegativeButton("Cancel", null).create().show();

            return true;
        }

        return super.onOptionsItemSelected(item);
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
                String dateString = ((TextView) findViewById(R.id.date_textview)).getText().toString();

                // format the date into a unix timestamp
                DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                Calendar cal  = Calendar.getInstance();
                try {
                    cal.setTime(df.parse(dateString));
                } catch (ParseException e) {
                    Log.e("EditCategoryActivity", "Error parsing date string " + dateString + " to calendar object.", e);
                }

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

                // get the dates
                long nextRefresh = (cal.getTimeInMillis() / 1000);

                // figure out when the last refresh should be
                if (refreshCode == Category.RefreshCode.MONTHLY) {
                    cal.add(Calendar.MONTH, -1);
                } else {
                    cal.add(Calendar.DATE, -14);
                }

                long lastRefresh = 0;

                BudgetSQLiteHelperOld helper = new BudgetSQLiteHelperOld(v.getContext());

                if (getIntent().hasExtra(getString(R.string.intent_extra_category))) {
                    Category c = (Category) getIntent().getParcelableExtra(getString(R.string.intent_extra_category));
                    c.setCategory(name);
                    c.setAmount(amount);
                    c.setDateNextRefresh(nextRefresh);
                    c.setRefreshCode(refreshCode);

                    helper.updateCategory(c);
                } else {
                    Category c = new Category(0, name, amount, nextRefresh, lastRefresh, refreshCode);
                    helper.addCategory(c);
                }

                finish();
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
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark));
        }
    }
}
