package com.vismaad.naad.newwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.vismaad.naad.R;
import com.vismaad.naad.navigation.NavigationActivity;
import com.vismaad.naad.navigation.home.HomeFragment;
import com.vismaad.naad.navigation.home.adapter.RaagiInfoAdapter;
import com.vismaad.naad.navigation.home.view.HomeView;
import com.vismaad.naad.newwork.adapter.PopularRagisAdapter;
import com.vismaad.naad.newwork.adapter.PopularShabadAdapter;
import com.vismaad.naad.newwork.adapter.RadioStationsAdapter;
import com.vismaad.naad.newwork.adapter.kathavaachaksInfoAdapter;
import com.vismaad.naad.player.ShabadPlayerActivity;
import com.vismaad.naad.player.service.App;
import com.vismaad.naad.player.service.MediaPlayerState;
import com.vismaad.naad.player.service.ShabadPlayerForegroundService;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.rest.model.raagi.ShabadTutorial;
import com.vismaad.naad.rest.service.RaagiService;
import com.vismaad.naad.shabadtutorials.ShabadTutoralsAdapter;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vismaad.naad.Constants.PLAY_SONG;
import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.PAUSED;
import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.PLAYING;
import static com.vismaad.naad.player.service.ShabadPlayerForegroundService.STOPPED;

public class HomeFragment2 extends Fragment implements HomeView, ShabadTutoralsAdapter.ItemListener, AdapterView.OnItemClickListener {

    private RaagiService raagiService;
    private ArrayList<PopRagiAndShabad.PopularShabad> ArrpopShabads = new ArrayList<>();
    private List<PopRagiAndShabad.RaagisInfo> ArrpopRagi = new ArrayList<>();
    private ArrayList<PopRagiAndShabad.RadioChannels> ArrayRadioChannel = new ArrayList<>();
    private List<PopRagiAndShabad.kathavaachaksInfo> ArrpopKathavaachaks = new ArrayList<>();

    private PopularShabadAdapter popShabadAdapter;
    private PopularRagisAdapter popRagisRealAdapter;
    private RadioStationsAdapter mRadioStationsAdapter;
    private kathavaachaksInfoAdapter mKathavaachaksInfoAdapter;


    RaagiInfoAdapter raagiInfoAdapter;

    private RecyclerView raagi_RV, shabadRecycle, radioRecycle, rv_katha;
    private TextView see_more, see_more_ragi, radio_see_more, see_more_katha;
    ACProgressFlower dialog;
    private RelativeLayout mainLayOut;
    private ShabadPlayerForegroundService playerService;
    private SharedPreferences mSharedPreferences;
    private PopularShabadRaagisActivity.UpdateUIReceiver updater;
    private ImageView raagi_thumbnail_IV, shabad_menu_IV, playBtn;
    private RelativeLayout miniPlayerLayout;
    private String raagi_image_url, raagi_name = "";

    public ArrayList<Shabad> shabadsList = new ArrayList<>();
    private String[] shabadLinks, shabadTitles;
    private int originalShabadIndex = 0;
    private Shabad currentShabad;
    AdView adView_mini;
    private AdView mAdView;
    private TextView shabadName, raagiName;

    RaagiService mCreatePlayList;
    private ArrayList<Shabad> shabadList = new ArrayList<>();
    Intent mIntent;

    public HomeFragment2() {

    }

