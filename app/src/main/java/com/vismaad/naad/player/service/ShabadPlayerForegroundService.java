package com.vismaad.naad.player.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.support.v4.media.*;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Patterns;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.gson.JsonElement;
import com.vismaad.naad.Constants;
import com.vismaad.naad.R;
import com.vismaad.naad.newwork.PopularShabadRaagisActivity;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.playlist.ShabadListener;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.rest.service.PlayList;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vismaad.naad.player.ShabadPlayerActivity.current_shabad;

/**
 * Created by DELL on 1/29/2018.
 */

public class ShabadPlayerForegroundService extends Service {

    static public final int STOPPED = -1, PAUSED = 0, PLAYING = 1;
    public static final String TAG = "MyServiceTag";

    CountDownTimer countDownTimer;

    private static final String CHANNEL_ID = "PlayerShabad";
    private static final long MAX_POSITION_FOR_SEEK_TO_PREVIOUS = 3000;
    private final IBinder mBinder = new LocalBinder();
    public static Context context;
    private Timeline.Window window;
    private HeadphoneReceiver headphoneReceiver;
    private NotificationManager notificationManager;
    private int notification_id;
    private static int status = STOPPED;
    private DefaultTrackSelector trackSelector;
    private static SimpleExoPlayer player;
    private DefaultDataSourceFactory dataSourceFactory;
    static ShabadPlayerForegroundService instance;
    private static boolean headsetConnected = false;
    private String[] shabad_links, shabad_titles;
    String link = "", name = "";
    private String raagi_name;
    private int original_shabad_ind = 0;
    private int lastWindowIndex = -1;
    private Shabad currentShabad;
    private ArrayList<Shabad> shabadList = new ArrayList<>();
    // lock screen control
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private long lastTimePlayPauseClicked;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        window = new Timeline.Window();
        headphoneReceiver = new HeadphoneReceiver();
        notificationManager = new NotificationManager(context);
        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        initPlayer();
        countDownTimer = new CountDownTimer(25 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished / 1000 == 1)
                    listenerCall();
            }

            @Override
            public void onFinish() {

            }
        };
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                countDownTimer.cancel();
                countDownTimer.start();
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            @Override
            public void onSeekProcessed() {
            }
        });
        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                super.onPlayerError(error);
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }


            @Override

            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady && playbackState == Player.STATE_READY) {
                    if (link.equalsIgnoreCase("")) {
                        showNotification();
                    } else {
                        showNotificationRadio();
                    }
                } else {
                    if ( link.equalsIgnoreCase("")) {
                        showNotification();
                    } else {
                        showNotificationRadio();
                    }
                    stopForeground(false);
                }
                if (playWhenReady) {
//                    log("PLAYING");
                    setStatus(PLAYING);
                    setSessionState();
                    updateUI();
                } else {
//                    log("PAUSED");
                    setStatus(PAUSED);
                    setSessionState();
                    updateUI();
                    App.setPreferencesLong(MediaPlayerState.SHABAD_DURATION, player.getCurrentPosition());
                }
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                int latestWindowsIndex = player.getCurrentWindowIndex();
                if (link.equalsIgnoreCase("")) {

                    showNotification();
                } else {
                    showNotificationRadio();
                }
                showShabad(latestWindowsIndex);
                lastWindowIndex = latestWindowsIndex;
            }
        });

        dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), "Exoplayer01");
        //register for headphone plug, notification buttons broadcast
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        filter.addAction(MediaPlayerState.Action_Next);
        filter.addAction(MediaPlayerState.Action_Stop);
        filter.addAction(MediaPlayerState.Action_Pause_play);
        filter.addAction(MediaPlayerState.Action_Previous);
        registerReceiver(headphoneReceiver, filter);

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            //bluetooth button control and lock screen albumName art
            InitializeMediaSession();
        }
    }


    private void listenerCall() {

        if (current_shabad != null && current_shabad.getShabadId() != null && !Constants.shouldCallListerAPI.contains(current_shabad.getShabadId())) {
            Call<JsonElement> call = RetrofitClient.getClient().create(PlayList.class).shabadListeners(new ShabadListener(Integer.parseInt(current_shabad.getShabadId())));
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    Constants.shouldCallListerAPI = Constants.shouldCallListerAPI + current_shabad.getShabadId();
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                }
            });


        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && intent.getAction().equals(Constants.STARTFOREGROUND_ACTION)) {
            link="";

            if (intent.hasExtra(MediaPlayerState.SHABAD_LINKS) && !intent.hasExtra(MediaPlayerState.RADIO)) {

                raagi_name = intent.getStringExtra(MediaPlayerState.RAAGI_NAME);
                shabad_links = intent.getExtras().getStringArray(MediaPlayerState.SHABAD_LINKS);
                shabad_titles = intent.getExtras().getStringArray(MediaPlayerState.SHABAD_TITLES);
                original_shabad_ind = intent.getExtras().getInt(MediaPlayerState.ORIGINAL_SHABAD);
                currentShabad = intent.getExtras().getParcelable(MediaPlayerState.SHABAD);
                shabadList = intent.getParcelableArrayListExtra(MediaPlayerState.shabad_list);
                boolean play = intent.getBooleanExtra(MediaPlayerState.Action_Play, false);
                long duration = intent.getLongExtra(MediaPlayerState.SHABAD_DURATION, 0);

                setPlaylist(shabad_links, original_shabad_ind, play, duration);

                saveLastShabadToPlay();
            }
            if (intent != null && intent.hasExtra(MediaPlayerState.RADIO)) {

                raagi_name = intent.getStringExtra(MediaPlayerState.RAAGI_NAME);
                link = intent.getExtras().getString(MediaPlayerState.SHABAD_LINKS);
                name = intent.getExtras().getString(MediaPlayerState.SHABAD_TITLES);
                original_shabad_ind = intent.getExtras().getInt(MediaPlayerState.ORIGINAL_SHABAD);
                currentShabad = intent.getExtras().getParcelable(MediaPlayerState.SHABAD);
                shabadList = intent.getParcelableArrayListExtra(MediaPlayerState.shabad_list);
                boolean play = intent.getBooleanExtra(MediaPlayerState.Action_Play, false);
                long duration = intent.getLongExtra(MediaPlayerState.SHABAD_DURATION, 0);
                setRadioPlaylist(link, original_shabad_ind, play, duration);
            }


        } else {
            if (intent != null && intent.getAction().equals(
                    Constants.STOPFOREGROUND_ACTION)) {
                stop();
            }
        }
        return START_STICKY;
    }

    private void saveLastShabadToPlay() {
        // store shabad list in shared pref using set in android or in list of json
        String json = App.getGson().toJson(shabadList);
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

    public int getStatus() {
        return status;
    }

    private void setStatus(int s) {
        status = s;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(headphoneReceiver);
    }

    public ArrayList<Shabad> getTrackList() {
        if (shabadList != null && shabadList.size() > 0) {
            return shabadList;
        } else {
            return new ArrayList<>();
        }
    }

    public Shabad getCurrentTrack() {
        return currentShabad;
    }

    public void setDuration(long duration) {
        if (player != null) {
            player.seekTo(duration);
        }
    }

    public void play() {
        if (player != null) {
            getAudioFocusAndPlay();
            setStatus(PLAYING);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                setSessionState();
                setMediaSessionMetadata(true);
            }
        }
    }

    public void pause() {
        if (player != null) {
            player.setPlayWhenReady(false);
            setStatus(PAUSED);
            setSessionState();
        }
    }


    public void next() {
        if (player != null) {
            Timeline timeline = player.getCurrentTimeline();
            if (timeline.isEmpty()) {
                return;
            }
            int windowIndex = player.getCurrentWindowIndex();
            int nextWindowIndex = player.getNextWindowIndex();
            if (nextWindowIndex != C.INDEX_UNSET) {
                player.seekTo(nextWindowIndex, C.TIME_UNSET);
            } else if (timeline.getWindow(windowIndex, window, false).isDynamic) {
                player.seekTo(windowIndex, C.TIME_UNSET);
            }
        }
    }

    public void previous() {
        if (player != null) {
            Timeline timeline = player.getCurrentTimeline();
            if (timeline.isEmpty()) {
                return;
            }
            int windowIndex = player.getCurrentWindowIndex();
            int previousWindowIndex = player.getPreviousWindowIndex();
            if (previousWindowIndex != C.INDEX_UNSET) {
                player.seekTo(previousWindowIndex, C.TIME_UNSET);
            } else {
                player.seekTo(windowIndex, C.TIME_UNSET);
            }
        }
    }

    private void showShabad(int showShabadIndex) {
        Intent intent = new Intent(MediaPlayerState.SHOW_SHABAD);
        intent.putExtra(MediaPlayerState.SHOW_SHABAD, showShabadIndex);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void updateUI() {
        Intent intent = new Intent(MediaPlayerState.updateUI);
        intent.putExtra(MediaPlayerState.updateUI, true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void stop() {
        lastWindowIndex = 0;
        player.setPlayWhenReady(false);
        App.setPreferencesInt(Constants.PLAYER_STATE, 0);
        setStatus(STOPPED);
        setSessionState();
        stopForeground(true);
        stopSelf();
        log("Service Stopped and exit");
    }

    public SimpleExoPlayer getPlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        }
        return player;
    }

    private void showNotification() {
        Intent prevIntent = new Intent();
        prevIntent.setAction(MediaPlayerState.Action_Previous);
        PendingIntent previousPI = PendingIntent.getBroadcast(context, MediaPlayerState.PREVIOUS, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pausePlayIntent = new Intent();
        pausePlayIntent.setAction(MediaPlayerState.Action_Pause_play);
        PendingIntent pausePlayPI = PendingIntent.getBroadcast(context, MediaPlayerState.PAUSE, pausePlayIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent stopIntent = new Intent();
        stopIntent.setAction(MediaPlayerState.Action_Stop);
        PendingIntent stopPI = PendingIntent.getBroadcast(context, MediaPlayerState.STOP, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent();
        nextIntent.setAction(MediaPlayerState.Action_Next);
        PendingIntent nextPI = PendingIntent.getBroadcast(context, MediaPlayerState.NEXT, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, PopularShabadRaagisActivity.class), 0);



        Intent swipeToDismissIntent = new Intent(this, ShabadPlayerForegroundService.class);
        swipeToDismissIntent.setAction(MediaPlayerState.SWIPE_TO_DISMISS);
        PendingIntent pSwipeToDismiss = PendingIntent.getService(this, 0,
                swipeToDismissIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(context, notificationManager.getMainNotificationId());
        } else {
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        }

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setSmallIcon(R.drawable.status_icon)
                .setContentTitle(shabad_titles[player.getCurrentWindowIndex()])
                .setContentText(raagi_name)
                .setDeleteIntent(pSwipeToDismiss)
                .setContentIntent(contentIntent)
                .setChannelId(CHANNEL_ID)
                .setAutoCancel(true);

        KeyguardManager keyguardManager =
                (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

        androidx.media.app.NotificationCompat.MediaStyle mediaStyle = new androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1, 2);

        if (mMediaSession != null) {
            mediaStyle.setMediaSession(mMediaSession.getSessionToken());
        }

        //builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle());

        //posting notification fails for huawei devices in case of mediastyle notification
        boolean isHuawei = (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1
                || android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP)
                && Build.MANUFACTURER.toLowerCase(Locale.getDefault()).contains("huawei");
        if (!isHuawei) {
            builder.setStyle(mediaStyle);
        }

        builder.addAction(new NotificationCompat.Action(R.drawable.ic_skip_previous_black_24dp, "Prev", previousPI));
        if (player.getPlayWhenReady()) {
            builder.addAction(new NotificationCompat.Action(R.drawable.ic_pause_black_24dp, "Pause", pausePlayPI));
        } else {
            builder.addAction(new NotificationCompat.Action(R.drawable.ic_play_arrow_black_24dp, "Play", pausePlayPI));
        }
        builder.addAction(new NotificationCompat.Action(R.drawable.ic_skip_next_black_24dp, "Next", nextPI));
        builder.addAction(new NotificationCompat.Action(R.drawable.ic_close_white_24dp, "Close", stopPI));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        }

        if (keyguardManager.isKeyguardLocked() && Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            builder.setPriority(Notification.PRIORITY_MAX);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        Notification notification = builder.build();
        startForeground(MediaPlayerState.NOTIF_ID, notification);
    }


    private void showNotificationRadio() {
       /* Intent prevIntent = new Intent();
        prevIntent.setAction(MediaPlayerState.Action_Previous);
        PendingIntent previousPI = PendingIntent.getBroadcast(context, MediaPlayerState.PREVIOUS, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
*/
        Intent pausePlayIntent = new Intent();
        pausePlayIntent.setAction(MediaPlayerState.Action_Pause_play);
        PendingIntent pausePlayPI = PendingIntent.getBroadcast(context, MediaPlayerState.PAUSE, pausePlayIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent stopIntent = new Intent();
        stopIntent.setAction(MediaPlayerState.Action_Stop);
        PendingIntent stopPI = PendingIntent.getBroadcast(context, MediaPlayerState.STOP, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

       /* Intent nextIntent = new Intent();
        nextIntent.setAction(MediaPlayerState.Action_Next);
        PendingIntent nextPI = PendingIntent.getBroadcast(context, MediaPlayerState.NEXT, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
*/
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, PopularShabadRaagisActivity.class), 0);

        Intent swipeToDismissIntent = new Intent(this, RadioPlayerService.class);
        swipeToDismissIntent.setAction(MediaPlayerState.SWIPE_TO_DISMISS);
        PendingIntent pSwipeToDismiss = PendingIntent.getService(this, 0, swipeToDismissIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(context, notificationManager.getMainNotificationId());
        } else {
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        }

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setSmallIcon(R.drawable.status_icon)
                .setContentTitle(name)
                .setContentText(raagi_name)
                .setDeleteIntent(pSwipeToDismiss)
                .setContentIntent(contentIntent)
                .setChannelId(CHANNEL_ID)
                .setAutoCancel(true);

        KeyguardManager keyguardManager =
                (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

        androidx.media.app.NotificationCompat.MediaStyle mediaStyle = new androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0);

        if (mMediaSession != null) {
            mediaStyle.setMediaSession(mMediaSession.getSessionToken());
        }
        //posting notification fails for huawei devices in case of mediastyle notification
        boolean isHuawei = (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1
                || android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP)
                && Build.MANUFACTURER.toLowerCase(Locale.getDefault()).contains("huawei");
        if (!isHuawei) {
            builder.setStyle(mediaStyle);
        }

        // builder.addAction(new NotificationCompat.Action(R.drawable.ic_skip_previous_black_24dp, "Prev", previousPI));
        if (player.getPlayWhenReady()) {
            builder.addAction(new NotificationCompat.Action(R.drawable.ic_pause_black_24dp, "Pause", pausePlayPI));
        } else {
            builder.addAction(new NotificationCompat.Action(R.drawable.ic_play_arrow_black_24dp, "Play", pausePlayPI));
        }
        //builder.addAction(new NotificationCompat.Action(R.drawable.ic_skip_next_black_24dp, "Next", nextPI));
        builder.addAction(new NotificationCompat.Action(R.drawable.ic_close_white_24dp, "Close", stopPI));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        }

        if (keyguardManager.isKeyguardLocked() && Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            builder.setPriority(Notification.PRIORITY_MAX);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        Notification notification = builder.build();
        startForeground(MediaPlayerState.NOTIF_ID, notification);
    }


    public void initPlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        } else {
            return;
        }
    }

    public void setPlaylist(String[] songUrl, int currentInd, boolean play, long duration) {
        if (player != null) {
            player.prepare(buildMediaSource(songUrl));
            player.seekTo(currentInd, duration);
            if (play) {
                play();
            }
        }
    }

    public void setRadioPlaylist(String songUrl, int currentInd, boolean play, long duration) {
        if (player != null) {
            Uri mUri = Uri.parse(songUrl);
            MediaSource mediaSource = buildMediaSources(mUri);
            player.prepare(mediaSource);
            player.seekTo(currentInd, duration);
            if (play) {
                play();
            }
        }
    }


    private ConcatenatingMediaSource buildMediaSource(String[] url) {
        ExtractorMediaSource[] mediaS = new ExtractorMediaSource[url.length];
        for (int i = 0; i < url.length; i++) {
            if (Patterns.WEB_URL.matcher(url[i]).matches()) {
                mediaS[i] = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url[i]));
            }
        }
        return new ConcatenatingMediaSource(mediaS);
    }

    private MediaSource buildMediaSources(Uri uri) {


        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }


    public void log(String message) {
    }

    public class LocalBinder extends Binder {
        public ShabadPlayerForegroundService getService() {
            return ShabadPlayerForegroundService.this;
        }
    }

    public ShabadPlayerForegroundService getInstance() {
        return instance;
    }

    public class HeadphoneReceiver extends BroadcastReceiver {

        public HeadphoneReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();

            ServiceConnection mConnection = new ServiceConnection() {
                public void onServiceConnected(ComponentName className, IBinder service) {

                    instance = ((ShabadPlayerForegroundService.LocalBinder) service).getService();
                    log("service connected");

                }

                public void onServiceDisconnected(ComponentName className) {
                    instance = null;
                    log("Service disconnected");
//
                }
            };
            bindService(new Intent(context,
                    ShabadPlayerForegroundService.class), mConnection, Context.BIND_AUTO_CREATE);
            if (s != null) {
                switch (s) {
                    case MediaPlayerState.Action_Pause_play:
                        if (instance != null) {
                            if (instance.player.getPlayWhenReady()) {
                                pause();
                            } else {
                                play();
                            }
                        }
                        break;
                    case MediaPlayerState.Action_Next:
                        if (instance != null) {
                            instance.next();
                            showShabad(player.getCurrentWindowIndex());
                        }

                        break;
                    case MediaPlayerState.Action_Previous:
                        if (instance != null) {
                            instance.previous();

                            showShabad(player.getCurrentWindowIndex());
                        }
                        break;

                    case MediaPlayerState.Action_Stop:
                        if (instance != null) {
                            instance.stop();
                        }
                    default:
                        break;
                }
            }
            if (intent.hasExtra("state")) {
                if (headsetConnected && intent.getIntExtra("state", 0) == 0) {
                    headsetConnected = false;
                    if (instance.player != null && instance.player.getPlayWhenReady()) {
                        instance.pause();
                    }
                } else if (!headsetConnected && intent.getIntExtra("state", 0) == 1) {
                    headsetConnected = true;
                }
            }
        }
    }

    // Audio Focus
    private AudioManager am;
    private boolean playingBeforeInterruption = false;

    public void getAudioFocusAndPlay() {
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        // Request Audio Focus
        int result = am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            player.setPlayWhenReady(true);
        }
    }

    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @TargetApi(Build.VERSION_CODES.O)
        @Override
        public void onAudioFocusChange(int focusChange) {

            // For Receiving phone calls
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                if (getStatus() == PLAYING) {
                    playingBeforeInterruption = true;
                } else {
                    playingBeforeInterruption = false;
                }
                pause();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                if (playingBeforeInterruption == true)
                    play();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                pause();
                //am.abandonAudioFocusRequest(afChangeListener);
            }
        }
    };


    @SuppressLint("WrongConstant")
    @TargetApi(21)
    private void InitializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getApplicationContext(), getPackageName() + "." + TAG);

        mMediaSession.setCallback(new MediaSessionCompat.Callback() {
            public boolean onMediaButtonEvent(@NonNull Intent mediaButtonIntent) {
                return super.onMediaButtonEvent(mediaButtonIntent);
            }

            public void onPause() {
                onPlayPauseButtonClicked();
                super.onPause();
            }

            public void onSkipToPrevious() {
                previous();
                super.onSkipToPrevious();
            }

            public void onSkipToNext() {
                next();
                super.onSkipToNext();
            }

            public void onPlay() {
                onPlayPauseButtonClicked();
                super.onPlay();

            }

            public void onStop() {
                stop();
                super.onStop();
            }

            private void onPlayPauseButtonClicked() {
                //if pressed multiple times in 500 ms, skip to next song
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTimePlayPauseClicked < 500) {
                    next();
                    return;
                }
                lastTimePlayPauseClicked = System.currentTimeMillis();
                play();
            }
        });

        mMediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID | PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
        PlaybackStateCompat state = stateBuilder
                .setState(PlaybackStateCompat.STATE_STOPPED, 0, 1)
                .build();

        mMediaSession.setPlaybackState(state);
        mMediaSession.setActive(true);
    }

    private void setSessionState() {
        //set state play
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP && stateBuilder != null) {
            if (status == PLAYING) {
                stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, 0, 1);
            } else if (status == PAUSED) {
                stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, 0, 1);
            } else {
                stateBuilder.setState(PlaybackStateCompat.STATE_STOPPED, 0, 1);
            }
            mMediaSession.setPlaybackState(stateBuilder.build());
        }
    }

    @TargetApi(21)
    public void setMediaSessionMetadata(final boolean enable) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                if (currentShabad == null) return;
                MediaMetadataCompat.Builder metadataBuilder = new MediaMetadataCompat.Builder();
                metadataBuilder.putString(MediaMetadata.METADATA_KEY_TITLE, currentShabad.getShabadEnglishTitle());
                metadataBuilder.putString(MediaMetadata.METADATA_KEY_ARTIST, currentShabad.getRaagiName());
                metadataBuilder.putLong(MediaMetadata.METADATA_KEY_DURATION, player.getDuration());
                mMediaSession.setMetadata(metadataBuilder.build());
            }
        });
    }

}
