package com.gmail.rixx.justin.envelopebudget.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.rixx.justin.envelopebudget.DataObjects.Transaction;
import com.gmail.rixx.justin.envelopebudget.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by justin on 6/19/15.
 */
public class TransactionRecyclerViewAdapter extends RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder> {

    private List<Transaction> data;

    public TransactionRecyclerViewAdapter(List<Transaction> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // transform the date to something human readable
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(data.get(position).getDate() * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        String formattedDate = dateFormat.format(c.getTime());

        holder.dateTextView.setText(formattedDate);

        // format the number
        double d = data.get(position).getCost();
        DecimalFormat df = new DecimalFormat("#0.00");

        holder.amountTextView.setText("$" + df.format(Math.abs(d)));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView dateTextView;
        public TextView amountTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            dateTextView = (TextView) itemView.findViewById(R.id.date_textview);
            amountTextView = (TextView) itemView.findViewById(R.id.amount_textview);
        }
    }
}
