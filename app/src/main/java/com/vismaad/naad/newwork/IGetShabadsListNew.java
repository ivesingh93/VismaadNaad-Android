package com.vismaad.naad.newwork;

import com.vismaad.naad.addPlayList.presenter.IGetShabadsInterface;
import com.vismaad.naad.addPlayList.view.IShabadsList;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.rest.service.RaagiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IGetShabadsListNew implements IGetShabadsInterface {
    RaagiService mCreatePlayList;
    private ArrayList<Shabad> shabadList;
    //JBShabadsList iLoginView;
    IShabadsList mIShabadsList;

    public IGetShabadsListNew(IShabadsList mIShabadsList) {
        this.mIShabadsList = mIShabadsList;
        mCreatePlayList = RetrofitClient.getClient().create(RaagiService.class);
        shabadList = new ArrayList<>();
        // shabadAdapter = new ShabadsAdapter(context, shabadList);
    }

    @Override
    public void getList(String username, String playlistName) {
        Call<List<Shabad>> call = mCreatePlayList.getPlayListShabads();
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

