package com.vismaad.naad.rest.service;

import com.vismaad.naad.rest.model.playlist.JBPopularShabads;
import com.vismaad.naad.rest.model.raagi.RaagiInfo;
import com.vismaad.naad.rest.model.raagi.Shabad;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PopularShabads {
    @GET("raagiRoutes/popularShabads")
    Call<List<JBPopularShabads>> popular_shabads();



}
