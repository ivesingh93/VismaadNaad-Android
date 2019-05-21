package com.vismaad.naad.AddPlayList.model;

import com.google.gson.annotations.SerializedName;
import com.vismaad.naad.rest.model.playlist.JBPopularShabads;
import com.vismaad.naad.rest.model.raagi.RaagiInfo;

import java.util.List;

public class JBAllInfo {
    @SerializedName("popularShabads")
    private List<JBPopularShabads> popularList;
    @SerializedName("recentlyAddedShabads")
    private List<JBRecentAdded> recentList;


    @SerializedName("raagisInfo")
    private List<RaagiInfo> ragiInfoList;


    public List<JBPopularShabads> getPopularList() {
        return popularList;
    }

    public List<JBRecentAdded> getRecentList() {
        return recentList;
    }

    public List<RaagiInfo> getRagiInfoList() {
        return ragiInfoList;
    }


}
