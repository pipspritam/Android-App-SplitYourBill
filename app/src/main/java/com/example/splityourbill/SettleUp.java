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
import java.util.Locale;

public class SettleUp extends AppCompatActivity {
    private ListView lv;
    private ArrayAdapter<String> settleUpArrayAdapter;
    private Button goToHomeButton, viewTransButton, resetButton;
    private TextView groupNameTextView;
    private final dataBaseHelper dbHelper = new dataBaseHelper(this);

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
        resetButton = findViewById(R.id.resetButton);

        List<Group> groups = dbHelper.getEveryGroup();
        if (!groups.isEmpty()) {
            groupNameTextView.setText(groups.get(0).getGroupName());
        } else {
            groupNameTextView.setText(R.string.group_name);
        }

        resetButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to reset?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.clearDatabase();
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        viewTransButton.setOnClickListener(v -> {
            Intent viewTransIntent = new Intent(SettleUp.this, ViewTransaction.class);
            startActivity(viewTransIntent);
            finish();
        });

        goToHomeButton.setOnClickListener(v -> finish());

        List<person> listOfPerson = dbHelper.getEveryOne();
        listOfPerson.removeIf(p -> Math.abs(p.balance) < 0.01);
        List<String> resultsList = new ArrayList<>();

        while (listOfPerson.size() > 1) {
            sortPeopleByBalance(listOfPerson);
            person debtor = listOfPerson.get(0);
            person creditor = listOfPerson.get(listOfPerson.size() - 1);

            if (debtor.balance >= -0.01 || creditor.balance <= 0.01) {
                break;
            }

            double debit = Math.abs(debtor.balance);
            double credit = creditor.balance;
            double settledAmount = Math.min(debit, credit);

            resultsList.add(String.format(Locale.getDefault(), "%s pays %s Rs %.2f", debtor.name, creditor.name, settledAmount));

            debtor.balance += settledAmount;
            creditor.balance -= settledAmount;

            if (Math.abs(debtor.balance) < 0.01) {
                listOfPerson.remove(0);
            }
            if (!listOfPerson.isEmpty() && Math.abs(listOfPerson.get(listOfPerson.size() - 1).balance) < 0.01) {
                listOfPerson.remove(listOfPerson.size() - 1);
            }
        }

        if (resultsList.isEmpty()) {
            resultsList.add("Everything is settled up already");
        }

        settleUpArrayAdapter = new ArrayAdapter<>(SettleUp.this, android.R.layout.simple_list_item_1, resultsList);
        lv.setAdapter(settleUpArrayAdapter);
    }
}