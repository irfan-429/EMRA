package com.myapp.emra.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.emra.R;
import com.myapp.emra.model.History;
import com.myapp.emra.view.HistoryDetails;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private Context context;
    private List<History> arrayList;
    private MyViewHolder viewHolder;
    private History model;

    public HistoryAdapter(Context context, List<History> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listitem_history, parent, false);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        model = arrayList.get(position);

        /*get and set data to list*/
        viewHolder.customerID.setText(model.getClientId());
        viewHolder.createdDate.setText(model.getDate());
        viewHolder.createdTime.setText(model.getTime());

        holder.historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mode = arrayList.get(position).getMode();
                String period = arrayList.get(position).getPeriod();
                String imgPath = arrayList.get(position).getImgPath();
                String croppedImgPath = arrayList.get(position).getCroppedImgPath();
                String kwhAutomatic = arrayList.get(position).getKwhAutomatic();
                String kwhActual = arrayList.get(position).getKwhActual();
                String regionProbability = arrayList.get(position).getRegionProbability();
                String meterType = arrayList.get(position).getMeterType();

                Intent intent = new Intent(context, HistoryDetails.class);
                intent.putExtra("mode", mode);
                intent.putExtra("period", period);
                intent.putExtra("imgPath", imgPath);
                intent.putExtra("croppedImgPath", croppedImgPath);
                intent.putExtra("kwhAutomatic", kwhAutomatic);
                intent.putExtra("kwhActual", kwhActual);
                intent.putExtra("regionProbability", regionProbability);
                intent.putExtra("meterType", meterType);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView customerID, createdTime, createdDate;
        CardView historyLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            customerID = itemView.findViewById(R.id.listitem_customerID);
            createdDate = itemView.findViewById(R.id.listitem_createdDate);
            createdTime = itemView.findViewById(R.id.listitem_createdTime);
            historyLayout = itemView.findViewById(R.id.listitem_cv_historyLayout);
        }
    }
}