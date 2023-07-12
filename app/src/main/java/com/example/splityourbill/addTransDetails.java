package com.example.splityourbill;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;


public class addTransDetails extends AppCompatActivity {

    EditText payeeEditText, amountEditText, descEditText, involvedEditText;
    ListView lv;
    Button addTransDB;
    dataBaseHelper dataBaseHelper = new dataBaseHelper(addTransDetails.this);
    ArrayAdapter transactionArrayAdapter;
    Spinner sp;

//    public String[] names = { "Arya", "Pritam", "Ayush" };

    public String[] getNameList() {
        String[] names = { "Arya", "Pritam", "Ayush" };


        return names;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trans_details);

//        payeeEditText = findViewById(R.id.addPayeeName);

        amountEditText = findViewById(R.id.addPayeeAmount);
        descEditText = findViewById(R.id.addPayeeDesc);
        involvedEditText = findViewById(R.id.addPersonInvolved);
        lv = findViewById(R.id.transViewAddLayout);

        addTransDB = findViewById(R.id.addTransToDB);

        sp = findViewById(R.id.payeeInput);
        ArrayAdapter<String> adapter_options = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,getNameList());
        sp.setAdapter(adapter_options);

//        sp.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
//        ArrayAdapter ad
//                = new ArrayAdapter(
//                this,
//                android.R.layout.simple_spinner_item,
//                names);
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//        sp.setAdapter(ad);


        ShowTrans(dataBaseHelper);



        addTransDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionModel transactionModel;
                try {
//                    transactionModel = new TransactionModel(payeeEditText.getText().toString(), Double.parseDouble(amountEditText.getText().toString()), descEditText.getText().toString(), involvedEditText.getText().toString());
                    transactionModel = new TransactionModel(sp.getSelectedItem().toString(), Double.parseDouble(amountEditText.getText().toString()), descEditText.getText().toString(), involvedEditText.getText().toString());
                }
                catch (Exception e) {
                    Toast.makeText(addTransDetails.this, "Error", Toast.LENGTH_SHORT).show();
                    transactionModel = new TransactionModel("Invalid",0,"Invalid","Invalid");

                }
//                List<person> lp = dataBaseHelper.getEveryOne();

                dataBaseHelper dataBaseHelper = new dataBaseHelper(addTransDetails.this);
                double totalAmount = Double.parseDouble(amountEditText.getText().toString());
//                String payeeVar = payeeEditText.getText().toString();
                String payeeVar = sp.getSelectedItem().toString();
//                String payeeVar = "Pritam";
                String[] involvedPerson = involvedEditText.getText().toString().split(" ");
                String ip = Arrays.toString(involvedPerson);
                int len = involvedPerson.length;
                double avg = totalAmount/len;

                for(String s: involvedPerson) {
                    dataBaseHelper.updateBalance(s,avg*(-1));
//                    System.out.println(s);
                }
                dataBaseHelper.updateBalance(payeeVar,totalAmount);
//                int x = dataBaseHelper.getNumberOfPerson();
//                System.out.println(payeeVar);
//                System.out.println(totalAmount);
//                System.out.println(len);
//                System.out.println(avg);



                boolean success = dataBaseHelper.addOneTrans(transactionModel);
                Toast.makeText(addTransDetails.this,"Success = "+success, Toast.LENGTH_SHORT).show();
//                payeeEditText.setText(null);
                amountEditText.setText(null);
                descEditText.setText(null);
                involvedEditText.setText(null);


                ShowTrans(dataBaseHelper);



            }
        });
    }

    private void ShowTrans(dataBaseHelper dataBaseHelper) {
        transactionArrayAdapter = new ArrayAdapter<>(addTransDetails.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryTrans());
        lv.setAdapter(transactionArrayAdapter);
    }
}