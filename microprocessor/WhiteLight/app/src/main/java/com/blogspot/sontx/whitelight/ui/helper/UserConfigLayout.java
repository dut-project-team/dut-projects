package com.blogspot.sontx.whitelight.ui.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blogspot.sontx.whitelight.R;
import com.blogspot.sontx.whitelight.bean.Light;
import com.blogspot.sontx.whitelight.bean.UserConfig;
import com.blogspot.sontx.whitelight.net.RequestPackage;
import com.blogspot.sontx.whitelight.net.ServerConnection;

/**
 * Copyright by NE 2015.
 * Created by noem on 23/11/2015.
 * Display time list, each it changed we will update to arduino
 */
public class UserConfigLayout extends ConfigLayout implements AdapterView.OnItemClickListener {
    private Light light;
    private TimeAdapter adapter;
    private ListView listView;
    private int lightId;
    private Handler inHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RequestPackage.COMMAND_EDIT_USERCONFIG:
                    Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });

    public UserConfigLayout(Light light, int lightId) {
        super(R.layout.layout_light_config_user);
        this.light = light;
        this.lightId = lightId;
    }

    @Override
    protected void onApplyLayout(View view) {
        adapter = new TimeAdapter(context.getApplicationContext(), light.getLstTime(),
                light.getLstConfig(), light.getNConfig());
        listView = (ListView) view.findViewById(R.id.light_config_user_lv_configs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View header = inflater.inflate(R.layout.layout_light_config_user_header, null);
        listView.addHeaderView(header);

        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        // add new config
        if (position == 0) {
            if (light.getNConfig() >= UserConfig.MAX_NUM_CONFIG)
                Toast.makeText(context, String.format("Support only %d config", UserConfig.MAX_NUM_CONFIG), Toast.LENGTH_SHORT).show();
            else
                showDialog("Add new user config", false, (short) -1, new WhenOK() {
                    @Override
                    public void onWhenOK(boolean turnON, short time) {
                        byte index = light.getNConfig();
                        light.getLstConfig()[index] = (byte) (turnON ? 1 : 0);
                        light.getLstTime()[index] = time;
                        light.setNConfig((byte) (index + 1));

                        adapter.add();

                        // send request update user config to arduino
                        ServerConnection.getInstance().sendRequest(
                                inHandler,
                                RequestPackage.COMMAND_EDIT_USERCONFIG,
                                light.getBytes(lightId));

                        listView.invalidateViews();
                    }
                });
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.app_name);
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setMessage("Select option what you want to");
            builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editConfig(position);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    removeConfig(position);
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    private void editConfig(final int position) {
        short time = light.getLstTime()[position - 1];
        byte config = light.getLstConfig()[position - 1];
        showDialog("Edit current user config", config != UserConfig.CONFIG_LIGHT_OFF, time, new WhenOK() {
            @Override
            public void onWhenOK(boolean turnON, short time) {
                byte index = (byte) (position - 1);
                light.getLstConfig()[index] = (byte) (turnON ? 1 : 0);
                light.getLstTime()[index] = time;

                // send request update user config to arduino
                ServerConnection.getInstance().sendRequest(
                        inHandler,
                        RequestPackage.COMMAND_EDIT_USERCONFIG,
                        light.getBytes(lightId));

                listView.invalidateViews();
            }
        });
    }

    private void removeConfig(final int position) {
        int count = light.getNConfig();
        int index = position - 1;
        byte[] data1 = light.getLstConfig();
        short[] data2 = light.getLstTime();
        for (int i = index; i < count - 1; i++) {
            data1[i] = data1[i + 1];
            data2[i] = data2[i + 1];
        }
        light.setNConfig((byte) (count - 1));
        adapter.remove();

        // send request update user config to arduino
        ServerConnection.getInstance().sendRequest(
                inHandler,
                RequestPackage.COMMAND_EDIT_USERCONFIG,
                light.getBytes(lightId));

        listView.invalidateViews();
    }

    private void showDialog(String message, boolean turnON, short time, final WhenOK whenOK) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_light_config_user_add);
        dialog.setTitle(R.string.app_name);
        final TextView tvMsg = (TextView) dialog.findViewById(R.id.light_config_user_add_tv_msg);
        final Button btnOK = (Button) dialog.findViewById(R.id.light_config_user_add_btn_ok);
        final Button btnCancel = (Button) dialog.findViewById(R.id.light_config_user_add_btn_cancel);
        final Switch swState = (Switch) dialog.findViewById(R.id.light_config_user_add_sw_state);
        final TimePicker picker = (TimePicker) dialog.findViewById(R.id.light_config_user_add_tp_picker);

        tvMsg.setText(message);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean turnOn = swState.isChecked();
                short time = (short) (picker.getCurrentHour() * 60 + picker.getCurrentMinute());
                if (whenOK != null)
                    whenOK.onWhenOK(turnOn, time);
                swState.setOnCheckedChangeListener(null);
                btnCancel.setOnClickListener(null);
                btnOK.setOnClickListener(null);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swState.setOnCheckedChangeListener(null);
                btnCancel.setOnClickListener(null);
                btnOK.setOnClickListener(null);
                dialog.dismiss();
            }
        });
        swState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                swState.setText(isChecked ? "Turn ON" : "Turn OFF");
            }
        });
        swState.setChecked(turnON);
        if (time > -1) {
            int hour = time / 60;
            int minute = time % 60;
            picker.setCurrentHour(hour);
            picker.setCurrentMinute(minute);
        }
        dialog.show();
    }

    private interface WhenOK {
        void onWhenOK(boolean turnON, short time);
    }

    private static class ViewHolder {
        ImageView ivState;
        TextView tvTime;
    }

    private class TimeAdapter extends BaseAdapter {
        private short[] lstTimes;
        private byte[] lstConfigs;
        private int nConfig;
        private LayoutInflater inflater;

        public TimeAdapter(Context context, short[] lstTimes, byte[] lstConfigs, int nConfig) {
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.lstTimes = lstTimes;
            this.lstConfigs = lstConfigs;
            this.nConfig = nConfig;
        }

        public void add() {
            nConfig++;
            notifyDataSetChanged();
        }

        public void remove() {
            nConfig--;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return nConfig;
        }

        @Override
        public Object getItem(int position) {
            return lstTimes[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_light_config_user_item, null);
                holder = new ViewHolder();
                holder.ivState = (ImageView) convertView.findViewById(R.id.light_config_user_item_iv_state);
                holder.tvTime = (TextView) convertView.findViewById(R.id.light_config_user_item_tv_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            short time = lstTimes[position];
            byte config = lstConfigs[position];
            int imageState = config == UserConfig.CONFIG_LIGHT_OFF ? R.drawable.ic_light_off : R.drawable.ic_light_on;
            holder.ivState.setImageResource(imageState);
            holder.tvTime.setText(UserConfig.shortToTimeString(time));
            return convertView;
        }
    }
}
