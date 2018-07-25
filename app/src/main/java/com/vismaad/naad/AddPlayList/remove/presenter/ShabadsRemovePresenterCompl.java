package com.vismaad.naad.AddPlayList.remove.presenter;

import android.util.Log;

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
 * Created by satnamsingh on 25/06/18.
 */

public class ShabadsRemovePresenterCompl implements IShabadsRemovePresenter {
    IPlayListView iLoginView;
    PlayList mCreatePlayList;

    public ShabadsRemovePresenterCompl(IPlayListView iLoginView) {
        this.iLoginView = iLoginView;
        mCreatePlayList = RetrofitClient.getClient().create(PlayList.class);
    }

    @Override
    public void removeShabads(ArrayList<AddShabadsList> mAddShabads) {
        Call<JsonElement> call = mCreatePlayList.remove_playlist(mAddShabads);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.i("remove-shabads", "" + new Gson().toJson(response.body()));
                iLoginView.onResult(new Gson().toJson(response.body()), 1);

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }
}
