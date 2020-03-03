package com.vismaad.naad.navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.vismaad.naad.Constants;
import com.vismaad.naad.R;
import com.vismaad.naad.databinding.SettingsBinding;
import com.vismaad.naad.player.service.App;
import com.vismaad.naad.player.service.MediaPlayerState;
import com.vismaad.naad.player.service.ShabadPlayerForegroundService;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.utils.Utils;
import com.vismaad.naad.welcome.WelcomeActivity;

/**
 * Created by satnamsingh on 25/06/18.
 */

public class Settings extends Fragment implements View.OnClickListener {
    static public final int STOPPED = -1, PAUSED = 0, PLAYING = 1;

    SettingsBinding binding;
    View view;
    private SharedPreferences mSharedPreferences;
    private AdView mAdView;
    Intent i;
    Uri uri;
    Intent intent;
    private static SimpleExoPlayer player;
    private static int status = STOPPED;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,
                R.layout.settings, container, false);
        view = binding.getRoot();

        initial();
        return view;
    }

    private void initial() {
        MobileAds.initialize(getActivity(),
                getResources().getString(R.string.YOUR_ADMOB_APP_ID));
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mSharedPreferences = getActivity().getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
        // binding.rl1.setOnClickListener(this);
        binding.rl2.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.shabadThumbnailIV.setOnClickListener(this);
        // binding.txtLogout.setOnClickListener(this);
        binding.rlFbLike.setOnClickListener(this);
        binding.txtLikeFb.setOnClickListener(this);
        binding.imageLike.setOnClickListener(this);
        binding.btnSignup.setOnClickListener(this);


        if (JBSehajBaniPreferences.getLoginId(mSharedPreferences).equalsIgnoreCase("")) {
            binding.shabadThumbnailIV.setVisibility(View.GONE);
            binding.txtLogout.setVisibility(View.GONE);
            if (JBSehajBaniPreferences.getBtnSkip(mSharedPreferences).equalsIgnoreCase("YES")) {
                binding.btnSignup.setVisibility(View.VISIBLE);
            } else {
                binding.btnSignup.setVisibility(View.GONE);
            }
        } else {
            binding.shabadThumbnailIV.setVisibility(View.VISIBLE);
            binding.txtLogout.setVisibility(View.VISIBLE);
            binding.btnSignup.setVisibility(View.GONE);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /* ((AppCompatActivity) getActivity()).getSupportActionBar().hide();*/

    }

    @Override
    public void onStop() {
        super.onStop();
        /* ((AppCompatActivity) getActivity()).getSupportActionBar().show();*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSignup:
                JBSehajBaniPreferences.setBtnSkip(mSharedPreferences, "NO");
                Intent mIntent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(mIntent);
                break;

            case R.id.rl_fb_like:
                uri = Uri.parse("https://www.facebook.com/Vismaad-Apps-1413125452234300/"); // missing 'http://' will cause crashed
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

            case R.id.imageLike:
                uri = Uri.parse("https://www.facebook.com/Vismaad-Apps-1413125452234300/"); // missing 'http://' will cause crashed
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;


            case R.id.txtLikeFb:
                uri = Uri.parse("https://www.facebook.com/Vismaad-Apps-1413125452234300/"); // missing 'http://' will cause crashed
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;


            case R.id.shabad_thumbnail_IV:

                Intent stopIntent = new Intent(getActivity(), ShabadPlayerForegroundService.class);
                stopIntent.setAction(Constants.STOPFOREGROUND_ACTION);
                getActivity().startService(stopIntent);
                JBSehajBaniPreferences.setRaggiId(mSharedPreferences, "");
                JBSehajBaniPreferences.setLoginId(mSharedPreferences, "");
                JBSehajBaniPreferences.setJwtToken(mSharedPreferences, "");
                JBSehajBaniPreferences.setBtnSkip(mSharedPreferences, "");
                App.setPreferences(MediaPlayerState.SHABAD, "");
                App.setPreferences(MediaPlayerState.shabad_list, "");
                LoginManager.getInstance().logOut();

                ActivityCompat.finishAffinity(getActivity());
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                break;

            case R.id.rl2:
                boolean isClear = Utils.deleteCache(getContext());
                if (isClear) {
                    Toast.makeText(getActivity(), "Delete cache successfully", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.btnSubmit:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{binding.emailUsernameET.getText().toString()});
                email.putExtra(Intent.EXTRA_SUBJECT, "Question");
                email.putExtra(Intent.EXTRA_TEXT, binding.megET.getText().toString());
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));

                break;

            default:
                break;

        }
    }
}
