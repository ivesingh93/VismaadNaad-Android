package com.vismaad.naad.navigation;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.vismaad.naad.Constants;
import com.vismaad.naad.R;
import com.vismaad.naad.navigation.home.HomeFragment;
import com.vismaad.naad.navigation.playlist.PlayListFrag;
import com.vismaad.naad.player.ShabadPlayerActivity;
import com.vismaad.naad.player.service.App;
import com.vismaad.naad.player.service.MediaPlayerState;
import com.vismaad.naad.player.service.ShabadPlayerForegroundService;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.JBfeedback.JBFeedback;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.rest.service.PlayList;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.utils.Utils;
import com.vismaad.naad.welcome.WelcomeActivity;
import com.vismaad.naad.welcome.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.PAUSED;
import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.PLAYING;
import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.STOPPED;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {

    public ShowShabadReceiver showShabadReceiver;
    // private ActionBar toolbar;
    private LinearLayout layout;
    private ShabadPlayerForegroundService playerService;

    private RelativeLayout miniPlayerLayout;
    // private AdView adView_mini;
    private ImageView playBtn;
    private TextView shabadName, raagiName;
    private Shabad currentShabad;
    public static ArrayList<Shabad> shabadsList = new ArrayList<>();
    private String[] shabadLinks, shabadTitles;
    private int originalShabadIndex = 0;
    //    private int playerState = 0;
    private View borderView;
    private UpdateUIReceiver updater;
    SearchView searchView = null;
    BroadcastReceiver mBroadcastReceiver;
    private SharedPreferences mSharedPreferences;
    int count = 0;
    PlayList mCreatePlayList;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        if (!JBSehajBaniPreferences.getLoginId(mSharedPreferences).equalsIgnoreCase("")) {
            WelcomeActivity.welcomeActivity.finish();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //toolbar.setTitle("Home");
                    loadFragment(new HomeFragment());
                    return true;
               /* case R.id.navigation_search:
                    toolbar.setTitle("Search");
                    return true;*/
                case R.id.navigation_library:
                    // toolbar.setTitle("Library");
                    if (!JBSehajBaniPreferences.getLoginId(mSharedPreferences).equalsIgnoreCase("")) {
                        loadFragment(new PlayListFrag());
                    } else {
                        createDialog();
                    }

                    return true;
                case R.id.navigation_settings:
                    // toolbar.setTitle("Settings");
                    loadFragment(new Settings());
                    return true;
            }
            return false;
        }
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        updater = new UpdateUIReceiver();
        showShabadReceiver = new ShowShabadReceiver();
        FirebaseApp.initializeApp(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(updater, new IntentFilter(MediaPlayerState.updateUI));
        LocalBroadcastManager.getInstance(this).registerReceiver(showShabadReceiver, new IntentFilter(MediaPlayerState.SHOW_SHABAD));

        playerService = App.getService();

        //  toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
//        layoutParams.setBehavior(new BottomNavigationBehavior());
        mSharedPreferences = getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);

        if (JBSehajBaniPreferences.getCount(mSharedPreferences) > 0) {
            count = JBSehajBaniPreferences.getCount(mSharedPreferences);
            count++;
        } else {
            count++;
        }

        JBSehajBaniPreferences.setCount(mSharedPreferences, count++);
        Log.i("Lunch count", "" + JBSehajBaniPreferences.getYesFeedback(mSharedPreferences));

        //if (!JBSehajBaniPreferences.getYesGiven(mSharedPreferences).equalsIgnoreCase("YES")) {
        if (!JBSehajBaniPreferences.getYesRating(mSharedPreferences).equalsIgnoreCase("YES")) {
            if (JBSehajBaniPreferences.getCount(mSharedPreferences) == 10) {

                showAboutDialog();
                JBSehajBaniPreferences.setCount(mSharedPreferences, 0);
            }
        }
