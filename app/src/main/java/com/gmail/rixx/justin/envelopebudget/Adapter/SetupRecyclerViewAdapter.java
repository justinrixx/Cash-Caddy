package com.gmail.rixx.justin.envelopebudget.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.rixx.justin.envelopebudget.DataObjects.Category;
import com.gmail.rixx.justin.envelopebudget.EditCategoryActivity;
import com.gmail.rixx.justin.envelopebudget.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by justin on 6/20/15.
 */
public class SetupRecyclerViewAdapter extends RecyclerView.Adapter<SetupRecyclerViewAdapter.ViewHolder> {

    private List<Category> data;

    public SetupRecyclerViewAdapter(List<Category> data) {
        this.data = data;
    }

    @Override
    public SetupRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.setup_recyclerview_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SetupRecyclerViewAdapter.ViewHolder holder, int position) {

        DecimalFormat df = new DecimalFormat("#0.00");

        holder.categoryTextView.setText(data.get(position).getCategory());
        holder.amountTextView.setText("$" + String.valueOf(df.format(data.get(position).getAmount())));

        if (data.get(position).getRefreshCode().equals(Category.RefreshCode.MONTHLY)) {
            holder.refreshTextView.setText("Refresh monthly");
        } else {
            holder.refreshTextView.setText("Refresh every two weeks");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View itemView;
        public TextView categoryTextView;
        public TextView amountTextView;
        public TextView refreshTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            itemView.setOnClickListener(this);
            categoryTextView = (TextView) itemView.findViewById(R.id.category_name_textview);
            amountTextView = (TextView) itemView.findViewById(R.id.amount_textview);
            refreshTextView = (TextView) itemView.findViewById(R.id.refresh_textview);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), EditCategoryActivity.class);
            i.putExtra(v.getContext().getString(R.string.intent_extra_category), data.get(getAdapterPosition()));

            v.getContext().startActivity(i);
        }
    }
}
