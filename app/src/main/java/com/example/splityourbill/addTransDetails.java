package com.example.splityourbill;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class addTransDetails extends AppCompatActivity {

    EditText payeeEditText, amountEditText, descEditText, involvedEditText;
    ListView lv;
    Button addTransDB;
    dataBaseHelper dataBaseHelper = new dataBaseHelper(addTransDetails.this);
    ArrayAdapter transactionArrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trans_details);

        payeeEditText = findViewById(R.id.addPayeeName);
        amountEditText = findViewById(R.id.addPayeeAmount);
        descEditText = findViewById(R.id.addPayeeDesc);
        involvedEditText = findViewById(R.id.addPersonInvolved);
        lv = findViewById(R.id.transViewAddLayout);

        addTransDB = findViewById(R.id.addTransToDB);

        ShowTrans(dataBaseHelper);



        addTransDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionModel transactionModel;
                try {
                    transactionModel = new TransactionModel(payeeEditText.getText().toString(), Double.parseDouble(amountEditText.getText().toString()), descEditText.getText().toString(), involvedEditText.getText().toString());

                }
                catch (Exception e) {
                    Toast.makeText(addTransDetails.this, "Error", Toast.LENGTH_SHORT).show();
                    transactionModel = new TransactionModel("Invalid",0,"Invalid","Invalid");

                }
                double totalAmount = Double.parseDouble(amountEditText.getText().toString());
                String payeeVar = payeeEditText.getText().toString();
                String involvedPerson []= involvedEditText.getText().toString().split(" ");
                int len = involvedPerson.length;
                double avg = totalAmount/len;
                for(String s: involvedPerson) {

                }


                dataBaseHelper dataBaseHelper = new dataBaseHelper(addTransDetails.this);

                boolean success = dataBaseHelper.addOneTrans(transactionModel);
                Toast.makeText(addTransDetails.this,"Success = "+success, Toast.LENGTH_SHORT).show();
                payeeEditText.setText(null);
                amountEditText.setText(null);
                descEditText.setText(null);
                involvedEditText.setText(null);


                ShowTrans(dataBaseHelper);



            }
        });
    }

    private void ShowTrans(dataBaseHelper dataBaseHelper) {
        transactionArrayAdapter = new ArrayAdapter<TransactionModel>(addTransDetails.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryTrans());
        lv.setAdapter(transactionArrayAdapter);
    }
}