//        else {
//            if (!JBSehajBaniPreferences.getYesFeedback(mSharedPreferences).equalsIgnoreCase("YES")) {
//                if (JBSehajBaniPreferences.getCount(mSharedPreferences) == 10) {
//
//                    showAboutDialog();
//                    JBSehajBaniPreferences.setCount(mSharedPreferences, 0);
//                }
//            }
//            // }
//        }


        miniPlayerLayout = (RelativeLayout)

                findViewById(R.id.mini_player);
        miniPlayerLayout.setOnClickListener(this);
        // adView_mini = (AdView) findViewById(R.id.adView_mini);
        playBtn = (ImageView)

                findViewById(R.id.play_pause_mini_player);
        playBtn.setOnClickListener(this);
        shabadName = (TextView)

                findViewById(R.id.shabad_name_mini_player);

        raagiName = (TextView)

                findViewById(R.id.raagi_name_mini_player);

        borderView =

                findViewById(R.id.border_line);
        MobileAds.initialize(NavigationActivity.this,

                getResources().

                        getString(R.string.YOUR_ADMOB_APP_ID));

        checkMiniPlayerVisibility();

        //        toolbar.setTitle("Home");
        loadFragment(new HomeFragment());


   /*     registerReceiver(mBroadcastReceiver, new IntentFilter("start.fragment.action"));

        mBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //This piece of code will be executed when you click on your item
                // Call your fragment...
                loadFragment(new PlayListFrag());
            }
        };*/
        LocalBroadcastManager.getInstance(NavigationActivity.this).

                registerReceiver(
                        mRandomNumberReceiver,
                        new IntentFilter("BROADCAST_RANDOM_NUMBER")
                );
        mCreatePlayList = RetrofitClient.getClient().

                create(PlayList.class);

    }

    private void showAboutDialog() {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.rate_popup);
        dialog.show();


        Button btnRate = (Button) dialog.findViewById(R.id.btnRate);
        Button btnFeedback = (Button) dialog.findViewById(R.id.btnFeedback);
        Button btnClose = (Button) dialog.findViewById(R.id.btnClose);


        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!JBSehajBaniPreferences.getYesRating(mSharedPreferences).equalsIgnoreCase("YES")) {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                    try {
                       // JBSehajBaniPreferences.setYesGiven(mSharedPreferences, "YES");
                        JBSehajBaniPreferences.setYesRating(mSharedPreferences, "YES");
                        startActivity(goToMarket);
                        dialog.dismiss();
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                } else {
                    Toast.makeText(NavigationActivity.this, "You already given the rating", Toast.LENGTH_LONG).show();
                }
            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                JBSehajBaniPreferences.setCount(mSharedPreferences, 0);
            }
        });


        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!JBSehajBaniPreferences.getYesFeedback(mSharedPreferences).equalsIgnoreCase("YES")) {
                    showFeedbackDialog();
                } else {
                    Toast.makeText(NavigationActivity.this, "You already given the feedback", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });

    }


    public void showFeedbackDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.feed_back);
        dialog.show();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        dialog.getWindow().setLayout(width, 700);
        final EditText email_username_ET = (EditText) dialog.findViewById(R.id.email_username_ET);
        email_username_ET.setVisibility(View.GONE);
        final EditText meg_ET = (EditText) dialog.findViewById(R.id.meg_ET);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  sendFeedback(email_username_ET.getText().toString(), meg_ET.getText().toString());


                Log.i("user-name", JBSehajBaniPreferences.getLoginId(mSharedPreferences));

                Call<JsonElement> call = mCreatePlayList.feedback(new JBFeedback(JBSehajBaniPreferences.getLoginId(mSharedPreferences),
                        meg_ET.getText().toString()));
                call.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        Log.i("feedback", "" + response.message());
                        dialog.dismiss();


                        /*{
                            "ResponseCode": 200,
                                "Message": "Feedback submitted successfully"
                        }*/


                        try {
                            JSONObject json = (JSONObject) new JSONTokener(new Gson().toJson(response.body())).nextValue();
                            int responseCode = (int) json.get("ResponseCode");
                            String msg = (String) json.get("Message");

                            if (responseCode == 200) {
                                //JBSehajBaniPreferences.setYesFeedback(mSharedPreferences, "YES");
                              //  JBSehajBaniPreferences.setYesGiven(mSharedPreferences, "YES");

                            } else {
                                JBSehajBaniPreferences.setCount(mSharedPreferences, 0);
                            }

                            Toast toast = Toast.makeText(NavigationActivity.this, msg, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                /*try {
                   // JSONObject object = (JSONObject) new JSONTokener(new Gson().toJson(response.body())).nextValue();
                    Log.i("feedback", response.message());

                    *//*if (object.getBoolean("Result")) {
                        like.setImageResource(R.drawable.favorite_filled);
                        like.setColorFilter(ContextCompat.getColor(ShabadPlayerActivity.this, R.color.appThemeColor), android.graphics.PorterDuff.Mode.MULTIPLY);
                        isLiked = true;
                    } else {
                        like.setImageResource(R.drawable.favorite);
                        like.setColorFilter(Color.argb(255, 255, 255, 255));
                        isLiked = false;
                    }*//*
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {

                        //for getting error in network put here Toast, so get the error on network
                    }
                });


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void createDialog() {
        // dialog.show();
        final Dialog dialog = new Dialog(NavigationActivity.this);
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
                //Intent mIntent = new Intent(NavigationActivity.this, NavigationActivity.class);
                // startActivity(mIntent);
                //finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JBSehajBaniPreferences.setBtnSkip(mSharedPreferences, "NO");
                Intent mIntent = new Intent(NavigationActivity.this, WelcomeActivity.class);
                startActivity(mIntent);
                finish();
                dialog.dismiss();
            }
        });

    }

    private BroadcastReceiver mRandomNumberReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            loadFragment(new PlayListFrag());
        }
    };


    public void sendFeedback(String username, String feedback) {

    }


 /*   @Override
    protected void o {
        super.onPause();
        if (mRandomNumberReceiver != null) {
            unregisterReceiver(mRandomNumberReceiver);
        }
    }*/


    private void startPlayerService() {
        Intent intent = new Intent(this, ShabadPlayerForegroundService.class);
        intent.putExtra(MediaPlayerState.RAAGI_NAME, currentShabad.getRaagiName());
        intent.putExtra(MediaPlayerState.SHABAD_TITLES, shabadTitles);
        intent.putExtra(MediaPlayerState.SHABAD_LINKS, shabadLinks);
        intent.putExtra(MediaPlayerState.ORIGINAL_SHABAD, originalShabadIndex);
        intent.putExtra(MediaPlayerState.SHABAD, currentShabad);
        intent.putExtra(MediaPlayerState.shabad_list, shabadsList);
        intent.putExtra(MediaPlayerState.Action_Play, true);
        intent.putExtra(MediaPlayerState.SHABAD_DURATION, App.getPreferenceLong(MediaPlayerState.SHABAD_DURATION));
        intent.addCategory(ShabadPlayerForegroundService.TAG);
        intent.setAction(Constants.STARTFOREGROUND_ACTION);
        startService(intent);
        App.setPreferencesInt(Constants.PLAYER_STATE, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkMiniPlayerVisibility();
        if (playerService != null) {
            updateUI();
        }
    }

    private void updateUI() {
        if (playerService.getStatus() == PLAYING) {
            playBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_button));
        } else if (playerService.getStatus() == PAUSED) {
            playBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_button));
        } else if (playerService.getStatus() == STOPPED) {
            playBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_button));
        } else {
        }
    }

    private void checkMiniPlayerVisibility() {
        shabadsList = App.getGson().fromJson(App.getPrefranceData(MediaPlayerState.shabad_list), new TypeToken<ArrayList<Shabad>>() {
        }.getType());

        currentShabad = App.getGson().fromJson(App.getPrefranceData(MediaPlayerState.SHABAD), new TypeToken<Shabad>() {
        }.getType());

        if (currentShabad != null) {
            if (currentShabad.getShabadEnglishTitle() != null && currentShabad.getShabadEnglishTitle().length() > 0) {
                shabadName.setText(currentShabad.getShabadEnglishTitle());
                raagiName.setText(currentShabad.getRaagiName());
//                if (playerService.getStatus() == STOPPED) {
//                    miniPlayerLayout.setVisibility(View.GONE);
//                    borderView.setVisibility(View.GONE);
//                } else {
                miniPlayerLayout.setVisibility(View.VISIBLE);
                borderView.setVisibility(View.VISIBLE);
//                }
            } else {
                miniPlayerLayout.setVisibility(View.GONE);
                borderView.setVisibility(View.GONE);
            }
        } else {
            miniPlayerLayout.setVisibility(View.GONE);
            borderView.setVisibility(View.GONE);
        }

        if (shabadsList != null && shabadsList.size() > 0) {
            shabadLinks = new String[shabadsList.size()];
            shabadTitles = new String[shabadsList.size()];
            for (int i = 0; i < shabadsList.size(); i++) {
                //shabadLinks[i] = shabadsList.get(i).getShabadUrl().replace(" ", "+");
                shabadLinks[i] = shabadsList.get(i).getShabadUrl().replace(" ", "+");
                if (shabadsList.get(i).getShabadUrl().equals(currentShabad.getShabadUrl())) {
                    originalShabadIndex = i;
                }

                shabadTitles[i] = shabadsList.get(i).getShabadEnglishTitle();
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        if (!isFinishing()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);

            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mini_player:
                // redirect to shabad playing screen 3rd screen
                create_intent();
                break;
            case R.id.play_pause_mini_player:
                playPauseShabad();
                break;
        }
    }

    private void playPauseShabad() {
        if (playerService.getStatus() == STOPPED) {
            startPlayerService();
            playerService.play();
            playerService.setDuration(App.getPreferenceLong(MediaPlayerState.SHABAD_DURATION));
        } else if (playerService.getStatus() == PLAYING) {
            playerService.pause();
            updateUI();
            App.setPreferencesInt(Constants.PLAYER_STATE, 1);
        } else if (playerService.getStatus() == PAUSED) {
            if (App.getPreferanceInt(Constants.PLAYER_STATE) == 0) {
                startPlayerService();
            }
            playerService.play();
            updateUI();
        }
    }

    private void create_intent() {
        Utils.goToShabadPlayerActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updater);
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(showShabadReceiver);
        /*if (mRandomNumberReceiver != null) {
            unregisterReceiver(mRandomNumberReceiver);
        }*/
    }

    public class UpdateUIReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                updateUI();
            }
        }
    }

    private void saveLastShabadToPlay() {
        // store shabad list in shared pref using set in android or in list of json
        String json = App.getGson().toJson(shabadsList);
        if (App.getPrefranceData(MediaPlayerState.shabad_list) != null && App.getPrefranceData(MediaPlayerState.shabad_list).length() > 0) {
            App.setPreferences(MediaPlayerState.shabad_list, "");
        }
        App.setPreferences(MediaPlayerState.shabad_list, json);

        String jsonShabad = App.getGson().toJson(currentShabad);
        if (App.getPrefranceData(MediaPlayerState.SHABAD) != null && App.getPrefranceData(MediaPlayerState.SHABAD).length() > 0) {
            App.setPreferences(MediaPlayerState.SHABAD, "");
        }
        App.setPreferences(MediaPlayerState.SHABAD, jsonShabad);
    }

    private void showCurrentShabad(int showShabadIndex) {
        if (shabadsList != null && shabadsList.size() > 0) {
            currentShabad = shabadsList.get(showShabadIndex);
            shabadName.setText(currentShabad.getShabadEnglishTitle());
            raagiName.setText(currentShabad.getRaagiName());
            saveLastShabadToPlay();
        }
    }

    public class ShowShabadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                int showShabadIndex = intent.getIntExtra(MediaPlayerState.SHOW_SHABAD, 0);
                showCurrentShabad(showShabadIndex);
            }
        }
    }

}
