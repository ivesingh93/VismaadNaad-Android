package com.vismaad.naad.AddPlayList.view;

import com.vismaad.naad.AddPlayList.model.JBShabadsList;
import com.vismaad.naad.rest.model.raagi.Shabad;

import java.util.ArrayList;

/**
 * Created by satnamsingh on 25/06/18.
 */

public interface IShabadsList {
    public void onResult(ArrayList<Shabad> code, int pageID);
}
