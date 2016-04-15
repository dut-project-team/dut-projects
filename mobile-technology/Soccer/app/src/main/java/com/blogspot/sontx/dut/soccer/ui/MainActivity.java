package com.blogspot.sontx.dut.soccer.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.blogspot.sontx.dut.soccer.App;
import com.blogspot.sontx.dut.soccer.R;
import com.blogspot.sontx.dut.soccer.bo.DatabaseManager;
import com.blogspot.sontx.dut.soccer.utils.FileUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int LOGIN_REQUEST_CODE = 1;
    private int mAccountId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseManager.getInstance().open(new File(FileUtils.getAppDirectory(), App.DB_NAME).getPath());
        gotoLoginActivity();
    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mAccountId = data.getIntExtra("id", -1);
                Toast.makeText(MainActivity.this, "Login success! your id is " + mAccountId, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "You must login first!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
