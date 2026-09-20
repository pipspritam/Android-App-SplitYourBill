package com.example.splityourbill;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class customNameBaseAdapter extends BaseAdapter {
    private final List<person> personList;
    private final LayoutInflater inflater;

    customNameBaseAdapter(Context context, List<person> personList) {
        this.personList = personList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public person getItem(int position) {
        return personList.get(position);
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
            convertView = inflater.inflate(R.layout.activity_custom_name_list, parent, false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.customTextView);
            holder.textViewAmount = convertView.findViewById(R.id.customTextViewAmount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        person item = getItem(position);
        double balance = item.getBalance();

        if (balance < -0.005) {
            holder.textView.setText(String.format(Locale.getDefault(), "%s owes ", item.getName()));
            holder.textViewAmount.setTextColor(Color.parseColor("#FF0000"));
            holder.textViewAmount.setText(String.format(Locale.getDefault(), "Rs %.2f", Math.abs(balance)));
        } else if (balance > 0.005) {
            holder.textView.setText(String.format(Locale.getDefault(), "%s gets ", item.getName()));
            holder.textViewAmount.setTextColor(Color.parseColor("#64ED26"));
            holder.textViewAmount.setText(String.format(Locale.getDefault(), "Rs %.2f", balance));
        } else {
            holder.textView.setText(String.format(Locale.getDefault(), "%s has no balance", item.getName()));
            holder.textViewAmount.setTextColor(Color.parseColor("#64ED26"));
            holder.textViewAmount.setText(String.format(Locale.getDefault(), "Rs 0.00"));
        }
        return convertView;
    }
}
