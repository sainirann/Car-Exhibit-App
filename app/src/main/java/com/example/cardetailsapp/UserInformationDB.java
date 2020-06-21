package com.example.cardetailsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * User infrmation database storing Helper
 */
public class UserInformationDB extends SQLiteOpenHelper {

    public static final String dataBaseName = "RegistrationDB.db";
    public static final String tableName = "UserRegistration";
    public static final String userName = "Name";
    public static final String password = "Password";
    public static final String emailID = "Email_ID";
    public static final String phoneNumber = "Phone_Number";


    public UserInformationDB (Context context) {
        super(context, dataBaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + tableName + "(Name TEXT PRIMARY KEY, Password TEXT, Email_ID TEXT, Phone_Number TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }

    /**
     * Insert the user data into database
     * @param user
     * @param pwd
     * @param email
     * @param phone
     * @return
     */
    public boolean insertData(String user, String pwd, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(userName, user);
        contentValues.put(password, pwd);
        contentValues.put(emailID, email);
        contentValues.put(phoneNumber, phone);
        long isValuesInserted = db.insert(tableName, null, contentValues);
        return isValuesInserted != -1;
    }

    /**
     * Query user information and verifies whether username and password match
     * @param name
     * @param pwd
     * @return
     */
    public boolean queryData(String name, String pwd) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + tableName + " WHERE " + userName + " = '" + name + "' AND " + password + " = '" + pwd +"'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }
}
