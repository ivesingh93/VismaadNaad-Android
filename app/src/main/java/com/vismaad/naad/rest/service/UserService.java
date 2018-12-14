package com.vismaad.naad.rest.service;

import com.google.gson.JsonElement;
import com.vismaad.naad.rest.model.user.UserCredentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ivesingh on 1/3/18.
 */

public interface UserService {
    @Headers("Content-Type: application/json")

    @POST("userRoutes/signup")
    Call<JsonElement> create_user(@Body UserCredentials userCredentials);

    @GET("userRoutes/accounts/{account_id}")
    Call<UserCredentials> has_account(@Path("account_id") String account_id);

    @GET("userRoutes/usernames/{username}")
    Call<UserCredentials> has_username(@Path("username") String username);

    @POST("userRoutes/authenticate")
    Call<JsonElement> login(@Body UserCredentials userCredentials);

    @POST("userrRoutes/feedback")
    Call<UserCredentials> feedback(@Path("feedback") String feedback, @Path("username") String username);

}
