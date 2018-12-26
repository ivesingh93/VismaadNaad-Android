package com.vismaad.naad.navigation.home.raagi_detail.presenter;

import android.app.Activity;

import com.vismaad.naad.navigation.home.raagi_detail.view.RaagiView;
import com.vismaad.naad.navigation.home.raagi_detail.adapter.ShabadAdapter;
import com.vismaad.naad.navigation.home.raagi_detail.model.RaagiInteractor;
import com.vismaad.naad.navigation.home.raagi_detail.model.RaagiInteractorImpl;

/**
 * Created by ivesingh on 2/3/18.
 */

public class RaagiPresenterImpl implements RaagiPresenter {

    private RaagiView raagiView;
    private RaagiInteractor raagiInteractor;

    public RaagiPresenterImpl(RaagiView raagiView, Activity context){
        this.raagiView = raagiView;
        this.raagiInteractor = new RaagiInteractorImpl(context);
    }

    @Override
    public void init() {
        raagiView.init();
    }

    @Override
    public void prepareRaagiInfo() {
        raagiView.showRaagiInfo();
    }

    @Override
    public void prepareShabads(String raagi_name) {
        ShabadAdapter shabadAdapter = raagiInteractor.fetchShabads(raagi_name);
        raagiView.showShabads(shabadAdapter);
    }
}
