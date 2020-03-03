package com.vismaad.naad.addPlayList.presenter;

import com.vismaad.naad.addPlayList.adapter.ShabadsAdapter;
import com.vismaad.naad.addPlayList.view.IPlayListSelectionView;
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
                shabadList.clear();
                for (Shabad raagiShabad : response.body()) {
                    shabadList.add(raagiShabad);
                }

                iLoginView.onResult(shabadList, 2);
            }

            @Override
            public void onFailure(Call<List<Shabad>> call, Throwable t) {

            }
        });
    }
}
