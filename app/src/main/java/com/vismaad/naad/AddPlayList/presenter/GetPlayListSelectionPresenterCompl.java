package com.vismaad.naad.AddPlayList.presenter;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vismaad.naad.AddPlayList.SelectPlayListShabads;
import com.vismaad.naad.AddPlayList.adapter.ShabadsAdapter;
import com.vismaad.naad.AddPlayList.view.IPlayListSelectionView;
import com.vismaad.naad.navigation.home.raagi_detail.adapter.ShabadAdapter;
import com.vismaad.naad.navigation.playlist.view.IPlayListView;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.rest.service.PlayList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by satnamsingh on 31/05/18.
 */

public class GetPlayListSelectionPresenterCompl implements IGetPlayListSelectionPresenter {

    IPlayListSelectionView iLoginView;
    PlayList mCreatePlayList;
    private ArrayList<Shabad> shabadList;
    private ShabadsAdapter shabadAdapter;

    public GetPlayListSelectionPresenterCompl(IPlayListSelectionView iLoginView) {
        this.iLoginView = iLoginView;
        mCreatePlayList = RetrofitClient.getClient().create(PlayList.class);
        shabadList = new ArrayList<>();
       // shabadAdapter = new ShabadsAdapter(context, shabadList);
    }




    @Override
    public void doFetchList(String userName) {


        Call<List<Shabad>> call = mCreatePlayList.raagi_shabads(userName);
        call.enqueue(new Callback<List<Shabad>>() {
            @Override
            public void onResponse(Call<List<Shabad>> call, Response<List<Shabad>> response) {
                Log.i("fetch_play_list", "" + new Gson().toJson(response.body()));
                shabadList.clear();
                for (Shabad raagiShabad : response.body()) {
                    shabadList.add(raagiShabad);
                }
                //shabadAdapter.notifyDataSetChanged();

                iLoginView.onResult(shabadList, 2);
            }

            @Override
            public void onFailure(Call<List<Shabad>> call, Throwable t) {

            }
        });
    }
}
