package com.vismaad.naad.rest.service;

import com.vismaad.naad.rest.model.sggs.ShabadInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ivesingh on 1/8/18.
 */

public interface ShabadService {

    @GET("sggsRoutes/linesFrom/{from}/linesto/{to}")
    Call<List<ShabadInfo>> shabad_info(@Path("from") int from, @Path("to") int to);

}
