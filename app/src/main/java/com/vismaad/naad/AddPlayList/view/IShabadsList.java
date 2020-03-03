package com.vismaad.naad.addPlayList.view;

import com.vismaad.naad.rest.model.raagi.Shabad;

import java.util.ArrayList;

/**
 * Created by satnamsingh on 25/06/18.
 */

public interface IShabadsList {
    public void onResult(ArrayList<Shabad> code, int pageID);
}
