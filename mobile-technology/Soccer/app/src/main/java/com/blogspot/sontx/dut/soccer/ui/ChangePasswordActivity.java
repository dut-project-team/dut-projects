package com.blogspot.sontx.dut.soccer.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.sontx.dut.soccer.R;
import com.blogspot.sontx.dut.soccer.bo.DatabaseManager;

public class ChangePasswordActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(this);

        mConfirmPasswordView = (EditText) findViewById(R.id.confirm_password);
        mConfirmPasswordView.setOnEditorActionListener(this);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptChangePassword();
            }
        });
    }

    private void attemptChangePassword() {
        mPasswordView.setError(null);
        mConfirmPasswordView.setError(null);

        String password = mPasswordView.getText().toString();
        String confirmPassword = mConfirmPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!confirmPassword.equals(password)) {
            mConfirmPasswordView.setError(getString(R.string.error_not_match_password));
            focusView = mConfirmPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            changePassword(password);
        }
    }

    private void changePassword(String password) {
        Intent intent = getIntent();
        if (intent != null) {
            int accountId = intent.getIntExtra("id", -1);
            if (accountId != -1) {
                DatabaseManager.getInstance().changePassword(accountId, password);
                Toast.makeText(ChangePasswordActivity.this, "Changed your password!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        Toast.makeText(ChangePasswordActivity.this, "Your password not changed!", Toast.LENGTH_SHORT).show();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @Override
    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
            attemptChangePassword();
            return true;
        }
        return false;
    }
}
