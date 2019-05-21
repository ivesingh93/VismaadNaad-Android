package com.vismaad.naad.rest.service;

import com.vismaad.naad.rest.model.raagi.ShabadTutorial;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ShabadTutorialsService {


    @GET("raagiRoutes/shabadTutorials/limit/{limit}")
    Call<List<ShabadTutorial>> shabad_tutorials( @Path("limit") String limit);

}
