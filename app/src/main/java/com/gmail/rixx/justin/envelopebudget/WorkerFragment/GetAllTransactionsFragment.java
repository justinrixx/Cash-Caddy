package com.gmail.rixx.justin.envelopebudget.WorkerFragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import com.gmail.rixx.justin.envelopebudget.DataObjects.Transaction;
import com.gmail.rixx.justin.envelopebudget.SQLite.BudgetSQLiteHelperOld;

import java.util.ArrayList;

/**
 * Created by justin on 7/3/15.
 */
public class GetAllTransactionsFragment extends WorkerFragment< ArrayList<Transaction> > {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // start up the task
        new GetTransactionsTask().execute();
    }

    private class GetTransactionsTask extends AsyncTask<Void, Void, ArrayList<Transaction>> {

        @Override
        protected ArrayList<Transaction> doInBackground(Void... params) {

            ArrayList<Transaction> list = new ArrayList<>();

            BudgetSQLiteHelperOld helper = new BudgetSQLiteHelperOld(getActivity());
            SQLiteDatabase db = helper.getReadableDatabase();

            // build the database query
            Cursor cursor = db.query(helper.TRANSACTION_TABLE_NAME, helper.TRANSACTION_COLUMNS, null, null, null,
                    null, helper.TRANSACTION_KEY_DATE + " DESC"); // newest transaction first

            // get all the objects
            if (cursor != null) {
                if (cursor.moveToFirst()) {

                    do {
                        list.add(new Transaction(cursor.getInt(0), cursor.getString(1),
                                cursor.getInt(2), cursor.getDouble(3), cursor.getString(4)));
                    } while (cursor.moveToNext());

                    cursor.close();
                }
            }

            db.close();

            isRunning = false;
            return list;
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
