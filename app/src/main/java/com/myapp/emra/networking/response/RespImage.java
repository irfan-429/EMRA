package com.myapp.emra.networking.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespImage {

    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("image_source_file")
    @Expose
    private String imgSrcFile;
    @SerializedName("reading")
    @Expose
    private String readingValue;
    @SerializedName("result_image_path")
    @Expose
    private String resultImgPath;
    @SerializedName("region_probability")
    @Expose
    private String regionProbability;
//    @SerializedName("ocr_probability")
//    @Expose
//    private List<ObjOcrProbability> resultsClass; //array
//
//
//    public static class ObjOcrProbability {
//        @SerializedName("class")
//        private String mClass;
//        @SerializedName("score")
//        private String score;
//    }


    public String getMode() {
        return mode;
    }

    public String getImgSrcFile() {
        return imgSrcFile;
    }

    public String getReadingValue() {
        return readingValue;
    }

    public String getResultImgPath() {
        return resultImgPath;
    }

    public String getRegionProbability() {
        return regionProbability;
    }
}
