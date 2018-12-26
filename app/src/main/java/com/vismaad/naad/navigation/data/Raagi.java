package com.vismaad.naad.navigation.data;

import com.vismaad.naad.rest.model.raagi.Shabad;

import java.util.List;

/**
 * Created by ivesingh on 1/18/18.
 */

public class Raagi {

    private String raagiImageURL;
    private String raagiName;
    private int numOfShabads;
    private int totalShabadsLength;


    public Raagi(){

    }

    public Raagi(String raagiImageURL, String raagiName, int numOfShabads, int totalShabadsLength){
        this.raagiImageURL = raagiImageURL;
        this.raagiName = raagiName;
        this.numOfShabads = numOfShabads;
        this.totalShabadsLength = totalShabadsLength;
    }

    public String getRaagiImageURL(){
        return getRaagiImageURL();
    }

    public void setRaagiImageURL(String raagiImageURL){
        this.raagiImageURL = raagiImageURL;
    }

    public String getRaagiName() {
        return raagiName;
    }

    public void setRaagiName(String raagiName) {
        this.raagiName = raagiName;
    }

    public int getNumOfShabads() {
        return numOfShabads;
    }

    public void setNumOfShabads(int numOfShabads) {
        this.numOfShabads = numOfShabads;
    }

    public int getTotalShabadsLength() {
        return totalShabadsLength;
    }

    public void setTotalShabadsLength(int totalShabadsLength) {
        this.totalShabadsLength = totalShabadsLength;
    }

}
