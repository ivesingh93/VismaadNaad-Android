package com.vismaad.naad.addshabads.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vismaad.naad.addshabads.model.AddShabadsList;
import com.vismaad.naad.addshabads.view.IPlayListView;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.service.PlayList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by satnamsingh on 31/05/18.
 */

public class ShabadsPresenterCompl implements IShabadsPresenter {

    IPlayListView iLoginView;
    PlayList mCreatePlayList;


    public ShabadsPresenterCompl(IPlayListView iLoginView) {
        this.iLoginView = iLoginView;
        mCreatePlayList = RetrofitClient.getClient().create(PlayList.class);

    }


    @Override
    public void doAddShabads(ArrayList<AddShabadsList> mAddShabads) {

        Call<JsonElement> call = mCreatePlayList.add_playlist(mAddShabads);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                iLoginView.onResult(new Gson().toJson(response.body()), 3);

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }
}
