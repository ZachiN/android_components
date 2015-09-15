package com.examples.components.addressbookapp.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zachi on 29/08/2015.
 */
public class DBTools extends SQLiteOpenHelper {
    public DBTools(Context applicationContext) {
        super(applicationContext, "contactbook.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query = "CREATE TABLE contacts (contactId INTEGER PRIMARY KEY, firstName TEXT, " +
                "lastName TEXT, phoneNumber TEXT, emailAddress TEXT, homeAddress TEXT)";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS contacts";
        database.execSQL(query);
        onCreate(database);
    }

    public void insertContact(HashMap<String, String> queryValues) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("firstName", queryValues.get("firstName"));
        values.put("lastName", queryValues.get("lastName"));
        values.put("phoneNumber", queryValues.get("phoneNumber"));
        values.put("emailAddress", queryValues.get("emailAddress"));
        values.put("homeAddress", queryValues.get("homeAddress"));

        database.insert("contacts", null, values);
        database.close();
    }

    public int updateContact(HashMap<String, String> queryValues) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("firstName", queryValues.get("firstName"));
        values.put("lastName", queryValues.get("lastName"));
        values.put("phoneNumber", queryValues.get("phoneNumber"));
        values.put("emailAddress", queryValues.get("emailAddress"));
        values.put("homeAddress", queryValues.get("homeAddress"));

        return database.update("contacts", values,
                "contactId" + " = ?", new String[]{queryValues.get("contactId")});

    }

    public void deleteContact(String id) {

        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM contacts WHERE contactId='" + id + "'";

        database.execSQL(deleteQuery);

    }

    public ArrayList<HashMap<String, String>> getAllContacts() {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        String selectQuery = "SELECT * FROM contacts ORDER BY lastName";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> contactMap = new HashMap<>();

                contactMap.put("contactId", cursor.getString(0));
                contactMap.put("firstName", cursor.getString(1));
                contactMap.put("lastName", cursor.getString(2));
                contactMap.put("phoneNumber", cursor.getString(3));
                contactMap.put("emailAddress", cursor.getString(4));
                contactMap.put("homeAddress", cursor.getString(5));

                result.add(contactMap);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public HashMap<String, String> getContactInfo(String id) {
        HashMap<String, String> result = new HashMap<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM contacts WHERE contactId='" + id + "'";
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                result.put("contactId", cursor.getString(0));
                result.put("firstName", cursor.getString(1));
                result.put("lastName", cursor.getString(2));
                result.put("phoneNumber", cursor.getString(3));
                result.put("emailAddress", cursor.getString(4));
                result.put("homeAddress", cursor.getString(5));
            } while (cursor.moveToNext());
        }
        return result;

    }
}
