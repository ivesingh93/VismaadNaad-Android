package com.vismaad.naad.welcome.signup.presenter;

/**
 * Created by satnamsingh on 31/05/18.
 */

public interface ISignupPresenter {
    void doSignup(String firstname, String lastName,
                  String email, String userID,
                  String password, String dob,
                  String gender,String type);

    void setProgressBarVisiblity(int visiblity);
}
