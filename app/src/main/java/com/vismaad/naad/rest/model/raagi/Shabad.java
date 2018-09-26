package com.vismaad.naad.rest.model.raagi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivesingh on 1/16/18.
 */

public class Shabad implements Parcelable {

    @SerializedName("shabad_english_title")
    private String shabad_english_title;

    @SerializedName("shabad_length")
    private String shabad_length;

    @SerializedName("sathaayi_id")
    private int sathaayi_id;

    @SerializedName("starting_id")
    private int starting_id;

    @SerializedName("ending_id")
    private int ending_id;

    @SerializedName("raagi_name")
    private String raagi_name;

    @SerializedName("shabad_url")
    private String shabad_url;

    @SerializedName("listeners")
    private int listeners;

    public int getListeners() {
        return listeners;
    }

    public void setListeners(int listeners) {
        this.listeners = listeners;
    }

    @SerializedName("id")
    private String id;

    private List<String> gurmukhiList;
    private List<String> punjabiList;
    private List<String> teekaPadArthList;
    private List<String> teekaArthList;
    private List<String> englishList;
    private int shabadSize;

    public Shabad() {

    }

    protected Shabad(Parcel in) {
        shabad_english_title = in.readString();
        shabad_length = in.readString();
        sathaayi_id = in.readInt();
        starting_id = in.readInt();
        ending_id = in.readInt();
        listeners = in.readInt();
        raagi_name = in.readString();
        shabad_url = in.readString();
        id = in.readString();
    }

    public String getShabadEnglishTitle() {
        return shabad_english_title;
    }

    public String getShabadLength() {
        return shabad_length;
    }

    public int getSathaayiId() {
        return sathaayi_id;
    }

    public int getStartingId() {
        return starting_id;
    }

    public int getEndingId() {
        return ending_id;
    }

    public String getRaagiName() {
        return raagi_name;
    }

    public String getShabadUrl() {
        return shabad_url;
    }

    public String getShabadId() {
        return id;
    }


    public List<String> getGurmukhiList() {
        return gurmukhiList;
    }

    public void setGurmukhiList(List<String> gurmukhiList) {
        this.gurmukhiList = gurmukhiList;
    }

    public List<String> getPunjabiList() {
        return punjabiList;
    }

    public void setPunjabiList(List<String> punjabiList) {
        this.punjabiList = punjabiList;
    }

    public List<String> getTeekaPadArthList() {
        return teekaPadArthList;
    }

    public void setTeekaPadArthList(List<String> teekaPadArthList) {
        this.teekaPadArthList = teekaPadArthList;
    }

    public List<String> getTeekaArthList() {
        return teekaArthList;
    }

    public void setTeekaArthList(List<String> teekaArthList) {
        this.teekaArthList = teekaArthList;
    }

    public List<String> getEnglishList() {
        return englishList;
    }

    public void setEnglishList(List<String> englishList) {
        this.englishList = englishList;
    }

    public int getShabadSize() {
        return shabadSize;
    }

    public void setShabadSize(int shabadSize) {
        this.shabadSize = shabadSize;
    }

    public static final Creator<Shabad> CREATOR = new Creator<Shabad>() {
        @Override
        public Shabad createFromParcel(Parcel in) {
            return new Shabad(in);
        }

        @Override
        public Shabad[] newArray(int size) {
            return new Shabad[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shabad_english_title);
        parcel.writeString(shabad_length);
        parcel.writeInt(sathaayi_id);
        parcel.writeInt(starting_id);
        parcel.writeInt(ending_id);
        parcel.writeInt(listeners);
        parcel.writeString(raagi_name);
        parcel.writeString(shabad_url);
        parcel.writeString(id);
    }

}
