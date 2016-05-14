package com.blogspot.sontx.dut.soccer.ui.dlg;

import android.content.Context;
import android.content.Intent;

import com.blogspot.sontx.dut.soccer.bean.Field;
import com.blogspot.sontx.dut.soccer.bean.Match;
import com.blogspot.sontx.dut.soccer.bo.DatabaseManager;
import com.blogspot.sontx.dut.soccer.utils.DateTime;

/**
 * Copyright NoEm 2016
 * Created by Noem on 14/5/2016.
 */
public class ShareMyMatchDialog extends MyMatchPicker {
    public ShareMyMatchDialog(Context context) {
        super(context);
    }

    @Override
    protected Intent processMatch(Match match) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        Field field = DatabaseManager.getInstance().getField(match.getFieldId());
        String subject = String.format("Battle of the heroes!");
        String content = String.format("I just created a match at %s at %s, come join with me :D", field.getName(), DateTime.getFriendlyString(match.getStartTime()));
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        return Intent.createChooser(shareIntent, "Share via");
    }
}
