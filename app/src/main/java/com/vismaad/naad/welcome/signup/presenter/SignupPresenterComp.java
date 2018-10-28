package com.vismaad.naad.welcome.signup.presenter;

import android.graphics.Color;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.raagi.RaagiInfo;
import com.vismaad.naad.rest.model.user.UserCredentials;
import com.vismaad.naad.rest.service.UserService;
import com.vismaad.naad.welcome.login.view.ILoginView;
import com.vismaad.naad.welcome.signup.model.iSignup;
import com.vismaad.naad.welcome.signup.view.ISignupView;

import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by satnamsingh on 31/05/18.
 */

public class SignupPresenterComp implements ISignupPresenter {
    ISignupView iSignupView;
    iSignup mISignup;
    UserService mUserService;

    public SignupPresenterComp(ISignupView iSignupView) {
        this.iSignupView = iSignupView;
        mUserService = RetrofitClient.getClient().create(UserService.class);
    }

    @Override
    public void doSignup(String firstname, String lastName,
                         String email, String userID,
                         String password, String dob, String gender, String type) {


        //final int code = mISignup.checkUserValidity(firstname, lastName, email, userID, password, dob, gender);
        //iSignupView.onSignupResult(code);


        Call<JsonElement> call = mUserService.create_user(new UserCredentials(firstname,
                lastName, email, userID, password, type));

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response != null) {
                    iSignupView.onSignupResult(new Gson().toJson(response.body()));
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //iSignupView.onSignupResult(2);
            }

        });
    }

    @Override
    public void setProgressBarVisiblity(int visiblity) {
        iSignupView.onSetProgressBarVisibility(visiblity);
    }

    public void createUser(UserCredentials user, Callback<JsonElement> callback) {
        Call<JsonElement> userCall = mUserService.create_user(user);
        userCall.enqueue(callback);
    }

}
