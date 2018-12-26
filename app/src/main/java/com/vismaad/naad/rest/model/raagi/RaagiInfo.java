package com.vismaad.naad.rest.model.raagi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ivesingh on 1/12/18.
 */


public class RaagiInfo {

    @SerializedName("raagi_id")
    private int raagi_id;

    @SerializedName("raagi_name")
    private String raagi_name;

    @SerializedName("shabads_count")
    private int shabads_count;

    @SerializedName("minutes_of_shabads")
    private int minutes_of_shabads;

    @SerializedName("raagi_image_url")
    private String raagi_image_url;

    public int getRaagiId() {
        return raagi_id;
    }

    public String getRaagiName() {
        return raagi_name;
    }

    public int getShabadsCount() {
        return shabads_count;
    }

    public int getMinutesOfShabads() {
        return minutes_of_shabads;
    }

    public String getRaagiImageURL() {
        return raagi_image_url;
    }

}
