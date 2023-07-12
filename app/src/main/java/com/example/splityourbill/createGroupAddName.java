package com.example.splityourbill;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class createGroupAddName extends AppCompatActivity {

    EditText nameEditText;
    Button addButton, startTrans;
    ListView lv;

    dataBaseHelper dataBaseHelper = new dataBaseHelper(createGroupAddName.this);
    ArrayAdapter personArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_add_name);

        nameEditText = findViewById(R.id.addName);
        addButton = findViewById(R.id.addNameButton);
        lv = findViewById(R.id.listViewAddLayout);
        startTrans = findViewById(R.id.startTrans);

        ShowPerson(dataBaseHelper);


        addButton.setOnClickListener(v -> {
            person person;
            String nameinputCheck = nameEditText.getText().toString();
            nameinputCheck.trim();
            if(nameinputCheck.isEmpty()) {
                Toast.makeText(createGroupAddName.this,"Enter a valid name", Toast.LENGTH_SHORT).show();
                nameEditText.setText(null);
            }
            else {
                    person = new person (nameEditText.getText().toString(), 0);
                dataBaseHelper dataBaseHelper = new dataBaseHelper(createGroupAddName.this);
                boolean success = dataBaseHelper.addOne(person);
                ShowPerson(dataBaseHelper);
                Toast.makeText(createGroupAddName.this,"Name Added", Toast.LENGTH_SHORT).show();
                nameEditText.setText(null);
            }


        });

        startTrans.setOnClickListener(v -> {
//                MainActivity.createGroupButton.setVisibility(View.GONE);
            Intent mainIntent = new Intent(createGroupAddName.this,MainActivity.class);
            startActivity(mainIntent);
        });
    }

    private void ShowPerson(dataBaseHelper dataBaseHelper) {
        personArrayAdapter = new ArrayAdapter<>(createGroupAddName.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryOne());
        lv.setAdapter(personArrayAdapter);
    }
}