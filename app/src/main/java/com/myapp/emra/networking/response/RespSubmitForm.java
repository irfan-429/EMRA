package com.myapp.emra.networking.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespSubmitForm {

    @SerializedName("client")
    private String clientID;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("reading_image")
    private String readingImg;
    @SerializedName("kwh_value_predicted")
    private String kWhValuePredicted;
    @SerializedName("kwh_value_actual")
    private String kWhValueActual;

    public String getClientID() {
        return clientID;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getReadingImg() {
        return readingImg;
    }

    public String getkWhValuePredicted() {
        return kWhValuePredicted;
    }

    public String getkWhValueActual() {
        return kWhValueActual;
    }
}
