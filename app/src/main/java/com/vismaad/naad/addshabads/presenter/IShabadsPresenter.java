package com.vismaad.naad.addshabads.presenter;

import com.vismaad.naad.addshabads.model.AddShabadsList;
import com.vismaad.naad.rest.model.playlist.AddShabads;

import java.util.ArrayList;

/**
 * Created by satnamsingh on 31/05/18.
 */

public interface IShabadsPresenter {

    void doAddShabads(ArrayList<AddShabadsList> mAddShabads);

}
