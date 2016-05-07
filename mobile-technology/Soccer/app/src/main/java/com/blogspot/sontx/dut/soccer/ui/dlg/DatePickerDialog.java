package com.blogspot.sontx.dut.soccer.ui.dlg;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.blogspot.sontx.dut.soccer.R;

/**
 * Copyright NoEm 2016
 * Created by Noem on 6/5/2016.
 */
public class DatePickerDialog extends NoTitleDialog implements View.OnClickListener {
    private DatePicker mDatePicker;
    private OnDateSelectedListener mOnDateSelectedListener = null;

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        mOnDateSelectedListener = listener;
    }

    public DatePickerDialog(Context context) {
        super(new Dialog(context));
        mDialog.setContentView(R.layout.dialog_date);
        mDatePicker = (DatePicker) mDialog.findViewById(R.id.dp_datepicker_picker);
        Button okButton = (Button) mDialog.findViewById(R.id.btn_datepicker_ok);
        okButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int day = mDatePicker.getDayOfMonth();
        int month = mDatePicker.getMonth();
        int year = mDatePicker.getYear();
        if (mOnDateSelectedListener != null)
            mOnDateSelectedListener.onDateSelected(day, month, year);
        mDialog.dismiss();
    }

    public interface OnDateSelectedListener {
        void onDateSelected(int day, int month, int year);
    }
}
