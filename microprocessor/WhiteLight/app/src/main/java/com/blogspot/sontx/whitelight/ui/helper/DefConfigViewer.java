package com.blogspot.sontx.whitelight.ui.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
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
import com.blogspot.sontx.whitelight.net.RequestPackage;
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
    private int hackSelectionState = 0;
    private Handler inHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RequestPackage.COMMAND_EDIT_DEFCONFIG:
                    Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });

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

        for (SimpleSpinner spinner : spLevels) {
            spinner.setOnItemSelectedListener(this);
        }

        btnApply = (Button) view.findViewById(R.id.light_config_def_btn_apply);
        btnApply.setOnClickListener(this);
    }

    public void setChangeable(boolean enable) {
        for (int i = 0; i < spLevels.length; i++) {
            spLevels[i].setEnabled(enable);
        }
        btnApply.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
    }

    public int getDefConfigId() {
        return defConfigId;
    }

    public void setDefConfig(int defConfigId) {
        List<DefConfig> defConfigs = (List<DefConfig>) SharedObject.getInstance().get(Config.SHARED_DEFCONFIG);
        this.defConfig = defConfigs.get(defConfigId);
        this.defConfigId = defConfigId;

        String[] lightTypes = context.getResources().getStringArray(R.array.light_types);
        tvLightType.setText(String.format("Apply default config for %s", lightTypes[defConfigId]));

        String[] items = context.getResources().getStringArray(R.array.def_configs_list);
        DefConfig.Level[] levels = defConfig.exportState();
        hackSelectionState = 0;
        for (int i = 0; i < 4; i++) {
            SimpleSpinner spinner = spLevels[i];
            // unregister event on old adapter
            LevelAdapter adapter = (LevelAdapter) spinner.getAdapter();
            if (adapter != null)
                adapter.setSpecialItemOnClickListener(null, -1);
            // init new adapter
            adapter = new LevelAdapter(context, R.layout.layout_simple_spinner_item, items);
            DefConfig.Level level = levels[i];
            int position = level.value;
            if (level.isTime) {
                String time = UserConfig.shortToTimeString((short) level.value);
                adapter.setValue(position = 7, time);
            }
            // register event for new adapter
            adapter.setSpecialItemOnClickListener(this, 7);
            // init new adapter for spinner
            spinner.setAdapter(adapter);
            spinner.setSelection(position);
        }
        btnApply.setEnabled(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (hackSelectionState < 4) {
            hackSelectionState++;
            return;
        }
        switch (parent.getId()) {
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
        if (configId < 7) {
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

        // get current time in spinner
        SimpleSpinner spinner = spLevels[level];
        String st_time = spinner.getItemAtPosition(7).toString();
        if (st_time.contains(":")) {
            String st_hour = st_time.substring(0, 2);
            String st_minute = st_time.substring(3, 5);
            int hour = Integer.parseInt(st_hour);
            int minute = Integer.parseInt(st_minute);
            picker.setCurrentHour(hour);
            picker.setCurrentMinute(minute);
        }

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

    @Override
    public void onClick(View v) {
        if (v.equals(btnApply)) {
            btnApply.setEnabled(false);
            Toast.makeText(context, "Apply...", Toast.LENGTH_SHORT).show();
            ServerConnection.getInstance().sendRequest(
                    inHandler,
                    RequestPackage.COMMAND_EDIT_DEFCONFIG,
                    defConfig.getBytes(defConfigId));
        } else {
            // spinner item clicked, only with time off
            View parent = (View) v.getParent();
            switch (parent.getId()) {
                case R.id.light_config_def_sp_level0:
                    pickTimeOff(0);
                    break;
                case R.id.light_config_def_sp_level1:
                    pickTimeOff(1);
                    break;
                case R.id.light_config_def_sp_level2:
                    pickTimeOff(2);
                    break;
                case R.id.light_config_def_sp_level3:
                    pickTimeOff(3);
                    break;
            }
        }
    }

    private class LevelAdapter extends ArrayAdapter<String> {
        private String[] objects;
        private View.OnClickListener mOnClickListener = null;
        private int specialId;

        public LevelAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            this.objects = objects.clone();
        }

        public void setSpecialItemOnClickListener(View.OnClickListener listener, int specialId) {
            mOnClickListener = listener;
            this.specialId = specialId;
        }

        @Override
        public String getItem(int position) {
            return objects[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if (position == specialId) {
                view.setOnClickListener(mOnClickListener);
            }
            return view;
        }

        public void setValue(int pos, String value) {
            objects[pos] = value;
            notifyDataSetChanged();
        }
    }
}
