package com.vismaad.naad.rest.service;

import com.vismaad.naad.newwork.PopRagiAndShabad;
import com.vismaad.naad.rest.model.raagi.RaagiInfo;
import com.vismaad.naad.rest.model.raagi.Shabad;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ivesingh on 1/8/18.
 */

public interface RaagiService {

    @GET("raagiRoutes/raagi_info")
    Call<List<RaagiInfo>> raagi_info();

    @GET("raagiRoutes/raagis/{raagi_name}/shabads")
    Call<List<Shabad>> raagi_shabads(@Path("raagi_name") String raagi_name);

    @GET("raagiRoutes/homePage")
    Call<PopRagiAndShabad> popularRagiandShabad();



    @GET("raagiRoutes/popularShabads")
    Call<List<Shabad>> getPlayListShabads();

}
