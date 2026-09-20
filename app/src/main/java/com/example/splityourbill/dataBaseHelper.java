package com.example.splityourbill;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class dataBaseHelper extends SQLiteOpenHelper {

    public static final String personT = "PERSON";
    public static final String name = "NAME";
    public static final String balance = "BALANCE";

    public static final String transactionT = "TRANSACTIONTABLE";
    public static final String payee = "PAYEE";
    public static final String amount = "AMOUNT";
    public static final String description = "PAYDESCRIPTION";
    public static final String involved = "INVOLVED";

    public static final String groupName = "GROUPNAME";
    public static final String gName = "GNAME";

    public dataBaseHelper(@Nullable Context context) {
        super(context, "splitBillDataBase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatementPerson = "CREATE TABLE " + personT + " ( " + name + " TEXT PRIMARY KEY, " + balance + " REAL)";
        db.execSQL(createTableStatementPerson);

        String createTableTransaction = "CREATE TABLE " + transactionT + " (" + payee + " TEXT, " + amount + " REAL, " + description + " TEXT, " + involved + " TEXT)";
        db.execSQL(createTableTransaction);

        String createTableStatementGroupName = "CREATE TABLE " + groupName + " (" + gName + " TEXT PRIMARY KEY)";
        db.execSQL(createTableStatementGroupName);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOneGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(gName, group.getGroupName());
        long insert = db.insert(groupName, null, cv);
        return insert != -1;
    }

    public List<Group> getEveryGroup() {
        List<Group> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + groupName;
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(queryString, null)) {
            if (cursor.moveToFirst()) {
                do {
                    String nameP = cursor.getString(0);
                    Group newP = new Group(nameP);
                    returnList.add(newP);
                } while (cursor.moveToNext());
            }
        }
        return returnList;
    }

    public boolean addOne(person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(name, person.getName());
        cv.put(balance, person.getBalance());
        long insert = db.insert(personT, null, cv);
        return insert != -1;
    }

    public List<person> getEveryOne() {
        List<person> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + personT;
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(queryString, null)) {
            if (cursor.moveToFirst()) {
                do {
                    String nameP = cursor.getString(0);
                    double balanceP = cursor.getDouble(1);
                    person newP = new person(nameP, balanceP);
                    returnList.add(newP);
                } while (cursor.moveToNext());
            }
        }
        return returnList;
    }

    public boolean addOneTrans(TransactionModel transactionModel) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues cv1 = new ContentValues();
        cv1.put(payee, transactionModel.getPayee());
        cv1.put(amount, transactionModel.getAmount());
        cv1.put(description, transactionModel.getDescription());
        cv1.put(involved, transactionModel.getInvolve());
        long insert = db1.insert(transactionT, null, cv1);
        return insert != -1;
    }

    public List<TransactionModel> getEveryTrans() {
        List<TransactionModel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + transactionT;
        SQLiteDatabase db2 = this.getReadableDatabase();
        try (Cursor cursor = db2.rawQuery(queryString, null)) {
            if (cursor.moveToFirst()) {
                do {
                    String payee = cursor.getString(0);
                    double amount = cursor.getDouble(1);
                    String desc = cursor.getString(2);
                    String inv = cursor.getString(3);
                    TransactionModel transactionModel = new TransactionModel(payee, amount, desc, inv);
                    returnList.add(transactionModel);
                } while (cursor.moveToNext());
            }
        }
        return returnList;
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(personT, null, null);
            db.delete(transactionT, null, null);
            db.delete(groupName, null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void updateBalance(String personName, double val) {
        SQLiteDatabase db = this.getWritableDatabase();
        double currentBalance = 0.0;
        try (Cursor cursor = db.rawQuery("SELECT " + balance + " FROM " + personT + " WHERE " + name + " = ?", new String[]{personName})) {
            if (cursor.moveToFirst()) {
                currentBalance = cursor.getDouble(0);
            }
        }
        double newBalance = currentBalance + val;
        ContentValues cv = new ContentValues();
        cv.put(balance, newBalance);
        db.update(personT, cv, name + " = ?", new String[]{personName});
    }
}
