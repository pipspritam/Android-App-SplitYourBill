package com.example.splityourbill;

import androidx.annotation.NonNull;

public class person {
    public String name;
    public double balance;

    public person(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    @NonNull
    @Override
    public String toString() {
        if(balance>0) {
            return name+ " gets " + Math.round(balance*100.0)/100.0;
        }
        else if(balance<0) {
            return name+ " owes " + Math.round(Math.abs(balance)*100.0)/100.0;
        }
        else {
            return name+ " is settled up";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
