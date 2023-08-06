package com.example.splityourbill;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SettleUp extends AppCompatActivity {
    public ListView lv;
    ArrayAdapter settleUpArrayAdapter;
    Button goToHomeButton, viewTransButton, resetButton;

    TextView groupNameTextView;
    dataBaseHelper dataBaseHelper = new dataBaseHelper(SettleUp.this);

    public static void sortPeopleByBalance(List<person> people) {
        people.sort(Comparator.comparingDouble(p -> p.balance));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle_up);
        lv = findViewById(R.id.settleUpListView);
        goToHomeButton = findViewById(R.id.goToHomeButton);
        viewTransButton = findViewById(R.id.showTransButton);
        groupNameTextView = findViewById(R.id.viewGroupName);
        groupNameTextView.setText(dataBaseHelper.getEveryGroup().get(0).getGroupName());
        resetButton = findViewById(R.id.resetButton);

        resetButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation").setMessage("Are you sure you want to reset?").setPositiveButton("Yes", (dialog, which) -> {
                dataBaseHelper.clearDatabase();
                finish();
                startActivity(getIntent());
            }).setNegativeButton("No", (dialog, which) -> {
                // Do nothing or handle the cancel action
            }).show();
        });


        viewTransButton.setOnClickListener(v -> {
            if (v.getId() == R.id.showTransButton) {
                Intent viewTransIntent = new Intent(SettleUp.this, ViewTransaction.class);
                startActivity(viewTransIntent);
            }
        });


        goToHomeButton.setOnClickListener(v -> {
            if (v.getId() == R.id.goToHomeButton) {
                Intent homeIntent = new Intent(SettleUp.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });
        List<person> listOfPerson = dataBaseHelper.getEveryOne();
        int n = listOfPerson.size();
        List<String> results_list = new ArrayList<>();
        sortPeopleByBalance(listOfPerson);

        for (int i = 0; i < n; i++) {
            if (listOfPerson.get(i).balance == 0) {
                listOfPerson.remove(i);
                n = listOfPerson.size();
            }
        }
        n = listOfPerson.size();

        while (n > 1) {
            n = listOfPerson.size();
            sortPeopleByBalance(listOfPerson);
            if ((listOfPerson.get(0).balance + listOfPerson.get(n - 1).balance) == 0) {
                if (listOfPerson.get(0).balance != 0) {
                    results_list.add(listOfPerson.get(0).name + " pays " + listOfPerson.get(n - 1).name + " Rs " + Math.round(listOfPerson.get(n - 1).balance * 100.0) / 100.0 + " \n");
                }
                listOfPerson.remove(0);
                n = listOfPerson.size();
                listOfPerson.remove(n - 1);
                n = listOfPerson.size();
            } else if ((listOfPerson.get(0).balance + listOfPerson.get(n - 1).balance) > 0) {
                results_list.add(listOfPerson.get(0).name + " pays " + listOfPerson.get(n - 1).name + " Rs " + Math.round(Math.abs(listOfPerson.get(0).balance) * 100.0) / 100.0);
                listOfPerson.get(n - 1).balance = listOfPerson.get(n - 1).balance + listOfPerson.get(0).balance;
                listOfPerson.remove(0);
                n = listOfPerson.size();
            } else {
                results_list.add(listOfPerson.get(0).name + " pays " + listOfPerson.get(n - 1).name + " Rs " + Math.round(listOfPerson.get(n - 1).balance * 100.0) / 100.0);
                listOfPerson.get(0).balance = listOfPerson.get(n - 1).balance + listOfPerson.get(0).balance;
                listOfPerson.remove(n - 1);
                n = listOfPerson.size();
            }
        }
        if (results_list.isEmpty()) {
            results_list.add("Everything is settled up already");
        }


        settleUpArrayAdapter = new ArrayAdapter<>(SettleUp.this, android.R.layout.simple_list_item_1, results_list);
        lv.setAdapter(settleUpArrayAdapter);

    }
}