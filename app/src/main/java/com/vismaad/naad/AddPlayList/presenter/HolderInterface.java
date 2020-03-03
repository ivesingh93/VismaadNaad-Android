package com.vismaad.naad.addPlayList.presenter;

import android.widget.TextView;

import com.vismaad.naad.rest.model.raagi.Shabad;

import java.util.ArrayList;

/**
 * Created by satnamsingh on 10/07/18.
 */

public interface HolderInterface {
    public void onResultget(ArrayList<Shabad> code, int pageID, TextView mTextView);
}
