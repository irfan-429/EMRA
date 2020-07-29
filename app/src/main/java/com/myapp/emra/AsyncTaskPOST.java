package com.myapp.emra;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.myapp.emra.utils.LoadingDialog;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;



import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AsyncTaskPOST extends AsyncTask<String, Void, Void> {

    private Bitmap bitmap;

    private Context context;
    private Activity activity;
    private MultipartEntity entity;
    private String url;
    private LoadingDialog loadingDialog;

    public AsyncTaskPOST(Context context, String url, MultipartEntity entity) {
        this.context = context;
        this.url=url;
        this.entity=entity;
        activity = (Activity) context;
        loadingDialog = new LoadingDialog(context);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog.show();
    }

    @Override
    protected Void doInBackground(String... strings) {
        uploadData();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        loadingDialog.dismiss();
    }



    public void uploadData() {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 60000); // Timeout Limit
        HttpResponse response;
        //JSONObject json = new JSONObject();

        try {
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response =  client.execute(post);

            /* Checking response */
            if (response != null) {
                InputStream in = response.getEntity().getContent();
                String result = convertInputStreamToString(in);


                Log.e("response", result);

//                try {
//                    JSONObject object = new JSONObject(result);
//                    JSONArray JA = object.getJSONArray("data");
//
//                    if (JA.length() <= 0) {
//                        //progressDialog.dismiss();
//                        // Utils.showCustomToastSuccess(context, "User already registered ");
//
//                    } else {
//                        for (int i = 0; i < JA.length(); i++) {
//                            JSONObject object1 = JA.getJSONObject(i);
//                            //Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                            //startActivity(intent);
//                        }
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Error", "Cannot Estabilish Connection");
        }

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}
