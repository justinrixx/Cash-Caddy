package com.gmail.rixx.justin.envelopebudget.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.rixx.justin.envelopebudget.HomeFragment.Category;
import com.gmail.rixx.justin.envelopebudget.R;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Category> data;

    public HomeRecyclerViewAdapter(ArrayList<Category> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // set the category name
        holder.categoryTextView.setText(data.get(position).getCategory());

        // is the net positive or negative?
        double d = data.get(position).getValue();
        DecimalFormat df = new DecimalFormat("#0.00");

        if (d < 0.0) {
            // set the color to red
            holder.netTextView.setTextColor(holder.itemView.getResources().getColor(R.color.red));
            holder.netTextView.setText("-$" + df.format(Math.abs(d)));
        } else {
            // set the color to green
            holder.netTextView.setTextColor(holder.itemView.getResources().getColor(R.color.green));
            holder.netTextView.setText("+$" + df.format(d));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView categoryTextView;
        public TextView netTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            categoryTextView = (TextView) itemView.findViewById(R.id.category_name_textview);
            netTextView = (TextView) itemView.findViewById(R.id.category_net_textview);
        }
    }
}
