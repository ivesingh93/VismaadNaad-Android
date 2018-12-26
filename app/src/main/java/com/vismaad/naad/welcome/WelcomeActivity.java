package com.vismaad.naad.welcome;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vismaad.naad.R;
import com.vismaad.naad.databinding.ActivityWelcomeBinding;
import com.vismaad.naad.newwork.PopularShabadRaagisActivity;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.utils.BlurBuilder;
import com.vismaad.naad.welcome.login.LoginActivity;
import com.vismaad.naad.welcome.login.presenter.LoginFBGMail;
import com.vismaad.naad.welcome.login.view.ILoginFB;
import com.vismaad.naad.welcome.signup.SignupActivity;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Author - Ivkaran Singh
 * Date - 12/18/2017
 */

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, ILoginFB {
    ActivityWelcomeBinding binding;
    private SharedPreferences mSharedPreferences;
    Intent mIntent;
    ACProgressFlower dialog_progress;
    CallbackManager mFacebookCallbackManager;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    boolean isGmailLoginAlready;
    LoginFBGMail loginPresenter;
    GoogleSignInAccount acct;
    String firstName, secondName, email;
    String id, FBfirstName, FBLastName, FBEmail;
    public static Activity welcomeActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        generateKeyHash();
        initial();
        if (!JBSehajBaniPreferences.getLoginId(mSharedPreferences).equalsIgnoreCase("")) {
            mIntent = new Intent(WelcomeActivity.this, PopularShabadRaagisActivity.class);
            startActivity(mIntent);
            finish();
        }

        if (JBSehajBaniPreferences.getBtnSkip(mSharedPreferences).equalsIgnoreCase("YES")) {
            mIntent = new Intent(WelcomeActivity.this, PopularShabadRaagisActivity.class);
            startActivity(mIntent);
            finish();
        }

        welcomeActivity = this;
        //binding.rl.setImageBitmap
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gold);
        Bitmap blurredBitmap = BlurBuilder.blur(this, originalBitmap);
        binding.rl.setBackground(new BitmapDrawable(getResources(), blurredBitmap));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                        .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            isGmailLoginAlready = true;
            mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }
    }

    private void initial() {
        mSharedPreferences = WelcomeActivity.this.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
        binding.btnSkip.setOnClickListener(this);
        binding.connectUsingGoogleButton.setOnClickListener(this);
        binding.continueWithFacebookButton.setOnClickListener(this);
        binding.signUpFreeButton.setOnClickListener(this);
        binding.loginB.setOnClickListener(this);
        dialog_progress = new ACProgressFlower.Builder(WelcomeActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .bgColor(Color.TRANSPARENT)
                .bgAlpha(0)
                .bgCornerRadius(0)
                .fadeColor(Color.DKGRAY).build();
        dialog_progress.setCanceledOnTouchOutside(true);
        loginPresenter = new LoginFBGMail(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mFacebookCallbackManager = CallbackManager.Factory.create();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up_free_button:
                mIntent = new Intent(WelcomeActivity.this, SignupActivity.class);
                startActivity(mIntent);

                break;
            case R.id.continue_with_facebook_button:
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    Intent mIntent = new Intent(WelcomeActivity.this, PopularShabadRaagisActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent);
                    finish();
                } else {
                    fbLogin();
                }
                break;
            case R.id.connect_using_google_button:
               /* if (isGmailLoginAlready == true) {
                    Intent mIntent = new Intent(WelcomeActivity.this, NavigationActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent);
                    finish();

                } else {*/
                google_sign_in();

//                mGoogleApiClient = new GoogleApiClient.Builder(this)
//                        .enableAutoManage(this,this)
//                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                        .build();
//                OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//                if (opr.isDone()) {
//                    // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//                    // and the GoogleSignInResult will be available instantly.
//                    GoogleSignInResult result = opr.get();
//                    handleSignInResult(result);
//                } else {
//                    // If the user has not previously signed in on this device or the sign-in has expired,
//                    // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//                    // single sign-on will occur in this branch.
//                    //  dialog.show();
//
//                    opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                        @Override
//                        public void onResult(GoogleSignInResult googleSignInResult) {
//                            //hideProgressDialog();
//                            //dialog.dismiss();
//                            google_sign_in();
//                            // handleSignInResult(googleSignInResult);
//                        }
//                    });
//                }
                /*if (mGoogleApiClient.isConnected()) {
                    // signed in. Show the "sign out" button and explanation.
                    // ...
                    Intent mIntent = new Intent(WelcomeActivity.this, NavigationActivity.class);
                    startActivity(mIntent);
                    finish();
                } else {
                    // not signed in. Show the "sign in" button and explanation.
                    // ...
                    google_sign_in();
                }*/
                // }

                break;
            case R.id.login_B:
                mIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
                break;

            case R.id.btnSkip:
                createDialog();

                break;
            default:
                break;
        }
    }

    public void createDialog() {
        // dialog.show();
        final Dialog dialog = new Dialog(WelcomeActivity.this);
        dialog.setContentView(R.layout.dialog_login);
        dialog.setTitle("Alert!");

        dialog.show();

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        final EditText editName = (EditText) dialog.findViewById(R.id.editName);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
                JBSehajBaniPreferences.setBtnSkip(mSharedPreferences, "YES");

                Intent mIntent = new Intent(WelcomeActivity.this, PopularShabadRaagisActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(WelcomeActivity.this, SignupActivity.class);
                startActivity(mIntent);

                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void generateKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.vismaad.naad",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void google_sign_in() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try {
            acct = completedTask.getResult(ApiException.class);
            String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
            String[] parts = personName.split(" ");
            firstName = parts[0];//"hello"
            secondName = parts[1];
            email = acct.getEmail();
            dialog_progress.show();
            loginPresenter.doLogin(email, "gmail", "GM");

        } catch (ApiException e) {
        }

    }


    public void fbLogin() {

        LoginManager.getInstance().logInWithReadPermissions
                (this, Arrays.asList("email", "public_profile"));
        /*LoginManager.getInstance().logInWithPublishPermissions(this,
                Arrays.asList("publish_actions"));*/
        LoginManager.getInstance().registerCallback(mFacebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        String accessToken = loginResult.getAccessToken().getToken();

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject jsonObject,
                                                            GraphResponse response) {

                                        // Getting FB User Data
                                        Bundle facebookData = getFacebookData(jsonObject);


                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,last_name,email,gender");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        //dialog_progress.dismiss();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        //dialog_progress.dismiss();
                    }
                });
    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();

        try {
            id = object.getString("id");
          /*  URL profile_pic;
            try {
                profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }*/

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            FBfirstName = object.getString("first_name");
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));

            FBLastName = object.getString("last_name");
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            FBEmail = object.getString("email");
            dialog_progress.show();
            loginPresenter.doLogin(object.getString("id"), "facebook", "FB");

           /* Intent mIntent = new Intent(WelcomeActivity.this, SignupActivity.class);
            mIntent.putExtra("TYPE", "FB");
            mIntent.putExtra("FB_ID", id);
            mIntent.putExtra("FIRST_NAME", object.getString("first_name"));
            mIntent.putExtra("LAST_NAME", object.getString("last_name"));
            mIntent.putExtra("EMAIL", object.getString("id"));
            startActivity(mIntent);
*/




            /*prefUtil.saveFacebookUserInfo(object.getString("first_name"),
                    object.getString("last_name"),object.getString("email"),
                    object.getString("gender"), profile_pic.toString());*/

        } catch (Exception e) {
        }

        return bundle;
    }

    @Override
    public void onLoginResult(String code, String loginType) {

        try {
            dialog_progress.dismiss();
            JSONObject json = (JSONObject) new JSONTokener(code).nextValue();
            int responseCode = (int) json.get("ResponseCode");
            String msg = (String) json.get("Message");
            if (responseCode == 200) {
                if (loginType.equalsIgnoreCase("GM")) {
                    Intent mIntent = new Intent(WelcomeActivity.this, PopularShabadRaagisActivity.class);
                    startActivity(mIntent);
                    finish();

                    JBSehajBaniPreferences.setLoginId(mSharedPreferences, (String) json.get("username"));
                } else {
                    Intent mIntent = new Intent(WelcomeActivity.this, PopularShabadRaagisActivity.class);
                    startActivity(mIntent);
                    finish();

                    JBSehajBaniPreferences.setLoginId(mSharedPreferences, (String) json.get("username"));
                }
            } else {
                if (loginType.equalsIgnoreCase("GM")) {
                    Intent mIntent = new Intent(WelcomeActivity.this, SignupActivity.class);
                    mIntent.putExtra("TYPE", "GMAIL");
                    mIntent.putExtra("FB_ID", acct.getId());
                    mIntent.putExtra("FIRST_NAME", firstName);
                    mIntent.putExtra("LAST_NAME", secondName);
                    mIntent.putExtra("EMAIL", email);
                    startActivity(mIntent);
                } else {
                    Intent mIntent = new Intent(WelcomeActivity.this, SignupActivity.class);
                    mIntent.putExtra("TYPE", "FB");
                    mIntent.putExtra("FB_ID", id);
                    mIntent.putExtra("FIRST_NAME", FBfirstName);
                    mIntent.putExtra("LAST_NAME", FBLastName);
                    mIntent.putExtra("EMAIL", id);
                    startActivity(mIntent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
       /* if (co) {
            Intent mIntent = new Intent(WelcomeActivity.this, SignupActivity.class);
            mIntent.putExtra("TYPE", "GMAIL");
            mIntent.putExtra("FB_ID", acct.getId());
            mIntent.putExtra("FIRST_NAME", firstName);
            mIntent.putExtra("LAST_NAME", secondName);
            mIntent.putExtra("EMAIL", email);
            startActivity(mIntent);

        }*/
    }
}