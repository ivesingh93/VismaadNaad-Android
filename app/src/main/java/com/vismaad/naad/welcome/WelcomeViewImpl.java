package com.vismaad.naad.welcome;

import android.app.Activity;
import android.app.NativeActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vismaad.naad.R;
import com.vismaad.naad.welcome.login.LoginActivity;
import com.vismaad.naad.welcome.signup.SignupActivity;

/**
 * Created by ivesingh on 2/1/18.
 */

public class WelcomeViewImpl implements WelcomeView, View.OnClickListener {

    private Activity context;
    private WelcomePresenter welcomePresenter;
    private EditText editUserName, editPassword;
    private Button btnSignIn, btnFB, btnGmail, btnSignup, btnSkip;

    public WelcomeViewImpl(Activity context) {
        this.context = context;
        welcomePresenter = new WelcomePresenterImpl(this);
    }

    @Override
    public void init() {
        editUserName = (EditText) context.findViewById(R.id.editUserName);
        editPassword = (EditText) context.findViewById(R.id.editPassword);

        btnSignIn = (Button) context.findViewById(R.id.btnSignIn);
        btnFB = (Button) context.findViewById(R.id.btnFB);

        btnGmail = (Button) context.findViewById(R.id.btnGmail);
        btnSignup = (Button) context.findViewById(R.id.btnSignup);

        btnSkip = (Button) context.findViewById(R.id.btnSkip);

        btnSignIn.setOnClickListener(this);
        btnFB.setOnClickListener(this);
        btnGmail.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        btnSkip.setOnClickListener(this);


    }

    @Override
    public void navigateToRegistration() {

        Intent intent = new Intent(context, SignupActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void navigateToLoginPage() {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void skip() {
        Intent intent = new Intent(context, NativeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn:
                navigateToRegistration();
                break;

            case R.id.btnFB:
                welcomePresenter.continueWithFacebook();
                break;

            case R.id.btnGmail:
                welcomePresenter.connectUsingGoogle();
                break;

            case R.id.btnSignup:
                navigateToLoginPage();

            case R.id.btnSkip:
                skip();
                break;
        }
    }
}
