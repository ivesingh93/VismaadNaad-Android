package com.vismaad.naad.rest.instance;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by satnamsingh on 19/06/18.
 */

public class RetrofitClientShabads {
    private static Retrofit retrofit = null;
    public static final String BASE_URL = "https://s3.eu-west-2.amazonaws.com/vismaadnaad/";
 //   public static final String BASE_URL = "https://vismaadnaad.com:3200/api/";
    public static Retrofit getClient(){

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
