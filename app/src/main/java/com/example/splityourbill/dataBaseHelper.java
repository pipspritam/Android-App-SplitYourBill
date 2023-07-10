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
    public static int numberOfPerson=0;

    public static final String personT = "PERSON";
    public static final String name = "NAME";
    public static final String balance = "BALANCE";

    public dataBaseHelper(@Nullable Context context) {
        super(context,"splitBillDataBase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableStatementPerson = "CREATE TABLE " + personT + " ( "+ name + " STRING PRIMARY KEY, " + balance + " DOUBLE(10.2))";
        db.execSQL(createTableStatementPerson);
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
            numberOfPerson++;
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

//    public int getNumberOfPerson() {
//        String quarryString = "SELECT count(*) FROM "+ personT;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(quarryString, null);
//        System.out.println(cursor);
//
//        return 0;
//
//    }
}
