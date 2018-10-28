package com.vismaad.naad.rest.model.playlist;

import com.google.gson.annotations.SerializedName;

public class JBPopularShabads {
    @SerializedName("id")
    private int id;

    @SerializedName("listeners")
    private int listeners;

    public String getSathaayi_title() {
        return sathaayi_title;
    }

    public void setSathaayi_title(String sathaayi_title) {
        this.sathaayi_title = sathaayi_title;
    }

    @SerializedName("sathaayi_title")
    private String sathaayi_title;

    @SerializedName("sathaayi_id")
    private int sathaayi_id;

    @SerializedName("starting_id")
    private int starting_id;

    @SerializedName("ending_id")
    private int ending_id;

    @SerializedName("name")
    private String name;

    @SerializedName("image_url")
    private String image_url;


    public int getId() {
        return id;
    }

    public int getListeners() {
        return listeners;
    }

    public int getSathaayi_id() {
        return sathaayi_id;
    }

    public int getStarting_id() {
        return starting_id;
    }

    public int getEnding_id() {
        return ending_id;
    }

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }


}
