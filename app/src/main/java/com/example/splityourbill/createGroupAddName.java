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

import java.util.List;

public class createGroupAddName extends AppCompatActivity {

    private EditText nameEditText, groupEditText;
    private Button addButton, startTrans, addGroupButton;
    private ListView lv;
    private TextView groupNameTextView, addNameTextView;
    private final dataBaseHelper dbHelper = new dataBaseHelper(this);
    private ArrayAdapter<person> personArrayAdapter;
    private Button goToHomeButton, resetButton;

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

        showPerson();
        if (dbHelper.getEveryOne().size() >= 2) {
            startTrans.setEnabled(true);
        }

        List<Group> groups = dbHelper.getEveryGroup();
        if (groups.size() == 1) {
            groupNameTextView.setText(groups.get(0).getGroupName());
            addGroupButton.setVisibility(View.GONE);
            groupEditText.setVisibility(View.GONE);
            nameEditText.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.VISIBLE);
            lv.setVisibility(View.VISIBLE);
            addNameTextView.setVisibility(View.VISIBLE);
        }

        addGroupButton.setOnClickListener(v -> {
            String groupName = groupEditText.getText().toString().trim();
            if (groupName.isEmpty()) {
                Toast.makeText(createGroupAddName.this, "Enter a valid Group Name", Toast.LENGTH_SHORT).show();
                groupEditText.setText(null);
            } else {
                Group group = new Group(groupName);
                if (dbHelper.addOneGroup(group)) {
                    Toast.makeText(createGroupAddName.this, "Group Added", Toast.LENGTH_SHORT).show();
                    groupNameTextView.setText(groupName);
                    groupEditText.setText(null);
                    addGroupButton.setVisibility(View.GONE);
                    groupEditText.setVisibility(View.GONE);
                    nameEditText.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.VISIBLE);
                    addNameTextView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(createGroupAddName.this, "Failed to create group", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addButton.setOnClickListener(v -> {
            String nameInput = nameEditText.getText().toString().trim();
            if (nameInput.isEmpty()) {
                Toast.makeText(createGroupAddName.this, "Enter a valid name", Toast.LENGTH_SHORT).show();
                nameEditText.setText(null);
                return;
            }

            person newPerson = new person(nameInput, 0);
            boolean inserted = dbHelper.addOne(newPerson);
            if (inserted) {
                showPerson();
                Toast.makeText(createGroupAddName.this, "Name Added", Toast.LENGTH_SHORT).show();
                nameEditText.setText(null);
                if (dbHelper.getEveryOne().size() >= 2) {
                    startTrans.setEnabled(true);
                }
            } else {
                Toast.makeText(createGroupAddName.this, "Person with this name already exists", Toast.LENGTH_SHORT).show();
            }
        });

        startTrans.setOnClickListener(v -> {
            Intent mainIntent = new Intent(createGroupAddName.this, addTransDetails.class);
            startActivity(mainIntent);
        });

        goToHomeButton.setOnClickListener(v -> finish());

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
    }

    private void showPerson() {
        personArrayAdapter = new ArrayAdapter<>(createGroupAddName.this, android.R.layout.simple_list_item_1, dbHelper.getEveryOne());
        lv.setAdapter(personArrayAdapter);
    }
}