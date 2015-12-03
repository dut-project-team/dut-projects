package com.blogspot.sontx.whitelight.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.sontx.libex.DateTime;
import com.blogspot.sontx.whitelight.R;
import com.blogspot.sontx.whitelight.bean.DefConfig;
import com.blogspot.sontx.whitelight.bean.Light;
import com.blogspot.sontx.whitelight.bean.UserConfig;
import com.blogspot.sontx.whitelight.lib.Config;
import com.blogspot.sontx.whitelight.lib.SharedObject;
import com.blogspot.sontx.whitelight.net.RequestPackage;
import com.blogspot.sontx.whitelight.net.ResponseDefConfigPackage;
import com.blogspot.sontx.whitelight.net.ResponseLightStatePackage;
import com.blogspot.sontx.whitelight.net.ResponseUserConfigPackage;
import com.blogspot.sontx.whitelight.net.ServerConnection;
import com.blogspot.sontx.whitelight.sample.LightSample;
import com.blogspot.sontx.whitelight.ui.view.LimitEditText;
import com.blogspot.sontx.whitelight.ui.view.SimpleSpinner;

import java.util.ArrayList;
import java.util.List;

public class LightActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener, View.OnClickListener {
    private List<Light> lights;
    private List<DefConfig> defConfigs;
    private LightAdapter lightAdapter;
    private ListView listView;
    private SharedPreferences sharedPreferences;
    private Dialog processDialog;
    private List<UserConfig> userConfigs = null;// holder temp user configs

