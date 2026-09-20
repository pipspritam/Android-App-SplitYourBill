package com.example.splityourbill;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ViewTransaction extends AppCompatActivity {

    private ListView lv1;
    private Button goToHomeButton;
    private ImageButton goToBackButton;
    private Button settleUpButton, addTransButton;
    private TextView textViewGroupName;
    private final dataBaseHelper dbHelper = new dataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);

        goToHomeButton = findViewById(R.id.goToHomeButton);
        goToBackButton = findViewById(R.id.goToBackButton);
        settleUpButton = findViewById(R.id.goToSettleUp);
        addTransButton = findViewById(R.id.goToAddTrans);
        textViewGroupName = findViewById(R.id.viewGroupName);

        List<Group> groups = dbHelper.getEveryGroup();
        if (!groups.isEmpty()) {
            textViewGroupName.setText(groups.get(0).getGroupName());
        } else {
            textViewGroupName.setText(R.string.group_name);
        }

        settleUpButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(ViewTransaction.this, SettleUp.class);
            startActivity(backIntent);
            finish();
        });

        addTransButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(ViewTransaction.this, addTransDetails.class);
            startActivity(backIntent);
            finish();
        });

        goToBackButton.setOnClickListener(v -> finish());
        goToHomeButton.setOnClickListener(v -> finish());

        lv1 = findViewById(R.id.lv1);
        customBaseAdapter customAdapter = new customBaseAdapter(this, dbHelper.getEveryTrans());
        lv1.setAdapter(customAdapter);
    }
}


