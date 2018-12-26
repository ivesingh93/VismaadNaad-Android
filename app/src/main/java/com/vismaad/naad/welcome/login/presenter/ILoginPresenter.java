package com.vismaad.naad.welcome.login.presenter;

/**
 * Created by satnamsingh on 31/05/18.
 */

public interface ILoginPresenter {
    void clear();

    void doLogin(String username, String password, String source_of_account);

    void setProgressBarVisiblity(int visiblity);
}
