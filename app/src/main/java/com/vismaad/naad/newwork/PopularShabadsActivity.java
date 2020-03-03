package com.vismaad.naad.newwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.Postprocessor;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.reflect.TypeToken;
import com.vismaad.naad.addPlayList.view.IShabadsList;
import com.vismaad.naad.Constants;
import com.vismaad.naad.R;
import com.vismaad.naad.custom_views.HeaderView;
import com.vismaad.naad.newwork.adapter.ShabadListAdapterNew;
import com.vismaad.naad.player.service.App;
import com.vismaad.naad.player.service.MediaPlayerState;
import com.vismaad.naad.player.service.ShabadPlayerForegroundService;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.utils.BlurBuilder;
import com.vismaad.naad.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.PAUSED;
import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.PLAYING;
import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.STOPPED;

public class PopularShabadsActivity  extends AppCompatActivity implements IShabadsList,
        View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

//    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
//    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
//    private static final int ALPHA_ANIMATIONS_DURATION = 200;
//
//    private boolean mIsTheTitleVisible = false;
//    private boolean mIsTheTitleContainerVisible = true;
//
//    private LinearLayout linearlayoutTitle;
//    private TextView textviewTitle;


    // private RaagiPresenterImpl raagiPresenterImpl;
    //    private ActionBar raagi_detail_AB;
    private Toolbar toolbar;
    private RecyclerView shabad_RV;
    private ImageView raagi_thumbnail_IV, shabad_menu_IV, playBtn;
    private TextView raagi_name_TV, shabads_count_TV;
    private RelativeLayout miniPlayerLayout;
    private RecyclerView.LayoutManager layoutManager;
    private String raagi_image_url, raagi_name;
    private int num_of_shabads, total_shabads_length;

    private ShabadPlayerForegroundService playerService;
    private TextView shabadName, raagiName;
    private Shabad currentShabad;
    private ArrayList<Shabad> shabadsList = new ArrayList<>();
    private String[] shabadLinks, shabadTitles;
    private int originalShabadIndex = 0;
    //    private int playerState;
    private UpdateUIReceiver updater;

    protected HeaderView toolbarHeaderView, floatHeaderView;

    //    private ImageView blurImageView;
    private SimpleDraweeView blurImageView;


    private boolean isHideToolbarView = false;
    private SearchView searchView;
    IGetShabadsListNew mIGetShabadsList;
    private RelativeLayout extraView;
    private TextView extraRaagiName, extraShabadCount;
    private ImageView extraRaagiImage;

    //variables for image blur using fresco
//    1.Processor
    private Postprocessor postprocessor;
    //2.Image Request
    private ImageRequest imageRequest;
    //3.Controller
    private PipelineDraweeController controller;

    //static variables
    private int BLUR_PRECENTAGE = 50;

    private SharedPreferences mSharedPreferences;
    ShabadListAdapterNew mShabadsListAdaters;
    Bundle extras;
    String strRaggiName, playlistName;
    ACProgressFlower dialog;
    AdView adView_mini;
    androidx.appcompat.widget.SearchView search;
    LinearLayout rootView;
    private AdView mAdView;
    // ShabadsListAdaters mShabadsListAdaters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_popular_shabads);
        init();
        /*if (extras != null) {
            playlistName = extras.getString("PLAYLIST_NAME");
         */   fetchData();
        //}
//        toolbar.setTitle("Popular shabads");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        updater = new UpdateUIReceiver();
        mSharedPreferences = PopularShabadsActivity.this.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(updater,
                new IntentFilter(MediaPlayerState.updateUI));

        playerService = App.getService();

       /* raagi_image_url = getIntent().getStringExtra("raagi_image_url");
        raagi_name = getIntent().getStringExtra("raagi_name");
        num_of_shabads = getIntent().getIntExtra("num_of_shabads", 0);
        total_shabads_length = getIntent().getIntExtra("total_shabads_length", 0);
*///        toolbar.setTitle(raagi_name);

       // 9599972362
        JBSehajBaniPreferences.setRaggiId(mSharedPreferences, raagi_name);

        //initCollapsingToolbar();
