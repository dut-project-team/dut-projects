package com.blogspot.sontx.dut.soccer.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.blogspot.sontx.dut.soccer.App;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 08/04/2016.
 */
public final class FileUtils {
    FileUtils() {}

    public static void copyStream(InputStream src, OutputStream dst) throws IOException {
        byte[] buffer = new byte[1024];
        int chunk;
        while ((chunk = src.read(buffer)) > 0) {
            dst.write(buffer, 0, chunk);
        }
    }

    public static String getAppDirectory() {
        PackageManager packageManager = App.getInstance().getPackageManager();
        String packageName = App.getInstance().getPackageName();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        packageName = packageInfo.applicationInfo.dataDir;
        return packageName;
    }
}
