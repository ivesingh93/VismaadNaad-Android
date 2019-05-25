package com.vismaad.naad.newwork;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.vismaad.naad.R;
import com.vismaad.naad.player.view.ShabadPlayerView;
import com.vismaad.naad.rest.model.raagi.Shabad;

public class RadioPlayer extends AppCompatActivity implements ShabadPlayerView {
    private ActionBar shabad_player_AB;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void getIntentValues() {

    }

    @Override
    public void initUI() {
        shabad_player_AB = getSupportActionBar();
    }

    @Override
    public void showCustomAppbar() {
        shabad_player_AB.setDisplayShowTitleEnabled(false);
        shabad_player_AB.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void generateShabadsData() {

    }

    @Override
    public void initPlayer() {
        simpleExoPlayerView = findViewById(R.id.player);
        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        simpleExoPlayerView.setPlayer(player);
    }

    @Override
    public void setFetchedShabadValues(Shabad fetched_shabad) {

    }

    @Override
    public void changeShabadColor(int color) {

    }

    @Override
    public void showGurmukhi() {

    }

    @Override
    public void showGurmukhiTeeka() {

    }

    @Override
    public void showGurmukhiPunjabi() {

    }

    @Override
    public void showGurmukhiEnglish() {

    }

    @Override
    public void showGurmukhiTeekaPunjabi() {

    }

    @Override
    public void showGurmukhiTeekaEnglish() {

    }

    @Override
    public void showGurmukhiPunjabiEnglish() {

    }

    @Override
    public void showGurmukhiTeekaPunjabiEnglish() {

    }

    @Override
    public void changeTranlationSize(int size) {

    }
}
