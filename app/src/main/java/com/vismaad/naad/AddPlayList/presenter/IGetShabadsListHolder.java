package com.vismaad.naad.addPlayList.presenter;

import android.widget.TextView;

import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.rest.service.PlayList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by satnamsingh on 10/07/18.
 */

public class IGetShabadsListHolder implements IGetShabadsInterfaceHolder {
    PlayList mCreatePlayList;
    private ArrayList<Shabad> shabadList;
    HolderInterface mIShabadsList;

    public IGetShabadsListHolder(HolderInterface mIShabadsList) {
        this.mIShabadsList = mIShabadsList;
        mCreatePlayList = RetrofitClient.getClient().create(PlayList.class);
        shabadList = new ArrayList<>();
    }

    @Override
    public void getListholder(String username, String playlistName, final TextView mTextView) {
        Call<List<Shabad>> call = mCreatePlayList.getPlayListShabads(username, playlistName);
        shabadList.clear();
        call.enqueue(new Callback<List<Shabad>>() {
            @Override
            public void onResponse(Call<List<Shabad>> call, Response<List<Shabad>> response) {
                for (Shabad raagiShabad : response.body()) {
                    shabadList.add(raagiShabad);
                }

                mIShabadsList.onResultget(shabadList, 1,mTextView);
            }

            @Override
            public void onFailure(Call<List<Shabad>> call, Throwable t) {

            }
        });
    }
}
