package com.blogspot.sontx.whitelight.ui.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blogspot.sontx.whitelight.R;
import com.blogspot.sontx.whitelight.bean.DefConfig;
import com.blogspot.sontx.whitelight.bean.UserConfig;
import com.blogspot.sontx.whitelight.lib.Config;
import com.blogspot.sontx.whitelight.lib.SharedObject;
import com.blogspot.sontx.whitelight.net.ServerConnection;
import com.blogspot.sontx.whitelight.ui.view.SimpleSpinner;

import java.util.List;

/**
 * Copyright by NE 2015.
 * Created by noem on 25/11/2015.
 */
public class DefConfigViewer implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private SimpleSpinner[] spLevels;
    private TextView tvLightType;
    private DefConfig defConfig;
    private int defConfigId;
    private Button btnApply;
    private Context context;

    public DefConfigViewer(View root) {
        this.context = root.getContext();
        loadLayout(root);
    }

    private void loadLayout(View view) {
        tvLightType = (TextView) view.findViewById(R.id.light_config_def_tv_type);

        spLevels = new SimpleSpinner[4];

        spLevels[0] = (SimpleSpinner) view.findViewById(R.id.light_config_def_sp_level0);
        spLevels[1] = (SimpleSpinner) view.findViewById(R.id.light_config_def_sp_level1);
        spLevels[2] = (SimpleSpinner) view.findViewById(R.id.light_config_def_sp_level2);
        spLevels[3] = (SimpleSpinner) view.findViewById(R.id.light_config_def_sp_level3);

        btnApply = (Button) view.findViewById(R.id.light_config_def_btn_apply);
        btnApply.setOnClickListener(this);
    }

    public void setChangeable(boolean enable) {
        for (int i = 0; i < spLevels.length; i++) {
            spLevels[i].setEnabled(enable);
        }
        btnApply.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
    }

    public void setDefConfig(int defConfigId) {
        List<DefConfig> defConfigs = (List<DefConfig>) SharedObject.getInstance().get(Config.SHARED_DEFCONFIG);
        this.defConfig = defConfigs.get(defConfigId);
        this.defConfigId = defConfigId;

        String[] lightTypes = context.getResources().getStringArray(R.array.light_types);
        tvLightType.setText(String.format("Apply default config for %s", lightTypes[defConfigId]));

        String[] items = context.getResources().getStringArray(R.array.def_configs_list);
        DefConfig.Level[] levels = defConfig.exportState();
        for (int i = 0; i < 4; i++) {
            SimpleSpinner spinner = spLevels[i];
            LevelAdapter adapter = new LevelAdapter(context,
                    android.R.layout.simple_spinner_dropdown_item, items);
            DefConfig.Level level = levels[i];
            int position = level.value;
            if (level.isTime) {
                String time = UserConfig.shortToTimeString((short) level.value);
                adapter.setValue(position = 7, time);
            }
            spinner.setAdapter(adapter);
            spinner.setSelection(position);
            spinner.setOnItemSelectedListener(this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.light_config_def_sp_level0:
                cacheChanged(0, position);
                break;
            case R.id.light_config_def_sp_level1:
                cacheChanged(1, position);
                break;
            case R.id.light_config_def_sp_level2:
                cacheChanged(2, position);
                break;
            case R.id.light_config_def_sp_level3:
                cacheChanged(3, position);
                break;
        }
    }

    private void cacheChanged(int level, int configId) {
        // time off
        if (configId == 7) {
            pickTimeOff(level);
        } else {
            // save to def config object
            DefConfig.Level[] levels = defConfig.exportState();
            DefConfig.Level _level = new DefConfig.Level(configId, false);
            levels[level] = _level;
            defConfig.importState(levels);
            btnApply.setEnabled(true);
        }
    }

    private void pickTimeOff(final int level) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_light_config_def_timepicker);
        final TimePicker picker = (TimePicker) dialog.findViewById(R.id.light_config_def_timepicker_tp_picker);
        Button btnOK = (Button) dialog.findViewById(R.id.light_config_def_timepicker_btn_ok);
        Button btnCancel = (Button) dialog.findViewById(R.id.light_config_def_timepicker_btn_cancel);

        dialog.setTitle(R.string.app_name);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get time from picker
                int hour = picker.getCurrentHour();
                int minute = picker.getCurrentMinute();
                short time = (short) (hour * 60 + minute);
                /* note: if user select 00:00 then we will set time to 1440 */
                if (time == 0)
                    time = 1440;
                // save to def config object
                DefConfig.Level[] levels = defConfig.exportState();
                DefConfig.Level _level = new DefConfig.Level(time, true);
                levels[level] = _level;
                defConfig.importState(levels);
                // update UI
                SimpleSpinner spinner = spLevels[level];
                LevelAdapter adapter = (LevelAdapter) spinner.getAdapter();
                adapter.setValue(7, UserConfig.shortToTimeString(time));
                // enable apply button
                btnApply.setEnabled(true);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(context, "Fail!", Toast.LENGTH_SHORT).show();
                    btnApply.setEnabled(true);
                    break;
            }
            return true;
        }
    });

    @Override
    public void onClick(View v) {
        btnApply.setEnabled(false);
        Toast.makeText(context, "Apply...", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ok = ServerConnection.getInstance().updateDefConfig(defConfig, defConfigId);
                handler.sendEmptyMessage(ok ? 1 : 0);
            }
        }).start();
    }

    private class LevelAdapter extends ArrayAdapter<String> {
        private String[] objects;

        public LevelAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            this.objects = objects;
        }

        @Override
        public String getItem(int position) {
            return objects[position];
        }

        public void setValue(int pos, String value) {
            objects[pos] = value;
            notifyDataSetChanged();
        }
    }
}
