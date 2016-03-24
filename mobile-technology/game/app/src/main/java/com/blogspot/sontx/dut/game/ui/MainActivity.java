package com.blogspot.sontx.dut.game.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blogspot.sontx.dut.game.lib.SystemAlert;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 16/1/2016.
 */
public class MainActivity extends AppCompatActivity {
    private static Activity instance = null;

    public static Activity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setContentView(WindowSurface.getInstance());
            SystemAlert.init(this);
        }
        instance = this;
    }
}
