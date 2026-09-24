package com.example.splityourbill;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class addTransDetails extends AppCompatActivity {

    private EditText amountEditText, descEditText;
    private Button addTransDB, goToHomeButton, showTransButton, settleUpButton;
    private final dataBaseHelper dbHelper = new dataBaseHelper(this);
    private Spinner sp;
    private TextView textViewInvolvedPeople, textViewGroupName;
    private ImageButton goToBackButton;
    private boolean[] selectedPerson;
    private final ArrayList<Integer> selectedPersonIndices = new ArrayList<>();

    public String[] getNameList() {
        List<person> pl = dbHelper.getEveryOne();
        String[] names = new String[pl.size()];
        for (int i = 0; i < pl.size(); i++) {
            names[i] = pl.get(i).getName();
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

        List<Group> groups = dbHelper.getEveryGroup();
        if (!groups.isEmpty()) {
            textViewGroupName.setText(groups.get(0).getGroupName());
        } else {
            textViewGroupName.setText(R.string.group_name);
        }

        goToBackButton.setOnClickListener(v -> finish());
        goToHomeButton.setOnClickListener(v -> finish());

        showTransButton.setOnClickListener(v -> {
            Intent showTransIntent = new Intent(addTransDetails.this, ViewTransaction.class);
            startActivity(showTransIntent);
        });

        settleUpButton.setOnClickListener(v -> {
            Intent settleUpIntent = new Intent(addTransDetails.this, SettleUp.class);
            startActivity(settleUpIntent);
        });

        textViewInvolvedPeople = findViewById(R.id.textViewInvolved);
        String[] names = getNameList();
        selectedPerson = new boolean[names.length];

        if (savedInstanceState != null) {
            boolean[] savedChecks = savedInstanceState.getBooleanArray("selectedPerson");
            ArrayList<Integer> savedIndices = savedInstanceState.getIntegerArrayList("selectedPersonIndices");
            if (savedChecks != null && savedChecks.length == names.length) {
                selectedPerson = savedChecks;
            }
            if (savedIndices != null) {
                selectedPersonIndices.clear();
                selectedPersonIndices.addAll(savedIndices);
                updateSelectedItems();
            }
        }

        textViewInvolvedPeople.setOnClickListener(v -> {
            String[] currentNames = getNameList();
            if (selectedPerson == null || selectedPerson.length != currentNames.length) {
                selectedPerson = new boolean[currentNames.length];
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(addTransDetails.this);
            builder.setTitle("Select Involved Person");
            builder.setCancelable(false);
            builder.setMultiChoiceItems(currentNames, selectedPerson, (dialog, which, isChecked) -> {
                if (isChecked) {
                    if (!selectedPersonIndices.contains(which)) {
                        selectedPersonIndices.add(which);
                        Collections.sort(selectedPersonIndices);
                    }
                } else {
                    selectedPersonIndices.remove(Integer.valueOf(which));
                }
            });
            builder.setPositiveButton("Ok", (dialogInterface, i) -> updateSelectedItems());
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.setNeutralButton("Select All", (dialog, which) -> {
                for (int j = 0; j < selectedPerson.length; j++) {
                    selectedPerson[j] = true;
                    if (!selectedPersonIndices.contains(j)) {
                        selectedPersonIndices.add(j);
                    }
                }
                Collections.sort(selectedPersonIndices);
                updateSelectedItems();
            });
            builder.show();
        });

        amountEditText = findViewById(R.id.addPayeeAmount);
        descEditText = findViewById(R.id.addPayeeDesc);
        addTransDB = findViewById(R.id.addTransToDB);

        sp = findViewById(R.id.payeeInput);
        ArrayAdapter<String> adapterOptions = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, names);
        sp.setAdapter(adapterOptions);

        addTransDB.setOnClickListener(v -> {
            String amountStr = amountEditText.getText().toString().trim();
            String desc = descEditText.getText().toString().trim();
            String involvedStr = textViewInvolvedPeople.getText().toString().trim();

            if (amountStr.isEmpty() || desc.isEmpty() || involvedStr.isEmpty()) {
                Toast.makeText(addTransDetails.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (sp.getSelectedItem() == null) {
                Toast.makeText(addTransDetails.this, "Please select a payee", Toast.LENGTH_SHORT).show();
                return;
            }

            double totalAmount;
            try {
                totalAmount = Double.parseDouble(amountStr);
                if (totalAmount <= 0) {
                    Toast.makeText(addTransDetails.this, "Please enter a valid positive amount", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(addTransDetails.this, "Invalid amount format", Toast.LENGTH_SHORT).show();
                return;
            }

            String payeeVar = sp.getSelectedItem().toString();
            String[] involvedPerson = involvedStr.split(",");
            int len = involvedPerson.length;
            if (len == 0) {
                return;
            }
            double avg = totalAmount / len;

            for (String s : involvedPerson) {
                String trimmedName = s.trim();
                if (!trimmedName.isEmpty()) {
                    dbHelper.updateBalance(trimmedName, -avg);
                }
            }
            dbHelper.updateBalance(payeeVar, totalAmount);

            TransactionModel transactionModel = new TransactionModel(payeeVar, totalAmount, desc, involvedStr);
            dbHelper.addOneTrans(transactionModel);

            Toast.makeText(addTransDetails.this, "Transaction Added", Toast.LENGTH_SHORT).show();
            amountEditText.setText(null);
            descEditText.setText(null);
            textViewInvolvedPeople.setText("");

            selectedPersonIndices.clear();
            if (selectedPerson != null) {
                Arrays.fill(selectedPerson, false);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBooleanArray("selectedPerson", selectedPerson);
        outState.putIntegerArrayList("selectedPersonIndices", selectedPersonIndices);
    }

    private void updateSelectedItems() {
        String[] currentNames = getNameList();
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < selectedPersonIndices.size(); j++) {
            int index = selectedPersonIndices.get(j);
            if (index >= 0 && index < currentNames.length) {
                stringBuilder.append(currentNames[index]);
                if (j != selectedPersonIndices.size() - 1) {
                    stringBuilder.append(",");
                }
            }
        }
        textViewInvolvedPeople.setText(stringBuilder.toString());
    }
}