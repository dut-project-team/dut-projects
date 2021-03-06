package com.blogspot.sontx.dut.soccer.bo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blogspot.sontx.dut.soccer.bean.Account;
import com.blogspot.sontx.dut.soccer.bean.City;
import com.blogspot.sontx.dut.soccer.bean.District;
import com.blogspot.sontx.dut.soccer.bean.Field;
import com.blogspot.sontx.dut.soccer.bean.Match;
import com.blogspot.sontx.dut.soccer.bean.Profile;
import com.blogspot.sontx.dut.soccer.utils.DateTime;

import java.lang.String;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public void changePassword(int accountId, String password) {
        String sql = "UPDATE accounts SET password = '%s' WHERE account_id=%d";
        sql = String.format(sql, password, accountId);
        mSQLiteDatabase.execSQL(sql);
    }

    public List<Match> getMatchesByCityId(int cityId) {
        String sql = "SELECT matches.* FROM matches " +
                " JOIN fields ON matches.field_id=fields.field_id " +
                " JOIN districts ON fields.district_id=districts.district_id " +
                " JOIN cities ON cities.city_id=districts.city_id AND cities.city_id=%d";
        sql = String.format(sql, cityId);
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        List<Match> matches = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                Match match = new Match();
                match.setMatchId(cursor.getInt(0));
                match.setFieldId(cursor.getInt(1));
                match.setHostId(cursor.getInt(2));
                match.setNumberOfAvailableSlots(cursor.getInt(3));
                match.setNumberOfSlots(cursor.getInt(4));
                match.setMoneyPerSlot(cursor.getInt(5));
                match.setStartTime(DateTime.parse(cursor.getString(6)));
                match.setEndTime(DateTime.parse(cursor.getString(7)));
                match.setIsVerified(cursor.getInt(8) != 0);
                match.setCreatedTime(DateTime.parse(cursor.getString(10)));
                matches.add(match);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        cursor.close();
        return matches;
    }

    public Field getField(int fieldId) {
        String sql = "SELECT * FROM fields WHERE field_id=%d";
        sql = String.format(sql, fieldId);
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        Field field = null;
        if (cursor.moveToFirst()) {
            field = new Field();
            field.setFieldId(fieldId);
            field.setName(cursor.getString(1));
            field.setDistrictId(cursor.getInt(2));
            field.setAddress(cursor.getString(3));
            field.setLatitude(cursor.getFloat(4));
            field.setLongitude(cursor.getFloat(5));
            field.setPhoneNumber(cursor.getString(6));
            field.setCreated(DateTime.parse(cursor.getString(7)));
            field.setUpdated(DateTime.parse(cursor.getString(8)));
            field.setDeleted(DateTime.parse(cursor.getString(9)));
        }
        cursor.close();
        return field;
    }

    public District getDistrict(int districtId) {
        String sql = "SELECT * FROM districts WHERE district_id=%d";
        sql = String.format(sql, districtId);
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        District district = null;
        if (cursor.moveToFirst()) {
            district = new District();
            district.setDistrictId(districtId);
            district.setName(cursor.getString(1));
            district.setCityId(cursor.getInt(2));
        }
        cursor.close();
        return district;
    }

    public City getCity(int cityId) {
        String sql = "SELECT * FROM cities WHERE city_id=%d";
        sql = String.format(sql, cityId);
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        City city = null;
        if (cursor.moveToFirst()) {
            city = new City();
            city.setCityId(cityId);
            city.setName(cursor.getString(1));
        }
        cursor.close();
        return city;
    }

    public Profile getHostProfile(int hostId) {
        String sql = "SELECT * FROM user_profiles WHERE user_id=%d";
        sql = String.format(sql, hostId);
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        Profile profile = null;
        if (cursor.moveToFirst()) {
            profile = new Profile();
            profile.setUserId(hostId);
            profile.setUsername(cursor.getString(1));
            profile.setEmail(cursor.getString(2));
        }
        cursor.close();
        return profile;
    }

    public void updateMatch(Match match) {
        String sql = "UPDATE matches SET status=%d, price=%d, is_verified=%d, updated='%s' WHERE match_id=%d";
        sql = String.format(sql, match.getNumberOfAvailableSlots(), match.getMoneyPerSlot(), match.isVerified() ? 1 : 0, DateTime.now(), match.getMatchId());
        mSQLiteDatabase.execSQL(sql);
    }

    public List<City> getCities() {
        String sql = "SELECT * FROM cities";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        List<City> cities = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            City city = new City();
            city.setCityId(cursor.getInt(0));
            city.setName(cursor.getString(1));
            cities.add(city);
            cursor.moveToNext();
        }
        cursor.close();
        return cities;
    }

    public List<Match> getMatchesByAccountId(int accountId) {
        String sql = "SELECT * FROM matches WHERE  matches.host_id=%d";
        sql = String.format(sql, accountId);
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        List<Match> matches = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Match match = new Match();
            match.setMatchId(cursor.getInt(0));
            match.setFieldId(cursor.getInt(1));
            match.setHostId(cursor.getInt(2));
            match.setNumberOfAvailableSlots(cursor.getInt(3));
            match.setNumberOfSlots(cursor.getInt(4));
            match.setMoneyPerSlot(cursor.getInt(5));
            match.setStartTime(DateTime.parse(cursor.getString(6)));
            match.setEndTime(DateTime.parse(cursor.getString(7)));
            match.setIsVerified(cursor.getInt(8) != 0);
            match.setCreatedTime(DateTime.parse(cursor.getString(10)));
            matches.add(match);
            cursor.moveToNext();
        }
        cursor.close();
        return matches;
    }

    public List<Field> getFields(int cityId) {
        String sql = "SELECT * FROM fields, districts, cities WHERE " +
                " fields.district_id=districts.district_id AND " +
                " cities.city_id=%d";
        sql = String.format(sql, cityId);
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        List<Field> fields = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Field field = new Field();
            field.setAddress(cursor.getString(3));
            //field.setCreated();
            //field.setDeleted();
            field.setDistrictId(cursor.getInt(2));
            field.setFieldId(cursor.getInt(0));
            field.setLatitude(cursor.getFloat(4));
            field.setLongitude(cursor.getFloat(5));
            field.setName(cursor.getString(1));
            field.setPhoneNumber(cursor.getString(6));
            fields.add(field);
            cursor.moveToNext();
        }
        cursor.close();
        return fields;
    }

    public void addMatch(Match match) {
        String sql = "INSERT INTO matches(field_id, host_id, status, maximum_players, price, start_time, is_verified, created) VALUES(%d, %d, %d, %d, %d, '%s', %d, '%s')";
        String startTime = DateTime.getFriendlyString(match.getStartTime());
        String createTime = DateTime.getFriendlyString(match.getCreatedTime());
        sql = String.format(sql, match.getFieldId(), match.getHostId(),
                match.getNumberOfAvailableSlots(), match.getNumberOfSlots(),
                match.getMoneyPerSlot(), startTime,
                match.isVerified() ? 1 : 0, createTime);
        mSQLiteDatabase.execSQL(sql);
    }
}
