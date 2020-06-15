package com.myapp.emra.networking.retrofit;

import android.content.Context;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myapp.emra.utils.Storage;
import com.myapp.emra.utils.Utilities;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            //get server URL from local storage
            final Storage storage = new Storage(context);
            if (storage.getBaseURL() != null) {
                Utilities.BASE_URL = "http://" + storage.getBaseURL();
            }

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Utilities.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }


    public static <T> void callRetrofit(Call<T> call, final String requestName, final RetrofitRespondListener listener) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                listener.onRetrofitSuccess(response, requestName);
                if (response.isSuccessful())
                    Log.i(requestName + "=> SUCCESS", new Gson().toJson(response.body()));
                else Log.e(requestName + "=> UnSuccess", new Gson().toJson(response.errorBody()));
            }

            @Override
            public void onFailure(Call<T> call, Throwable throwable) {
                listener.onRetrofitFailure(throwable.getMessage(), requestName);
                Log.e(requestName + "=> FAILED", throwable.getMessage());
            }
        });
    }
}