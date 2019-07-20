package com.vismaad.naad.navigation.home.presenter;

import android.app.Activity;
import android.view.View;

import com.vismaad.naad.navigation.home.model.HomeInteractor;
import com.vismaad.naad.navigation.home.model.HomeInteractorImpl;
import com.vismaad.naad.navigation.home.adapter.RaagiInfoAdapter;
import com.vismaad.naad.navigation.home.view.HomeView;

/**
 * Created by ivesingh on 2/2/18.
 */

public class HomePresenterImpl implements HomePresenter{

    private HomeView homeView;
    private HomeInteractor homeInteractor;

    public HomePresenterImpl(HomeView homeView, Activity context){
        this.homeView = homeView;
        homeInteractor = new HomeInteractorImpl(context);
    }

    @Override
    public void init(View view) {
        homeView.init(view);
    }

    @Override
    public void prepareRaagis(String status) {
        RaagiInfoAdapter raagiInfoAdapter = homeInteractor.fetchRaagis(status);
        homeView.showRaagis(raagiInfoAdapter);
    }
}
