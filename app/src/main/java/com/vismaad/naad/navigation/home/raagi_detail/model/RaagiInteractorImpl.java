package com.vismaad.naad.navigation.home.raagi_detail.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.gson.Gson;
import com.vismaad.naad.navigation.home.raagi_detail.adapter.ShabadAdapter;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.rest.service.RaagiService;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ivesingh on 2/3/18.
 */

public class RaagiInteractorImpl implements RaagiInteractor {

    private Context context;
    private ArrayList<Shabad> shabadList;
    private RaagiService raagiService;
    private ShabadAdapter shabadAdapter;

    public RaagiInteractorImpl(Activity context){
        shabadList = new ArrayList<>();
        shabadAdapter = new ShabadAdapter(context, shabadList);
        raagiService = RetrofitClient.getClient().create(RaagiService.class);
        this.context = context;
    }


    @Override
    public ShabadAdapter fetchShabads(String raagi_name) {
        Call<List<Shabad>> raagiShabadsCall = raagiService.raagi_shabads(raagi_name);

        final ACProgressFlower dialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .bgColor(Color.TRANSPARENT)
                .bgAlpha(0)
                .bgCornerRadius(0)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        raagiShabadsCall.enqueue(new Callback<List<Shabad>>() {
            @Override
            public void onResponse(Call<List<Shabad>> call, Response<List<Shabad>> response) {
                shabadList.clear();
                for(Shabad raagiShabad: response.body()){
                    shabadList.add(raagiShabad);
                }
                shabadAdapter.notifyDataSetChanged();
                dialog.cancel();
            }

            @Override
            public void onFailure(Call<List<Shabad>> call, Throwable t) {
                // TODO - Failed Raagi Shabads Call
            }
        });

        return shabadAdapter;
    }

}
