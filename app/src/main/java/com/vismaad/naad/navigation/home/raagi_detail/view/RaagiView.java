package com.vismaad.naad.navigation.home.raagi_detail.view;

import com.vismaad.naad.navigation.home.raagi_detail.adapter.ShabadAdapter;

/**
 * Created by ivesingh on 2/3/18.
 */

public interface RaagiView {

    void init();

    void showCustomTitleBar();

    void showRaagiInfo();

    void showShabads(ShabadAdapter shabadAdapter);


}
