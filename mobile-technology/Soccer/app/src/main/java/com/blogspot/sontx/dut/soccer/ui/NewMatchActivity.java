package com.blogspot.sontx.dut.soccer.ui;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blogspot.sontx.dut.soccer.App;
import com.blogspot.sontx.dut.soccer.R;
import com.blogspot.sontx.dut.soccer.bean.Field;
import com.blogspot.sontx.dut.soccer.bean.Match;
import com.blogspot.sontx.dut.soccer.bean.Money;
import com.blogspot.sontx.dut.soccer.bo.DatabaseManager;
import com.blogspot.sontx.dut.soccer.bo.SampleData;
import com.blogspot.sontx.dut.soccer.utils.DateTime;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Date;

public class NewMatchActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private static final int REQUEST_SELECT_LOCATION = 1;
    private Spinner mSlotsView;
    private Spinner mMoneyView;
    private Spinner mWhereView;
    private Button mStartTimeView;
    private Button mStartDateView;
    private ImageButton mWhereButtonView;
    //private TextView mWhereNameView;
    private Button mAddView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);

        mSlotsView = (Spinner) findViewById(R.id.sp_new_match_slots);
        mMoneyView = (Spinner) findViewById(R.id.sp_new_match_money);
        mWhereView = (Spinner) findViewById(R.id.sp_new_match_where);
        mStartTimeView = (Button) findViewById(R.id.btn_new_match_startime);
        mStartDateView = (Button) findViewById(R.id.btn_new_match_startdate);
        mWhereButtonView = (ImageButton) findViewById(R.id.ib_new_match_where);
       // mWhereNameView = (TextView) findViewById(R.id.tv_new_match_where);
        mAddView = (Button) findViewById(R.id.btn_new_match_add);

        mStartTimeView.setOnClickListener(this);
        mStartDateView.setOnClickListener(this);
        mWhereButtonView.setOnClickListener(this);
        mAddView.setOnClickListener(this);
        //mWhereView.setOnClickListener(this);

        loadSpinnersData();
    }

    private void loadSpinnersData() {
        ArrayAdapter<Integer> slotsAdapter = new ArrayAdapter<>(mSlotsView.getContext(), android.R.layout.simple_spinner_dropdown_item, SampleData.getSlots());
        ArrayAdapter<Money> moneyAdapter = new ArrayAdapter<>(mMoneyView.getContext(), android.R.layout.simple_spinner_dropdown_item, SampleData.getMoney());
        ArrayAdapter<Field> fieldAdapter = new ArrayAdapter<>(mWhereView.getContext(), android.R.layout.simple_spinner_dropdown_item, DatabaseManager.getInstance().getFields(MainActivity.DEFAULT_CITY_ID));
        mSlotsView.setAdapter(slotsAdapter);
        mMoneyView.setAdapter(moneyAdapter);
        mWhereView.setAdapter(fieldAdapter);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_SELECT_LOCATION) {
//            if (resultCode == RESULT_OK) {
//                String address = data.getStringExtra("address");
//                mWhereNameView.setText(address);
//                mWhereNameView.setOnClickListener(this);
//                mWhereView.setVisibility(View.GONE);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public void onClick(View v) {
        DateTime now = DateTime.now();
        if (mStartTimeView.equals(v)) {
            android.app.TimePickerDialog dialog = new TimePickerDialog(v.getContext(), this, now.getMinute(), now.getHour(), true);
            dialog.show();
        } else if (mStartDateView.equals(v)) {
            DatePickerDialog dialog = new DatePickerDialog(v.getContext(), this, now.getYear(), now.getMonth(), now.getDay());
            dialog.show();
//        } else if (mWhereView.equals(v) || mWhereNameView.equals(v)) {
//            Intent intent = new Intent(this, MapActivity.class);
//            startActivityForResult(intent, REQUEST_SELECT_LOCATION);
        } else if (mAddView.equals(v)) {
            addMyMatch();
        } else if (mWhereButtonView.equals(v)) {
            showMap();
        }
    }

    private void showMap() {
        Intent intent = new Intent(this, MapActivity.class);
        Bundle bundle = new Bundle();
        Field selectedField = (Field) mWhereView.getSelectedItem();
        bundle.putFloat("latitude", selectedField.getLatitude());
        bundle.putFloat("longitude", selectedField.getLongitude());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private Date getStartTime() {
        String startTime = mStartTimeView.getText().toString();
        String startDate = mStartDateView.getText().toString();
        return DateTime.parse(startTime, startDate);
    }

    private void addMyMatch() {
        Match match = new Match();
        match.setCreatedTime(DateTime.now().toDate());
        match.setIsVerified(true);
        match.setMoneyPerSlot(Integer.parseInt(mMoneyView.getSelectedItem().toString().split(" ")[0]));
        match.setNumberOfSlots(Integer.parseInt(mSlotsView.getSelectedItem().toString()));
        match.setNumberOfAvailableSlots(match.getNumberOfSlots());
        match.setHostId(App.getInstance().getCurrentAccountId());
        match.setStartTime(getStartTime());
        match.setFieldId(((Field) mWhereView.getSelectedItem()).getFieldId());
        DatabaseManager.getInstance().addMatch(match);
        Toast.makeText(NewMatchActivity.this, "Added your own match!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mStartTimeView.setText(String.format("%02d:%02d", hourOfDay, minute));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mStartDateView.setText(String.format("%02d/%02d/%d", dayOfMonth, monthOfYear, year));
    }
}
