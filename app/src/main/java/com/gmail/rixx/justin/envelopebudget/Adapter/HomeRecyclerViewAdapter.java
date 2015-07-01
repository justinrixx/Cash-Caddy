package com.gmail.rixx.justin.envelopebudget.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.rixx.justin.envelopebudget.CategoryActivity;
import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;
import com.gmail.rixx.justin.envelopebudget.R;

import java.text.DecimalFormat;
import java.util.List;


public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {

    private List<Category> data;

    public HomeRecyclerViewAdapter(List<Category> data) {
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
        double d = data.get(position).getAmount();
        DecimalFormat df = new DecimalFormat("#0.00");

        Log.d("HomeAdapter", "Net: " + String.valueOf(data.get(position).getAmount()));

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View itemView;
        public TextView categoryTextView;
        public TextView netTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            itemView.setOnClickListener(this);
            categoryTextView = (TextView) itemView.findViewById(R.id.category_name_textview);
            netTextView = (TextView) itemView.findViewById(R.id.category_net_textview);
        }

        @Override
        public void onClick(View v) {

            Log.i("HomeRecyclerViewAdapter", "In ViewHolder onClick");

            // launch the category activity
            Intent i = new Intent(itemView.getContext(), CategoryActivity.class);
            i.putExtra(v.getContext().getString(R.string.intent_extra_category), data.get(getAdapterPosition()));

            Log.d("HomeAdapter", "Net: " + String.valueOf(data.get(getAdapterPosition()).getAmount()));
            itemView.getContext().startActivity(i);
        }
    }
}
