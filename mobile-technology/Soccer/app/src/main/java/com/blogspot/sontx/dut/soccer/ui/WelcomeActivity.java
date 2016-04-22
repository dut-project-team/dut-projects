package com.blogspot.sontx.dut.soccer.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blogspot.sontx.dut.soccer.R;

public class WelcomeActivity extends AppCompatActivity {
    private static final int LOGIN_REQUEST_CODE = 1;
    private static final int REGIStER_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        View loginView = findViewById(R.id.ib_login);
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginActivity();
            }
        });
        View registerView = findViewById(R.id.ib_register);
        registerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterActivity();
            }
        });
    }

    private void showRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REGIStER_REQUEST_CODE);
    }

    private void showLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
                tryForwardToMainActivity(data);
        } else if (requestCode == REGIStER_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
                tryForwardToMainActivity(data);
        }
    }

    private void tryForwardToMainActivity(Intent data) {
        int accountId = data.getIntExtra("id", -1);
        if (accountId != -1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("id", accountId);
            startActivity(intent);
            finish();
        }
    }
}
