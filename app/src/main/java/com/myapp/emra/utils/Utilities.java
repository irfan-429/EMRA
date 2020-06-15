package com.myapp.emra.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.myapp.emra.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {

    public static String BASE_URL = "http://46.17.97.55:8000";
    public static String API_CLIENT = BASE_URL+ "/api/client/";


    @SuppressLint("CheckResult")
    public static void loadImage(Context context, String imgPath, AppCompatImageView imageView) {
        Glide.with(context)
                .load(BASE_URL + imgPath)
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_not_found)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .override(100, 200)
//                .centerCrop()
//                .fitCenter() // scale to fit entire image within ImageView
                .into(imageView);
    }

    public static String formatDateTimeFromTS(long timestamp, String outputFormat) {
        Date time = new Date(timestamp);
        DateFormat outPut = new SimpleDateFormat(outputFormat);
        //Hear Define your returning date formate
        return outPut.format(time);
    }
}
