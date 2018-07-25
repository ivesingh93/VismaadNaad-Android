package com.vismaad.naad.addshabads.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vismaad.naad.AddPlayList.view.IShabadsSelected;
import com.vismaad.naad.addshabads.model.AddShabadsList;
import com.vismaad.naad.addshabads.view.IPlayListView;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.playlist.AddShabads;
import com.vismaad.naad.rest.model.playlist.CreatePlayList;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.rest.service.PlayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                Log.i("create_playy", "" + new Gson().toJson(response.body()));
                iLoginView.onResult(new Gson().toJson(response.body()), 3);

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }
}
