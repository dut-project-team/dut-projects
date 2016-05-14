package com.blogspot.sontx.dut.soccer.ui.dlg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ArrayAdapter;

import com.blogspot.sontx.dut.soccer.App;
import com.blogspot.sontx.dut.soccer.bean.Match;
import com.blogspot.sontx.dut.soccer.bo.DatabaseManager;

import java.util.List;

/**
 * Copyright NoEm 2016
 * Created by Noem on 14/5/2016.
 */
public abstract class MyMatchPicker implements DialogInterface.OnClickListener {
    private AlertDialog.Builder builder;
    private ArrayAdapter<Match> matchAdapter;
    private OnSelectedMatchListener mOnSelectedMatchListener = null;

    public void setOnSendSMSListener(OnSelectedMatchListener listener) {
        mOnSelectedMatchListener = listener;
    }

    public MyMatchPicker(Context context) {
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Select your match to send");
        builder.setNegativeButton("Cancel", this);
        loadMatches();
    }

    public void show() {
        builder.show();
    }

    private void loadMatches() {
        matchAdapter = new ArrayAdapter<>(builder.getContext(), android.R.layout.select_dialog_singlechoice);
        List<Match> matches = DatabaseManager.getInstance().getMatchesByAccountId(App.getInstance().getCurrentAccountId());
        for (Match match : matches) {
            matchAdapter.add(match);
        }
        builder.setAdapter(matchAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Match match = matchAdapter.getItem(which);
                dialog.dismiss();
                onSelectedMatch(match);
            }
        });
    }

    private void onSelectedMatch(Match match) {
        if (mOnSelectedMatchListener != null)
            mOnSelectedMatchListener.onSelectedMatch(processMatch(match));
    }

    protected abstract Intent processMatch(Match match);

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }

    public interface OnSelectedMatchListener {
        void onSelectedMatch(Intent intent);
    }
}
