package com.vismaad.naad.player.model;

import android.app.Activity;

import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.instance.RetrofitClientShabads;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.rest.model.sggs.ShabadInfo;
import com.vismaad.naad.rest.service.ShabadService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ivesingh on 2/4/18.
 */

public class ShabadPlayerInteractorImpl implements ShabadPlayerInteractor {

    private ShabadService shabadService;
    private Activity context;

    public ShabadPlayerInteractorImpl(Activity context){
        this.context = context;
        shabadService = RetrofitClient.getClient().create(ShabadService.class);
    }

    @Override
    public void fetchShabad(int startingId, int endingId, final onFetchFinishedListener listener) {
        final Shabad shabad = new Shabad();
        Call<List<ShabadInfo>> shabadInfoCall = shabadService.shabad_info(startingId, endingId);
        shabadInfoCall.enqueue(new Callback<List<ShabadInfo>>() {
            @Override
            public void onResponse(Call<List<ShabadInfo>> call, Response<List<ShabadInfo>> response) {
                shabad.setShabadSize(response.body().size());

                List<String> gurmukhi = new ArrayList<>();
                List<String> punjabi = new ArrayList<>();
                List<String> teeka_pad_arth = new ArrayList<>();
                List<String> teeka_arth = new ArrayList<>();
                List<String> english = new ArrayList<>();

                for(ShabadInfo shabadInfo: response.body()){
                    gurmukhi.add(shabadInfo.getGurmukhi());
                    punjabi.add(shabadInfo.getPunjabi());
                    teeka_pad_arth.add(shabadInfo.getTeeka_pad_arth());
                    teeka_arth.add(shabadInfo.getTeeka_arth());
                    english.add(shabadInfo.getEnglish());
                }

                shabad.setGurmukhiList(gurmukhi);
                shabad.setPunjabiList(punjabi);
                shabad.setTeekaPadArthList(teeka_pad_arth);
                shabad.setTeekaArthList(teeka_arth);
                shabad.setEnglishList(english);

                listener.onShabadFetched(shabad);

            }

            @Override
            public void onFailure(Call<List<ShabadInfo>> call, Throwable t) {
                //TODO - Shabad Info on Failed
            }
        });


    }
}
