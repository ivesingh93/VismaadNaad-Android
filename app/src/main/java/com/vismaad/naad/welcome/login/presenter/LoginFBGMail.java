package com.vismaad.naad.welcome.login.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.user.UserCredentials;
import com.vismaad.naad.rest.service.UserService;
import com.vismaad.naad.welcome.login.view.ILoginFB;
import com.vismaad.naad.welcome.login.view.ILoginView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by satnamsingh on 14/07/18.
 */

public class LoginFBGMail implements ILoginFBGMail {
    UserService mUserService;
    ILoginFB iLoginView;


    public LoginFBGMail(ILoginFB iLoginView) {
        this.iLoginView = iLoginView;
        mUserService = RetrofitClient.getClient().create(UserService.class);
    }


    @Override
    public void doLogin(String account_id, String source_of_account, final String loginType) {
        Call<JsonElement> call = mUserService.login(new UserCredentials(account_id, source_of_account));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.i("Signup--Gmail", "" + new Gson().toJson(response.body()));
                iLoginView.onLoginResult(new Gson().toJson(response.body()), loginType);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
            }
        });

    }
}
