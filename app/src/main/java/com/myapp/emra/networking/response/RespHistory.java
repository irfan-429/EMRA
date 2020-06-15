package com.myapp.emra.networking.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespHistory {
    @SerializedName("results")
    private List<Results> resultsClass; //array

    public List<Results> getResultsClass() {
        return resultsClass;
    }

    public class Results {
        @SerializedName("processing_mode")
        private String mode;
        @SerializedName("custid")
        private String clientId;
        @SerializedName("image_name")
        private String imgPath;
        @SerializedName("period")
        private String period;
        @SerializedName("date")
        private String date;
        @SerializedName("time")
        private String time;
        @SerializedName("cropped_image_name")
        private String croppedImgPath;
        @SerializedName("kwh_automatic")
        private String kwhAutomatic;
        @SerializedName("kwh_actual")
        private String kwhActual;
        @SerializedName("region_probability")
        private String regionProbability;

        public String getMode() {
            return mode;
        }

        public String getClientId() {
            return clientId;
        }

        public String getImgPath() {
            return imgPath;
        }

        public String getPeriod() {
            return period;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getCroppedImgPath() {
            return croppedImgPath;
        }

        public String getKwhAutomatic() {
            return kwhAutomatic;
        }

        public String getKwhActual() {
            return kwhActual;
        }

        public String getRegionProbability() {
            return regionProbability;
        }
    }

}
