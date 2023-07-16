package com.example.splityourbill;

import android.content.Context;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class customBaseAdapter extends BaseAdapter {
    Context context;
    List<TransactionModel> transactionModel;
    LayoutInflater inflater;

    customBaseAdapter(Context context, List<TransactionModel> transactionModels) {
        this.context=context;
        this.transactionModel = transactionModels;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return transactionModel.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_custom_list, null);
        TextView textView = convertView.findViewById(R.id.customTextView);
        TextView textView1 = convertView.findViewById(R.id.customTextViewAmount);
        textView.setText(transactionModel.get(position).getPayee()+ " Paid For " + transactionModel.get(position).getDescription()+"\n"+transactionModel.get(position).getInvolve());
        textView1.setText("Rs "+((new String(String.valueOf(transactionModel.get(position).getAmount())))));
        return convertView;
    }
}
