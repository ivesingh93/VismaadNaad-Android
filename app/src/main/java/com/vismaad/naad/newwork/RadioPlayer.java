package com.vismaad.naad.newwork;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.vismaad.naad.utils.Utils;

import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.PAUSED;
import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.PLAYING;
import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.STOPPED;

public class RadioPlayer extends AppCompatActivity implements View.OnClickListener {
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    AdView adView_mini;
    ACProgressFlower dialog;
    String name, link, image;
    Bundle bundle;
    TextView radioName;
    ImageView imageRadio, playBtn;
    private RadioPlayerService playerService;
    private UpdateUIReceiver updater;
    private RelativeLayout miniPlayerLayout;
    private TextView shabadName, raagiName;
    RadioPlayerService mRadioPlayerService;
    private boolean isBound;
    private boolean mServiceConnected = false, playSong = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radio_player);
        mRadioPlayerService = App.getRadioService();
        if (Util.SDK_INT > 23) {
            initial();
        }
        updater = new UpdateUIReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(updater,
                new IntentFilter(MediaPlayerState.updateUI));
        playerService = App.getRadioService();
    }

    private void initial() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bundle = getIntent().getExtras();
        playerView = (SimpleExoPlayerView) findViewById(R.id.radio_player);
        playBtn = findViewById(R.id.play_pause_mini_player);

        miniPlayerLayout = findViewById(R.id.mini_player);
        shabadName = findViewById(R.id.shabad_name_mini_player);
        raagiName = findViewById(R.id.raagi_name_mini_player);


        playBtn.setOnClickListener(this);
        MobileAds.initialize(RadioPlayer.this,
                getResources().getString(R.string.YOUR_ADMOB_APP_ID));
        adView_mini = findViewById(R.id.adView_mini);
        radioName = findViewById(R.id.radioName);
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
            startPlayerService();
            doBindService();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (playerService != null) {
            updateUI();
        }


        if (Utils.isMyServiceRunning(ShabadPlayerForegroundService.class, RadioPlayer.this) == true) {
            stopService(new Intent(RadioPlayer.this, ShabadPlayerForegroundService.class));
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updater);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mRadioPlayerService = ((RadioPlayerService.LocalBinder) service).getService();
            // player = shabadPlayerForegroundService.getPlayer();
            playerView.setPlayer(player);
            mServiceConnected = true;
            App.setService(mRadioPlayerService);
        }

        public void onServiceDisconnected(ComponentName className) {
            mRadioPlayerService = null;
            mServiceConnected = false;
        }
    };

    private MediaSource buildMediaSource(Uri uri) {
        dialog.dismiss();

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    @Override
    protected void onStop() {
        super.onStop();
        doUnbindService();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mini_player:
                // redirect to shabad playing screen 3rd screen
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
                break;
            case R.id.play_pause_mini_player:
                playPauseShabad();
                break;
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

    private void updateUI() {
        if (playerService.getStatus() == PLAYING) {
            playBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_button));
        } else if (playerService.getStatus() == PAUSED) {
            playBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_button));
        } else if (playerService.getStatus() == STOPPED) {
            playBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_button));
        }
    }

    private void playPauseShabad() {
        if (playerService.getStatus() == STOPPED) {
            startPlayerService();
            playerService.play();
            //  playerService.setDuration(App.getPreferenceLong(MediaPlayerState.SHABAD_DURATION));
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

    private void doBindService() {
        bindService(new Intent(RadioPlayer.this,
                RadioPlayerService.class), mConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    private void doUnbindService() {
        if (isBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            isBound = false;
        }
    }

}
