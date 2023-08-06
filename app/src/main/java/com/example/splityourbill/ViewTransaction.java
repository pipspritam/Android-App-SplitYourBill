package com.example.splityourbill;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewTransaction extends AppCompatActivity {

    ListView lv1;
    Button goToHomeButton;
    ImageButton goToBackButton;
    Button settleUpButton, addTransButton;
    TextView textViewGroupName;

    dataBaseHelper dataBaseHelper = new dataBaseHelper(ViewTransaction.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);

        goToHomeButton = findViewById(R.id.goToHomeButton);
        goToBackButton = findViewById(R.id.goToBackButton);
        settleUpButton = findViewById(R.id.goToSettleUp);
        addTransButton = findViewById(R.id.goToAddTrans);
        textViewGroupName = findViewById(R.id.viewGroupName);


        textViewGroupName.setText(dataBaseHelper.getEveryGroup().get(0).getGroupName());


        settleUpButton.setOnClickListener(v -> {
            if (v.getId() == R.id.goToSettleUp) {
                Intent backIntent = new Intent(ViewTransaction.this, SettleUp.class);
                startActivity(backIntent);
            }

        });

        addTransButton.setOnClickListener(v -> {
            if (v.getId() == R.id.goToAddTrans) {
                Intent backIntent = new Intent(ViewTransaction.this, addTransDetails.class);
                startActivity(backIntent);
            }
        });


        goToBackButton.setOnClickListener(v -> {
            if (v.getId() == R.id.goToBackButton) {
                Intent backIntent = new Intent(ViewTransaction.this, MainActivity.class);
                startActivity(backIntent);
            }
        });


        goToHomeButton.setOnClickListener(v -> {
            if (v.getId() == R.id.goToHomeButton) {
                Intent homeIntent = new Intent(ViewTransaction.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });

        lv1 = findViewById(R.id.lv1);
        dataBaseHelper dataBaseHelper = new dataBaseHelper(ViewTransaction.this);
        customBaseAdapter customBaseAdapter = new customBaseAdapter(getApplicationContext(), dataBaseHelper.getEveryTrans());
        lv1.setAdapter(customBaseAdapter);

    }
}


