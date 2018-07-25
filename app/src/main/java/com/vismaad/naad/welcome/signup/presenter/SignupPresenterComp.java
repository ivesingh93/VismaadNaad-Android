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
                    Log.i("Signup--RESPONSE", "" + new Gson().toJson(response.body()));
                    iSignupView.onSignupResult(new Gson().toJson(response.body()));
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //iSignupView.onSignupResult(2);
            }

        });

        /*call.enqueue(new Callback<UserCredentials>() {
            @Override
            public void onResponse(Call<UserCredentials> call, Response<UserCredentials> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )

                Log.i("Signup--RESPONSE", "" + new Gson().toJson(response.body()));

               *//* if(response.body().getError()){
                    Toast.makeText(getBaseContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();


                }else {
                    //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                    String msg = response.body().getMessage();
                    int docId = response.body().getDoctorid();
                    boolean error = response.body().getError();

                    boolean activie = response.body().getActive()();
                }*//*


            }

            @Override
            public void onFailure(Call<UserCredentials> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
            }
        });*/

  /*      createUser(user, new Callback<UserCredentials>() {
            @Override
            public void onResponse(Call<UserCredentials> call, Response<UserCredentials> response) {
                Log.i("Signup--RESPONSE", "" + new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<UserCredentials> call, Throwable t) {

            }
        });*/


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
