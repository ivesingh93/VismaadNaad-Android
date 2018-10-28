package com.vismaad.naad.navigation.home.raagi_detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.reflect.TypeToken;
import com.vismaad.naad.Constants;
import com.vismaad.naad.R;
import com.vismaad.naad.custom_views.HeaderView;
import com.vismaad.naad.navigation.NavigationActivity;
import com.vismaad.naad.navigation.home.raagi_detail.adapter.ShabadAdapter;
import com.vismaad.naad.navigation.home.raagi_detail.presenter.RaagiPresenterImpl;
import com.vismaad.naad.navigation.home.raagi_detail.view.RaagiView;
import com.vismaad.naad.player.service.App;
import com.vismaad.naad.player.service.MediaPlayerState;
import com.vismaad.naad.player.service.ShabadPlayerForegroundService;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.utils.Utils;

import java.util.ArrayList;

import jp.wasabeef.fresco.processors.BlurPostprocessor;

import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.PAUSED;
import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.PLAYING;
import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.STOPPED;

public class RaagiDetailActivity extends AppCompatActivity implements RaagiView, View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

//    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
//    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
//    private static final int ALPHA_ANIMATIONS_DURATION = 200;
//
//    private boolean mIsTheTitleVisible = false;
//    private boolean mIsTheTitleContainerVisible = true;
//
//    private LinearLayout linearlayoutTitle;
//    private TextView textviewTitle;

    public ShowShabadReceiver showShabadReceiver;

    private RaagiPresenterImpl raagiPresenterImpl;
    //    private ActionBar raagi_detail_AB;
    private Toolbar toolbar;
    private RecyclerView shabad_RV;
    private ImageView raagi_thumbnail_IV, shabad_menu_IV, playBtn;
    private TextView raagi_name_TV, shabads_count_TV;
    private RelativeLayout miniPlayerLayout;
    AdView adView_mini;
    private RecyclerView.LayoutManager layoutManager;
    private String raagi_image_url, raagi_name;
    private int num_of_shabads, total_shabads_length;

    private ShabadPlayerForegroundService playerService;
    private TextView shabadName, raagiName;
    private Shabad currentShabad;
    private String[] shabadLinks, shabadTitles;
    private int originalShabadIndex = 0;
    //    private int playerState;
    private UpdateUIReceiver updater;

    protected HeaderView toolbarHeaderView, floatHeaderView;

    private CollapsingToolbarLayout toolbarLayout;
    //    private ImageView blurImageView;
    private SimpleDraweeView blurImageView;

    private AppBarLayout appBarLayout;

    private boolean isHideToolbarView = false;
    //private SearchView searchView;

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
    ShabadAdapter shabadAdapter;
    SearchView search;
    LinearLayout rootView;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_raagi_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        updater = new UpdateUIReceiver();
        mSharedPreferences = RaagiDetailActivity.this.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(updater, new IntentFilter(MediaPlayerState.updateUI));

        playerService = App.getService();

        raagi_image_url = getIntent().getStringExtra("raagi_image_url");
        raagi_name = getIntent().getStringExtra("raagi_name");
        num_of_shabads = getIntent().getIntExtra("num_of_shabads", 0);
        total_shabads_length = getIntent().getIntExtra("total_shabads_length", 0);
//        toolbar.setTitle(raagi_name);

        raagiPresenterImpl = new RaagiPresenterImpl(this, RaagiDetailActivity.this);
        raagiPresenterImpl.init();
        LocalBroadcastManager.getInstance(this).registerReceiver(showShabadReceiver, new IntentFilter(MediaPlayerState.SHOW_SHABAD));

        raagiPresenterImpl.prepareRaagiInfo();
        raagiPresenterImpl.prepareShabads(raagi_name);

        JBSehajBaniPreferences.setRaggiId(mSharedPreferences, raagi_name);

        initCollapsingToolbar();
//        startAlphaAnimation(textviewTitle, 0, View.INVISIBLE);


        search.onActionViewExpanded();
        //search.setActivated(true);
        search.setQueryHint("Search Shabads");
        Drawable d = getResources().getDrawable(R.drawable.bg_white_rounded);
        search.setBackground(d);
        search.setFocusable(false);
        search.clearFocus();

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                shabadAdapter.getFilter().filter(query);

                return false;
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       /* getMenuInflater().inflate(R.menu.menusearch, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        // searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search a Shabad");
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        Drawable d = getResources().getDrawable(R.drawable.search_widget_background);
        searchView.setBackground(d);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // listening to search query text change
        searchView.setVisibility(View.GONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                shabadAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                shabadAdapter.getFilter().filter(query);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do what you want when search view expended
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //do what you want  searchview is not expanded
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                return false;
            }
        });*/


        return true;


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

    @Override
    public void init() {
//        raagi_detail_AB = getSupportActionBar();
        showShabadReceiver = new ShowShabadReceiver();
        rootView = findViewById(R.id.root_layout);
        search = findViewById(R.id.search);
        blurImageView = findViewById(R.id.blurred_image);
        appBarLayout = findViewById(R.id.appbar);
        toolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
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
        mAdView = findViewById(R.id.adView);
//        linearlayoutTitle = findViewById(R.id.linearlayout_title);
//        textviewTitle = findViewById(R.id.textview_title);

//        toolbarHeaderView = findViewById(R.id.toolbar_header_view);
//        floatHeaderView = findViewById(R.id.float_header_view);
        MobileAds.initialize(RaagiDetailActivity.this,
                getResources().getString(R.string.YOUR_ADMOB_APP_ID));
        adView_mini = findViewById(R.id.adView_mini);


        playBtn.setOnClickListener(this);
        miniPlayerLayout.setOnClickListener(this);


        // mAdView = findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        // mAdView.loadAd(adRequest);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void showCustomTitleBar() {
    }

    @Override
    public void showRaagiInfo() {
        Glide.with(getApplicationContext())
                .load(raagi_image_url)
                .apply(RequestOptions.circleCropTransform())
                .into(raagi_thumbnail_IV);

        Glide.with(getApplicationContext())
                .load(raagi_image_url)
                .into(extraRaagiImage);

        //INSTANTIATE BLUR POST PROCESSOR
        postprocessor = new BlurPostprocessor(this, BLUR_PRECENTAGE);

        //INSTATNTING IMAGE REQUEST USING POST PROCESSOR AS PARAMETER
        imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(raagi_image_url))
                .setPostprocessor(postprocessor)
                .build();

        //INSTANTATE CONTROLLOR()
        controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(blurImageView.getController())
                .build();

        //LOAD BLURRED IMAGE ON SimpleDraweeView(VIEW)
        blurImageView.setController(controller);

        appBarLayout.addOnOffsetChangedListener(this);

