package com.vismaad.naad.AddPlayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.vismaad.naad.AddPlayList.adapter.ShabadsAdapter;
import com.vismaad.naad.AddPlayList.adapter.ShabadsListAdaters;
import com.vismaad.naad.AddPlayList.model.JBShabadsList;
import com.vismaad.naad.AddPlayList.presenter.IGetShabadsList;
import com.vismaad.naad.AddPlayList.remove.presenter.ShabadsRemovePresenterCompl;
import com.vismaad.naad.AddPlayList.view.IShabadsList;
import com.vismaad.naad.R;
import com.vismaad.naad.addshabads.model.AddShabadsList;
import com.vismaad.naad.player.ShabadPlayerActivity;
import com.vismaad.naad.player.service.MediaPlayerState;
import com.vismaad.naad.rest.model.playlist.AddShabads;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

import static com.vismaad.naad.Constants.PLAY_SONG;

/**
 * Created by satnamsingh on 22/06/18.
 */

public class ShabadsPlayList extends AppCompatActivity implements IShabadsList,
        com.vismaad.naad.addshabads.view.IPlayListView, AdapterView.OnItemClickListener {
    ListView listSelect;
    Bundle extras;
    String strRaggiName, playlistName;
    private SharedPreferences mSharedPreferences;
    ACProgressFlower dialog;
    IGetShabadsList mIGetShabadsList;
    ShabadsListAdaters mShabadsListAdaters;
    ArrayList<AddShabadsList> mJBArrayList;
    private Menu menu;
    AddShabadsList mAddShabadsList;
    ArrayList<Shabad> mShabadArrayList;
    ArrayList<AddShabadsList> mAddShabadsLists;
    ShabadsRemovePresenterCompl mShabadsRemovePresenterCompl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_selection);
        initial();
        if (extras != null) {
            playlistName = extras.getString("PLAYLIST_NAME");
            Log.i("Playlist-Name", playlistName);
            fetchData();
        }
    }

    private void initial() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        extras = getIntent().getExtras();
        listSelect = (ListView) findViewById(R.id.listSelect);
       // listSelect.setOnItemClickListener(this);
        listSelect.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mSharedPreferences = ShabadsPlayList.this.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);

        dialog = new ACProgressFlower.Builder(ShabadsPlayList.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .bgColor(Color.TRANSPARENT)
                .bgAlpha(0)
                .bgCornerRadius(0)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCanceledOnTouchOutside(true);
        mIGetShabadsList = new IGetShabadsList(this);
        mShabadsRemovePresenterCompl = new ShabadsRemovePresenterCompl(this);

        mAddShabadsLists = new ArrayList<AddShabadsList>();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        MenuItem bedMenuItem = menu.findItem(R.id.action_done);
        bedMenuItem.setTitle("REMOVE");
        return true;
    }

    public void fetchData() {
        if (Utils.isNetworkAvailable(ShabadsPlayList.this) == true) {
            dialog.show();
            mIGetShabadsList.getList(JBSehajBaniPreferences.getLoginId(mSharedPreferences), playlistName);


        } else {
            Utils.showSnackBar(ShabadsPlayList.this, "No internet connection");
        }
    }

    @Override
    public void onResult(ArrayList<Shabad> code, int pageID) {
        if (pageID == 1) {
            Log.i("REMOVELIST", "" + code.size());
            dialog.dismiss();
            mShabadArrayList = code;
            mShabadsListAdaters = new ShabadsListAdaters(ShabadsPlayList.this, code);
            listSelect.setAdapter(mShabadsListAdaters);
            mShabadsListAdaters.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            MenuItem bedMenuItem = menu.findItem(R.id.action_done);
            bedMenuItem.setTitle("REMOVE");

           /* for (int k = 0; k < mShabadsListAdaters.getPos().size(); k++) {
                // Log.i("INSIDE-----", "" + mShabadArrayList.get(mAdapter.getPos().get(k)).getShabadId());
                mAddShabadsList = new AddShabadsList();
                //mShabadArrayList.get(mAdapter.getPos().get(k));
                mAddShabadsList.setId(String.valueOf(mShabadArrayList.get(mShabadsListAdaters.getPos().get(k)).getShabadId()));
                mAddShabadsList.setPlaylist_name(playlistName);
                mAddShabadsList.setUserName(JBSehajBaniPreferences.getLoginId(mSharedPreferences));


                mAddShabadsLists.add(mAddShabadsList);

            }*/
            mShabadsRemovePresenterCompl.removeShabads(mAddShabadsLists);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResult(String code, int pageID) {
        if (pageID == 1) {
            dialog.dismiss();
            if (!code.equalsIgnoreCase("")) {
                try {
                    JSONObject json = (JSONObject) new JSONTokener(code).nextValue();
                    int responseCode = (int) json.get("ResponseCode");
                    String msg = (String) json.get("Message");
                    if (responseCode == 200) {
                        //mShabadsListAdaters.notifyDataSetChanged();
                        fetchData();
                    }

                    Toast toast = Toast.makeText(ShabadsPlayList.this, msg, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.i("NAME", mShabadArrayList.get(position).getShabadEnglishTitle());
        Shabad shabad = mShabadArrayList.get(position);
        Intent intent = new Intent(ShabadsPlayList.this, ShabadPlayerActivity.class);
        intent.putExtra(PLAY_SONG, true);
        intent.putParcelableArrayListExtra("shabads", mShabadArrayList);
        intent.putExtra("current_shabad", shabad);
        intent.putExtra(MediaPlayerState.SHABAD_DURATION, (long) 0);
        startActivity(intent);
    }
}
