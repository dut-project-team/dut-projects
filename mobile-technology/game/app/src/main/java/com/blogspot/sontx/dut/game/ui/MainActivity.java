package com.blogspot.sontx.dut.game.ui;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Noem on 16/1/2016.
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(WindowSurface.getInstance());
    }
}
