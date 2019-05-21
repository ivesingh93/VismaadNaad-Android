package com.vismaad.naad.player.view;

import com.vismaad.naad.rest.model.raagi.Shabad;

/**
 * Created by ivesingh on 2/4/18.
 */

public interface ShabadPlayerView {

    void getIntentValues();

    void initUI();

    void showCustomAppbar();

    void generateShabadsData();

    void initPlayer();

    void setFetchedShabadValues(Shabad fetched_shabad);

    void changeShabadColor(int color);

    void showGurmukhi();

    void showGurmukhiTeeka();

    void showGurmukhiPunjabi();

    void showGurmukhiEnglish();

    void showGurmukhiTeekaPunjabi();

    void showGurmukhiTeekaEnglish();

    void showGurmukhiPunjabiEnglish();

    void showGurmukhiTeekaPunjabiEnglish();

    void changeTranlationSize(int size);

}
