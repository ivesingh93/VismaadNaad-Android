package com.vismaad.naad.AddPlayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.vismaad.naad.AddPlayList.adapter.PlaylistAdapter;
import com.vismaad.naad.AddPlayList.model.JBPlaylistCount;
import com.vismaad.naad.R;
import com.vismaad.naad.addshabads.model.AddShabadsList;
import com.vismaad.naad.addshabads.presenter.ShabadsPresenterCompl;
import com.vismaad.naad.databinding.AddPlayBinding;
import com.vismaad.naad.databinding.AddPlaylistNewBinding;
import com.vismaad.naad.databinding.FragmentPlayListBinding;
import com.vismaad.naad.navigation.fetchplaylist.presenter.GetPlayListPresenterCompl;
import com.vismaad.naad.navigation.playlist.adapter.PlayListAdapter;
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

public class AddPlayList extends AppCompatActivity implements IPlayListView, View.OnClickListener, com.vismaad.naad.addshabads.view.IPlayListView {


    //AddPlaylistNewBinding mBinding;
    AddPlayBinding mBinding;
    // FragmentPlayListBinding mBinding;
    private SharedPreferences mSharedPreferences;
    ACProgressFlower dialog;
    IPlayListPresenter playListPresenterCompl;
    GetPlayListPresenterCompl mGetPlayListPresenterCompl;
    ArrayList<JBPlaylistCount> mPlayListArrayList;
    PlaylistAdapter mPlayListAdapter;
    Bundle extras;
    String raggiName, shabadID, shabadName;
    boolean isHas;
    ArrayList<AddShabadsList> mAddShabadsLists;
    AddShabadsList mAddShabadsList;
    ShabadsPresenterCompl mShabadsPresenterCompl;
    private AdView mAdView;
    int pos = 0;
    JBPlaylistCount mJbPlaylistCount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.add_play);
        initial();
        if (extras != null) {

            raggiName = extras.getString("RAGGI_NAME");
            shabadID = extras.getString("SHABAD_ID");
            shabadName = extras.getString("SHABAD_NAME");


            mBinding.raagiRV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> mAdapter, View view, int i, long l) {

                    mAddShabadsLists= new ArrayList<AddShabadsList>();
                    pos = i;
                    mAddShabadsList = new AddShabadsList();
                    mAddShabadsList.setId(shabadID);
                    mAddShabadsList.setPlaylist_name(mPlayListArrayList.get(i).getName());
                    mAddShabadsList.setUserName(JBSehajBaniPreferences.getLoginId(mSharedPreferences));
                    mAddShabadsLists.add(mAddShabadsList);
                    mShabadsPresenterCompl.doAddShabads(mAddShabadsLists);

                }
            });

        }


    }

    private void initial() {
        mShabadsPresenterCompl = new ShabadsPresenterCompl(this);

        extras = getIntent().getExtras();
       // mPlayListArrayList = new ArrayList<String>();
        mSharedPreferences = AddPlayList.this.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
        mBinding.btnCreatePlayList.setOnClickListener(this);
        dialog = new ACProgressFlower.Builder(AddPlayList.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .bgColor(Color.TRANSPARENT)
                .bgAlpha(0)
                .bgCornerRadius(0)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCanceledOnTouchOutside(true);
        playListPresenterCompl = new PlayListPresenterCompl(this);
        mGetPlayListPresenterCompl = new GetPlayListPresenterCompl(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add to playlist");
        mAddShabadsLists = new ArrayList<AddShabadsList>();
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData();
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

                    Toast toast = Toast.makeText(AddPlayList.this, msg, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (pageID == 2) {
            try {
                mPlayListArrayList = new ArrayList<JBPlaylistCount>();

                JSONArray jArr = new JSONArray(code);

                for (int count = 0; count < jArr.length(); count++) {
                    mJbPlaylistCount = new JBPlaylistCount();
                    JSONObject obj = jArr.getJSONObject(count);
                    //String name = obj.getString("name");
                    // String imageName = obj.getString("shabads_count");
                    //so on
                    mJbPlaylistCount.setName(obj.getString("name"));
                    mJbPlaylistCount.setShabads_count(obj.getString("shabads_count"));
                    mPlayListArrayList.add(mJbPlaylistCount);
                    // as

                }

                //fetchData();

                mPlayListAdapter = new PlaylistAdapter(AddPlayList.this, mPlayListArrayList, isHas);
                mBinding.raagiRV.setAdapter(mPlayListAdapter);
                mPlayListAdapter.notifyDataSetChanged();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (pageID == 3) {
            if (!code.equalsIgnoreCase("")) {
                try {
                    JSONObject json = (JSONObject) new JSONTokener(code).nextValue();
                    int responseCode = (int) json.get("ResponseCode");
                    String msg = (String) json.get("Message");
                    if (responseCode == 200) {
                        //finish();
                         fetchData();

                        Toast toast = Toast.makeText(AddPlayList.this, shabadName + " added to " + mPlayListArrayList.get(pos).getName(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else {

                        Toast toast = Toast.makeText(AddPlayList.this, msg, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreatePlayList:

                //createDialog();
                Intent mIntent = new Intent(AddPlayList.this, AddPlayListPopup.class);
                mIntent.putExtra("PLAY_LIST_ARRAYLIST", mPlayListArrayList);
                startActivity(mIntent);
                break;

            default:
                break;
        }
    }


    public void createDialog() {
        // dialog.show();
        final Dialog dialog = new Dialog(AddPlayList.this);
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
                if (Utils.isNetworkAvailable(AddPlayList.this) == true) {
                    if (editName.getText().toString().equalsIgnoreCase("")) {
                        Utils.showSnackBar(AddPlayList.this, "Please enter name");
                    } else {
                        dialog.show();
                        playListPresenterCompl.doCreatePlayList1(JBSehajBaniPreferences.getLoginId(mSharedPreferences)
                                , editName.getText().toString());
                    }
                } else {
                    Utils.showSnackBar(AddPlayList.this, "No internet connection");
                }
                dialog.dismiss();
            }
        });

    }

    public void fetchData() {
        if (Utils.isNetworkAvailable(AddPlayList.this) == true) {
            mGetPlayListPresenterCompl.doFetchList(JBSehajBaniPreferences.getLoginId(mSharedPreferences));
        } else {
            Utils.showSnackBar(AddPlayList.this, "No internet connection");
        }
    }

}
