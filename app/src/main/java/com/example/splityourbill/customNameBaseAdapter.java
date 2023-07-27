package com.example.splityourbill;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class customNameBaseAdapter extends BaseAdapter {
    Context context;
    List<person> personList;
    LayoutInflater inflater;

    customNameBaseAdapter(Context context, List<person> personList) {
        this.context=context;
        this.personList = personList;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return  personList.size();
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
        convertView = inflater.inflate(R.layout.activity_custom_name_list, null);
        TextView textView = convertView.findViewById(R.id.customTextView);
        TextView textView1 = convertView.findViewById(R.id.customTextViewAmount);
        textView1.setText("Rs "+((new String(String.valueOf(Math.abs(Math.round(personList.get(position).getBalance()*100.0)/100.0))))));
        if(personList.get(position).getBalance()<0) {
            textView.setText(personList.get(position).getName() + " owes ");
            textView1.setTextColor(Color.parseColor("#FF0000"));
        }
//        else {
//            textView.setText(personList.get(position).getName() + " gets ");
//            textView1.setTextColor(Color.parseColor("#64ED26"));
//        }
        else if (personList.get(position).getBalance() > 0) {
            textView.setText(personList.get(position).getName() + " gets ");
            textView1.setTextColor(Color.parseColor("#64ED26"));
        } else {
            textView.setText(personList.get(position).getName() + " has no balance");
            textView1.setTextColor(Color.parseColor("#64ED26"));
        }
        return convertView;
    }
}
