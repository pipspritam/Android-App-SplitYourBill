package com.example.splityourbill;

import androidx.annotation.NonNull;

public class TransactionModel {
    public String payee;
    public double amount;
    public String description;
    public String involve;

    public TransactionModel(String payee, double amount, String description, String involve) {
        this.payee = payee;
        this.amount = amount;
        this.description = description;
        this.involve = involve;
    }

    @NonNull
    @Override
    public String toString() {

        return payee + " paid Rs " + amount + "\nCategory: " + description + " \nSplit Among: " + involve;
    }

    public String getPayee() {
        return payee;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getInvolve() {


        return involve;
    }

}
