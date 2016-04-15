package com.blogspot.sontx.dut.soccer;

import android.app.Application;

import com.blogspot.sontx.dut.soccer.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;import java.lang.Override;import java.lang.String;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 08/04/2016.
 */
public class App extends Application {
    public static final String DB_NAME = "bongda.sqlite";
    private static Application instance = null;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        prepareDatabaseIfNecessary();
    }

    private void prepareDatabaseIfNecessary() {
        File dbFile = new File(FileUtils.getAppDirectory(), DB_NAME);
        if (!dbFile.exists()) {
            try {
                InputStream dbInputStream = getAssets().open(DB_NAME);
                FileOutputStream dbOutputStream = new FileOutputStream(dbFile.getPath());
                FileUtils.copyStream(dbInputStream, dbOutputStream);
                dbInputStream.close();
                dbOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
