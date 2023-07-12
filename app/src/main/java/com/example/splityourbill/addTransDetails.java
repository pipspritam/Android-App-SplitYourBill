package com.example.splityourbill;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class addTransDetails extends AppCompatActivity {

    EditText  amountEditText, descEditText, involvedEditText;
    ListView lv;
    Button addTransDB;
    dataBaseHelper dataBaseHelper = new dataBaseHelper(addTransDetails.this);
    ArrayAdapter transactionArrayAdapter;
    Spinner sp;

    public String[] getNameList() {
        List<String> nn= new ArrayList<>();
        List<person> pl;
        pl = dataBaseHelper.getEveryOne();
        try {
            for(person element: pl) {
                nn.add(element.name);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String[] names = new String[nn.size()];
        for (int i = 0; i < nn.size(); i++) {
            names[i] = nn.get(i);
        }
        return names;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trans_details);

        amountEditText = findViewById(R.id.addPayeeAmount);
        descEditText = findViewById(R.id.addPayeeDesc);
        involvedEditText = findViewById(R.id.addPersonInvolved);
        lv = findViewById(R.id.transViewAddLayout);

        addTransDB = findViewById(R.id.addTransToDB);

        sp = findViewById(R.id.payeeInput);
        ArrayAdapter<String> adapter_options = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,getNameList());
        sp.setAdapter(adapter_options);


        ShowTrans(dataBaseHelper);



        addTransDB.setOnClickListener(v -> {
            TransactionModel transactionModel;
            try {
                transactionModel = new TransactionModel(sp.getSelectedItem().toString(), Double.parseDouble(amountEditText.getText().toString()), descEditText.getText().toString(), involvedEditText.getText().toString());
            }
            catch (Exception e) {
                Toast.makeText(addTransDetails.this, "Error", Toast.LENGTH_SHORT).show();
                transactionModel = new TransactionModel("Invalid",0,"Invalid","Invalid");

            }

            dataBaseHelper dataBaseHelper = new dataBaseHelper(addTransDetails.this);
            double totalAmount = Double.parseDouble(amountEditText.getText().toString());
            String payeeVar = sp.getSelectedItem().toString();
            String[] involvedPerson = involvedEditText.getText().toString().split(" ");
            int len = involvedPerson.length;
            double avg = totalAmount/len;

            for(String s: involvedPerson) {
                dataBaseHelper.updateBalance(s,avg*(-1));
            }
            dataBaseHelper.updateBalance(payeeVar,totalAmount);




            boolean success = dataBaseHelper.addOneTrans(transactionModel);
            Toast.makeText(addTransDetails.this,"Success = "+success, Toast.LENGTH_SHORT).show();
            amountEditText.setText(null);
            descEditText.setText(null);
            involvedEditText.setText(null);
            ShowTrans(dataBaseHelper);

        });
    }

    private void ShowTrans(dataBaseHelper dataBaseHelper) {
        transactionArrayAdapter = new ArrayAdapter<>(addTransDetails.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryTrans());
        lv.setAdapter(transactionArrayAdapter);
    }
}