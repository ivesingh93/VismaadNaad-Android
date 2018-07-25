package com.vismaad.naad.rest.service;

import com.google.gson.JsonElement;
import com.vismaad.naad.AddPlayList.model.JBShabadsList;
import com.vismaad.naad.addshabads.model.AddShabadsList;
import com.vismaad.naad.rest.model.playlist.AddShabads;
import com.vismaad.naad.rest.model.playlist.CreatePlayList;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.rest.model.sggs.ShabadInfo;
import com.vismaad.naad.rest.model.user.UserCredentials;

import org.json.JSONArray;
import org.json.JSONStringer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by satnamsingh on 14/06/18.
 */

public interface PlayList {
    @Headers("Content-Type: application/json")

    @POST("userRoutes/createPlaylist")
    Call<JsonElement> create_playlist(@Body CreatePlayList mCreatePlayList);


    @GET("userRoutes/users/{username}/playlists")
    Call<List<JsonElement>> get_playList(@Path("username") String username);


    @POST("userRoutes/deletePlaylist")
    Call<JsonElement> delete_playlist(@Body CreatePlayList mCreatePlayList);

    @GET("raagiRoutes/raagis/{raagi_name}/shabads")
    Call<List<Shabad>> raagi_shabads(@Path("raagi_name") String raagi_name);


    @Headers("Content-Type: application/json")
    @POST("userRoutes/addShabads")
    Call<JsonElement> add_playlist(@Body List<AddShabadsList> addShabadsList);
    ///api/userRoutes/addShabad


    @GET("userRoutes/users/{username}/playlists/{playlist_name}")
    Call<List<Shabad>> getPlayListShabads(@Path("username") String username, @Path("playlist_name") String playlist_name);


    @Headers("Content-Type: application/json")
    @POST("userRoutes/removeShabads")
    Call<JsonElement> remove_playlist(@Body List<AddShabadsList> addShabadsList);

     /*   @FormUrlEncoded
    @POST("userRoutes/addShabad")
    Call<List<AddShabadsList>> add_playlist(@Field("username") String user_id
            , @Field("playlist_name") String playlist_name, @Field("id") String id);*/

}
