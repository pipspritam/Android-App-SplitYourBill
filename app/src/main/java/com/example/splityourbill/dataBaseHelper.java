package com.example.splityourbill;

import android.app.Person;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class dataBaseHelper extends SQLiteOpenHelper
{


    public static final String personT = "PERSON";
    public static final String name = "NAME";
    public static final String balance = "BALANCE";

    public static final String transactionT = "TRANSACTIONTABLE";
    public static final String payee = "PAYEE";
    public static final String amount = "AMOUNT";
    public static final String description = "PAYDESCRIPTION";
    public static final String involved = "INVOLVED";

    public dataBaseHelper(@Nullable Context context) {
        super(context,"splitBillDataBase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableStatementPerson = "CREATE TABLE " + personT + " ( "+ name + " STRING PRIMARY KEY, " + balance + " DOUBLE(10.2))";
        db.execSQL(createTableStatementPerson);

        String createTableTransaction = "CREATE TABLE " + transactionT + " (" + payee + " STRING PRIMARY KEY, " + amount + " DOUBLE(10.2), " + description + " STRING, " + involved + " STRING)";
        db.execSQL(createTableTransaction);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addOne(person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(name, person.getName());
        cv.put(balance, person.getBalance());

        long insert = db.insert(personT,null,cv);

        if (insert==-1) {
            return false;
        }
        else {
//            numberOfPerson++;
            return true;
        }

    }
    public List<person> getEveryOne() {
        List<person> returnList = new ArrayList<>();
        String quarryString = "SELECT * FROM "+ personT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(quarryString, null);

        if (cursor.moveToFirst()) {
            do {
                String nameP = cursor.getString(0);
                double balanceP = cursor.getDouble(1);
                person newP = new person(nameP,balanceP);

                returnList.add(newP);
            }
            while (cursor.moveToNext());
        }
        else {
            cursor.close();
            db.close();
            return returnList;
        }
        return returnList;
    }

    public int getNumberOfPerson() {
        String quarryString = "SELECT * FROM "+ personT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(quarryString, null);
        int numberOfPerson = 0;

        if (cursor.moveToFirst()) {
            do {
                numberOfPerson++;
            }
            while (cursor.moveToNext());
        }
        else {
            cursor.close();
            db.close();
            return numberOfPerson;
        }
        return numberOfPerson;
    }

    public boolean addOneTrans(TransactionModel transactionModel) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues cv1 = new ContentValues();

        cv1.put(payee, transactionModel.getPayee());
        cv1.put(amount,transactionModel.getAmount());
        cv1.put(description,transactionModel.getDescription());
        cv1.put(involved,transactionModel.getInvolve());

        long insert = db1.insert(transactionT,null,cv1);

        if (insert==-1) {
            return false;
        }
        else {
            return true;
        }
    }

    public List<TransactionModel> getEveryTrans() {
        List<TransactionModel> returnList = new ArrayList<>();
        String quarryString = "SELECT * FROM "+ transactionT;
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(quarryString, null);

        if (cursor.moveToFirst()) {
            do {
                String payee = cursor.getString(0);
                Double amount = cursor.getDouble(1);
                String desc = cursor.getString(2);
                String inv = cursor.getString(3);
                TransactionModel transactionModel = new TransactionModel(payee,amount,desc,inv);

                returnList.add(transactionModel);
            }
            while (cursor.moveToNext());
        }
        else {
            cursor.close();
            db2.close();
            return returnList;
        }
        return returnList;
    }



}
