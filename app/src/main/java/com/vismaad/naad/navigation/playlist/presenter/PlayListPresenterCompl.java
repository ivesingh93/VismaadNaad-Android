package com.vismaad.naad.navigation.playlist.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vismaad.naad.navigation.playlist.view.IPlayListView;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.playlist.CreatePlayList;
import com.vismaad.naad.rest.model.user.UserCredentials;
import com.vismaad.naad.rest.service.PlayList;
import com.vismaad.naad.welcome.login.model.IUser;
import com.vismaad.naad.welcome.login.model.UserModel;
import com.vismaad.naad.welcome.login.view.ILoginView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by satnamsingh on 31/05/18.
 */

public class PlayListPresenterCompl implements IPlayListPresenter {

    IPlayListView iLoginView;
    PlayList mCreatePlayList;

    public PlayListPresenterCompl(IPlayListView iLoginView) {
        this.iLoginView = iLoginView;
        mCreatePlayList = RetrofitClient.getClient().create(PlayList.class);

    }


    @Override
    public void doCreatePlayList1(String userName, String playListName) {
        Call<JsonElement> call = mCreatePlayList.create_playlist(new CreatePlayList(userName, playListName));
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
