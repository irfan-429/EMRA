package com.myapp.emra.networking.response;

import com.google.gson.annotations.SerializedName;

public class RespCustomerDetails {
    @SerializedName("id")
    private String id;
    @SerializedName("client_id")
    private String customerId;
    @SerializedName("name")
    private String customerName;
    @SerializedName("meter_no")
    private String customerMeterNo;
    @SerializedName("unit_up")
    private String customerUnitUP;
    @SerializedName("tarif")
    private String customerTarif;
    @SerializedName("power")
    private String customerPower;


    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerMeterNo() {
        return customerMeterNo;
    }

    public String getCustomerUnitUP() {
        return customerUnitUP;
    }

    public String getCustomerTarif() {
        return customerTarif;
    }

    public String getCustomerPower() {
        return customerPower;
    }
}
