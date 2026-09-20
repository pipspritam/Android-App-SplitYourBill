package com.example.splityourbill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class customBaseAdapter extends BaseAdapter {
    private final List<TransactionModel> transactionModels;
    private final LayoutInflater inflater;

    customBaseAdapter(Context context, List<TransactionModel> transactionModels) {
        this.transactionModels = transactionModels;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return transactionModels.size();
    }

    @Override
    public TransactionModel getItem(int position) {
        return transactionModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView textView;
        TextView textViewAmount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_custom_list, parent, false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.customTextView);
            holder.textViewAmount = convertView.findViewById(R.id.customTextViewAmount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TransactionModel item = getItem(position);
        holder.textView.setText(String.format(Locale.getDefault(), "%s Paid For %s\n%s", item.getPayee(), item.getDescription(), item.getInvolve()));
        holder.textViewAmount.setText(String.format(Locale.getDefault(), "Rs %.2f", item.getAmount()));
        return convertView;
    }
}
