package com.myapp.emra.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.myapp.emra.R;
import com.myapp.emra.controller.HistoryAdapter;
import com.myapp.emra.model.History;
import com.myapp.emra.networking.response.RespHistory;
import com.myapp.emra.networking.retrofit.ApiConfig;
import com.myapp.emra.networking.retrofit.RetrofitClient;
import com.myapp.emra.networking.retrofit.RetrofitRespondListener;
import com.myapp.emra.utils.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity implements RetrofitRespondListener {

    private Context context = this;
    private RecyclerView recyclerView;
    private List<History> arrayList = new ArrayList<>();
    private LoadingDialog loadingDialog;
    private TextView tv_msgNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initViews();
        fetchHistory();
    }

    private void initViews() {
        getSupportActionBar().setTitle("History"); //setting toolbar title

        loadingDialog = new LoadingDialog(context);
        recyclerView = findViewById(R.id.history_rv);
        tv_msgNoData = findViewById(R.id.tv_msgNotFound);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void fetchHistory() {
        loadingDialog.show();
        Call<RespHistory> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_fetchHistory();
        RetrofitClient.callRetrofit(apiCall, "HISTORY", this);
    }

    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "HISTORY":
                responseHistory(response);
                break;
        }

        loadingDialog.dismiss();
    }

    @Override
    public void onRetrofitFailure(String responseError, String requestName) {

    }


    private void responseHistory(Response<?> response) {
        if (response.code() == 200) {
            RespHistory respHistory = (RespHistory) response.body(); //main obj
            List<RespHistory.Results> resultsList = respHistory.getResultsClass(); //array orders
            if (resultsList.size() > 0) {
                arrayList.clear();
                for (RespHistory.Results results : resultsList) {
                    String mode =results.getMode();
                    String clientId = results.getClientId();
                    String imgPath = results.getImgPath();
                    String period = results.getPeriod();
                    String date = results.getDate();
                    String time = results.getTime();
                    String croppedImgPath = results.getCroppedImgPath();
                    String kwhAutomatic = results.getKwhAutomatic();
                    String kwhActual = results.getKwhActual();
                    String regionProbability = results.getRegionProbability();
                    String meterType = results.getMeterType();

                    arrayList.add(new History(mode, clientId, imgPath, period, date, time, croppedImgPath, kwhAutomatic, kwhActual,regionProbability, meterType));
                }
            }

            if (arrayList.size() == 0) {
                tv_msgNoData.setVisibility(View.VISIBLE);
            }

            HistoryAdapter adapter = new HistoryAdapter(context, arrayList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else tv_msgNoData.setVisibility(View.VISIBLE);
    }

}