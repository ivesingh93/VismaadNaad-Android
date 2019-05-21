package com.vismaad.naad.rest.model.playlist;

import com.google.gson.annotations.SerializedName;

public class LikeShabad {
    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String username;

    public LikeShabad(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
