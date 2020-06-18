package com.myapp.emra.model;

public class History {

   private String mode, clientId, imgPath, period, date, time, croppedImgPath, kwhAutomatic, kwhActual,regionProbability, meterType;

   public History(String mode, String clientId, String imgPath, String period, String date, String time, String croppedImgPath, String kwhAutomatic, String kwhActual, String regionProbability, String meterType) {
      this.mode = mode;
      this.clientId = clientId;
      this.imgPath = imgPath;
      this.period = period;
      this.date = date;
      this.time = time;
      this.croppedImgPath = croppedImgPath;
      this.kwhAutomatic = kwhAutomatic;
      this.kwhActual = kwhActual;
      this.regionProbability = regionProbability;
      this.meterType = meterType;
   }

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

   public String getMeterType() {
      return meterType;
   }
}
