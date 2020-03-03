package com.vismaad.naad.welcome.signup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vismaad.naad.R;
import com.vismaad.naad.navigation.NavigationActivity;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.utils.Utils;
import com.vismaad.naad.welcome.signup.presenter.SignupPresenterComp;
import com.vismaad.naad.welcome.signup.view.ISignupView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class SignupActivity extends AppCompatActivity implements ISignupView {
    SignupPresenterComp mISignupPresenter;
    ACProgressFlower dialog;
    Bundle extras;
    private ViewGroup root_scene_transition, activity_signup_scene1, activity_signup_scene2;
    private Scene scene1, scene2;
    private int scene_num = 1;
    private EditText first_name_ET, last_name_ET, email_ET, username_ET, password_ET;
    private FrameLayout root_scene_background;
    private AnimationDrawable animation_drawable;
    private String firstName, lastName, email, username, password;
    private ImageButton next_IB, done_IB;
    private Transition transition;
    private boolean hasValidEmail, hasValidUsername;
    String Type = "Email";
    String FbID, Email, Gender;
    private SharedPreferences mSharedPreferences;
    TextView textView5, textView7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_root_scene);
        initial();
        if (extras != null) {
            if (extras.getString("TYPE") != null) {
                if (extras.getString("TYPE").equalsIgnoreCase("FB")) {
                    FbID = extras.getString("FB_ID");
                    firstName = extras.getString("FIRST_NAME");
                    lastName = extras.getString("LAST_NAME");
                    Email = extras.getString("EMAIL");
                    //Gender = extras.getString("GENDER");

                    first_name_ET.setText(firstName);
                    last_name_ET.setText(lastName);
                    //email_ET.setText(Email);
                    Type = "Facebook";
                    next_IB.setVisibility(View.GONE);
                    TransitionManager.go(scene2, transition);

                    done_IB = findViewById(R.id.done_IB);
                    done_IB.setEnabled(false);
                    next_IB.setEnabled(true);
                    email_ET = findViewById(R.id.email_ET);
                    username_ET = findViewById(R.id.username_ET);
                    password_ET = findViewById(R.id.password_ET);
                    textView5 = findViewById(R.id.textView5);
                    textView7 = findViewById(R.id.textView7);

                    email_ET.setVisibility(View.GONE);
                    password_ET.setVisibility(View.INVISIBLE);
                    textView5.setVisibility(View.GONE);
                    textView7.setVisibility(View.INVISIBLE);
                    username_ET.addTextChangedListener(new ErrorTextFBWatcher(username_ET));
                    email_ET.setText(Email);
                    password_ET.setText("");
                }

                if (extras.getString("TYPE").equalsIgnoreCase("GMAIL")) {
                    FbID = extras.getString("FB_ID");
                    firstName = extras.getString("FIRST_NAME");
                    lastName = extras.getString("LAST_NAME");
                    Email = extras.getString("EMAIL");

                    first_name_ET.setText(firstName);
                    last_name_ET.setText(lastName);
                    //email_ET.setText(Email);
                    Type = "GMAIL";
                    next_IB.setVisibility(View.GONE);
                    TransitionManager.go(scene2, transition);

                    done_IB = findViewById(R.id.done_IB);
                    done_IB.setEnabled(false);
                    next_IB.setEnabled(true);
                    email_ET = findViewById(R.id.email_ET);
                    username_ET = findViewById(R.id.username_ET);
                    password_ET = findViewById(R.id.password_ET);
                    textView5 = findViewById(R.id.textView5);
                    textView7 = findViewById(R.id.textView7);

                    email_ET.setVisibility(View.GONE);
                    password_ET.setVisibility(View.INVISIBLE);
                    textView5.setVisibility(View.GONE);
                    textView7.setVisibility(View.INVISIBLE);
                    username_ET.addTextChangedListener(new ErrorTextFBWatcher(username_ET));
                    email_ET.setText(Email);
                    password_ET.setText("");
                }
            }
        }

        first_name_ET.addTextChangedListener(new ErrorTextWatcher(first_name_ET));
        last_name_ET.addTextChangedListener(new ErrorTextWatcher(last_name_ET));

    }


    private void initial() {
        extras = getIntent().getExtras();
        dialog = new ACProgressFlower.Builder(SignupActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .bgColor(Color.TRANSPARENT)
                .bgAlpha(0)
                .bgCornerRadius(0)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCanceledOnTouchOutside(true);
        mISignupPresenter = new SignupPresenterComp(this);
        mISignupPresenter.setProgressBarVisiblity(View.INVISIBLE);

        root_scene_background = findViewById(R.id.root_scene_background);
        root_scene_transition = findViewById(R.id.root_scene_transition);
        activity_signup_scene1 = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_signup_scene1, root_scene_transition, false);
        activity_signup_scene2 = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_signup_scene2, root_scene_transition, false);
        //getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
        animation_drawable = (AnimationDrawable) root_scene_background.getBackground();
        animation_drawable.setEnterFadeDuration(500);
        animation_drawable.setExitFadeDuration(1500);

        transition = TransitionInflater.from(this).inflateTransition(R.transition.transition);

        scene1 = new Scene(root_scene_transition, activity_signup_scene1);
        scene2 = new Scene(root_scene_transition, activity_signup_scene2);
        scene1.enter();

        first_name_ET = findViewById(R.id.first_name_ET);
        last_name_ET = findViewById(R.id.last_name_ET);

        next_IB = findViewById(R.id.next_IB);
        next_IB.setEnabled(false);

        mSharedPreferences = getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void create_account(View view) {
        email = email_ET.getText().toString().trim();
        username = username_ET.getText().toString().trim();
        password = password_ET.getText().toString().trim();
        if (Utils.isNetworkAvailable(SignupActivity.this) == true) {


            //if (hasValidEmail && hasValidUsername) {
            dialog.show();
            mISignupPresenter.doSignup(first_name_ET.getText().toString(),
                    last_name_ET.getText().toString(),
                    email_ET.getText().toString(),
                    username_ET.getText().toString(),
                    password_ET.getText().toString(),
                    "dob",
                    "M", Type);
            //}
        } else {
            Utils.showSnackBar(SignupActivity.this, "No internet connection");
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void enter_scene1(View view) {
        if (Type.equalsIgnoreCase("Email")) {
            scene_num = 1;
            TransitionManager.go(scene1, transition);
        } else {
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void enter_scene2(View view) {
        scene_num = 2;
        firstName = first_name_ET.getText().toString().trim();
        lastName = last_name_ET.getText().toString().trim();

        TransitionManager.go(scene2, transition);

        done_IB = findViewById(R.id.done_IB);
        done_IB.setEnabled(false);

        email_ET = findViewById(R.id.email_ET);
        username_ET = findViewById(R.id.username_ET);
        password_ET = findViewById(R.id.password_ET);
        email_ET.setText(Email);
        email_ET.addTextChangedListener(new ErrorTextWatcher(email_ET));
        username_ET.addTextChangedListener(new ErrorTextWatcher(username_ET));
        password_ET.addTextChangedListener(new ErrorTextWatcher(password_ET));

    }

    public void exit_scene1(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animation_drawable != null && !animation_drawable.isRunning())
            animation_drawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animation_drawable != null && animation_drawable.isRunning())
            animation_drawable.stop();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        if (scene_num == 2) {
            enter_scene1(null);
        } else if (scene_num == 1) {
            exit_scene1(null);
        }
    }


    @Override
    public void onSignupResult(String code) {
        mISignupPresenter.setProgressBarVisiblity(View.INVISIBLE);
        dialog.dismiss();

        if (!code.equalsIgnoreCase("")) {
            try {
                JSONObject json = (JSONObject) new JSONTokener(code).nextValue();
                int responseCode = (int) json.get("ResponseCode");
                String msg = (String) json.get("Message");
                if (responseCode == 200) {
                    Toast toast = Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Intent mIntent = new Intent(SignupActivity.this, NavigationActivity.class);
                    startActivity(mIntent);
                    finish();
                    JBSehajBaniPreferences.setLoginId(mSharedPreferences, username_ET.getText().toString());

                } else {
                    Toast toast = Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
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
        public void afterTextChanged(final Editable editable) {

            switch (view.getId()) {
                case R.id.first_name_ET:
                    if (editable.toString().trim().length() == 0) {
                        next_IB.setEnabled(false);
                        first_name_ET.setError("First Name is required.");
                    } else {
                        hasName();
                    }
                    break;
                case R.id.last_name_ET:
                    if (editable.toString().trim().length() == 0) {
                        next_IB.setEnabled(false);
                        last_name_ET.setError("Last Name is required.");
                    } else {
                        hasName();
                    }
                    break;

                case R.id.email_ET:
                    if (editable.toString().trim().length() == 0) {
                        done_IB.setEnabled(false);
                        email_ET.setError("Please enter a valid email address.");
                    } else {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isValidEmail(editable);
                            }
                        }, 1000);
                    }
                    break;

                case R.id.username_ET:
                    if (editable.toString().trim().length() == 0) {
                        done_IB.setEnabled(false);
                        username_ET.setError("Username is required.");
                    } else {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // isValidUsername(editable);
                            }
                        }, 1000);
                    }
                    break;

                case R.id.password_ET:
                    if (editable.toString().length() == 0) {
                        done_IB.setEnabled(false);
                        password_ET.setError("Password is required.");
                    } else {
                        hasCredentials();
                    }
                    break;
            }
        }
    }

    private class ErrorTextFBWatcher implements TextWatcher {

        private View view;

        private ErrorTextFBWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(final Editable editable) {

            switch (view.getId()) {

                case R.id.username_ET:
                    if (editable.toString().trim().length() == 0) {
                        done_IB.setEnabled(false);
                        username_ET.setError("Username is required.");
                    } else {
                        hasFBUserName();
                    }
                    break;

             /*   case R.id.password_ET:
                    if (editable.toString().length() == 0) {
                        done_IB.setEnabled(false);
                        password_ET.setError("Password is required.");
                    } else {
                        hasCredentials();
                    }
                    break;*/
            }
        }
    }


    private void hasFBUserName() {
        if (username_ET.getText().toString().trim().length() > 0) {
            done_IB.setEnabled(true);
        }
    }

    private void hasName() {
        if (first_name_ET.getText().toString().trim().length() > 0 && last_name_ET.getText().toString().trim().length() > 0) {
            next_IB.setEnabled(true);
        }
    }

    private void hasCredentials() {
        if (email_ET.getText().toString().trim().length() > 0 && username_ET.getText().toString().trim().length() > 0
                && password_ET.getText().toString().length() > 0) {
            //if (hasValidEmail && hasValidUsername) {
            done_IB.setEnabled(true);
        } else {
            done_IB.setEnabled(false);
            //}
        }
    }

    private void isValidEmail(Editable editable) {

        if (!Patterns.EMAIL_ADDRESS.matcher(editable.toString()).matches()) {
            email_ET.setError("Please enter a valid email address.");
            email_ET.requestFocus();
            hasValidEmail = false;
        }

    }

}
