package com.vismaad.naad.navigation.fetchplaylist.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vismaad.naad.navigation.playlist.view.IPlayListView;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.service.PlayList;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by satnamsingh on 31/05/18.
 */

public class GetPlayListPresenterCompl implements IGetPlayListPresenter {

    IPlayListView iLoginView;
    PlayList mCreatePlayList;

    public GetPlayListPresenterCompl(IPlayListView iLoginView) {
        this.iLoginView = iLoginView;
        mCreatePlayList = RetrofitClient.getClient().create(PlayList.class);

    }


    @Override
    public void doFetchList(String userName) {


        Call<List<JsonElement>> call = mCreatePlayList.get_playList(userName);
        call.enqueue(new Callback<List<JsonElement>>() {
            @Override
            public void onResponse(Call<List<JsonElement>> call, Response<List<JsonElement>> response) {
                iLoginView.onResult(new Gson().toJson(response.body()), 2);
            }

            @Override
            public void onFailure(Call<List<JsonElement>> call, Throwable t) {

            }
        });
    }
}
