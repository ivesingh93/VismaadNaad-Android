package com.vismaad.naad.welcome.login.model;

import com.vismaad.naad.utils.Utils;

/**
 * Created by satnamsingh on 31/05/18.
 */

public class UserModel implements IUser {
    String name;
    String passwd;

    public UserModel(String name, String passwd) {
        this.name = name;
        this.passwd = passwd;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPasswd() {
        return passwd;
    }

    @Override
    public int checkUserValidity(String name, String passwd) {
        if (name.equalsIgnoreCase("")) {
            return -1;
        }
        if (!Utils.isValidEmail(name)) {
            return -2;
        }
        if (passwd.equalsIgnoreCase("")) {
            return -3;
        }


        return 0;
    }
}