 /*   public static HomeFragment newInstance(int player_state) {
        return new HomeFragment();
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        // ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_2, container, false);
        MobileAds.initialize(getActivity(),
                getResources().getString(R.string.YOUR_ADMOB_APP_ID));

        init(view);


        mSharedPreferences = getActivity().getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);


        mCreatePlayList = RetrofitClient.getClient().create(RaagiService.class);

        playerService = App.getService();
        JBSehajBaniPreferences.setRaggiId(mSharedPreferences, raagi_name);
        dialog = new ACProgressFlower.Builder(getActivity())
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
        popShabadAdapter = new PopularShabadAdapter(getActivity(), ArrpopShabads, shabadList);
        popRagisRealAdapter = new PopularRagisAdapter(getActivity(), ArrpopRagi);
        mKathavaachaksInfoAdapter = new kathavaachaksInfoAdapter(getActivity(), ArrpopKathavaachaks);

        mRadioStationsAdapter = new RadioStationsAdapter(getActivity(), ArrayRadioChannel);

        raagi_RV.setAdapter(popRagisRealAdapter);
        shabadRecycle.setAdapter(popShabadAdapter);
        radioRecycle.setAdapter(mRadioStationsAdapter);
        rv_katha.setAdapter(mKathavaachaksInfoAdapter);

        raagi_RV.setNestedScrollingEnabled(false);
        shabadRecycle.setNestedScrollingEnabled(false);
        radioRecycle.setNestedScrollingEnabled(false);
        rv_katha.setNestedScrollingEnabled(false);


        see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent = new Intent(getActivity(), PopularShabadsActivity.class);
                startActivity(mIntent);
                //startActivity(new Intent(getActivity(), PopularShabadsActivity.class));
            }
        });
        see_more_ragi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent = new Intent(getActivity(), NavigationActivity.class);
                mIntent.putExtra("STATUS", "RAGGI");
                startActivity(mIntent);
                // startActivity(new Intent(getActivity(), NavigationActivity.class));
            }
        });

        radio_see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent = new Intent(getActivity(), MoreRadioStation.class);
                startActivity(mIntent);
                //startActivity(new Intent(getActivity(), MoreRadioStation.class));
            }
        });

        see_more_katha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getActivity(), NavigationActivity.class);
                mIntent.putExtra("STATUS", "KATHA");
                startActivity(mIntent);
            }
        });


        ArrpopShabads.clear();
        ArrpopRagi.clear();
        ArrayRadioChannel.clear();
        ArrpopKathavaachaks.clear();

        Call<PopRagiAndShabad> raagiShabadsCall = raagiService.popularRagiandShabad();

        raagiShabadsCall.enqueue(new Callback<PopRagiAndShabad>() {
            @Override
            public void onResponse(Call<PopRagiAndShabad> call, Response<PopRagiAndShabad> response) {
                ArrpopShabads.addAll(response.body().getPopularShabads());
                ArrpopRagi.addAll(response.body().getRaagisInfo());
                ArrayRadioChannel.addAll(response.body().getRadioChannels());
                ArrpopKathavaachaks.addAll(response.body().getkathavaachaksInfo());


                popShabadAdapter.notifyDataSetChanged();
                popRagisRealAdapter.notifyDataSetChanged();
                mRadioStationsAdapter.notifyDataSetChanged();
                mKathavaachaksInfoAdapter.notifyDataSetChanged();
                mainLayOut.setVisibility(View.VISIBLE);


                fetchData();


            }

            @Override
            public void onFailure(Call<PopRagiAndShabad> call, Throwable t) {
                t.fillInStackTrace();
                dialog.dismiss();

            }
        });


        return view;
    }

    public void fetchData() {
        Call<List<Shabad>> call = mCreatePlayList.getPlayListShabads();
        shabadList.clear();
        call.enqueue(new Callback<List<Shabad>>() {
            @Override
            public void onResponse(Call<List<Shabad>> call, Response<List<Shabad>> response) {
                for (Shabad raagiShabad : response.body()) {
                    shabadList.add(raagiShabad);
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Shabad>> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();

        if ((dialog != null) && dialog.isShowing())
            dialog.dismiss();
        dialog = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Shabad shabad = shabadList.get(position);
        Intent intent = new Intent(getActivity(), ShabadPlayerActivity.class);
        intent.putExtra(PLAY_SONG, true);
        intent.putParcelableArrayListExtra("shabads", shabadList);
        intent.putExtra("current_shabad", shabad);
        intent.putExtra(MediaPlayerState.SHABAD_DURATION, (long) 0);
        startActivity(intent);
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
            playBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_pause_button));
        } else if (playerService.getStatus() == PAUSED) {
            playBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_play_button));
        } else if (playerService.getStatus() == STOPPED) {
            playBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_play_button));
        }
    }

    @Override
    public void init(View view) {
        miniPlayerLayout = view.findViewById(R.id.mini_player);
        raagi_RV = view.findViewById(R.id.raagi_RV);
        shabadRecycle = view.findViewById(R.id.shabadRecycle);

        radioRecycle = view.findViewById(R.id.radioRecycle);
        rv_katha = view.findViewById(R.id.rv_katha);
        see_more = view.findViewById(R.id.see_more);
        see_more_ragi = view.findViewById(R.id.see_more_raagis);
        radio_see_more = view.findViewById(R.id.radio_see_more);

        mainLayOut = view.findViewById(R.id.mainLayOut);
        playBtn = view.findViewById(R.id.play_pause_mini_player);
        mAdView = view.findViewById(R.id.adView);
        shabadName = view.findViewById(R.id.shabad_name_mini_player);
        raagiName = view.findViewById(R.id.raagi_name_mini_player);
        see_more_katha = view.findViewById(R.id.see_more_katha);
//        shabads_count_TV.setVisibility(View.GONE);
        MobileAds.initialize(getActivity(),
                getResources().getString(R.string.YOUR_ADMOB_APP_ID));
        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
    }

    @Override
    public void showRaagis(RaagiInfoAdapter raagiInfoAdapter) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(ShabadTutorial item) {

    }


    public interface OnItem1SelectedListener {
        void OnItem1SelectedListener(String item);
    }

}
