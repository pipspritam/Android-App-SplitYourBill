package com.example.splityourbill;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class addTransDetails extends AppCompatActivity {

    EditText amountEditText, descEditText;
    ListView lv;
    Button addTransDB, goToHomeButton;
    dataBaseHelper dataBaseHelper = new dataBaseHelper(addTransDetails.this);
    ArrayAdapter transactionArrayAdapter;
    Spinner sp;
    TextView textViewInvolvedPeople, textViewGroupName;

    Button showTransButton, settleUpButton;

    ImageButton goToBackButton;
    boolean[] selectedPerson;
    ArrayList<Integer> langList = new ArrayList<>();

    public String[] getNameList() {
        List<String> nn = new ArrayList<>();
        List<person> pl;
        pl = dataBaseHelper.getEveryOne();
        try {
            for (person element : pl) {
                nn.add(element.name);
            }
        } catch (Exception e) {
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

        goToHomeButton = findViewById(R.id.goToHomeButton);
        showTransButton = findViewById(R.id.showTransButton);
        settleUpButton = findViewById(R.id.goToSettleUp);
        goToBackButton = findViewById(R.id.goToBackButton);
        textViewGroupName = findViewById(R.id.viewGroupName);

        textViewGroupName.setText(dataBaseHelper.getEveryGroup().get(0).getGroupName());


        goToBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(addTransDetails.this, MainActivity.class);
                startActivity(backIntent);
            }
        });

        showTransButton.setOnClickListener(v -> {
            Intent showTransIntent = new Intent(addTransDetails.this, ViewTransaction.class);
            startActivity(showTransIntent);
        });

        settleUpButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(addTransDetails.this, SettleUp.class);
            startActivity(backIntent);
        });


        goToHomeButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(addTransDetails.this, MainActivity.class);
            startActivity(homeIntent);
        });


        textViewInvolvedPeople = findViewById(R.id.textViewInvolved);
        selectedPerson = new boolean[getNameList().length];
        textViewInvolvedPeople.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(addTransDetails.this);
            builder.setTitle("Select Involved Person");
            builder.setCancelable(false);
            builder.setMultiChoiceItems(getNameList(), selectedPerson, (dialog, which, isChecked) -> {
                if (isChecked) {
                    langList.add(which);
                    Collections.sort(langList);
                } else {
                    langList.remove(Integer.valueOf(which));
                }
            });
            builder.setPositiveButton("Ok", (dialogInterface, i) -> updateSelectedItems());

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.setNeutralButton("Select All", (dialog, which) -> {
                for (int j = 0; j < selectedPerson.length; j++) {
                    selectedPerson[j] = true;
                    if (!langList.contains(j)) {
                        langList.add(j);
                    }
                }
                updateSelectedItems();
            });

            builder.show();

        });

        amountEditText = findViewById(R.id.addPayeeAmount);
        descEditText = findViewById(R.id.addPayeeDesc);
//        lv = findViewById(R.id.transViewAddLayout);

        addTransDB = findViewById(R.id.addTransToDB);

        sp = findViewById(R.id.payeeInput);
        ArrayAdapter<String> adapter_options = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, getNameList());
        sp.setAdapter(adapter_options);


//        ShowTrans(dataBaseHelper);


        addTransDB.setOnClickListener(v -> {
            TransactionModel transactionModel;
            if (amountEditText.getText().toString().equals("") || descEditText.getText().toString().equals("") || textViewInvolvedPeople.getText().toString().equals("")) {
                Toast.makeText(addTransDetails.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            transactionModel = new TransactionModel(sp.getSelectedItem().toString(), Double.parseDouble(amountEditText.getText().toString()), descEditText.getText().toString(), textViewInvolvedPeople.getText().toString());

            dataBaseHelper dataBaseHelper = new dataBaseHelper(addTransDetails.this);
            double totalAmount = Double.parseDouble(amountEditText.getText().toString());
            String payeeVar = sp.getSelectedItem().toString();
            String[] involvedPerson = textViewInvolvedPeople.getText().toString().split(",");
            int len = involvedPerson.length;
            double avg = totalAmount / len;

            for (String s : involvedPerson) {
                dataBaseHelper.updateBalance(s, avg * (-1));
            }
            dataBaseHelper.updateBalance(payeeVar, totalAmount);


            dataBaseHelper.addOneTrans(transactionModel);
            Toast.makeText(addTransDetails.this, "Transaction Added", Toast.LENGTH_SHORT).show();
            amountEditText.setText(null);
            descEditText.setText(null);
            textViewInvolvedPeople.setText("");

//            ShowTrans(dataBaseHelper);

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backIntent = new Intent(addTransDetails.this, MainActivity.class);
        startActivity(backIntent);
        finish();
    }


//    private void ShowTrans(dataBaseHelper dataBaseHelper) {
//        transactionArrayAdapter = new ArrayAdapter<>(addTransDetails.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryTrans());
//        lv.setAdapter(transactionArrayAdapter);
//    }

    private void updateSelectedItems() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < langList.size(); j++) {
            stringBuilder.append(getNameList()[langList.get(j)]);
            if (j != langList.size() - 1) {
                stringBuilder.append(",");
            }
        }
        textViewInvolvedPeople.setText(stringBuilder.toString());
    }
}