package com.vismaad.naad.rest.model.playlist;

import com.google.gson.annotations.SerializedName;

/**
 * Created by satnamsingh on 14/06/18.
 */

public class CreatePlayList {

    @SerializedName("username")
    private String userName;

    @SerializedName("playlist_name")
    private String playlist_name;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPlaylist_name() {
        return playlist_name;
    }

    public void setPlaylist_name(String playlist_name) {
        this.playlist_name = playlist_name;
    }

    public CreatePlayList(String userName, String playlist_name) {
        this.userName = userName;
        this.playlist_name = playlist_name;
    }
}
