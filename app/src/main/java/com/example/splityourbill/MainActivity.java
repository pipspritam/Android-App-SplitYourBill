package com.example.splityourbill;

import static com.example.splityourbill.dataBaseHelper.numberOfPerson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    public  static  Button createGroupButton;
    ListView lv1;

    dataBaseHelper dataBaseHelper = new dataBaseHelper(MainActivity.this);
    ArrayAdapter personArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createGroupButton = findViewById(R.id.createGroupButton);
        ShowPerson(dataBaseHelper);

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