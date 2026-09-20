package com.example.splityourbill;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public Button createGroupButton;
    public Button addTransButton, showTransButton;
    public Button resetAll, settleUp;
    private TextView noPersonText;
    private ListView lv1;
    private TextView top, topName, groupName;
    private Button editPersonButton;

    private ConstraintLayout initialHomePage, summaryHomePage;

    private dataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new dataBaseHelper(this);

        top = findViewById(R.id.textViewTop);
        topName = findViewById(R.id.textViewTopName);
        groupName = findViewById(R.id.textViewGroupName);
        editPersonButton = findViewById(R.id.editPersonButton);
        noPersonText = findViewById(R.id.noPersonText);

        createGroupButton = findViewById(R.id.createGroupButton);
        addTransButton = findViewById(R.id.addTransButton);
        resetAll = findViewById(R.id.resetButton);
        settleUp = findViewById(R.id.splitButton);
        showTransButton = findViewById(R.id.showTransButton);
        lv1 = findViewById(R.id.lv1);

        initialHomePage = findViewById(R.id.initialHomePage);
        summaryHomePage = findViewById(R.id.summaryHomePage);

        showTransButton.setOnClickListener(v -> {
            Intent showTransIntent = new Intent(MainActivity.this, ViewTransaction.class);
            startActivity(showTransIntent);
        });

        editPersonButton.setOnClickListener(v -> {
            Intent addPersonIntent = new Intent(MainActivity.this, createGroupAddName.class);
            startActivity(addPersonIntent);
        });

        settleUp.setOnClickListener(v -> {
            Intent settleUpIntent = new Intent(MainActivity.this, SettleUp.class);
            startActivity(settleUpIntent);
        });

        createGroupButton.setOnClickListener(v -> {
            Intent createGroupIntent = new Intent(MainActivity.this, createGroupAddName.class);
            startActivity(createGroupIntent);
        });

        resetAll.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to reset?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.clearDatabase();
                        refreshData();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        addTransButton.setOnClickListener(v -> {
            Intent addTransIntent = new Intent(MainActivity.this, addTransDetails.class);
            startActivity(addTransIntent);
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", (dialog, which) -> finish())
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        List<person> people = dbHelper.getEveryOne();
        List<Group> groups = dbHelper.getEveryGroup();
        List<TransactionModel> transactions = dbHelper.getEveryTrans();

        customNameBaseAdapter customNameAdapter = new customNameBaseAdapter(this, people);
        lv1.setAdapter(customNameAdapter);

        boolean hasGroup = groups.size() == 1;
        if (hasGroup) {
            initialHomePage.setVisibility(View.GONE);
            summaryHomePage.setVisibility(View.VISIBLE);
            groupName.setText(groups.get(0).getGroupName());
        } else {
            initialHomePage.setVisibility(View.VISIBLE);
            summaryHomePage.setVisibility(View.GONE);
        }

        noPersonText.setVisibility((people.isEmpty() && hasGroup) ? View.VISIBLE : View.GONE);
        addTransButton.setEnabled(people.size() >= 2);
        settleUp.setEnabled(hasGroup && !transactions.isEmpty());
        showTransButton.setEnabled(hasGroup && !transactions.isEmpty());
    }
}