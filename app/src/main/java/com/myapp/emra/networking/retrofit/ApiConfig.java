package com.myapp.emra.networking.retrofit;

import com.myapp.emra.networking.response.RespCustomerDetails;
import com.myapp.emra.networking.response.RespHistory;
import com.myapp.emra.networking.response.RespImage;
import com.myapp.emra.networking.response.RespSubmitForm;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * This interface contains all API End Points used in Retrofit.
 */

public interface ApiConfig {

    @GET
    Call<RespCustomerDetails> API_customerDetails(@Url String clientDynamicUrl
    );


    @Multipart
    @POST("/api/reading/detection")
    Call<RespImage> API_submitMeterImg(@Part MultipartBody.Part image_source_file,
                                       @Part("mode") int mode,
                                       @Part("meter_type") RequestBody meterType

    );


    @FormUrlEncoded
    @POST("/api/reading/")
    Call<RespSubmitForm> API_submitForm(@Field("processing_mode") String mode,
                                        @Field("custid") String clientID,
                                        @Field("image_name") String imgPath,
                                        @Field("period") String period, //yyyy-MM-dd
                                        @Field("date") String date,//yyyy-MM-dd
                                        @Field("time") String time,
                                        @Field("cropped_image_name") String croppedImgPath,
                                        @Field("enhanced_image_name") String enhancedImgPath,
                                        @Field("kwh_automatic") String kwhAutomatic,
                                        @Field("kwh_actual") String kwhActual,
                                        @Field("region_probability") String regionProbability

    );


    @GET("/api/reading/")
    Call<RespHistory> API_fetchHistory();
}
