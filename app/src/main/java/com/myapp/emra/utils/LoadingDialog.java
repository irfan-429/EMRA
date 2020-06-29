package com.myapp.emra.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.myapp.emra.R;


public class LoadingDialog extends Dialog {

    Activity activity;

    public LoadingDialog(Context context) {
        super(context);
        // this.activity = (Activity) context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


    }

    @Override
    public void show() {
//        if (activity.isFinishing()) {
//        }
        super.show();

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}


