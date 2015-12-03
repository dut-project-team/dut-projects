package com.blogspot.sontx.whitelight.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.sontx.whitelight.R;
import com.blogspot.sontx.whitelight.bean.Light;
import com.blogspot.sontx.whitelight.bean.UserConfig;
import com.blogspot.sontx.whitelight.lib.Config;
import com.blogspot.sontx.whitelight.lib.SharedObject;
import com.blogspot.sontx.whitelight.net.RequestPackage;
import com.blogspot.sontx.whitelight.net.ServerConnection;
import com.blogspot.sontx.whitelight.ui.helper.ConfigLayout;
import com.blogspot.sontx.whitelight.ui.helper.DefConfigLayout;
import com.blogspot.sontx.whitelight.ui.helper.OffConfigLayout;
import com.blogspot.sontx.whitelight.ui.helper.SwitchModeHelper;
import com.blogspot.sontx.whitelight.ui.helper.UserConfigLayout;
import com.blogspot.sontx.whitelight.ui.view.SimpleSpinner;

import java.util.List;

public class LightDetailActivity extends AppCompatActivity {
    public static final String KEY_LIGHT_ID = "light_id";
    private Light light;
    private int lightId;
    private ConfigLayout currentLayout;
    private Handler inHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RequestPackage.COMMAND_EDIT_USERCONFIG:
                    Toast.makeText(LightDetailActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_detail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        lightId = bundle.getInt(KEY_LIGHT_ID, -1);
        if (lightId == -1) {
            finish();
            return;
        }

        List<Light> lights = (List<Light>) SharedObject.getInstance().get(Config.SHARED_USERCONFIG);
        light = lights.get(lightId);

        applyGeneralInfo(light);
        switch (light.getConfigType()) {
            case UserConfig.CONFIG_TYPE_OFF:
                applyConfigLayout(new OffConfigLayout());
                break;
            case UserConfig.CONFIG_TYPE_USER:
                applyConfigLayout(new UserConfigLayout(light, lightId));
                break;
            case UserConfig.CONFIG_TYPE_DEF:
                applyConfigLayout(new DefConfigLayout(light));
                break;
            default:
                finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.light_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.light_detail_menu_switch:
                SwitchModeHelper helper = new SwitchModeHelper(this, light, lightId);
                helper.showDialog();
                break;
            case R.id.light_detail_menu_setting:
                showSetting();
                break;
        }
        return true;
    }

    private void applyGeneralInfo(Light light) {
        ImageView ivState = (ImageView) findViewById(R.id.light_detail_iv_state);
        int imageState = light.getState() == UserConfig.CONFIG_LIGHT_OFF ? R.drawable.ic_light_off : R.drawable.ic_light_on;
        ivState.setImageResource(imageState);

        TextView tvName = (TextView) findViewById(R.id.light_detail_tv_name);
        String modeName = "";
        switch (light.getConfigType()) {
            case UserConfig.CONFIG_TYPE_DEF:
                modeName = "DEF";
                break;
            case UserConfig.CONFIG_TYPE_USER:
                modeName = "USER";
                break;
            case UserConfig.CONFIG_TYPE_OFF:
                modeName = "OFF";
                break;
        }
        tvName.setText(String.format("%s[%s mode]", light.getName(), modeName));
    }

    private void applyConfigLayout(ConfigLayout layout) {
        ViewStub viewStub = (ViewStub) findViewById(R.id.light_detail_vs_config);
        layout.applyLayout(viewStub);
        currentLayout = layout;
    }

    private void showSetting() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_light_detail_setting);
        dialog.setTitle(R.string.app_name);

        final SimpleSpinner spLightSensor = (SimpleSpinner) dialog.findViewById(R.id.light_detail_setting_sp_lsensor);
        final SimpleSpinner spPeopleSensor = (SimpleSpinner) dialog.findViewById(R.id.light_detail_setting_sp_psensor);
        final SimpleSpinner spLightPin = (SimpleSpinner) dialog.findViewById(R.id.light_detail_setting_sp_light);
        final SimpleSpinner spLightType = (SimpleSpinner) dialog.findViewById(R.id.light_detail_setting_sp_type);
        Button btnApply = (Button) dialog.findViewById(R.id.light_detail_setting_btn_apply);
        Button btnCancel = (Button) dialog.findViewById(R.id.light_detail_setting_btn_cancel);

        ArrayAdapter<String> lsAdapter = new ArrayAdapter<String>(this.getApplicationContext(),
                R.layout.layout_simple_spinner_item, getResources().getStringArray(R.array.light_sensor_pins));
        ArrayAdapter<String> psAdapter = new ArrayAdapter<String>(this.getApplicationContext(),
                R.layout.layout_simple_spinner_item, getResources().getStringArray(R.array.people_sensor_pins));
        ArrayAdapter<String> lpAdapter = new ArrayAdapter<String>(this.getApplicationContext(),
                R.layout.layout_simple_spinner_item, getResources().getStringArray(R.array.light_pins));
        ArrayAdapter<String> ltAdapter = new ArrayAdapter<String>(this.getApplicationContext(),
                R.layout.layout_simple_spinner_item, getResources().getStringArray(R.array.light_types));

        spLightSensor.setAdapter(lsAdapter);
        spPeopleSensor.setAdapter(psAdapter);
        spLightPin.setAdapter(lpAdapter);
        spLightType.setAdapter(ltAdapter);

        spLightSensor.setSelectionText(light.getLightSensor() + "");
        spPeopleSensor.setSelectionText(light.getPeopleSensor() + "");
        spLightPin.setSelectionText(light.getLightPin() + "");
        spLightType.setSelection(light.getLightType() - 1);// livingroom start at 1

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte lightSensor = Byte.parseByte(spLightSensor.getSelectedItem().toString());
                byte peopleSensor = Byte.parseByte(spPeopleSensor.getSelectedItem().toString());
                byte lightPin = Byte.parseByte(spLightPin.getSelectedItem().toString());
                final byte lightType = (byte) (spLightType.getSelectedItemPosition() + 1);

                light.setSensor(UserConfig.combine(lightSensor, peopleSensor));
                light.setExtra(UserConfig.combine(lightType, lightPin));

                Toast.makeText(LightDetailActivity.this, "Apply...", Toast.LENGTH_SHORT).show();

                // send request(light sensor, people sensor, pin and light type) to arduino
                ServerConnection.getInstance().sendRequest(
                        inHandler,
                        RequestPackage.COMMAND_EDIT_USERCONFIG,
                        light.getBytes(lightId));

                // only if current light is config by DEFCONFIG, apply new config(because light type changed)
                if (currentLayout instanceof DefConfigLayout)
                    ((DefConfigLayout) currentLayout).loadDefConfig(lightType - 1);

                // request light activity update for new light display surface changed
                SharedObject.getInstance().set(Config.SHARED_REQUEST_UPDATE, "1");

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
}
