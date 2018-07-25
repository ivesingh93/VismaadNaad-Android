package com.vismaad.naad.welcome.signup.model;

/**
 * Created by satnamsingh on 31/05/18.
 */

public interface iSignup {
    int checkUserValidity(String firstname, String lastName,
                          String email, String userID,
                          String password, String dob,
                          String gender);
}
