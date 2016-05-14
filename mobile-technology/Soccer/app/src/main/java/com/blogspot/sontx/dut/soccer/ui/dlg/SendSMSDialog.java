package com.blogspot.sontx.dut.soccer.ui.dlg;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.blogspot.sontx.dut.soccer.bean.Field;
import com.blogspot.sontx.dut.soccer.bean.Match;
import com.blogspot.sontx.dut.soccer.bo.DatabaseManager;
import com.blogspot.sontx.dut.soccer.utils.DateTime;

/**
 * Copyright NoEm 2016
 * Created by Noem on 14/5/2016.
 */
public class SendSMSDialog extends MyMatchPicker {
    public SendSMSDialog(Context context) {
        super(context);
    }

    @Override
    protected Intent processMatch(Match match) {
        Uri uri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        Field field = DatabaseManager.getInstance().getField(match.getFieldId());
        String content = String.format("I just created a match at %s at %s, come join with me :D", field.getName(), DateTime.getFriendlyString(match.getStartTime()));
        intent.putExtra("sms_body", content);
        return intent;
    }
}
