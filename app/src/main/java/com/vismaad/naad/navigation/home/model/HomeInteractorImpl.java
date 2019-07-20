package com.vismaad.naad.navigation.home.model;

import android.app.Activity;

import com.vismaad.naad.navigation.home.adapter.RaagiInfoAdapter;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.raagi.RaagiInfo;
import com.vismaad.naad.rest.service.RaagiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ivesingh on 2/2/18.
 */

public class HomeInteractorImpl implements HomeInteractor {

    private List<RaagiInfo> raagiInfoList;
    private RaagiService raagiService;
    private RaagiInfoAdapter raagiInfoAdapter;
    String status;

    public HomeInteractorImpl(Activity context,String status) {
        raagiInfoList = new ArrayList<>();
        raagiInfoAdapter = new RaagiInfoAdapter(context, raagiInfoList,status);
        raagiService = RetrofitClient.getClient().create(RaagiService.class);
    }

    @Override
    public RaagiInfoAdapter fetchRaagis(String status) {
        Call<List<RaagiInfo>> raagiInfoCall = null;
        this.status=status;
        if (status.equalsIgnoreCase("RAGGI")) {
            raagiInfoCall = raagiService.raagi_info();
        } else {
            if (status.equalsIgnoreCase("KATHA")) {
                raagiInfoCall = raagiService.morekathavaachaksInfo();
            }
        }
        raagiInfoCall.enqueue(new Callback<List<RaagiInfo>>() {
            @Override
            public void onResponse(Call<List<RaagiInfo>> call, Response<List<RaagiInfo>> response) {
                raagiInfoList.clear();
                for (RaagiInfo raagiInfo : response.body()) {
                    raagiInfoList.add(raagiInfo);
                }
                raagiInfoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<RaagiInfo>> call, Throwable t) {
                // TODO - Failed Raagi Info Call
            }
        });

        return raagiInfoAdapter;
    }


}
