package com.vismaad.naad.navigation.playlist;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.vismaad.naad.AddPlayList.AddPlayListPopup;
import com.vismaad.naad.AddPlayList.PlaylistShabads;
import com.vismaad.naad.AddPlayList.adapter.PlayListLibrayAdapter;
import com.vismaad.naad.AddPlayList.adapter.PlayListShabadsAdapter;
import com.vismaad.naad.AddPlayList.adapter.PlaylistAdapter;
import com.vismaad.naad.AddPlayList.model.JBPlaylistCount;
import com.vismaad.naad.R;
import com.vismaad.naad.databinding.AddPlaylistNewBinding;
import com.vismaad.naad.navigation.fetchplaylist.presenter.GetPlayListPresenterCompl;
import com.vismaad.naad.navigation.playlist.presenter.IPlayListPresenter;
import com.vismaad.naad.navigation.playlist.presenter.PlayListPresenterCompl;
import com.vismaad.naad.navigation.playlist.view.IPlayListView;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by satnamsingh on 14/06/18.
 */

public class PlayListFrag extends Fragment implements IPlayListView, View.OnClickListener

{
    AddPlaylistNewBinding binding;
    View view;
    private SharedPreferences mSharedPreferences;
    ACProgressFlower dialog;
    IPlayListPresenter playListPresenterCompl;
    GetPlayListPresenterCompl mGetPlayListPresenterCompl;
    ArrayList<JBPlaylistCount> mPlayListArrayList;
    JBPlaylistCount mJbPlaylistCount;
    PlaylistAdapter mPlayListAdapter;
    boolean isHas;
    PlayListLibrayAdapter mPlayListLibrayAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private AdView mAdView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.add_playlist_new, container, false);
        view = binding.getRoot();
        initial();

        fetchData();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
       /* ((AppCompatActivity) getActivity()).getSupportActionBar().hide();*/
        fetchData();
    }

    @Override
    public void onStop() {
        super.onStop();
       /* ((AppCompatActivity) getActivity()).getSupportActionBar().show();*/
    }

    private void initial() {
        MobileAds.initialize(getActivity(),
                getResources().getString(R.string.YOUR_ADMOB_APP_ID));
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // mPlayListArrayList = new ArrayList<JBPlaylistCount>();
        mSharedPreferences = getActivity().getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
        binding.btnCreatePlayList.setOnClickListener(this);
        binding.btnCreatePlayList.setText("Create playlist");
        //binding.btnCreatePlayList.setVisibility(View.GONE);
        dialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .bgColor(Color.TRANSPARENT)
                .bgAlpha(0)
                .bgCornerRadius(0)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCanceledOnTouchOutside(true);
        playListPresenterCompl = new PlayListPresenterCompl(this);
        mGetPlayListPresenterCompl = new GetPlayListPresenterCompl(this);

        layoutManager = new GridLayoutManager(getActivity(), 1);
    }

    @Override
    public void onResult(String code, int pageID) {
        dialog.dismiss();
        if (pageID == 1) {
            if (!code.equalsIgnoreCase("")) {
                try {
                    JSONObject json = (JSONObject) new JSONTokener(code).nextValue();
                    int responseCode = (int) json.get("ResponseCode");
                    String msg = (String) json.get("Message");
                    if (responseCode == 200) {
                        fetchData();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (pageID == 2) {
            try {
                mPlayListArrayList = new ArrayList<JBPlaylistCount>();

                //JSONArray json = new JSONArray(code);


                JSONArray jArr = new JSONArray(code);

                for (int count = 0; count < jArr.length(); count++) {
                    mJbPlaylistCount = new JBPlaylistCount();
                    JSONObject obj = jArr.getJSONObject(count);
                    //String name = obj.getString("name");
                    // String imageName = obj.getString("shabads_count");
                    //so on
                    mJbPlaylistCount.setName(obj.getString("name"));
                    mJbPlaylistCount.setShabads_count(obj.getString("shabads_count"));

                    // as
                    mPlayListArrayList.add(mJbPlaylistCount);
                }



                isHas = true;
                binding.raagiRV.setLayoutManager(layoutManager);
                binding.raagiRV.setItemAnimator(new DefaultItemAnimator());
                binding.raagiRV.setNestedScrollingEnabled(false);
                mPlayListLibrayAdapter = new PlayListLibrayAdapter(getActivity(), mPlayListArrayList, isHas);
                binding.raagiRV.setAdapter(mPlayListLibrayAdapter);
                mPlayListLibrayAdapter.notifyDataSetChanged();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreatePlayList:

                // createDialog();
                Intent mIntent = new Intent(getActivity(), AddPlayListPopup.class);
                mIntent.putExtra("PLAY_LIST_ARRAYLIST", mPlayListArrayList);
                startActivity(mIntent);
                break;

            default:
                break;
        }
    }


    public void createDialog() {
        // dialog.show();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Create Playlist");

        dialog.show();

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        final EditText editName = (EditText) dialog.findViewById(R.id.editName);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                if (Utils.isNetworkAvailable(getActivity()) == true) {
                    if (editName.getText().toString().equalsIgnoreCase("")) {
                        Utils.showSnackBar(getActivity(), "Please enter name");
                    } else {
                        dialog.show();
                        playListPresenterCompl.doCreatePlayList1(JBSehajBaniPreferences.getLoginId(mSharedPreferences)
                                , editName.getText().toString());
                    }
                } else {
                    Utils.showSnackBar(getActivity(), "No internet connection");
                }
                dialog.dismiss();
            }
        });

    }

    public void fetchData() {
        if (Utils.isNetworkAvailable(getActivity()) == true) {
            mGetPlayListPresenterCompl.doFetchList(JBSehajBaniPreferences.getLoginId(mSharedPreferences));
        } else {
            Utils.showSnackBar(getActivity(), "No internet connection");
        }
    }
}
