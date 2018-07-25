package com.vismaad.naad.rest.model.sggs;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ivesingh on 1/19/18.
 */

public class ShabadInfo {

    @SerializedName("Gurmukhi")
    private String gurmukhi;

    @SerializedName("Punjabi")
    private String punjabi;

    @SerializedName("Teeka_Pad_Arth")
    private String teeka_pad_arth;

    @SerializedName("Teeka_Arth")
    private String teeka_arth;

    @SerializedName("English")
    private String english;

    @SerializedName("Ang")
    private int ang;

    @SerializedName("Author")
    private String author;

    @SerializedName("Raag")
    private String raag;

    public String getGurmukhi() {
        return gurmukhi;
    }

    public void setGurmukhi(String gurmukhi) {
        this.gurmukhi = gurmukhi;
    }

    public String getPunjabi() {
        return punjabi;
    }

    public void setPunjabi(String punjabi) {
        this.punjabi = punjabi;
    }

    public String getTeeka_pad_arth() {
        return teeka_pad_arth;
    }

    public void setTeeka_pad_arth(String teeka_pad_arth) {
        this.teeka_pad_arth = teeka_pad_arth;
    }

    public String getTeeka_arth() {
        return teeka_arth;
    }

    public void setTeeka_arth(String teeka_arth) {
        this.teeka_arth = teeka_arth;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public int getAng() {
        return ang;
    }

    public void setAng(int ang) {
        this.ang = ang;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRaag() {
        return raag;
    }

    public void setRaag(String raag) {
        this.raag = raag;
    }
}
