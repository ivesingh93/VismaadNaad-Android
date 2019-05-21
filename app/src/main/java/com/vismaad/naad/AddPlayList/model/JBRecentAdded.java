package com.vismaad.naad.AddPlayList.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JBRecentAdded {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("raagi_name")
    @Expose
    private String raagi_name;


    @SerializedName("image_url")
    @Expose
    private String image_url;


    @SerializedName("shabad_english_title")
    @Expose
    private String shabad_english_title;


    @SerializedName("sathaayi_id")
    @Expose
    private String sathaayi_id;


    @SerializedName("starting_id")
    @Expose
    private String starting_id;


    @SerializedName("ending_id")
    @Expose
    private String ending_id;
    @SerializedName("shabad_url")
    @Expose
    private String shabad_url;


    @SerializedName("shabad_length")
    @Expose
    private String shabad_length;
    public String getId() {
        return id;
    }

    public String getRaagi_name() {
        return raagi_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getShabad_english_title() {
        return shabad_english_title;
    }

    public String getSathaayi_id() {
        return sathaayi_id;
    }

    public String getStarting_id() {
        return starting_id;
    }

    public String getEnding_id() {
        return ending_id;
    }

    public String getShabad_url() {
        return shabad_url;
    }

    public String getShabad_length() {
        return shabad_length;
    }


}
