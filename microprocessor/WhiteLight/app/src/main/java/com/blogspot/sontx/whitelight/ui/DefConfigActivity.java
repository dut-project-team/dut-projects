package com.blogspot.sontx.whitelight.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.blogspot.sontx.whitelight.R;
import com.blogspot.sontx.whitelight.ui.helper.DefConfigViewer;
import com.blogspot.sontx.whitelight.ui.view.SimpleSpinner;

public class DefConfigActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DefConfigViewer viewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_def_config);
        viewer = new DefConfigViewer(findViewById(R.id.def_config_root));
        viewer.setDefConfig(0);
        viewer.setChangeable(true);

        SimpleSpinner spinner = (SimpleSpinner) findViewById(R.id.def_config_sp_type);
        spinner.setOnItemSelectedListener(this);
        String[] lightTypes = getResources().getStringArray(R.array.light_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getApplicationContext(),
                R.layout.layout_simple_spinner_item, lightTypes);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (viewer.getDefConfigId() != position)
            viewer.setDefConfig(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
