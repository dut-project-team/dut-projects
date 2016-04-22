package com.blogspot.sontx.dut.soccer.bo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blogspot.sontx.dut.soccer.bean.Account;

import java.lang.String;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 08/04/2016.
 */
public final class DatabaseManager {
    private static DatabaseManager instance = new DatabaseManager();

    public static DatabaseManager getInstance() {
        return instance;
    }

    private SQLiteDatabase mSQLiteDatabase;

    DatabaseManager() {
    }

    public void open(String databasePath) {
        if (mSQLiteDatabase != null && mSQLiteDatabase.isOpen())
            mSQLiteDatabase.close();
        mSQLiteDatabase = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void close() {
        if (mSQLiteDatabase != null)
            mSQLiteDatabase.close();
    }

    public int getAccountId(String email, String password) {
        String sql = "SELECT account_id FROM accounts WHERE email='%s' AND password='%s'";
        sql = String.format(sql, email, password);
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        int id = -1;
        if (cursor.moveToFirst())
            id = cursor.getInt(0);
        cursor.close();
        return id;
    }

    public Account getAccountById(int accountId) {
        String sql = "SELECT email FROM accounts WHERE account_id=%d";
        sql = String.format(sql, accountId);
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        Account account = null;
        if (cursor.moveToFirst()) {
            account = new Account();
            account.setId(accountId);
            account.setEmail(cursor.getString(0));
        }
        cursor.close();
        return account;
    }

    public boolean checkAccountExists(String email) {
        String sql = "SELECT * FROM accounts WHERE email='%s'";
        sql = String.format(sql, email);
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean createAccount(String email, String password) {
        String sql = "INSERT INTO accounts(email, password) VALUES('%s', '%s')";
        sql = String.format(sql, email, password);
        mSQLiteDatabase.execSQL(sql);
        return true;
    }
}