//        toolbarHeaderView.bindTo(raagi_name, "");
//        floatHeaderView.bindTo(raagi_name, num_of_shabads + " shabads - " + total_shabads_length + " minutes");

//        textviewTitle.setText(raagi_name);
        extraRaagiName.setText(raagi_name);
        raagi_name_TV.setText(raagi_name);
        extraShabadCount.setText(num_of_shabads + " shabads - " + total_shabads_length + " minutes");
        shabads_count_TV.setText(num_of_shabads + " shabads - " + total_shabads_length + " minutes");
        checkMiniPlayerVisibility();
    }

    @Override
    public void showShabads(ShabadAdapter shabadAdapter) {
        this.shabadAdapter = shabadAdapter;
        shabad_RV.setLayoutManager(layoutManager);
        shabad_RV.setItemAnimator(new DefaultItemAnimator());
        shabad_RV.setAdapter(shabadAdapter);
        shabad_RV.setNestedScrollingEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_pause_mini_player:
                playPauseShabad();
                break;
            case R.id.mini_player:
                goToMusicPlayer();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkMiniPlayerVisibility();
        if (playerService != null) {
            updateUI();
        }
        search.setQuery("", false);
        rootView.requestFocus();
    }

    private void updateUI() {
        if (playerService.getStatus() == PLAYING) {
            playBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_button));
        } else if (playerService.getStatus() == PAUSED) {
            playBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_button));
        } else if (playerService.getStatus() == STOPPED) {
//            miniPlayerLayout.setVisibility(View.GONE);
            playBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_button));
        }
    }

    private void checkMiniPlayerVisibility() {
        NavigationActivity.shabadsList = App.getGson().fromJson(App.getPrefranceData(MediaPlayerState.shabad_list), new TypeToken<ArrayList<Shabad>>() {
        }.getType());

        currentShabad = App.getGson().fromJson(App.getPrefranceData(MediaPlayerState.SHABAD), new TypeToken<Shabad>() {
        }.getType());

        if (currentShabad != null) {
            if (currentShabad.getShabadEnglishTitle() != null && currentShabad.getShabadEnglishTitle().length() > 0) {
//                if (playerService.getStatus() == STOPPED) {
//                    miniPlayerLayout.setVisibility(View.GONE);
//                } else {
                miniPlayerLayout.setVisibility(View.VISIBLE);
                adView_mini.setVisibility(View.VISIBLE);
                mAdView.setVisibility(View.GONE);
                AdRequest adRequest = new AdRequest.Builder().build();
                adView_mini.loadAd(adRequest);
//                }
                shabadName.setText(currentShabad.getShabadEnglishTitle());
                raagiName.setText(currentShabad.getRaagiName());

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

        if (NavigationActivity.shabadsList != null && NavigationActivity.shabadsList.size() > 0) {
            shabadLinks = new String[NavigationActivity.shabadsList.size()];
            shabadTitles = new String[NavigationActivity.shabadsList.size()];
            for (int i = 0; i < NavigationActivity.shabadsList.size(); i++) {
                shabadLinks[i] = NavigationActivity.shabadsList.get(i).getShabadUrl().replace(" ", "+");
                if (NavigationActivity.shabadsList.get(i).getShabadUrl().equals(currentShabad.getShabadUrl())) {
                    originalShabadIndex = i;
                }
                shabadTitles[i] = NavigationActivity.shabadsList.get(i).getShabadEnglishTitle();
            }
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
        intent.putExtra(MediaPlayerState.shabad_list, NavigationActivity.shabadsList);
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
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(showShabadReceiver);
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
    private void initCollapsingToolbar() {

        toolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.main_background));
                    extraView.setVisibility(View.VISIBLE);
                    isShow = true;
                } else if (isShow) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
                    extraView.setVisibility(View.GONE);
                    isShow = false;
                }
            }
        });
    }

    private void saveLastShabadToPlay() {
        // store shabad list in shared pref using set in android or in list of json
        String json = App.getGson().toJson(NavigationActivity.shabadsList);
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
        if (NavigationActivity.shabadsList != null && NavigationActivity.shabadsList.size() > 0) {
            currentShabad = NavigationActivity.shabadsList.get(showShabadIndex);
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
