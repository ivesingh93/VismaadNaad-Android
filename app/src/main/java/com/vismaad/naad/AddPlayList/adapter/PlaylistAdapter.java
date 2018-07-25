package com.vismaad.naad.AddPlayList.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vismaad.naad.AddPlayList.AddPlayList;
import com.vismaad.naad.AddPlayList.PlaylistShabads;
import com.vismaad.naad.AddPlayList.model.JBPlaylistCount;
import com.vismaad.naad.AddPlayList.presenter.DeleteRefresh;
import com.vismaad.naad.AddPlayList.presenter.HolderInterface;
import com.vismaad.naad.AddPlayList.presenter.IGetShabadsList;
import com.vismaad.naad.AddPlayList.presenter.IGetShabadsListHolder;
import com.vismaad.naad.AddPlayList.view.IShabadsList;
import com.vismaad.naad.R;
import com.vismaad.naad.navigation.playlist.delete.PlayListDeletePresenterCompl;
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
import java.util.List;

import static com.vismaad.naad.Constants.PLAY_SONG;

/**
 * Created by satnamsingh on 04/07/18.
 */

public class PlaylistAdapter extends BaseAdapter implements IPlayListView, HolderInterface {

    Context mContext;
    private ArrayList<JBPlaylistCount> shabadList;
    LayoutInflater inflter;
    private boolean isSelected[];
    int position1;
    private SharedPreferences mSharedPreferences;
    boolean isHas;
    PlayListDeletePresenterCompl mPlayListDeletePresenterCompl;
    // DeleteRefresh mDeleteRefresh;
    IGetShabadsListHolder mIGetShabadsList;
    int shabadsSize;

    public PlaylistAdapter(Context mContext, ArrayList<JBPlaylistCount> shabadList, boolean isHas) {
        this.mContext = mContext;
        this.shabadList = shabadList;
        inflter = (LayoutInflater.from(mContext));
        isSelected = new boolean[shabadList.size()];
        //position1 = new ArrayList<Integer>();

        mSharedPreferences = mContext.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
        this.isHas = isHas;
        mPlayListDeletePresenterCompl = new PlayListDeletePresenterCompl(this);

       /*
        try {
            this.mDeleteRefresh = ((DeleteRefresh) mContext);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement AdapterCallback.");
        }*/

    }


    @Override
    public int getCount() {
        return shabadList.size();
    }


    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public boolean[] getSelectedFlags() {
        return isSelected;
    }


    @Override
    public int getViewTypeCount() {
        if (shabadList.size() == 0) {
            return 1;
        }
        return shabadList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final PlaylistAdapter.viewholder mViewholder;

        if (convertView == null) {
            convertView = inflter.inflate(R.layout.cardplaylist, viewGroup, false);
            mViewholder = new PlaylistAdapter.viewholder();
            mViewholder.txtPlayListName = convertView.findViewById(R.id.txtPlayListName);
            mViewholder.txtCreated = convertView.findViewById(R.id.txtCreated);
            mViewholder.shabad_menu_IV = convertView.findViewById(R.id.shabad_menu_IV);
            convertView.setTag(mViewholder);
        } else {
            mViewholder = (PlaylistAdapter.viewholder) convertView.getTag();
        }

        if (isHas == true) {
            mViewholder.shabad_menu_IV.setVisibility(View.VISIBLE);
        } else {
            mViewholder.shabad_menu_IV.setVisibility(View.GONE);
        }
        mViewholder.shabad_menu_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position1 = position;
                showPopupMenu(mViewholder.shabad_menu_IV, shabadList, position);
            }
        });
        mIGetShabadsList = new IGetShabadsListHolder(this);
        //fetchData(shabadList.get(position), position, mViewholder.txtCreated);
        // final Shabad shabad = shabadList.get(position);
        mViewholder.txtPlayListName.setText(shabadList.get(position).getName());
        mViewholder.txtCreated.setText(shabadList.get(position).getShabads_count() + " Shabads");
        // mViewholder.txtCreated.setVisibility(View.GONE);
        //Log.i("login-shabads", String.valueOf(shabadsSize) + " Shabads");
        //mViewholder.txtCreated.setText(String.valueOf(shabadsSize) + " Shabads");

        return convertView;

    }

    /*public void updateAdapter(ArrayList<String> shabadList) {
        this.shabadList = shabadList;

        //and call notifyDataSetChanged
        notifyDataSetChanged();
    }*/

    @Override
    public void onResult(String code, int pageID) {
        if (pageID == 1) {
            if (!code.equalsIgnoreCase("")) {
                try {
                    JSONObject json = (JSONObject) new JSONTokener(code).nextValue();
                    int responseCode = (int) json.get("ResponseCode");
                    String msg = (String) json.get("Message");
                    if (responseCode == 200) {
                        shabadList.remove(position1);

                        //updateAdapter(shabadList);
                        notifyDataSetChanged();
                    }

                    Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onResultget(ArrayList<Shabad> code, int pageID, TextView mTextView) {
        if (pageID == 1) {
            shabadsSize = code.size();

            mTextView.setText(String.valueOf(shabadsSize) + " Shabads");
            Log.i("login-sfas", String.valueOf(shabadsSize) + " Shabads");

        }
    }

    class viewholder {
        private TextView txtPlayListName, txtCreated;
        private ImageView shabad_menu_IV;
    }

    private void showPopupMenu(View view, final List<JBPlaylistCount> raagiInfo, final int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.remove_menu, popup.getMenu());
        // popup.getMenu().getItem(1).setVisible(true);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_remove:
                        // Toast.makeText(context, "Add Favorite", Toast.LENGTH_SHORT).show();
                        if (Utils.isNetworkAvailable(mContext) == true) {
                            mPlayListDeletePresenterCompl.doDeletePlayList1(JBSehajBaniPreferences.getLoginId(mSharedPreferences), raagiInfo.get(position).getName());
                        }
                        break;


                }
                return false;
            }
        });
        popup.show();
    }

    public void fetchData(String playlistName, int pos, TextView mTextView) {
        Log.i("posooo", "" + pos);
        mIGetShabadsList.getListholder(JBSehajBaniPreferences.getLoginId(mSharedPreferences), playlistName, mTextView);


    }
}
