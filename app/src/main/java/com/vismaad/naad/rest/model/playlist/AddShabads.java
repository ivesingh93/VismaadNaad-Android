package com.vismaad.naad.rest.model.playlist;

import com.google.gson.annotations.SerializedName;

/**
 * Created by satnamsingh on 20/06/18.
 */

public class AddShabads {
    @SerializedName("username")
    private String userName;
    @SerializedName("playlist_name")
    private String playlist_name;

    @SerializedName("id")
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public AddShabads(String userName, String playlist_name, String id) {
        this.userName = userName;
        this.playlist_name = playlist_name;
        this.id = id;
    }
}
