package com.example.splityourbill;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class createGroupAddName extends AppCompatActivity {

    EditText nameEditText, groupEditText;
    Button addButton, startTrans, addGroupButton;
    ListView lv;
    TextView groupNameTextView, addNameTextView;

    dataBaseHelper dataBaseHelper = new dataBaseHelper(createGroupAddName.this);
    ArrayAdapter personArrayAdapter;
    Button goToHomeButton, resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_add_name);

        nameEditText = findViewById(R.id.addName);
        addNameTextView = findViewById(R.id.addNameTitle);

        goToHomeButton = findViewById(R.id.goToHomeButton);
        resetButton = findViewById(R.id.resetButton);

        addButton = findViewById(R.id.addNameButton);
        lv = findViewById(R.id.listViewAddLayout);
        startTrans = findViewById(R.id.goToAddTrans);

        groupEditText = findViewById(R.id.addGroupNameEditText);
        addGroupButton = findViewById(R.id.addGroupNameButton);
        groupNameTextView = findViewById(R.id.addGroupName);

        ShowPerson(dataBaseHelper);

        if(dataBaseHelper.getEveryGroup().size() == 1){
            groupNameTextView.setText(dataBaseHelper.getEveryGroup().get(0).getGroupName());
            addGroupButton.setVisibility(View.GONE);
            groupEditText.setVisibility(View.GONE);
            nameEditText.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.VISIBLE);
            lv.setVisibility(View.VISIBLE);
            addNameTextView.setVisibility(View.VISIBLE);
        }


        addGroupButton.setOnClickListener(v -> {
            String groupName = groupEditText.getText().toString();
            groupName = groupName.trim();
            if (groupName.isEmpty()) {
                Toast.makeText(createGroupAddName.this, "Enter a valid Group Name", Toast.LENGTH_SHORT).show();
                groupEditText.setText(null);
            } else {
                Group group = new Group(groupName);
                dataBaseHelper.addOneGroup(group);
                Toast.makeText(createGroupAddName.this, "Group Added", Toast.LENGTH_SHORT).show();
                groupNameTextView.setText(groupName);
                groupEditText.setText(null);
                //reload activity
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });


        addButton.setOnClickListener(v -> {
            person person;
            String nameinputCheck = nameEditText.getText().toString();
            nameinputCheck = nameinputCheck.trim();
            if (nameinputCheck.isEmpty()) {
                Toast.makeText(createGroupAddName.this, "Enter a valid name", Toast.LENGTH_SHORT).show();
                nameEditText.setText(null);
            } else {
                person = new person(nameEditText.getText().toString(), 0);
                dataBaseHelper dataBaseHelper = new dataBaseHelper(createGroupAddName.this);
                dataBaseHelper.addOne(person);
                ShowPerson(dataBaseHelper);
                Toast.makeText(createGroupAddName.this, "Name Added", Toast.LENGTH_SHORT).show();
                nameEditText.setText(null);
                if (dataBaseHelper.getEveryOne().size() >= 2) {
                    startTrans.setEnabled(true);

                }
            }


        });

        startTrans.setOnClickListener(v -> {
            Intent mainIntent = new Intent(createGroupAddName.this, addTransDetails.class);
            startActivity(mainIntent);
        });


        goToHomeButton.setOnClickListener(v -> {
                Intent homeIntent = new Intent(createGroupAddName.this, MainActivity.class);
                startActivity(homeIntent);
        });

        resetButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation")
                    .setMessage("Are you sure you want to reset?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dataBaseHelper.clearDatabase();
                        Intent homeIntent = new Intent(createGroupAddName.this, MainActivity.class);
                        startActivity(homeIntent);

                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // Do nothing or handle the cancel action
                    })
                    .show();
        });
    }

    private void ShowPerson(dataBaseHelper dataBaseHelper) {
        personArrayAdapter = new ArrayAdapter<>(createGroupAddName.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryOne());
        lv.setAdapter(personArrayAdapter);
    }
}