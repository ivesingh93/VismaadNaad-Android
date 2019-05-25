package com.vismaad.naad.newwork;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vismaad.naad.rest.model.raagi.Shabad;

import java.util.ArrayList;
import java.util.List;

public class PopRagiAndShabad    {


        @SerializedName("popularShabads")
        @Expose
        public List<PopularShabad> popularShabads = new ArrayList<>();

        @SerializedName("raagisInfo")
        @Expose
        public List<RaagisInfo> raagisInfo = new ArrayList<>();


    @SerializedName("radioChannels")
    @Expose
    public List<RadioChannels> radioChannels = new ArrayList<>();




    public List<PopularShabad> getPopularShabads() {
        return popularShabads;
    }

    public List<RaagisInfo> getRaagisInfo() {
        return raagisInfo;
    }


    public List<RadioChannels> getRadioChannels() {
        return radioChannels;
    }



    public static class PopularShabad  implements Parcelable {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("listeners")
        @Expose
        public Integer listeners;
        @SerializedName("shabad_english_title")
        @Expose
        public String shabadEnglishTitle;
        @SerializedName("sathaayi_id")
        @Expose
        public Integer sathaayiId;
        @SerializedName("starting_id")
        @Expose
        public Integer startingId;
        @SerializedName("ending_id")
        @Expose
        public Integer endingId;
        @SerializedName("raagi_name")
        @Expose
        public String raagiName;
        @SerializedName("image_url")
        @Expose
        public String imageUrl;
        @SerializedName("shabad_url")
        @Expose
        public String shabadUrl;
        @SerializedName("shabad_length")
        @Expose
        public String shabadLength;

        public String getId() {
            return id;
        }

        public Integer getListeners() {
            return listeners;
        }

        public String getShabadEnglishTitle() {
            return shabadEnglishTitle;
        }

        public Integer getSathaayiId() {
            return sathaayiId;
        }

        public Integer getStartingId() {
            return startingId;
        }

        public Integer getEndingId() {
            return endingId;
        }

        public String getRaagiName() {
            return raagiName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getShabadUrl() {
            return shabadUrl;
        }

        public String getShabadLength() {
            return shabadLength;
        }

        public PopularShabad() {

        }

        protected PopularShabad(Parcel in) {
            shabadEnglishTitle = in.readString();
            shabadLength = in.readString();
            sathaayiId = in.readInt();
            startingId = in.readInt();
            endingId = in.readInt();
            listeners = in.readInt();
            raagiName = in.readString();
            shabadUrl = in.readString();
            imageUrl = in.readString();
            id = in.readString();
        }

        public static final Creator<PopularShabad> CREATOR = new Creator<PopularShabad>() {
            @Override
            public PopularShabad createFromParcel(Parcel in) {
                return new PopularShabad(in);
            }

            @Override
            public PopularShabad[] newArray(int size) {
                return new PopularShabad[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(shabadEnglishTitle);
            parcel.writeString(shabadLength);
            parcel.writeInt(sathaayiId);
            parcel.writeInt(startingId);
            parcel.writeInt(endingId);
            parcel.writeInt(listeners);
            parcel.writeString(raagiName);
            parcel.writeString(shabadUrl);
            parcel.writeString(imageUrl);
            parcel.writeString(id);
        }
    }

    public class RaagisInfo {

        @SerializedName("raagi_id")
        @Expose
        public Integer raagiId;
        @SerializedName("raagi_name")
        @Expose
        public String raagiName;
        @SerializedName("raagi_image_url")
        @Expose
        public String raagiImageUrl;
        @SerializedName("shabads_count")
        @Expose
        public Integer shabadsCount;
        @SerializedName("minutes_of_shabads")
        @Expose
        public Integer minutesOfShabads;

        public Integer getRaagiId() {
            return raagiId;
        }

        public String getRaagiName() {
            return raagiName;
        }

        public String getRaagiImageUrl() {
            return raagiImageUrl;
        }

        public Integer getShabadsCount() {
            return shabadsCount;
        }

        public Integer getMinutesOfShabads() {
            return minutesOfShabads;
        }
    }


    public class RadioChannels {

        @SerializedName("id")
        @Expose
        public Integer Id;

        @SerializedName("name")
        @Expose
        public String Name;


        @SerializedName("link")
        @Expose
        public String Link;


        @SerializedName("image_url")
        @Expose
        public String ImageURL;



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


    }


}
