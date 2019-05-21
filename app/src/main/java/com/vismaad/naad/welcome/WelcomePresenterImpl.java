package com.vismaad.naad.welcome;

/**
 * Created by ivesingh on 2/1/18.
 */

public class WelcomePresenterImpl implements WelcomePresenter  {

    private WelcomeView welcomeView;

    public WelcomePresenterImpl(WelcomeView welcomeView){
        this.welcomeView = welcomeView;
    }

    @Override
    public void init() {
        welcomeView.init();
    }

    @Override
    public void continueWithFacebook() {
        // TODO: Connect With Facebook
    }

    @Override
    public void connectUsingGoogle() {
        // TODO: Connect Using Google
    }
}
