package com.vismaad.naad.welcome.login.model;

/**
 * Created by satnamsingh on 31/05/18.
 */

public interface IUser {
    String getName();

    String getPasswd();

    int checkUserValidity(String name, String passwd);
}
