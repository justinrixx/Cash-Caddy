package com.gmail.rixx.justin.envelopebudget.WorkerFragment;

import android.os.AsyncTask;
import android.os.Bundle;

import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;
import com.gmail.rixx.justin.envelopebudget.SQLite.BudgetSQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Populate the categories
 */
public class PopulateCategoriesFragment extends WorkerFragment< ArrayList<Category> > {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // start the task
        new PopulateCategoriesTask().execute();
    }

    private class PopulateCategoriesTask extends AsyncTask<Void, Void, ArrayList<Category>> {

        /**
         * Give the UI time to do what it wants
         */
        @Override
        protected void onPreExecute() {
            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
            }
        }

        /**
         * Let the Activity know it was cancelled
         */
        @Override
        protected void onCancelled() {
            if (mCallbacks != null) {
                mCallbacks.onCancelled();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Category> categories) {
            if (mCallbacks != null) {
                mCallbacks.onPostExecute(categories);
            }
        }

        /**
         * Get the categories from the database ready to display to the user
         * @param params
         * @return
         */
        @Override
        protected ArrayList<Category> doInBackground(Void... params) {
            BudgetSQLiteHelper helper = new BudgetSQLiteHelper(getActivity());

            // make sure the categories are up to date
            helper.updateCategories();

            ArrayList<Category> categories = helper.getCategoriesForDisplay();

            // update the current costs
            for (int i = 0; !isCancelled() && i < categories.size(); ++i) {
                categories.get(i).setAmount(categories.get(i).getAmount()
                        - helper.getTotalCost(categories.get(i).getCategory(),
                        categories.get(i).getDateLastRefresh()));
            }

            isRunning = false;
            return categories;
        }
    }
}
