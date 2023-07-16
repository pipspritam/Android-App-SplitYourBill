package com.example.splityourbill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class ViewTransaction extends AppCompatActivity {

    ListView lv1;
    Button goToHomeButton;
    ImageButton goToBackButton;
    ArrayAdapter transactionArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);

        goToHomeButton = findViewById(R.id.goToHomeButton);
        goToBackButton = findViewById(R.id.goToBackButton);


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
        transactionArrayAdapter = new ArrayAdapter<>(ViewTransaction.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryTrans());
        lv1.setAdapter(transactionArrayAdapter);
    }
}


