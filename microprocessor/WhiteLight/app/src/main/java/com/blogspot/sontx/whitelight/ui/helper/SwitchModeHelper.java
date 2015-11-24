package com.blogspot.sontx.whitelight.ui.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.blogspot.sontx.whitelight.R;
import com.blogspot.sontx.whitelight.bean.Light;
import com.blogspot.sontx.whitelight.net.ServerConnection;
import com.blogspot.sontx.whitelight.ui.LightDetailActivity;

/**
 * Copyright by NE 2015.
 * Created by noem on 24/11/2015.
 */
public final class SwitchModeHelper implements DialogInterface.OnClickListener{
    private Activity activity;
    private Light light;
    private int lightId;
    private ArrayAdapter<String> adapter;

    public SwitchModeHelper(Activity activity, Light light, int lightId) {
        this.activity = activity;
        this.light = light;
        this.lightId = lightId;
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Switch current mode to");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        //builder.setMessage("Switch current mode to");

        adapter = new ArrayAdapter<String>(activity.getApplicationContext(),
                android.R.layout.select_dialog_singlechoice);
        switch (light.getConfigType()) {
            case Light.CONFIG_TYPE_USER:
                adapter.add("DEF MODE");
                adapter.add("OFF MODE");
                break;
            case Light.CONFIG_TYPE_DEF:
                adapter.add("USR MODE");
                adapter.add("OFF MODE");
                break;
            case Light.CONFIG_TYPE_OFF:
                adapter.add("USR MODE");
                adapter.add("DEF MODE");
                break;
        }

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setAdapter(adapter, this);
        builder.show();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(activity, "Done!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, LightDetailActivity.class);
                    activity.recreate();
                    break;
                case 0:
                    Toast.makeText(activity, "Fail!", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String item = adapter.getItem(which);
        switch (item) {
            case "USR MODE":
                light.setNConfig((byte) 0);// reset to user mode
                break;
            case "DEF MODE":
                light.setNConfig((byte) Light.CONFIG_TYPE_DEF);
                break;
            case "OFF MODE":
                light.setNConfig((byte) Light.CONFIG_TYPE_OFF);
                break;
        }
        Toast.makeText(activity, "Apply...", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ok = ServerConnection.getInstance().updateUserConfig(light, lightId);
                handler.sendEmptyMessage(ok ? 1 : 0);
            }
        }).start();
        dialog.dismiss();
    }
}
