package com.vismaad.naad.player.presenter;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

import com.vismaad.naad.Constants;
import com.vismaad.naad.R;
import com.vismaad.naad.player.model.ShabadPlayerInteractor;
import com.vismaad.naad.player.model.ShabadPlayerInteractorImpl;
import com.vismaad.naad.player.service.App;
import com.vismaad.naad.player.view.ShabadPlayerView;
import com.vismaad.naad.rest.model.raagi.Shabad;

/**
 * Created by ivesingh on 2/4/18.
 */

public class ShabadPlayerPresenterImpl implements ShabadPlayerPresenter, ShabadPlayerInteractor.onFetchFinishedListener {

    private ShabadPlayerView shabadPlayerView;
    private ShabadPlayerInteractor shabadPlayerInteractor;

    public ShabadPlayerPresenterImpl(ShabadPlayerView shabadPlayerView, Activity context) {
        this.shabadPlayerView = shabadPlayerView;
        this.shabadPlayerInteractor = new ShabadPlayerInteractorImpl(context);
    }

    @Override
    public void init() {
        shabadPlayerView.getIntentValues();
        shabadPlayerView.initUI();
        shabadPlayerView.showCustomAppbar();
        shabadPlayerView.generateShabadsData();
        shabadPlayerView.initPlayer();
    }

    @Override
    public void prepareTranslation(boolean teeka, boolean punjabi, boolean english) {

        if (!teeka && !punjabi && !english) {
            shabadPlayerView.showGurmukhi();
        } else if (teeka && !punjabi && !english) {
            shabadPlayerView.showGurmukhiTeeka();
        } else if (!teeka && punjabi && !english) {
            shabadPlayerView.showGurmukhiPunjabi();
        } else if (!teeka && !punjabi && english) {
            shabadPlayerView.showGurmukhiEnglish();
        } else if (teeka && punjabi && !english) {
            shabadPlayerView.showGurmukhiTeekaPunjabi();
        } else if (teeka && !punjabi && english) {
            shabadPlayerView.showGurmukhiTeekaEnglish();
        } else if (!teeka && punjabi && english) {
            shabadPlayerView.showGurmukhiPunjabiEnglish();
        } else if (teeka && punjabi && english) {
            shabadPlayerView.showGurmukhiTeekaPunjabiEnglish();
        }
    }

    @Override
    public void prepareShabad(int startingId, int endingId) {
        shabadPlayerInteractor.fetchShabad(startingId, endingId, this);
    }

    @Override
    public void onShabadFetched(Shabad fetched_shabad) {
        shabadPlayerView.setFetchedShabadValues(fetched_shabad);
        prepareTranslation(App.getPrefranceDataBoolean(Constants.TEEKA_CB),
                App.getPrefranceDataBoolean(Constants.PUNJABI_CB),
                App.getPrefranceDataBoolean(Constants.ENGLISH_CB));
    }

    @Override
    public void changeShabadView(int color_position) {
        int color_id = 0;
        if (color_position == 0) {
            color_id = R.color.white;
        } else if (color_position == 1) {
            color_id = R.color.light_blue;
        } else if (color_position == 2) {
            color_id = R.color.sepia;
        } else if (color_position == 3) {
            color_id = R.color.green;
        }

        shabadPlayerView.changeShabadColor(color_id);
    }

    @Override
    public void setTranslationSize(int size) {
        if (size >= 16 && size <= 25)
            shabadPlayerView.changeTranlationSize(size);
        else
            shabadPlayerView.changeTranlationSize(20);
    }
}
