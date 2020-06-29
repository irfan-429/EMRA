package com.myapp.emra.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.myapp.emra.R;
import com.myapp.emra.utils.Storage;

public class SettingsActivity extends AppCompatActivity {

    Context context = this;
    SwitchCompat sw_selectionMode;
    Storage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Settings"); //setting toolbar title

        storage = new Storage(context);
        sw_selectionMode = findViewById(R.id.sw_selectionMode);

        //get switch state
        if (storage.getImgSelection()) {
            sw_selectionMode.setChecked(true);
        } else sw_selectionMode.setChecked(false);


        sw_selectionMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    storage.setImgSelection(true);
                } else storage.setImgSelection(false);
            }
        });
    }
}