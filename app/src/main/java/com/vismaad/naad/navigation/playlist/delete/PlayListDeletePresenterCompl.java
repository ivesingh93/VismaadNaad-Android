package com.vismaad.naad.navigation.playlist.delete;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vismaad.naad.navigation.playlist.view.IPlayListView;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.playlist.CreatePlayList;
import com.vismaad.naad.rest.service.PlayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by satnamsingh on 31/05/18.
 */

public class PlayListDeletePresenterCompl implements IPlayListDeletePresenter {

    IPlayListView iLoginView;
    PlayList mCreatePlayList;

    public PlayListDeletePresenterCompl(IPlayListView iLoginView) {
        this.iLoginView = iLoginView;
        mCreatePlayList = RetrofitClient.getClient().create(PlayList.class);

    }


    @Override
    public void doDeletePlayList1(String userName, String playListName) {


        Call<JsonElement> call = mCreatePlayList.delete_playlist(new CreatePlayList(userName, playListName));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                iLoginView.onResult(new Gson().toJson(response.body()), 1);

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
            }
        });
    }
}
