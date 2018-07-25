package com.vismaad.naad.rest.model.playlist;

import com.google.gson.annotations.SerializedName;

/**
 * Created by satnamsingh on 14/06/18.
 */

public class FetchPlayList {

    @SerializedName("username")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public FetchPlayList(String userName) {
        this.userName = userName;

    }

}