//        startAlphaAnimation(textviewTitle, 0, View.INVISIBLE);
        //showRaagiInfo();

        search.setVisibility(View.GONE);
        search.onActionViewExpanded();
        //search.setActivated(true);
        search.setQueryHint("Search Shabads");
        Drawable d = getResources().getDrawable(R.drawable.bg_white_rounded);
        search.setBackground(d);
        search.setFocusable(false);
        search.clearFocus();

        search.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                mShabadsListAdaters.getFilter().filter(query);

                return false;
            }
        });

    }

    public void fetchData() {
        if (Utils.isNetworkAvailable(PopularShabadsActivity.this) == true) {
            dialog.show();
            mIGetShabadsList.getList(JBSehajBaniPreferences.getLoginId(mSharedPreferences), playlistName);
        } else {
            Utils.showSnackBar(PopularShabadsActivity.this, "No internet connection");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return true;
    }


    public void init() {
        rootView = findViewById(R.id.root_layout);
        search = findViewById(R.id.search);
        dialog = new ACProgressFlower.Builder(PopularShabadsActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .bgColor(Color.TRANSPARENT)
                .bgAlpha(0)
                .bgCornerRadius(0)
                .fadeColor(Color.DKGRAY).build();
        extras = getIntent().getExtras();
        mSharedPreferences = PopularShabadsActivity.this.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
        mIGetShabadsList = new IGetShabadsListNew(this);
        dialog.setCanceledOnTouchOutside(true);
        blurImageView = findViewById(R.id.blurred_image);
        layoutManager = new GridLayoutManager(this, 1);
        shabad_RV = findViewById(R.id.shabad_RV);
        raagi_thumbnail_IV = findViewById(R.id.raagi_thumbnail_IV);
        shabad_menu_IV = findViewById(R.id.shabad_menu_IV);
        raagi_name_TV = findViewById(R.id.raagi_name_TV);
        shabads_count_TV = findViewById(R.id.shabads_count_TV);
        playBtn = findViewById(R.id.play_pause_mini_player);
        miniPlayerLayout = findViewById(R.id.mini_player);
        shabadName = findViewById(R.id.shabad_name_mini_player);
        raagiName = findViewById(R.id.raagi_name_mini_player);
        extraView = findViewById(R.id.extra_view);
        extraRaagiImage = findViewById(R.id.extra_raagi_image);
        extraRaagiName = findViewById(R.id.raagi_name_extra);
        extraShabadCount = findViewById(R.id.shabad_count_extra);
        playBtn.setOnClickListener(this);
        miniPlayerLayout.setOnClickListener(this);
        mAdView = findViewById(R.id.adView);

//        shabads_count_TV.setVisibility(View.GONE);
        MobileAds.initialize(PopularShabadsActivity.this,
                getResources().getString(R.string.YOUR_ADMOB_APP_ID));
        adView_mini = findViewById(R.id.adView_mini);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        getSupportActionBar().setTitle("Popular Shabads");
    }

    public void showRaagiInfo() {

        Glide.with(getApplicationContext())
                .load(raagi_image_url)
                .into(raagi_thumbnail_IV);
        Glide.with(this).load(R.drawable.iotrack_black_24dp).into(raagi_thumbnail_IV);
        Glide.with(getApplicationContext())
                .load(R.drawable.iotrack_black_24dp)
                .into(extraRaagiImage);

        final TypedArray imgs = getResources().obtainTypedArray(R.array.apptour);
        final Random rand = new Random();
        final int rndInt = rand.nextInt(imgs.length());
        final int resID = imgs.getResourceId(rndInt, 0);

        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), resID);
        Bitmap blurredBitmap = BlurBuilder.blur(this, originalBitmap);
        blurImageView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));


        extraRaagiName.setText(playlistName);
        raagi_name_TV.setText(playlistName);
        checkMiniPlayerVisibility();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mini_player:
                // redirect to shabad playing screen 3rd screen
                if (JBSehajBaniPreferences.getRadioName(mSharedPreferences).equalsIgnoreCase("")) {
                    create_intent();
                } else {
                    Intent intent = new Intent(PopularShabadsActivity.this, RadioPlayer.class);
                    long duration = App.getPreferenceLong(MediaPlayerState.SHABAD_DURATION);
                    intent.putExtra("DURATION", duration);
                    intent.putExtra("radio", "radio");
                    intent.putExtra("RADIO_NAME", JBSehajBaniPreferences.getRadioName(mSharedPreferences));
                    intent.putExtra("NAME", JBSehajBaniPreferences.getRadioLink(mSharedPreferences));
                    intent.putExtra("IMAGE", JBSehajBaniPreferences.getRadioImage(mSharedPreferences));
                   startActivity(intent);
                }
                break;
            case R.id.play_pause_mini_player:
                playPauseShabad();
                break;
        }
    }
    private void create_intent() {
        Utils.goToShabadPlayerActivity(this);
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
        }
    }

    private void checkMiniPlayerVisibility() {
        shabadsList = App.getGson().fromJson(App.getPrefranceData(MediaPlayerState.shabad_list), new TypeToken<ArrayList<Shabad>>() {
        }.getType());

        currentShabad = App.getGson().fromJson(App.getPrefranceData(MediaPlayerState.SHABAD), new TypeToken<Shabad>() {
        }.getType());

        if (currentShabad != null) {
            if (currentShabad.getShabadEnglishTitle() != null && currentShabad.getShabadEnglishTitle().length() > 0) {
                miniPlayerLayout.setVisibility(View.VISIBLE);
                adView_mini.setVisibility(View.VISIBLE);
                mAdView.setVisibility(View.GONE);
                shabadName.setText(currentShabad.getShabadEnglishTitle());
                raagiName.setText(currentShabad.getRaagiName());
                AdRequest adRequest = new AdRequest.Builder().build();
                adView_mini.loadAd(adRequest);
            } else {
                miniPlayerLayout.setVisibility(View.GONE);
                adView_mini.setVisibility(View.GONE);
                mAdView.setVisibility(View.VISIBLE);
            }
        } else {
            miniPlayerLayout.setVisibility(View.GONE);
            adView_mini.setVisibility(View.GONE);
            mAdView.setVisibility(View.VISIBLE);
        }

        if (shabadsList != null && shabadsList.size() > 0) {
            shabadLinks = new String[shabadsList.size()];
            shabadTitles = new String[shabadsList.size()];
            for (int i = 0; i < shabadsList.size(); i++) {
                shabadLinks[i] = shabadsList.get(i).getShabadUrl().replace(" ", "+");
                if (shabadsList.get(i).getShabadUrl().equals(currentShabad.getShabadUrl())) {
                    originalShabadIndex = i;
                }
                shabadTitles[i] = shabadsList.get(i).getShabadEnglishTitle();
            }
        }

        if (!JBSehajBaniPreferences.getRadioName(mSharedPreferences).equalsIgnoreCase("")) {
            miniPlayerLayout.setVisibility(View.VISIBLE);

            shabadName.setText(JBSehajBaniPreferences.getRadioName(mSharedPreferences));
            raagiName.setText("");
            AdRequest adRequest = new AdRequest.Builder().build();

        }
    }

    private void goToMusicPlayer() {
        // todo go to music player
        Utils.goToShabadPlayerActivity(this);
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
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updater);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
//        int maxScroll = appBarLayout.getTotalScrollRange();
//        float percentage = (float) Math.abs(offset) / (float) maxScroll;

