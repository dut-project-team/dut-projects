package com.blogspot.sontx.dut.soccer.ui.dlg;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.blogspot.sontx.dut.soccer.R;

/**
 * Copyright NoEm 2016
 * Created by Noem on 7/5/2016.
 */
public class TimePickerDialog extends NoTitleDialog implements View.OnClickListener {
    private TimePicker mTimePicker;
    private OnTimeSelectedListener mOnTimeSelectedListener = null;

    public void setOnTimeSelectedListener(OnTimeSelectedListener listener) {
        mOnTimeSelectedListener = listener;
    }

    public TimePickerDialog(Context context) {
        super(new Dialog(context));
        mDialog.setContentView(R.layout.dialog_time);
        mTimePicker = (TimePicker) mDialog.findViewById(R.id.tp_timepicker_picker);
        Button okButton = (Button) mDialog.findViewById(R.id.btn_timepicker_ok);
        okButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int minute = mTimePicker.getCurrentMinute();
        int hour = mTimePicker.getCurrentHour();
        if (mOnTimeSelectedListener != null)
            mOnTimeSelectedListener.onTimeSelected(minute, hour);
        mDialog.dismiss();
    }

    public interface OnTimeSelectedListener {
        void onTimeSelected(int minute, int hour);
    }
}
