package com.vismaad.naad.rest.model.playlist;

import com.google.gson.annotations.SerializedName;

public class ShabadListener {
    @SerializedName("id")
    private int id;

    public ShabadListener(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