    // the first, get user configs -> get light states -> get def configs
    private Handler inHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            byte[] buff;
            switch (msg.what) {
                case RequestPackage.COMMAND_GET_USERCONFIG:
                    loadUserConfigs((byte[]) msg.obj);
                    break;
                case RequestPackage.COMMAND_GET_LIGHTSTATE:
                    loadLightStates((byte[]) msg.obj);
                    break;
                case RequestPackage.COMMAND_GET_DEFCONFIG:
                    loadDefConfigs((byte[]) msg.obj);
                    break;
                case RequestPackage.COMMAND_ADD_LIGHT:
                    buff = (byte[]) msg.obj;
                    Toast.makeText(LightActivity.this, buff[0] != 0 ? "Added!" : "FAIL", Toast.LENGTH_SHORT).show();
                    break;
                case RequestPackage.COMMAND_REMOVE_LIGHT:
                    Toast.makeText(LightActivity.this, "Removed!", Toast.LENGTH_SHORT).show();
                    break;
                case RequestPackage.COMMAND_UPDATE_TIME:
                    Toast.makeText(LightActivity.this, "Updated!!", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });
    private Handler outHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RequestPackage.COMMAND_GET_USERCONFIG:
                    ServerConnection.getInstance().sendRequest(
                            inHandler,
                            RequestPackage.COMMAND_GET_USERCONFIG,
                            new byte[]{});
                    break;
                case RequestPackage.COMMAND_GET_LIGHTSTATE:
                    ServerConnection.getInstance().sendRequest(
                            inHandler,
                            RequestPackage.COMMAND_GET_LIGHTSTATE,
                            new byte[]{});
                    break;
                case RequestPackage.COMMAND_GET_DEFCONFIG:
                    ServerConnection.getInstance().sendRequest(
                            inHandler,
                            RequestPackage.COMMAND_GET_DEFCONFIG,
                            new byte[]{});
                    break;
            }
            return true;
        }
    });

    private void loadUserConfigs(byte[] data) {
        ResponseUserConfigPackage response = new ResponseUserConfigPackage();
        response.init(data);
        userConfigs = response.getUserConfigs();

        getLightStates();
    }

    private void loadLightStates(byte[] data) {
        ResponseLightStatePackage response = new ResponseLightStatePackage();
        response.init(data);
        byte[] states = response.getStates();
        lights = new ArrayList<>(states.length);
        for (int i = 0; i < states.length; i++) {
            UserConfig userConfig = userConfigs.get(i);
            Light light = new Light();
            light.setState(states[i]);
            light.setName(userConfig.getName());
            light.setSensor(userConfig.getSensor());
            light.setExtra(userConfig.getExtra());
            light.setLstConfig(userConfig.getLstConfig());
            light.setLstTime(userConfig.getLstTime());
            light.setNConfig(userConfig.getNConfig());
            lights.add(light);
        }
        userConfigs = null;
        SharedObject.getInstance().set(Config.SHARED_USERCONFIG, lights);
        SharedObject.getInstance().set(Config.SHARED_LIGHTS, lights);
        getDefConfigs();
    }

    private void loadDefConfigs(byte[] data) {
        ResponseDefConfigPackage response = new ResponseDefConfigPackage();
        response.init(data);
        defConfigs = response.getDefConfigs();
        SharedObject.getInstance().set(Config.SHARED_DEFCONFIG, defConfigs);

        loadLightToUI();

        closeProcessDialog();
    }

    private void initLoading() {
        processDialog = new Dialog(this);
        processDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        processDialog.setContentView(R.layout.layout_progress);
        processDialog.setCancelable(false);
    }

    private boolean isUIThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private void showProcessDialog(final String text) {
        if (isUIThread()) {
            TextView textView = (TextView) processDialog.findViewById(R.id.progress_tv_text);
            textView.setText(text);
            processDialog.show();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView = (TextView) processDialog.findViewById(R.id.progress_tv_text);
                    textView.setText(text);
                    processDialog.show();
                }
            });
        }
    }

    private void closeProcessDialog() {
        if (isUIThread()) {
            processDialog.hide();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    processDialog.hide();
                }
            });
        }
    }

    private void loadConfig() {
        sharedPreferences = getSharedPreferences("content_saved", MODE_PRIVATE);
        SharedObject.getInstance().set(Config.SHARED_REFRESH_RATE,
                sharedPreferences.getInt(Config.SHARED_REFRESH_RATE, 5000));
        SharedObject.getInstance().set(Config.SHARED_PORTNUM,
                sharedPreferences.getInt(Config.SHARED_PORTNUM, 2512));
        SharedObject.getInstance().set(Config.SHARED_IPADDR,
                sharedPreferences.getString(Config.SHARED_IPADDR, ""));
    }

    private void loadLightToUI() {
        listView.setOnItemClickListener(LightActivity.this);
        lightAdapter = new LightAdapter(LightActivity.this.getApplicationContext(), lights);
        lightAdapter.setOnClickListener(LightActivity.this);
        listView.setAdapter(lightAdapter);
    }

    private void getUserConfigs() {
        showProcessDialog("Fetch user configs...");
        Message msg = Message.obtain();
        msg.what = RequestPackage.COMMAND_GET_USERCONFIG;
        outHandler.sendMessageDelayed(msg, 500);
    }

    private void getLightStates() {
        showProcessDialog("Fetch lights state...");
        Message msg = Message.obtain();
        msg.what = RequestPackage.COMMAND_GET_LIGHTSTATE;
        outHandler.sendMessageDelayed(msg, 500);
    }

    private void getDefConfigs() {
        showProcessDialog("Fetch def configs...");
        Message msg = Message.obtain();
        msg.what = RequestPackage.COMMAND_GET_DEFCONFIG;
        outHandler.sendMessageDelayed(msg, 500);
    }

    private void fetchData() {
        getUserConfigs();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        initLoading();

        listView = (ListView) findViewById(R.id.light_lv_lights);
        View tvHeader = getLayoutInflater().inflate(R.layout.layout_light_header, null);
        listView.addHeaderView(tvHeader);

        ServerConnection.getInstance().setContext(this);
        Intent intent = getIntent();
        //Get the MAC address from the DeviceListActivty via EXTRA
        String address =intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        ServerConnection.getInstance().setMAC(address);

        showProcessDialog("Connecting...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean ok = ServerConnection.getInstance().connect();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProcessDialog();

                        loadConfig();

                        if (ok)
                            fetchData();
                    }
                });
            }
        }).start();

        // for test only

