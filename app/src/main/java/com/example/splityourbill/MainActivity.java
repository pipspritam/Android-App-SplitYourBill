package com.example.splityourbill;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    public  Button createGroupButton;
    public Button addTransButton;
    ListView lv1;

    dataBaseHelper dataBaseHelper = new dataBaseHelper(MainActivity.this);
    ArrayAdapter personArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createGroupButton = findViewById(R.id.createGroupButton);
        addTransButton  = findViewById(R.id.addTransButton);
        ShowPerson(dataBaseHelper);
        dataBaseHelper dbh = new dataBaseHelper(MainActivity.this);
        int numberOfPerson = dbh.getNumberOfPerson();

        if(numberOfPerson>0) {
            createGroupButton.setVisibility(View.GONE);
        }

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.createGroupButton) {
                    Intent createGroupIntent = new Intent(MainActivity.this,createGroupAddName.class);
                    startActivity(createGroupIntent);
                }
            }
        });

        addTransButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.addTransButton) {
                    Intent addTransIntent = new Intent(MainActivity.this,addTransDetails.class);
                    startActivity(addTransIntent);
                }
            }
        });

    }

    private void ShowPerson(dataBaseHelper dataBaseHelper) {
        lv1 = findViewById(R.id.listView);
        personArrayAdapter = new ArrayAdapter<person>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryOne());
        lv1.setAdapter(personArrayAdapter);
    }

    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}