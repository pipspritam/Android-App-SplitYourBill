package com.example.splityourbill;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    public Button createGroupButton;
    public Button addTransButton, showTransButton;
    public Button resetAll, settleUp;
    TextView noPersonText;
    ListView lv1;
    TextView top, topName, groupName;
    Button editPersonButton;

    ConstraintLayout initialHomePage, summaryHomePage;

    dataBaseHelper dataBaseHelper = new dataBaseHelper(MainActivity.this);

    @SuppressLint("UnsafeIntentLaunch")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        summaryHomePage = findViewById(R.id.summeryHomePage);


        showTransButton.setOnClickListener(v -> {
            Intent showTransIntent = new Intent(MainActivity.this, ViewTransaction.class);
            startActivity(showTransIntent);
        });

        ShowPerson(dataBaseHelper);
        int numberOfPerson = dataBaseHelper.getEveryOne().size();
        int numberOfTrans = dataBaseHelper.getEveryTrans().size();

        if (numberOfPerson == 0 && dataBaseHelper.getEveryGroup().size() == 1) {
            System.out.println("No person");
            noPersonText.setVisibility(View.VISIBLE);

        }
        if (numberOfPerson >= 2) {
            addTransButton.setEnabled(true);
        }

        if (dataBaseHelper.getEveryGroup().size() == 1) {
            initialHomePage.setVisibility(View.GONE);
            summaryHomePage.setVisibility(View.VISIBLE);
            groupName.setText(dataBaseHelper.getEveryGroup().get(0).getGroupName());
            settleUp.setEnabled(false);
            showTransButton.setEnabled(false);

        }
        if (numberOfTrans > 0) {
            settleUp.setEnabled(true);
            showTransButton.setEnabled(true);
        }


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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation").setMessage("Are you sure you want to reset?").setPositiveButton("Yes", (dialog, which) -> {
                dataBaseHelper.clearDatabase();
                finish();
                startActivity(getIntent());
            }).setNegativeButton("No", (dialog, which) -> {
                // Do nothing or handle the cancel action
            }).show();
        });


        addTransButton.setOnClickListener(v -> {
            Intent addTransIntent = new Intent(MainActivity.this, addTransDetails.class);
            startActivity(addTransIntent);
        });

    }

    private void ShowPerson(dataBaseHelper dataBaseHelper) {
        lv1 = findViewById(R.id.lv1);
        customNameBaseAdapter customNameBaseAdapter = new customNameBaseAdapter(getApplicationContext(), dataBaseHelper.getEveryOne());
        lv1.setAdapter(customNameBaseAdapter);
    }

    @SuppressLint("UnsafeIntentLaunch")
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation").setMessage("Are you sure you want to exit?").setPositiveButton("Yes", (dialog, which) -> {
            finishAffinity();
            System.exit(0);
        }).setNegativeButton("No", (dialog, which) -> {
            // Do nothing or handle the cancel action
        }).show();

    }
}