//        Toast.makeText(LightActivity.this, "Loading", Toast.LENGTH_SHORT).show();
//
//        lights = LightSample.getLights();
//        defConfigs = LightSample.getAllDefConfigs();
//        SharedObject.getInstance().set(Config.SHARED_USERCONFIG, lights);
//        SharedObject.getInstance().set(Config.SHARED_LIGHTS, lights);
//        SharedObject.getInstance().set(Config.SHARED_DEFCONFIG, defConfigs);
//
//        listView.setOnItemClickListener(this);
//
//        lightAdapter = new LightAdapter(this.getApplicationContext(), lights);
//        lightAdapter.setOnClickListener(this);
//        listView.setAdapter(lightAdapter);
//        Toast.makeText(this, "Loaded!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.light_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.light_menu_connect:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (ServerConnection.getInstance().connect()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LightActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
                break;
            case R.id.light_menu_disconnect:
                ServerConnection.getInstance().disconnect();
                break;
            case R.id.light_menu_defconfig:
                startActivity(new Intent(this, DefConfigActivity.class));
                break;
            case R.id.light_menu_time:
                updateTime();
                break;
            case R.id.light_menu_about:
                break;
        }
        return true;
    }

    private void updateTime() {
        DateTime now = DateTime.now();
        byte hour = (byte) now.getHours();
        byte minute = (byte) now.getMinutes();
        byte second = (byte) now.getSeconds();
        byte[] buff = new byte[3];
        buff[0] = hour;
        buff[1] = minute;
        buff[2] = second;
        Toast.makeText(LightActivity.this, String.format("Updating time: %02d:%02d:%02d", hour, minute, second), Toast.LENGTH_SHORT).show();
        ServerConnection.getInstance().sendRequest(inHandler, RequestPackage.COMMAND_UPDATE_TIME, buff);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            if (lights.size() >= Config.MAX_SUPPORT_LIGHTS)
                Toast.makeText(LightActivity.this, String.format("Support only %d lights", Config.MAX_SUPPORT_LIGHTS), Toast.LENGTH_SHORT).show();
            else
                showAddLightDialog();
        } else {
            Intent intent = new Intent(this, LightDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(LightDetailActivity.KEY_LIGHT_ID, position - 1);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void showAddLightDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_add_light);
        dialog.setTitle(R.string.app_name);

        final LimitEditText etName = (LimitEditText) dialog.findViewById(R.id.add_light_ed_name);
        final SimpleSpinner spMode = (SimpleSpinner) dialog.findViewById(R.id.add_light_sp_mode);
        final SimpleSpinner spType = (SimpleSpinner) dialog.findViewById(R.id.add_light_sp_type);
        final SimpleSpinner spPin = (SimpleSpinner) dialog.findViewById(R.id.add_light_sp_pin);
        final SimpleSpinner spLightSensor = (SimpleSpinner) dialog.findViewById(R.id.add_light_sp_lsensor);
        final SimpleSpinner spPeopleSensor = (SimpleSpinner) dialog.findViewById(R.id.add_light_sp_psensor);
        Button btnOK = (Button) dialog.findViewById(R.id.add_light_btn_add);
        Button btnCancel = (Button) dialog.findViewById(R.id.add_light_btn_cancel);

        ArrayAdapter<String> lsAdapter = new ArrayAdapter<String>(this.getApplicationContext(),
                R.layout.layout_simple_spinner_item, getResources().getStringArray(R.array.light_sensor_pins));
        ArrayAdapter<String> psAdapter = new ArrayAdapter<String>(this.getApplicationContext(),
                R.layout.layout_simple_spinner_item, getResources().getStringArray(R.array.people_sensor_pins));
        ArrayAdapter<String> lpAdapter = new ArrayAdapter<String>(this.getApplicationContext(),
                R.layout.layout_simple_spinner_item, getResources().getStringArray(R.array.light_pins));
        ArrayAdapter<String> ltAdapter = new ArrayAdapter<String>(this.getApplicationContext(),
                R.layout.layout_simple_spinner_item, getResources().getStringArray(R.array.light_types));
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this.getApplicationContext(),
                R.layout.layout_simple_spinner_item, getResources().getStringArray(R.array.light_modes));

        spLightSensor.setAdapter(lsAdapter);
        spPeopleSensor.setAdapter(psAdapter);
        spPin.setAdapter(lpAdapter);
        spType.setAdapter(ltAdapter);
        spMode.setAdapter(modeAdapter);

        etName.setMaxCharacters(10);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get values...
                String name = etName.getText().toString();
                byte lightSensor = Byte.parseByte(spLightSensor.getSelectedItem().toString());
                byte peopleSensor = Byte.parseByte(spPeopleSensor.getSelectedItem().toString());
                byte lightPin = Byte.parseByte(spPin.getSelectedItem().toString());
                final byte lightType = (byte) (spType.getSelectedItemPosition() + 1);
                byte mode = (byte) spMode.getSelectedItemPosition();
                if (mode == 1)
                    mode = UserConfig.CONFIG_TYPE_DEF;
                else if (mode == 2)
                    mode = UserConfig.CONFIG_TYPE_OFF;
                else
                    mode = UserConfig.CONFIG_TYPE_USER;
                // check values...
                if (name.length() == 0 || name.length() > 10) {
                    Toast.makeText(LightActivity.this, "Light name must have 1 to 10 characters", Toast.LENGTH_SHORT).show();
                } else {
                    final Light light = new Light();
                    light.setNConfig(mode);
                    light.setExtra(UserConfig.combine(lightType, lightPin));
                    light.setSensor(UserConfig.combine(lightSensor, peopleSensor));
                    light.setName(name);
                    light.setState((byte) Light.STATE_LIGHT_OFF);
                    Toast.makeText(LightActivity.this, "Adding...", Toast.LENGTH_SHORT).show();

                    // send request(add new light) to arduino
                    ServerConnection.getInstance().sendRequest(
                            inHandler,
                            RequestPackage.COMMAND_ADD_LIGHT,
                            light.getBytes()
                    );

                    // update to UI
                    lights.add(light);
                    lightAdapter.notifyDataSetChanged();
                    listView.invalidateViews();
                }

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

    // remove light
    @Override
    public void onClick(View v) {
        final LightHolder holder = (LightHolder) v.getTag();
        if (holder == null)
            return;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(String.format("Are you sure you want to remove '%s'?", holder.light.getName()));
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LightActivity.this, "removing..", Toast.LENGTH_SHORT).show();
                // send request(remove light by id) to arduino
                ServerConnection.getInstance().sendRequest(
                        inHandler,
                        RequestPackage.COMMAND_REMOVE_LIGHT,
                        new byte[] {(byte) holder.lightId}
                );
                // update to UI
                lights.remove(holder.light);
                lightAdapter.notifyDataSetChanged();
                listView.invalidateViews();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        ServerConnection.getInstance().disconnect();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String requestUpdate = (String) SharedObject.getInstance().pop(Config.SHARED_REQUEST_UPDATE);
        if (requestUpdate != null) {
            int update = Integer.parseInt(requestUpdate);
            if (update != 0) {
                listView.invalidateViews();
            }
        }
    }

    //@Override
    public void refreshLightStates(byte[] lightStates) {
        for (int i = 0; i < lightStates.length; i++) {
            lights.get(i).setState(lightStates[i]);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.invalidateViews();
            }
        });
    }

    private static class ViewHolder {
        ImageView ivState;
        TextView tvName;
        TextView tvConfig;
        ImageView ivType;
    }

    private static class LightHolder {
        public final Light light;
        public final int lightId;

        public LightHolder(Light light, int lightId) {
            this.light = light;
            this.lightId = lightId;
        }
    }

    private class LightAdapter extends BaseAdapter {
        private List<Light> lights;
        private LayoutInflater inflater;
        private View.OnClickListener mOnClickListener = null;
        private int[] roomImages = {R.drawable.livingroom_icon, R.drawable.bedroom_icon,
                R.drawable.kichen_icon, R.drawable.porch_icon,
                R.drawable.bathroom_icon, R.drawable.toilet_icon,
                R.drawable.dinningroom_icon, R.drawable.loftroom_icon};

        public LightAdapter(Context context, List<Light> lights) {
            this.inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            this.lights = lights;
        }

        public void setOnClickListener(View.OnClickListener listener) {
            mOnClickListener = listener;
        }

        @Override
        public int getCount() {
            return lights.size();
        }

        @Override
        public Object getItem(int position) {
            return lights.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_light_item, null);
                holder = new ViewHolder();
                holder.ivState = (ImageView) convertView.findViewById(R.id.light_item_iv_state);
                holder.tvName = (TextView) convertView.findViewById(R.id.light_item_tv_name);
                holder.tvConfig = (TextView) convertView.findViewById(R.id.light_item_tv_config);
                holder.ivType = (ImageView) convertView.findViewById(R.id.light_item_ib_remove);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Light light = (Light) getItem(position);
            int stateImage = light.getState() == Light.STATE_LIGHT_OFF ? R.drawable.ic_light_off : R.drawable.ic_light_on;
            holder.ivState.setImageResource(stateImage);
            holder.tvName.setText(light.getName());
            String configName = "";
            switch (light.getConfigType()) {
                case Light.CONFIG_TYPE_DEF:
                    configName = "DEF";
                    break;
                case Light.CONFIG_TYPE_USER:
                    configName = "USER";
                    break;
                case Light.CONFIG_TYPE_OFF:
                    configName = "OFF";
                    break;
            }
            holder.tvConfig.setText(configName);
            holder.ivType.setImageResource(roomImages[light.getLightType() - 1]);
            holder.ivType.setTag(new LightHolder(light, position));
            holder.ivType.setOnClickListener(mOnClickListener);
            return convertView;
        }
    }
}
