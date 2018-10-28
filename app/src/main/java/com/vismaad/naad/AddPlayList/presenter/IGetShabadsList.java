package com.vismaad.naad.AddPlayList.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vismaad.naad.AddPlayList.model.JBShabadsList;
import com.vismaad.naad.AddPlayList.view.IPlayListSelectionView;
import com.vismaad.naad.AddPlayList.view.IShabadsList;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.rest.service.PlayList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by satnamsingh on 25/06/18.
 */

public class IGetShabadsList implements IGetShabadsInterface {
    PlayList mCreatePlayList;
    private ArrayList<Shabad> shabadList;
    //JBShabadsList iLoginView;
    IShabadsList mIShabadsList;

    public IGetShabadsList(IShabadsList mIShabadsList) {
        this.mIShabadsList = mIShabadsList;
        mCreatePlayList = RetrofitClient.getClient().create(PlayList.class);
        shabadList = new ArrayList<>();
        // shabadAdapter = new ShabadsAdapter(context, shabadList);
    }

    @Override
    public void getList(String username, String playlistName) {
        Call<List<Shabad>> call = mCreatePlayList.getPlayListShabads(username, playlistName);
        shabadList.clear();
        call.enqueue(new Callback<List<Shabad>>() {
            @Override
            public void onResponse(Call<List<Shabad>> call, Response<List<Shabad>> response) {
                for (Shabad raagiShabad : response.body()) {
                    shabadList.add(raagiShabad);
                }

                mIShabadsList.onResult(shabadList, 1);
            }

            @Override
            public void onFailure(Call<List<Shabad>> call, Throwable t) {

            }
        });
    }
}
