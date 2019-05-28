package com.vismaad.naad.newwork;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.vismaad.naad.Constants;
import com.vismaad.naad.R;
import com.vismaad.naad.player.ShabadPlayerActivity;
import com.vismaad.naad.player.service.App;
import com.vismaad.naad.player.service.MediaPlayerState;
import com.vismaad.naad.player.service.RadioPlayerService;
import com.vismaad.naad.player.service.ShabadPlayerForegroundService;
import com.vismaad.naad.rest.model.raagi.MoreRadio;

import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class RadioPlayer extends AppCompatActivity {
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    AdView adView_mini;
    ACProgressFlower dialog;
    String name, link,image;
    Bundle bundle;
    TextView radioName;
    ImageView imageRadio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radio_player);
        if (Util.SDK_INT > 23) {
            initial();
        }

    }

    private void initial() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bundle = getIntent().getExtras();
        playerView = (SimpleExoPlayerView) findViewById(R.id.radio_player);
        MobileAds.initialize(RadioPlayer.this,
                getResources().getString(R.string.YOUR_ADMOB_APP_ID));
        adView_mini = findViewById(R.id.adView_mini);
        radioName  = findViewById(R.id.radioName);
        imageRadio = findViewById(R.id.imageRadio);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView_mini.loadAd(adRequest);
        dialog = new ACProgressFlower.Builder(RadioPlayer.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .bgColor(Color.TRANSPARENT)
                .bgAlpha(0)
                .bgCornerRadius(0)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCanceledOnTouchOutside(true);
        //loadRewardedVideoAd();
        dialog.show();


        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        playerView.setPlayer(player);

        if (bundle != null) {
            name = bundle.getString("RADIO_NAME");
            link = bundle.getString("NAME");
            image = bundle.getString("IMAGE");

            radioName.setText(name);
            RequestOptions option = new RequestOptions().fitCenter()
                    .override(Target.SIZE_ORIGINAL);
            Glide.with(RadioPlayer.this)
                    .load(image)
                    .into(imageRadio);
            Uri uri = Uri.parse(link);
            MediaSource mediaSource = buildMediaSource(uri);
            player.setPlayWhenReady(true);
            player.prepare(mediaSource, true, false);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        dialog.dismiss();

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.release();
        player = null;
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

    private void startPlayerService() {
        Intent intent = new Intent(this, RadioPlayerService.class);
        intent.putExtra(MediaPlayerState.RAAGI_NAME, name);
        intent.putExtra(MediaPlayerState.SHABAD_LINKS, link);
        intent.putExtra(MediaPlayerState.Action_Play, true);
        intent.setAction(Constants.STARTFOREGROUND_ACTION);
        startService(intent);
        App.setPreferencesInt(Constants.PLAYER_STATE, 1);
    }

}
