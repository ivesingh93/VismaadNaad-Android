package com.vismaad.naad.navigation.playlist.model;

/**
 * Created by satnamsingh on 31/05/18.
 */

public interface IPlayList {
    String getName();

    String getPasswd();

    int checkUserValidity(String name, String passwd);
}
