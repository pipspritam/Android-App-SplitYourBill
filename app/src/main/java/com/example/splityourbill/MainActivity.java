package com.example.splityourbill;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public Button createGroupButton;
    public Button addTransButton;
    public Button resetAll, settleUp;
    ListView lv1;

    dataBaseHelper dataBaseHelper = new dataBaseHelper(MainActivity.this);
    ArrayAdapter personArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createGroupButton = findViewById(R.id.createGroupButton);
        addTransButton = findViewById(R.id.addTransButton);
        resetAll = findViewById(R.id.resetButton);
        settleUp = findViewById(R.id.splitButton);
        ShowPerson(dataBaseHelper);
        int numberOfPerson = dataBaseHelper.getEveryOne().size();
        int numberOfTrans = dataBaseHelper.getEveryTrans().size();

        if (numberOfPerson > 0) {
            createGroupButton.setVisibility(View.GONE);
            addTransButton.setVisibility(View.VISIBLE);
            resetAll.setVisibility(View.VISIBLE);
        }
        if (numberOfTrans > 0) {
            settleUp.setVisibility(View.VISIBLE);
        }


        settleUp.setOnClickListener(v -> {
            if (v.getId() == R.id.splitButton) {
                Intent settleUpIntent = new Intent(MainActivity.this, SettleUp.class);
                startActivity(settleUpIntent);
            }
        });

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.createGroupButton) {
                    Intent createGroupIntent = new Intent(MainActivity.this, createGroupAddName.class);
                    startActivity(createGroupIntent);
                }
            }
        });

        resetAll.setOnClickListener(v -> {
            dataBaseHelper.clearDatabase();
            finish();
            startActivity(getIntent());
        });

        addTransButton.setOnClickListener(v -> {
            if (v.getId() == R.id.addTransButton) {
                Intent addTransIntent = new Intent(MainActivity.this, addTransDetails.class);
                startActivity(addTransIntent);
            }
        });

    }

    private void ShowPerson(dataBaseHelper dataBaseHelper) {
        lv1 = findViewById(R.id.listView);
        personArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryOne());
        lv1.setAdapter(personArrayAdapter);
    }

    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    //on back pressed
    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }
}