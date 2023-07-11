package com.example.splityourbill;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Person;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SettleUp extends AppCompatActivity {
    ArrayAdapter settleUpArrayAdapter;

    public ListView lv;

    dataBaseHelper dataBaseHelper = new dataBaseHelper(SettleUp.this);

    public static void sortPeopleByBalance(List<person> people) {
        people.sort(Comparator.comparingDouble(p -> p.balance));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle_up);
        lv = findViewById(R.id.settleUpListView);

        List<person> listOfPerson= dataBaseHelper.getEveryOne();

        int n = listOfPerson.size();
//        System.out.println(n);
        List<String> results_list = new ArrayList<>();

        sortPeopleByBalance(listOfPerson);
        for(person element: listOfPerson) {
            System.out.println(element.name);
            System.out.println(element.balance);
        }
        while (n > 0) {
            n = listOfPerson.size();
            sortPeopleByBalance(listOfPerson);
            //if 1 and n-1 same
            if ((listOfPerson.get(0).balance + listOfPerson.get(n - 1).balance) == 0) {
                results_list.add(listOfPerson.get(0).name+" pays "+ listOfPerson.get(n - 1).name+ " Rs "+ listOfPerson.get(n - 1).balance+"\n");
                listOfPerson.remove(0);
                n = listOfPerson.size();
                listOfPerson.remove(n-1);
                n = listOfPerson.size();
            } else if ((listOfPerson.get(0).balance + listOfPerson.get(n - 1).balance) > 0) {
                results_list.add(listOfPerson.get(0).name + " pays "+ listOfPerson.get(n - 1).name+ " Rs "+ Math.abs(listOfPerson.get(0).balance));
                listOfPerson.get(n - 1).balance = listOfPerson.get(n - 1).balance + listOfPerson.get(0).balance;
                listOfPerson.remove(0);
                n = listOfPerson.size();
            } else {
                results_list.add(listOfPerson.get(0).name + " pays "+ listOfPerson.get(n - 1).name +  " Rs "+ listOfPerson.get(n - 1).balance);
                listOfPerson.get(0).balance = listOfPerson.get(n - 1).balance + listOfPerson.get(0).balance;
                listOfPerson.remove(n-1);
                n = listOfPerson.size();
            }
        }

        System.out.println(results_list);

        settleUpArrayAdapter = new ArrayAdapter<>(SettleUp.this, android.R.layout.simple_list_item_1, results_list);
        lv.setAdapter(settleUpArrayAdapter);



    }
}