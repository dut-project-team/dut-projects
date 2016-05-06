package com.blogspot.sontx.dut.soccer.ui;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.sontx.dut.soccer.R;
import com.blogspot.sontx.dut.soccer.bean.Account;
import com.blogspot.sontx.dut.soccer.bean.Match;
import com.blogspot.sontx.dut.soccer.bo.DatabaseManager;
import com.blogspot.sontx.dut.soccer.ui.dlg.BaseDialog;
import com.blogspot.sontx.dut.soccer.ui.dlg.MatchDialog;
import com.blogspot.sontx.dut.soccer.ui.frag.MatchesFragment;
import com.blogspot.sontx.dut.soccer.ui.frag.OnFragmentDataChangedListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MatchesFragment.OnListFragmentInteractionListener {
    private static final int LOGIN_REQUEST_CODE = 1;
    private static final int DEFAULT_CITY_ID = 43;
    private List<OnFragmentDataChangedListener> mOnFragmentDataChangedListeners = new ArrayList<>();
    private int mAccountId = -1;
    private boolean mDoubleBackToExitPressedOnce = false;
    private boolean mAttachedMatchesFragment = false;

    private void onLoginSuccess() {
        TextView tvEmail = (TextView) findViewById(R.id.tv_user_email);
        if (tvEmail != null && tvEmail.getText().length() == 0) {
            Account account = DatabaseManager.getInstance().getAccountById(mAccountId);
            tvEmail.setText(account.getEmail());
        }
        if (!mAttachedMatchesFragment)
            attachMatchesFragment();
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                onLoginSuccess();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void registerNavigationItemSelectedListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void requestLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    private void onLoginSuccess(int accountId) {
        mAccountId = accountId;
        AlertDialog.Builder builder;
        onLoginSuccess();
    }

    private boolean checkAccountIdIsPassed(Bundle bundle) {
        int accountId = -1;
        if (bundle != null)
            accountId = bundle.getInt("id", -1);
        else {
            Intent intent = getIntent();
            if (intent != null)
                accountId = intent.getIntExtra("id", -1);
        }
        if (accountId != -1) {
            onLoginSuccess(accountId);
            return true;
        }
        return false;
    }

    private void attachMatchesFragment(int cityId) {
        mAttachedMatchesFragment = true;
        FragmentManager fragmentManager = getFragmentManager();
        MatchesFragment fragment = MatchesFragment.newInstance(cityId);
        fragment.setOnListFragmentInteractionListener(this);
        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
        addOnFragmentDataChangedListener(fragment);
    }

    private void attachMatchesFragment() {
        attachMatchesFragment(DEFAULT_CITY_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeToolbar();
        registerNavigationItemSelectedListener();
        if (!checkAccountIdIsPassed(savedInstanceState))
            requestLogin();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAccountId != -1)
            outState.putInt("id", mAccountId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int accountId = data.getIntExtra("id", -1);
                if (accountId != -1) {
                    onLoginSuccess(accountId);
                } else {
                    Toast.makeText(MainActivity.this, "Login failed, try again!", Toast.LENGTH_SHORT).show();
                    requestLogin();
                }
            } else {
                Toast.makeText(MainActivity.this, "You must login first!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!mDoubleBackToExitPressedOnce) {
            mDoubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDoubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_change_password) {
            changePassword();
        } else if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changePassword() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("id", mAccountId);
        startActivity(intent);
    }

    private void logout() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onListFragmentInteraction(final Match item) {
        MatchDialog dialog = new MatchDialog(this, item);
        dialog.setOnDialogDataChangedListener(new BaseDialog.OnDialogDataChangedListener() {
            @Override
            public void onDialogDataChanged(BaseDialog dialog) {
                for (OnFragmentDataChangedListener listener : mOnFragmentDataChangedListeners) {
                    listener.onFragmentDataChanged(item);
                }
            }
        });
        dialog.show();
    }

    public void addOnFragmentDataChangedListener(OnFragmentDataChangedListener listener) {
        mOnFragmentDataChangedListeners.add(listener);
    }

    public void removeOnFragmentDataChangedListener(OnFragmentDataChangedListener listener) {
        mOnFragmentDataChangedListeners.remove(listener);
    }
}
