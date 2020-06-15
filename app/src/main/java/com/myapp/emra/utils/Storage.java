package com.myapp.emra.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.myapp.emra.R;


public class Storage {
    //    all global field constants for the local stoarage
    private  final String BASE_URL = "url";
    private  final String IMG_SELECTION = "mode";

    private Context context;

    public Storage(Context context) {
        this.context = context;
    }

    private SharedPreferences.Editor getPreferencesEditor() {
        return getsharedPreferences().edit();
    }

    private SharedPreferences getsharedPreferences() {
        return context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void saveBaseURL(String str) {
        getPreferencesEditor().putString(BASE_URL, str).commit();
    }

    public String getBaseURL() {
        return getsharedPreferences().getString(BASE_URL, null);
    }

    public void setImgSelection(boolean flag) {
        getPreferencesEditor().putBoolean(IMG_SELECTION, flag).commit();
    }

    public Boolean getImgSelection() {
        return getsharedPreferences().getBoolean(IMG_SELECTION, false);
    }


}