//        handleAlphaOnTitle(percentage);
//        handleToolbarTitleVisibility(percentage);

//        int maxScroll = appBarLayout.getTotalScrollRange();
//        float percentage = (float) Math.abs(offset) / (float) maxScroll;
//
//        if (percentage == 1f && isHideToolbarView) {
//            toolbar.setTitle(raagi_name);
////            toolbarHeaderView.setVisibility(View.VISIBLE);
//            isHideToolbarView = !isHideToolbarView;
//
//        } else if (percentage < 1f && !isHideToolbarView) {
//            toolbar.setTitle(" ");
////            toolbarHeaderView.setVisibility(View.GONE);
//            isHideToolbarView = !isHideToolbarView;
//        }
    }

    @Override
    public void onResult(ArrayList<Shabad> code, int pageID) {
        dialog.dismiss();
        if (pageID == 1) {
            shabad_RV.setLayoutManager(layoutManager);
            shabad_RV.setItemAnimator(new DefaultItemAnimator());
            shabad_RV.setNestedScrollingEnabled(false);
            mShabadsListAdaters = new ShabadListAdapterNew(PopularShabadsActivity.this, code, playlistName);
            shabad_RV.setAdapter(mShabadsListAdaters);
            mShabadsListAdaters.notifyDataSetChanged();
        }
    }

    public class UpdateUIReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                updateUI();
            }
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */

}