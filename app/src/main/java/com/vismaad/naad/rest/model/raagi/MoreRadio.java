package com.vismaad.naad.rest.model.raagi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoreRadio implements Parcelable {
    @SerializedName("id")
    public Integer Id;

    @SerializedName("name")
    public String Name;


    @SerializedName("link")
    public String Link;


    @SerializedName("image_url")
    public String ImageURL;

    protected MoreRadio(Parcel in) {
        Link = in.readString();
        Name = in.readString();
        Id = in.readInt();
        ImageURL = in.readString();

    }

    public MoreRadio() {

    }
    public static final Creator<MoreRadio> CREATOR = new Creator<MoreRadio>() {
        @Override
        public MoreRadio createFromParcel(Parcel in) {
            return new MoreRadio(in);
        }

        @Override
        public MoreRadio[] newArray(int size) {
            return new MoreRadio[size];
        }
    };


    public Integer getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getLink() {
        return Link;
    }

    public String getImageUrl() {
        return ImageURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Name);
        parcel.writeString(Link);
        parcel.writeInt(Id);
        parcel.writeString(ImageURL);
    }
}
