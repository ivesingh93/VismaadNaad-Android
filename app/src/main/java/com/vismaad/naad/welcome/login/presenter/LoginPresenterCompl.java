package com.vismaad.naad.welcome.login.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.user.UserCredentials;
import com.vismaad.naad.rest.service.RaagiService;
import com.vismaad.naad.rest.service.UserService;
import com.vismaad.naad.welcome.login.model.IUser;
import com.vismaad.naad.welcome.login.model.UserModel;
import com.vismaad.naad.welcome.login.view.ILoginView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by satnamsingh on 31/05/18.
 */

public class LoginPresenterCompl implements ILoginPresenter {
    ILoginView iLoginView;
    IUser user;
    UserService mUserService;
    //Handler    handler;

    public LoginPresenterCompl(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
        mUserService = RetrofitClient.getClient().create(UserService.class);
        //initUser();
        // handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void clear() {
        iLoginView.onClearText();
    }

    @Override
    public void doLogin(String username, String password, String source_of_account) {


        Call<JsonElement> call = mUserService.login(new UserCredentials(username, password, source_of_account));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )

                Log.i("Signup--RESPONSE", "" + new Gson().toJson(response.body()));

                iLoginView.onLoginResult(new Gson().toJson(response.body()));
               /* if(response.body().getError()){
                    Toast.makeText(getBaseContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();


                }else {
                    //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                    String msg = response.body().getMessage();
                    int docId = response.body().getDoctorid();
                    boolean error = response.body().getError();

                    boolean activie = response.body().getActive()();
                }*/


            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
            }
        });
    }


    @Override
    public void setProgressBarVisiblity(int visiblity) {
        iLoginView.onSetProgressBarVisibility(visiblity);
    }


    private void initUser() {
        user = new UserModel("mvp", "mvp");
    }
}
