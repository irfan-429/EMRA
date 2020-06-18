package com.myapp.emra.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.myapp.emra.R;
import com.myapp.emra.utils.Utilities;

public class HistoryDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);

        getSupportActionBar().setTitle("Details"); //setting toolbar title
        initViews();
    }

    private void initViews() {
        TextView tv_mode = findViewById(R.id.histDetails_tv_mode);
        TextView tv_period = findViewById(R.id.histDetails_tv_period);
        TextView tv_regionProbability = findViewById(R.id.histDetails_tv_regionProbability);
        TextView tv_valPredicted = findViewById(R.id.histDetails_tv_valPredicted);
        TextView tv_valActual = findViewById(R.id.histDetails_tv_valActual);
        TextView tv_meterType= findViewById(R.id.histDetails_tv_meterType);
        AppCompatImageView iv_image = findViewById(R.id.histDetails_iv_image);
        AppCompatImageView iv_readingImage = findViewById(R.id.histDetails_iv_readingImage);

        Intent intent=getIntent();
        tv_mode.setText(intent.getExtras().getString("mode"));
        tv_period.setText(intent.getExtras().getString("period"));
        tv_regionProbability.setText(intent.getExtras().getString("regionProbability"));
        tv_valPredicted.setText(intent.getExtras().getString("kwhAutomatic"));
        tv_valActual.setText(intent.getExtras().getString("kwhActual"));
        tv_meterType.setText(intent.getExtras().getString("meterType"));

        Utilities.loadImage(this, intent.getExtras().getString("imgPath"), iv_image);
        Utilities.loadImage(this,intent.getExtras().getString("croppedImgPath"), iv_readingImage);
    }
}