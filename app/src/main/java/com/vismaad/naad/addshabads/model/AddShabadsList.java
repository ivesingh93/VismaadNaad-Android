package com.vismaad.naad.addshabads.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddShabadsList {

    @SerializedName("username")
    @Expose
    private String userName;
    @SerializedName("playlist_name")
    @Expose
    private String playlist_name;

    @SerializedName("id")
    @Expose
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

    public AddShabadsList(String userName, String playlist_name, String id) {
        this.userName = userName;
        this.playlist_name = playlist_name;
        this.id = id;
    }

    public AddShabadsList( ) {

    }
}
