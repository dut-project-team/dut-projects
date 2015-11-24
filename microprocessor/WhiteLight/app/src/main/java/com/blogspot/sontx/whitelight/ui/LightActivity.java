package com.blogspot.sontx.whitelight.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.sontx.whitelight.R;
import com.blogspot.sontx.whitelight.bean.DefConfig;
import com.blogspot.sontx.whitelight.bean.Light;
import com.blogspot.sontx.whitelight.bean.UserConfig;
import com.blogspot.sontx.whitelight.lib.Config;
import com.blogspot.sontx.whitelight.lib.SharedObject;
import com.blogspot.sontx.whitelight.net.ServerConnection;
import com.blogspot.sontx.whitelight.sample.LightSample;
import com.blogspot.sontx.whitelight.ui.view.LimitEditText;
import com.blogspot.sontx.whitelight.ui.view.SimpleSpinner;

import java.util.List;

public class LightActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<Light> lights;
    private List<DefConfig> defConfigs;
    private LightAdapter lightAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        listView = (ListView) findViewById(R.id.light_lv_lights);
        View tvHeader = getLayoutInflater().inflate(R.layout.layout_light_header, null);
        listView.addHeaderView(tvHeader);
        Toast.makeText(LightActivity.this, "Loading", Toast.LENGTH_SHORT).show();

        lights = LightSample.getLights();
        defConfigs = LightSample.getAllDefConfigs();
        SharedObject.getInstance().set(Config.SHARED_USERCONFIG, lights);
        SharedObject.getInstance().set(Config.SHARED_LIGHTS, lights);
        SharedObject.getInstance().set(Config.SHARED_DEFCONFIG, defConfigs);

        listView.setOnItemClickListener(this);

        lightAdapter = new LightAdapter(this.getApplicationContext(), lights);
        listView.setAdapter(lightAdapter);
        Toast.makeText(this, "Loaded!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0) {
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
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.light_sensor_pins));
        ArrayAdapter<String> psAdapter = new ArrayAdapter<String>(this.getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.people_sensor_pins));
        ArrayAdapter<String> lpAdapter = new ArrayAdapter<String>(this.getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.light_pins));
        ArrayAdapter<String> ltAdapter = new ArrayAdapter<String>(this.getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.light_types));
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this.getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.light_modes));

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
                if(mode == 1)
                    mode = UserConfig.CONFIG_TYPE_DEF;
                else if(mode == 2)
                    mode = UserConfig.CONFIG_TYPE_OFF;
                else
                    mode = UserConfig.CONFIG_TYPE_USER;
                // check values...
                if(name.length() == 0 || name.length() > 10) {
                    Toast.makeText(LightActivity.this, "Light name must have 1 to 10 characters", Toast.LENGTH_SHORT).show();
                } else {
                    final Light light = new Light();
                    light.setNConfig(mode);
                    light.setExtra(UserConfig.combine(lightType, lightPin));
                    light.setSensor(UserConfig.combine(lightSensor, peopleSensor));
                    light.setName(name);
                    light.setState((byte) Light.STATE_LIGHT_OFF);
                    Toast.makeText(LightActivity.this, "Adding...", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final boolean ok = ServerConnection.getInstance().addLight(light);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (ok) {
                                        List<Light> lights = (List<Light>) SharedObject.getInstance().get(Config.SHARED_LIGHTS);
                                        lights.add(light);
                                        lightAdapter.notifyDataSetChanged();
                                        listView.invalidateViews();
                                        Toast.makeText(LightActivity.this, "Added!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LightActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start();
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

    private static class ViewHolder {
        ImageView ivState;
        TextView tvName;
        TextView tvConfig;
    }

    private class LightAdapter extends BaseAdapter {
        private List<Light> lights;
        private LayoutInflater inflater;

        public LightAdapter(Context context, List<Light> lights) {
            this.inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            this.lights = lights;
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
            return convertView;
        }
    }
}
