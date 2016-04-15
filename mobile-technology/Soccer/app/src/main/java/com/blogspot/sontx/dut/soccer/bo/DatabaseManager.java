package com.blogspot.sontx.dut.soccer.bo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;import java.lang.String;

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
}
