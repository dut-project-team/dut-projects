package com.blogspot.sontx.dut.soccer.ui.dlg;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.sontx.dut.soccer.R;
import com.blogspot.sontx.dut.soccer.bean.City;
import com.blogspot.sontx.dut.soccer.bean.District;
import com.blogspot.sontx.dut.soccer.bean.Field;
import com.blogspot.sontx.dut.soccer.bean.Match;
import com.blogspot.sontx.dut.soccer.bean.Profile;
import com.blogspot.sontx.dut.soccer.bo.DatabaseManager;
import com.blogspot.sontx.dut.soccer.utils.DateTime;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 29/04/2016.
 */
public class MatchDialog extends BaseDialog implements View.OnClickListener {
    private TextView mStartTimeView;
    private TextView mAvailableSlotView;
    private TextView mMoneyView;
    private TextView mWhereView;
    private TextView mFieldView;
    private TextView mVerifiedView;
    private Button mJoinView;
    private Button mCloseView;
    private Match mMatch;

    private void getViewFromIds() {
        mStartTimeView = (TextView) mDialog.findViewById(R.id.tv_match_start);
        mAvailableSlotView = (TextView) mDialog.findViewById(R.id.tv_match_available_slot);
        mMoneyView = (TextView) mDialog.findViewById(R.id.tv_match_money);
        mWhereView = (TextView) mDialog.findViewById(R.id.tv_match_where);
        mFieldView = (TextView) mDialog.findViewById(R.id.tv_match_field);
        mVerifiedView = (TextView) mDialog.findViewById(R.id.tv_match_verified);
        mJoinView = (Button) mDialog.findViewById(R.id.btn_match_join);
        mCloseView = (Button) mDialog.findViewById(R.id.btn_match_close);
    }

    public MatchDialog(Context context, Match match) {
        super(new Dialog(context));
        mDialog.setContentView(R.layout.dialog_match);
        getViewFromIds();
        loadMatchDetail(match);
        mMatch = match;
    }

    private void loadMatchDetail(Match match) {
        Field field = DatabaseManager.getInstance().getField(match.getFieldId());
        District district = DatabaseManager.getInstance().getDistrict(field.getDistrictId());
        City city = DatabaseManager.getInstance().getCity(district.getCityId());
        Profile profile = DatabaseManager.getInstance().getHostProfile(match.getHostId());

        mDialog.setTitle(String.format("Match of %s", profile.getUsername()));

        mStartTimeView.setText(DateTime.getFriendlyString(match.getStartTime()));
        mAvailableSlotView.setText(String.format("%d/%d", match.getNumberOfSlots() - match.getNumberOfAvailableSlots(), match.getNumberOfSlots()));
        mMoneyView.setText(String.format("%d VND", match.getMoneyPerSlot()));
        mWhereView.setText(String.format("%s, %s", district.getName(), city.getName()));
        mFieldView.setText(String.format("%s, %s", field.getName(), field.getAddress()));
        mVerifiedView.setText(match.isVerified() ? "Verified" : "Not verified");
        mVerifiedView.setTextColor(match.isVerified() ? Color.GREEN : Color.RED);
        if (match.getNumberOfAvailableSlots() > 0)
            mJoinView.setOnClickListener(this);
        else
            mJoinView.setEnabled(false);
        mCloseView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mJoinView)) {
            mMatch.setNumberOfAvailableSlots(mMatch.getNumberOfAvailableSlots() - 1);
            DatabaseManager.getInstance().updateMatch(mMatch);
            Toast.makeText(mDialog.getContext(), "Joined!", Toast.LENGTH_SHORT).show();
            fireOnDialogDataChanged();
        } else if (v.equals(mCloseView)) {
        }
        mDialog.dismiss();
    }
}
