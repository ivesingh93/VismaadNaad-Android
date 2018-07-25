package com.vismaad.naad.welcome.signup.model;

import android.view.Gravity;
import android.widget.Toast;

import com.vismaad.naad.utils.Utils;

/**
 * Created by satnamsingh on 01/06/18.
 */

public class SignupModel implements iSignup {

    String firstname, lastName, email, userID, password, dob, gender;

    public SignupModel(String firstname,
                       String lastName,
                       String email,
                       String userID,
                       String password,
                       String dob,
                       String gender) {
        this.firstname = firstname;
        this.lastName = lastName;
        this.email = email;
        this.userID = userID;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
    }

    @Override
    public int checkUserValidity(String firstname, String
            lastName, String email, String userID,
                                 String password,
                                 String dob,
                                 String gender) {

        if (firstname.equalsIgnoreCase("")) {
            return -1;
        }
        if (lastName.equalsIgnoreCase("")) {
            return -2;
        }
        if (email.equalsIgnoreCase("")) {
            return -3;
        }
        if (!Utils.isValidEmail(email)) {
            return -4;
        }
        if (password.equalsIgnoreCase("")) {
            return -5;
        }
        if (password.length() < 5) {
            return -6;
        }
        if (dob.equalsIgnoreCase("")) {
            return -7;
        }
        if (gender.equalsIgnoreCase("")) {
            return -8;
        }

        return 0;
    }
}
