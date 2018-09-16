package com.vismaad.naad.rest.model.raagi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShabadTutorial {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("harmonium_scale")
    @Expose
    private String harmoniumScale;
    @SerializedName("name")
    @Expose
    private String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHarmoniumScale() {
        return harmoniumScale;
    }

    public void setHarmoniumScale(String harmoniumScale) {
        this.harmoniumScale = harmoniumScale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
