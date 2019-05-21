package com.vismaad.naad.AddPlayList.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by satnamsingh on 25/06/18.
 */

public class JBShabadsList implements Parcelable {

    /**
     * id : 313
     * raagi_name : Bhai Dalbir Singh Jee
     * shabad_english_title : Aayeo Sunan Parhan Ko Baanee
     * to_char : 09:17
     * recording_title : Bhai Dalbir Singh Jee Keertan Duty 22-11-17
     * sathaayi_id : 52501
     * starting_id : 52500
     * ending_id : 52506
     */
    @SerializedName("id")
    private int id;
    @SerializedName("raagi_name")
    private String raagi_name;
    @SerializedName("shabad_english_title")
    private String shabad_english_title;
    @SerializedName("to_char")
    private String to_char;
    @SerializedName("recording_title")
    private String recording_title;
    @SerializedName("sathaayi_id")
    private int sathaayi_id;
    @SerializedName("starting_id")
    private int starting_id;
    @SerializedName("ending_id")
    private int ending_id;

    public static final Creator<JBShabadsList> CREATOR = new Creator<JBShabadsList>() {
        @Override
        public JBShabadsList createFromParcel(Parcel in) {
            return new JBShabadsList(in);
        }

        @Override
        public JBShabadsList[] newArray(int size) {
            return new JBShabadsList[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRaagi_name() {
        return raagi_name;
    }

    public void setRaagi_name(String raagi_name) {
        this.raagi_name = raagi_name;
    }

    public String getShabad_english_title() {
        return shabad_english_title;
    }

    public void setShabad_english_title(String shabad_english_title) {
        this.shabad_english_title = shabad_english_title;
    }

    public String getTo_char() {
        return to_char;
    }

    public void setTo_char(String to_char) {
        this.to_char = to_char;
    }

    public String getRecording_title() {
        return recording_title;
    }

    public void setRecording_title(String recording_title) {
        this.recording_title = recording_title;
    }

    public int getSathaayi_id() {
        return sathaayi_id;
    }

    public void setSathaayi_id(int sathaayi_id) {
        this.sathaayi_id = sathaayi_id;
    }

    public int getStarting_id() {
        return starting_id;
    }

    public void setStarting_id(int starting_id) {
        this.starting_id = starting_id;
    }

    public int getEnding_id() {
        return ending_id;
    }

    public void setEnding_id(int ending_id) {
        this.ending_id = ending_id;
    }

    public JBShabadsList( ) {

    }

    protected JBShabadsList(Parcel in) {
        shabad_english_title = in.readString();
        to_char = in.readString();
        sathaayi_id = in.readInt();
        starting_id = in.readInt();
        ending_id = in.readInt();
        raagi_name = in.readString();
        recording_title = in.readString();
        id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shabad_english_title);
        parcel.writeString(to_char);
        parcel.writeInt(sathaayi_id);
        parcel.writeInt(starting_id);
        parcel.writeInt(ending_id);
        parcel.writeString(raagi_name);
        parcel.writeString(recording_title);
        parcel.writeInt(id);
    }
}
