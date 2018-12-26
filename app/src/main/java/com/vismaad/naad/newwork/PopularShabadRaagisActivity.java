package com.vismaad.naad.newwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.reflect.TypeToken;
import com.vismaad.naad.AddPlayList.view.IShabadsList;
import com.vismaad.naad.Constants;
import com.vismaad.naad.R;
import com.vismaad.naad.navigation.NavigationActivity;
import com.vismaad.naad.navigation.home.adapter.RaagiInfoAdapter;
import com.vismaad.naad.newwork.adapter.PopularShabadAdapter;
import com.vismaad.naad.newwork.adapter.PopularRagisAdapter;
import com.vismaad.naad.newwork.adapter.ShabadListAdapterNew;
import com.vismaad.naad.player.service.App;
import com.vismaad.naad.player.service.MediaPlayerState;
import com.vismaad.naad.player.service.ShabadPlayerForegroundService;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.rest.service.RaagiService;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.PAUSED;
import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.PLAYING;
import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.STOPPED;

public class PopularShabadRaagisActivity extends AppCompatActivity  implements IShabadsList,
        View.OnClickListener, AppBarLayout.OnOffsetChangedListener{
    private RaagiService raagiService;
    private ArrayList<PopRagiAndShabad.PopularShabad> ArrpopShabads= new ArrayList<>();
    private List<PopRagiAndShabad.RaagisInfo> ArrpopRagi= new ArrayList<>();
    private PopularShabadAdapter popShabadAdapter;
    private PopularRagisAdapter popRagisRealAdapter;
    RaagiInfoAdapter raagiInfoAdapter;

    private RecyclerView raagi_RV,shabadRecycle;
    private TextView see_more,see_more_ragi;
    ACProgressFlower dialog;
    private RelativeLayout mainLayOut;
    private ShabadPlayerForegroundService playerService;
    private SharedPreferences mSharedPreferences;
    private UpdateUIReceiver updater;
    private ImageView raagi_thumbnail_IV, shabad_menu_IV, playBtn;
    private RelativeLayout miniPlayerLayout;
    private String raagi_image_url, raagi_name="";

    public ArrayList<Shabad> shabadsList = new ArrayList<>();
    private String[] shabadLinks, shabadTitles;
    private int originalShabadIndex = 0;
    private Shabad currentShabad;
    AdView adView_mini;
    private AdView mAdView;
    private TextView shabadName, raagiName;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_shabad_raagis);
        init();


        updater = new UpdateUIReceiver();

        mSharedPreferences = PopularShabadRaagisActivity.this.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(updater, new IntentFilter(MediaPlayerState.updateUI));

        playerService = App.getService();
        JBSehajBaniPreferences.setRaggiId(mSharedPreferences, raagi_name);
        dialog = new ACProgressFlower.Builder(PopularShabadRaagisActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .bgColor(Color.TRANSPARENT)
                .bgAlpha(0)
                .bgCornerRadius(0)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();






        mainLayOut.setVisibility(View.GONE);
        raagiService = RetrofitClient.getClient().create(RaagiService.class);
        popShabadAdapter = new PopularShabadAdapter(PopularShabadRaagisActivity.this, ArrpopShabads);
        popRagisRealAdapter = new PopularRagisAdapter(PopularShabadRaagisActivity.this, ArrpopRagi);

        raagi_RV.setAdapter(popRagisRealAdapter);
        shabadRecycle.setAdapter(popShabadAdapter);

        see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PopularShabadRaagisActivity.this,PopularShabadsActivity.class));
            }
        });
        see_more_ragi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PopularShabadRaagisActivity.this,NavigationActivity.class));
            }
        });

        ArrpopShabads.clear();
        ArrpopRagi.clear();

        Call<PopRagiAndShabad> raagiShabadsCall = raagiService.popularRagiandShabad();

        raagiShabadsCall.enqueue(new Callback<PopRagiAndShabad>() {
            @Override
            public void onResponse(Call<PopRagiAndShabad> call, Response<PopRagiAndShabad> response) {
                ArrpopShabads.addAll(response.body().getPopularShabads());
                ArrpopRagi.addAll( response.body().getRaagisInfo());

                popShabadAdapter.notifyDataSetChanged();
                popRagisRealAdapter.notifyDataSetChanged();
                mainLayOut.setVisibility(View.VISIBLE);
                dialog.dismiss();


            }

            @Override
            public void onFailure(Call<PopRagiAndShabad> call, Throwable t) {
                t.fillInStackTrace();
                dialog.dismiss();

            }
        });



    }



    public void init(){
        miniPlayerLayout = findViewById(R.id.mini_player);
        raagi_RV = findViewById(R.id.raagi_RV);
        shabadRecycle =findViewById(R.id.shabadRecycle);
        see_more =findViewById(R.id.see_more);
        see_more_ragi =findViewById(R.id.see_more_raagis);
        mainLayOut =findViewById(R.id.mainLayOut);
        playBtn = findViewById(R.id.play_pause_mini_player);
        mAdView = findViewById(R.id.adView);
        shabadName = findViewById(R.id.shabad_name_mini_player);
        raagiName = findViewById(R.id.raagi_name_mini_player);
        miniPlayerLayout.setOnClickListener(this);
        playBtn.setOnClickListener(this);

//        shabads_count_TV.setVisibility(View.GONE);
        MobileAds.initialize(PopularShabadRaagisActivity.this,
                getResources().getString(R.string.YOUR_ADMOB_APP_ID));
        adView_mini = findViewById(R.id.adView_mini);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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

            default:
                break;
        }
    }


    @Override
    public void onResult(ArrayList<Shabad> code, int pageID) {

    }

    public class UpdateUIReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                updateUI();
            }
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
    @Override
    protected void onResume() {
        super.onResume();
        checkMiniPlayerVisibility();
        if (playerService != null) {
            updateUI();
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


}
