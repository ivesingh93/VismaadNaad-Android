package com.vismaad.naad.addPlayList.view;

import com.vismaad.naad.rest.model.raagi.Shabad;

import java.util.ArrayList;

/**
 * Created by satnamsingh on 31/05/18.
 */

public interface IPlayListSelectionView {

    public void onResult(ArrayList<Shabad> code, int pageID);

}
