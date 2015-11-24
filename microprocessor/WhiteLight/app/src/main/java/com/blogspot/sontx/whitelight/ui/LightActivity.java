package com.blogspot.sontx.whitelight.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.sontx.whitelight.R;
import com.blogspot.sontx.whitelight.bean.DefConfig;
import com.blogspot.sontx.whitelight.bean.Light;
import com.blogspot.sontx.whitelight.lib.Config;
import com.blogspot.sontx.whitelight.lib.SharedObject;
import com.blogspot.sontx.whitelight.sample.LightSample;

import java.util.List;

public class LightActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<Light> lights;
    private List<DefConfig> defConfigs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        ListView listView = (ListView) findViewById(R.id.light_lv_lights);
        Toast.makeText(LightActivity.this, "Loading", Toast.LENGTH_SHORT).show();

        lights = LightSample.getLights();
        defConfigs = LightSample.getAllDefConfigs();
        SharedObject.getInstance().set(Config.SHARED_USERCONFIG, lights);
        SharedObject.getInstance().set(Config.SHARED_LIGHTS, lights);
        SharedObject.getInstance().set(Config.SHARED_DEFCONFIG, defConfigs);

        listView.setOnItemClickListener(this);

        LightAdapter adapter = new LightAdapter(this.getApplicationContext(), lights);
        listView.setAdapter(adapter);
        Toast.makeText(this, "Loaded!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, LightDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(LightDetailActivity.KEY_LIGHT_ID, position);
        intent.putExtras(bundle);
        startActivity(intent);
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
