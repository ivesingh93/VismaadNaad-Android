package com.vismaad.naad.welcome.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.vismaad.naad.R;
import com.vismaad.naad.navigation.NavigationActivity;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.utils.Utils;
import com.vismaad.naad.welcome.WelcomeActivity;
import com.vismaad.naad.welcome.login.presenter.ILoginPresenter;
import com.vismaad.naad.welcome.login.presenter.LoginPresenterCompl;
import com.vismaad.naad.welcome.login.view.ILoginView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class LoginActivity extends AppCompatActivity implements ILoginView, View.OnClickListener {
    ILoginPresenter loginPresenter;
    ACProgressFlower dialog;
    private SharedPreferences mSharedPreferences;
    private EditText email_username_ET, password_ET;
    private ImageButton back_IB, login_IB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initial();
        email_username_ET.addTextChangedListener(new ErrorTextWatcher(email_username_ET));
        password_ET.addTextChangedListener(new ErrorTextWatcher(password_ET));
        login_IB.setEnabled(false);
    }

    private void initial() {
        mSharedPreferences = getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
        loginPresenter = new LoginPresenterCompl(this);
        loginPresenter.setProgressBarVisiblity(View.INVISIBLE);
        dialog = new ACProgressFlower.Builder(LoginActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .bgColor(Color.TRANSPARENT)
                .bgAlpha(0)
                .bgCornerRadius(0)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCanceledOnTouchOutside(true);
        email_username_ET = findViewById(R.id.email_username_ET);
        password_ET = findViewById(R.id.password_ET);
        back_IB = findViewById(R.id.back_IB);
        back_IB.setOnClickListener(this);
        login_IB = findViewById(R.id.login_IB);
        login_IB.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_IB:
                //loginPresenter.setProgressBarVisiblity(View.VISIBLE);
                if (Utils.isNetworkAvailable(LoginActivity.this) == true) {
                    dialog.show();
                    loginPresenter.doLogin(email_username_ET.getText().toString(), password_ET.getText().toString(), "EMAIL");
                } else {
                    Utils.showSnackBar(LoginActivity.this, "No internet connection");
                }
                break;

            case R.id.back_IB:
                finish();
                break;


            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClearText() {

    }

    @Override
    public void onLoginResult(String code) {
        loginPresenter.setProgressBarVisiblity(View.INVISIBLE);
        // btnSignIn.setEnabled(true);
        dialog.dismiss();
        if (!code.equalsIgnoreCase("")) {
            try {
                JSONObject json = (JSONObject) new JSONTokener(code).nextValue();
                int responseCode = (int) json.get("ResponseCode");
                String msg = (String) json.get("Message");
                String account_id = (String) json.get("account_id");
                if (responseCode == 200) {
                    Intent mIntent = new Intent(LoginActivity.this, NavigationActivity.class);
                    startActivity(mIntent);
                    finish();
                    JBSehajBaniPreferences.setLoginId(mSharedPreferences, email_username_ET.getText().toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {

    }


    private class ErrorTextWatcher implements TextWatcher {

        private View view;

        private ErrorTextWatcher(View view) {
            this.view = view;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.email_username_ET:
                    if (editable.toString().trim().length() == 0) {
                        login_IB.setEnabled(false);
                        email_username_ET.setError("Email or username is required.");
                    } else {
                        hasCredentials();
                    }
                    break;

                case R.id.password_ET:
                    if (editable.toString().trim().length() == 0) {
                        login_IB.setEnabled(false);
                        password_ET.setError("Password is required.");
                    } else {
                        hasCredentials();
                    }
                    break;
            }
        }
    }

    private void hasCredentials() {
        if (email_username_ET.getText().toString().trim().length() > 0 && password_ET.getText().toString().length() > 0) {
            login_IB.setEnabled(true);
        }
    }

}
