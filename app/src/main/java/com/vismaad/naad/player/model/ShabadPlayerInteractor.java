package com.vismaad.naad.player.model;

import com.vismaad.naad.rest.model.raagi.Shabad;

/**
 * Created by ivesingh on 2/4/18.
 */

public interface ShabadPlayerInteractor {

    interface onFetchFinishedListener{
        void onShabadFetched(Shabad fetchedShabad);
    }

    void fetchShabad(int startingId, int endingId, onFetchFinishedListener listener);

}
