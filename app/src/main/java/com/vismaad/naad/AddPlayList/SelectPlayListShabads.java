package com.vismaad.naad.AddPlayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.vismaad.naad.AddPlayList.adapter.ShabadsAdapter;
import com.vismaad.naad.AddPlayList.presenter.GetPlayListSelectionPresenterCompl;
import com.vismaad.naad.AddPlayList.presenter.IGetPlayListSelectionPresenter;
import com.vismaad.naad.AddPlayList.view.IPlayListSelectionView;
import com.vismaad.naad.AddPlayList.view.IShabadsSelected;
import com.vismaad.naad.R;
import com.vismaad.naad.addshabads.model.AddShabadsList;
import com.vismaad.naad.addshabads.presenter.ShabadsPresenterCompl;
import com.vismaad.naad.navigation.fetchplaylist.presenter.GetPlayListPresenterCompl;
import com.vismaad.naad.navigation.playlist.presenter.PlayListPresenterCompl;
import com.vismaad.naad.navigation.playlist.view.IPlayListView;
import com.vismaad.naad.player.ShabadPlayerActivity;
import com.vismaad.naad.player.service.MediaPlayerState;
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
 * Created by satnamsingh on 20/06/18.
 */

public class SelectPlayListShabads extends AppCompatActivity implements IPlayListSelectionView,
        AdapterView.OnItemClickListener,
        com.vismaad.naad.addshabads.view.IPlayListView {
    ListView listSelect;
    Bundle extras;
    String strRaggiName, playlistName;
    IGetPlayListSelectionPresenter mIGetPlayListSelectionPresenter;
    GetPlayListSelectionPresenterCompl mGetPlayListSelectionPresenterCompl;
    private SharedPreferences mSharedPreferences;
    ACProgressFlower dialog;
    ShabadsAdapter mAdapter;
    // private Toolbar toolbar;
    private int listSelectPos;
    ArrayList<Shabad> mShabadArrayList;
    ShabadsPresenterCompl mShabadsPresenterCompl;
    ArrayList<AddShabadsList> mAddShabadsLists;
    AddShabadsList mAddShabadsList;
    Shabad shabad;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_selection);
        initial();
        if (extras != null) {
            playlistName = extras.getString("PLAYLIST_NAME");
            Log.i("Playlist-Name", playlistName);
        }
        fetchData();
    }

    private void initial() {
        //toolbar = (Toolbar) findViewById(R.id.tool_bar);
        // Setting toolbar as the ActionBar with setSupportActionBar() call
        //setSupportActionBar(toolbar);
        //mShabadArrayList = new ArrayList<Shabad>();
        extras = getIntent().getExtras();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listSelect = (ListView) findViewById(R.id.listSelect);
         //listSelect.setOnItemClickListener(this);
        listSelect.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mSharedPreferences = SelectPlayListShabads.this.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);

        dialog = new ACProgressFlower.Builder(SelectPlayListShabads.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .bgColor(Color.TRANSPARENT)
                .bgAlpha(0)
                .bgCornerRadius(0)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCanceledOnTouchOutside(true);
        mIGetPlayListSelectionPresenter = new GetPlayListSelectionPresenterCompl(this);
        mShabadsPresenterCompl = new ShabadsPresenterCompl(this);

        mAddShabadsLists = new ArrayList<AddShabadsList>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            //Toast.makeText(SelectPlayListShabads.this, "Action clicked", Toast.LENGTH_LONG).show();
           /* boolean isSelected[] = mAdapter.getSelectedFlags();
          //  ArrayList<Integer> pos= mAdapter.getPos();
            ArrayList<String> selectedItems = new ArrayList<String>();
            for (int i = 0; i < isSelected.length; i++) {
                if (isSelected[i]) {
                    //selectedItems.add(list.get(i));
                    Log.i("INSIDE", ""+isSelected[i]);
                }
            }*/
            // Toast.makeText(SelectPlayListShabads.this, "Selected: " + selectedItems, Toast.LENGTH_SHORT).show();


            for (int k = 0; k < mAdapter.getPos().size(); k++) {
                Log.i("INSIDE-----", "" + mShabadArrayList.get(mAdapter.getPos().get(k)).getShabadId());
                mAddShabadsList = new AddShabadsList();
                //mShabadArrayList.get(mAdapter.getPos().get(k));
                mAddShabadsList.setId(mShabadArrayList.get(mAdapter.getPos().get(k)).getShabadId());
                mAddShabadsList.setPlaylist_name(playlistName);
                mAddShabadsList.setUserName(JBSehajBaniPreferences.getLoginId(mSharedPreferences));


                mAddShabadsLists.add(mAddShabadsList);

            }
            mShabadsPresenterCompl.doAddShabads(mAddShabadsLists);
            //mAddShabadsLists.add(mAddShabadsList);
            // mShabadsPresenterCompl.doAddShabads(mAddShabadsList);


            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }


        return super.onOptionsItemSelected(item);
    }

    public void fetchData() {
        if (Utils.isNetworkAvailable(SelectPlayListShabads.this) == true) {
            dialog.show();
            mIGetPlayListSelectionPresenter.doFetchList(JBSehajBaniPreferences.getRaggiId(mSharedPreferences));


        } else {
            Utils.showSnackBar(SelectPlayListShabads.this, "No internet connection");
        }
    }

    @Override
    public void onResult(final ArrayList<Shabad> code, int pageID) {
        if (pageID == 2) {
            dialog.dismiss();
            //Log.i("kuchbhi", "gdjgkjdsfhdklas");
            mShabadArrayList = code;
            mAdapter = new ShabadsAdapter(SelectPlayListShabads.this, code);
            listSelect.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();


        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //listSelectPos = position;
        //mAdapter.SetSelectedPosition(position);
        //mAdapter.notifyDataSetChanged();

        Log.i("NAME", mShabadArrayList.get(position).getShabadEnglishTitle());
        shabad = mShabadArrayList.get(position);
        Intent intent = new Intent(SelectPlayListShabads.this, ShabadPlayerActivity.class);
        intent.putExtra(PLAY_SONG, true);
        intent.putParcelableArrayListExtra("shabads", mShabadArrayList);
        intent.putExtra("current_shabad", shabad);
        intent.putExtra(MediaPlayerState.SHABAD_DURATION, (long) 0);
        startActivity(intent);

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
                        finish();
                        //  fetchData();
                    }

                    Toast toast = Toast.makeText(SelectPlayListShabads.this, msg, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
