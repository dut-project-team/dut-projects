package com.blogspot.sontx.dut.game.lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 19/1/2016.
 */
public final class SystemAlert {
    private static Context mContext;

    public static void init(Activity activity) {
        mContext = activity;
    }

    public static AlertDialog.Builder getBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        return builder;
    }
}
