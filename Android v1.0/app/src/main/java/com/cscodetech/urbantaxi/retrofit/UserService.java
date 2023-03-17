package com.cscodetech.urbantaxi.retrofit;


import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserService {


    @POST(APIClient.APPEND_URL + "country_code.php")
    Call<JsonObject> getCodelist(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "r_login_user.php")
    Call<JsonObject> login(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "r_vehicle_list.php")
    Call<JsonObject> getVehicleList(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "r_home_data.php")
    Call<JsonObject> homeData(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "pending.php")
    Call<JsonObject> tripependingHistory(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "complete.php")
    Call<JsonObject> tripeCompletHistory(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "trip_details.php")
    Call<JsonObject> tripDetails(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "make_decision.php")
    Call<JsonObject> makeDecision(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "reach_location.php")
    Call<JsonObject> reachLocation(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "trip_start_cancle.php")
    Call<JsonObject> tripStartCancle(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "trip_end.php")
    Call<JsonObject> tripEnd(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "earn_report.php")
    Call<JsonObject> earnReport(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "request_withdraw.php")
    Call<JsonObject> requestWithdraw(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "payout_list.php")
    Call<JsonObject> getPayoutlist(@Body RequestBody requestBody);


    @Multipart
    @POST(APIClient.APPEND_URL + "r_reg_user.php")
    Call<JsonObject> personalDocument(@Part("fname") RequestBody fname,
                                      @Part("lname") RequestBody lname,
                                      @Part("vehicle_number") RequestBody vehicle_number,
                                      @Part("email") RequestBody email,
                                      @Part("mobile") RequestBody mobile,
                                      @Part("ccode") RequestBody ccode,
                                      @Part("address") RequestBody address,
                                      @Part("password") RequestBody password,
                                      @Part("gender") RequestBody gender,
                                      @Part("vehicle_id") RequestBody vid,
                                      @Part List<MultipartBody.Part> identity_fa,
                                      @Part List<MultipartBody.Part> identity_ba,
                                      @Part List<MultipartBody.Part> license_fa,
                                      @Part List<MultipartBody.Part> license_ba,
                                      @Part List<MultipartBody.Part> pro_imga);


    @Multipart
    @POST(APIClient.APPEND_URL + "reup_identity.php")
    Call<JsonObject> reupIdentit(@Part("rider_id") RequestBody rider,
                                 @Part List<MultipartBody.Part> identity_front,
                                 @Part List<MultipartBody.Part> identity_back);

    @Multipart
    @POST(APIClient.APPEND_URL + "reup_license.php")
    Call<JsonObject> reuplicense(@Part("rider_id") RequestBody rider,
                                 @Part List<MultipartBody.Part> license_front,
                                 @Part List<MultipartBody.Part> license_back);


}
