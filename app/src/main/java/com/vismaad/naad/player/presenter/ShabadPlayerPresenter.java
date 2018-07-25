package com.vismaad.naad.player.presenter;

/**
 * Created by ivesingh on 2/4/18.
 */

public interface ShabadPlayerPresenter {

    void init();

    void prepareTranslation(boolean teeka, boolean punjabi, boolean english);

    void prepareShabad(int startingId, int endingId);

    void changeShabadView(int color_position);

    void setTranslationSize(int size);

}
