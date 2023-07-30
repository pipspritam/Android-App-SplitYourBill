package com.example.splityourbill;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    public Button createGroupButton;
    public Button addTransButton, showTransButton;
    public Button resetAll, settleUp;
//    LinearLayout layout;
    TextView noPersonText;
    ListView lv1;
TextView top, topName, groupName;
    Button editPersonButton;

    LinearLayout initialHomePage, summaryHomePage;

    dataBaseHelper dataBaseHelper = new dataBaseHelper(MainActivity.this);

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
//        layout = findViewById(R.id.linearlayout);
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
            noPersonText.setVisibility(View.VISIBLE);

        }

        if (dataBaseHelper.getEveryGroup().size() == 1) {
            initialHomePage.setVisibility(View.GONE);
            summaryHomePage.setVisibility(View.VISIBLE);
//            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.background));
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

    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }
}