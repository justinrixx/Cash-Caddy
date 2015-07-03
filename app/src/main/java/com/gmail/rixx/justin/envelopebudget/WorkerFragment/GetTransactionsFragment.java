package com.gmail.rixx.justin.envelopebudget.WorkerFragment;

import android.os.AsyncTask;
import android.os.Bundle;

import com.gmail.rixx.justin.envelopebudget.DataObjects.Transaction;
import com.gmail.rixx.justin.envelopebudget.SQLite.BudgetSQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by justin on 7/3/15.
 */
public class GetTransactionsFragment extends WorkerFragment< ArrayList<Transaction> > {

    String category;
    long minDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the data needed for the database request
        Bundle args = getArguments();
        category = args.getString("CATEGORY_NAME");
        minDate = args.getLong("LAST_REFRESH");

        // start up the task
        new GetTransactionsTask().execute();
    }


    private class GetTransactionsTask extends AsyncTask<Void, Void, ArrayList<Transaction>> {

        @Override
        protected ArrayList<Transaction> doInBackground(Void... params) {

            BudgetSQLiteHelper helper = new BudgetSQLiteHelper(getActivity());
            ArrayList<Transaction> data = helper.getTransactions(category, minDate);

            isRunning = false;
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<Transaction> transactions) {
            mCallbacks.onPostExecute(transactions);
        }

        @Override
        protected void onPreExecute() {
            mCallbacks.onPreExecute();
        }

        @Override
        protected void onCancelled() {
            mCallbacks.onCancelled();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            mCallbacks.onProgressUpdate(0);
        }
    }

}
