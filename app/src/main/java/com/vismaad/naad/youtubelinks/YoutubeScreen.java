package com.vismaad.naad.youtubelinks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.vismaad.naad.R;

public class YoutubeScreen extends YouTubeBaseActivity {

    private String mVideoId;
    private String mVideoUrl;
    private int mSeekTime = 0;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        title= findViewById(R.id.title);
        title.setText(""+getIntent().getStringExtra("title"));
        mVideoId = "M2Eh7zVlmzY";
        mVideoUrl = "https://www.youtube.com/watch?v=M2Eh7zVlmzY";
        if (getIntent().getStringExtra("video_url") != null && getIntent().getStringExtra("video_url").length() > 0) {
            mVideoUrl = getIntent().getStringExtra("video_url");
            if (mVideoUrl.contains("&"))
                mVideoId = mVideoUrl.substring(mVideoUrl.indexOf("=")+1, mVideoUrl.indexOf("&") );
            else
                mVideoId = mVideoUrl.substring(mVideoUrl.indexOf("=")+1);
        }
        Log.e("mVideoId","mVideoId===>"+mVideoId);
        Log.e("mVideoUrl","mVideoUrl===>"+mVideoUrl);
        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize("AIzaSyB9GnNz3UfS72Ad2ayeoWgEyuZjXq30_5E", mOnInitializedListener);
    }


    YouTubePlayer.OnInitializedListener mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                            YouTubePlayer youTubePlayer, boolean wasRestored) {
            if (!wasRestored && mVideoId != null && !mVideoId.equals("")) {
                if (mSeekTime < 0)
                    mSeekTime = 0;

                try {
                    youTubePlayer.loadVideo(mVideoId, mSeekTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {
                    }

                    @Override
                    public void onLoaded(String s) {
                    }

                    @Override
                    public void onAdStarted() {
                    }


                    @Override
                    public void onVideoStarted() {
                        if (isConnected()) {
                            Toast.makeText(YoutubeScreen.this, "success", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onVideoEnded() {
                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {
                    }
                });
            }
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                            YouTubeInitializationResult youTubeInitializationResult) {
            if (youTubeInitializationResult.isUserRecoverableError()) {
                youTubeInitializationResult.getErrorDialog(YoutubeScreen.this,
                        RECOVERY_DIALOG_REQUEST).show();
            } else {

                Toast.makeText(YoutubeScreen.this, "Error", Toast.LENGTH_LONG).show();
            }
        }
    };
    //endregion

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
