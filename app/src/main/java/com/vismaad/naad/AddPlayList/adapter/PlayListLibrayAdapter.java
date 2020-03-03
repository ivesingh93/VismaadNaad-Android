package com.vismaad.naad.addPlayList.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.vismaad.naad.addPlayList.PlaylistShabads;
import com.vismaad.naad.addPlayList.model.JBPlaylistCount;
import com.vismaad.naad.addPlayList.presenter.HolderInterface;
import com.vismaad.naad.addPlayList.presenter.IGetShabadsListHolder;
import com.vismaad.naad.R;
import com.vismaad.naad.navigation.playlist.delete.PlayListDeletePresenterCompl;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satnamsingh on 07/07/18.
 */

public class PlayListLibrayAdapter extends RecyclerView.Adapter<PlayListLibrayAdapter.ShabadViewHolder>
        implements com.vismaad.naad.navigation.playlist.view.IPlayListView, HolderInterface {

    Context mContext;
    private ArrayList<JBPlaylistCount> shabadList;
    int position1;
    private SharedPreferences mSharedPreferences;
    boolean isHas;
    PlayListDeletePresenterCompl mPlayListDeletePresenterCompl;
    IGetShabadsListHolder mIGetShabadsList;
    int shabadsSize;
    ArrayList<Shabad> code;

    public PlayListLibrayAdapter(Context mContext, ArrayList<JBPlaylistCount> shabadList, boolean isHas) {
        this.mContext = mContext;
        this.shabadList = shabadList;
        mSharedPreferences = mContext.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
        this.isHas = isHas;
        mPlayListDeletePresenterCompl = new PlayListDeletePresenterCompl(this);
        code = new ArrayList<Shabad>();
    }

    @Override
    public PlayListLibrayAdapter.ShabadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_playlist, parent, false);
        return new PlayListLibrayAdapter.ShabadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlayListLibrayAdapter.ShabadViewHolder mViewholder, final int position) {
        if (isHas == true) {
            mViewholder.shabad_menu_IV.setVisibility(View.VISIBLE);
        } else {
            mViewholder.shabad_menu_IV.setVisibility(View.GONE);
        }
        mViewholder.txtPlayListName.setText(shabadList.get(position).getName());
        mViewholder.txtCreated.setText(shabadList.get(position).getShabads_count() + " Shabads");
        // mViewholder.txtCreated.setVisibility(View.GONE);
        RequestOptions option = new RequestOptions().fitCenter()
                .override(Target.SIZE_ORIGINAL);

        /*Glide.with(mContext)
                .load(shabadList.get(0).get)
                .apply(option)
                .into(mViewholder.shabad_thumbnail_IV);*/


        mIGetShabadsList = new IGetShabadsListHolder(this);
        //fetchData(shabadList.get(position), position, mViewholder.txtCreated);
        //mViewholder.txtCreated.setText("Created by " + JBSehajBaniPreferences.getLoginId(mSharedPreferences));


        mViewholder.shabad_menu_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position1 = position;
                showPopupMenu(mViewholder.shabad_menu_IV, shabadList, position);
            }
        });

        mViewholder.shabad_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_intent(shabadList, position);
            }
        });

    }

    private void create_intent(ArrayList<JBPlaylistCount> shabadList, final int position) {

        Intent mIntent = new Intent(mContext, PlaylistShabads.class);
        mIntent.putExtra("PLAYLIST_NAME", shabadList.get(position).getName());
        mContext.startActivity(mIntent);
    }

    private void showPopupMenu(View view, final List<JBPlaylistCount> raagiInfo, final int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.playlist_menu, popup.getMenu());
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

    @Override
    public int getItemCount() {
        return shabadList.size();
    }

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
            this.code = code;
            mTextView.setText(String.valueOf(shabadsSize) + " Shabads");

        }
    }

    class ShabadViewHolder extends RecyclerView.ViewHolder {

        private TextView txtPlayListName, txtCreated;
        private ImageView shabad_menu_IV, shabad_thumbnail_IV;
        private View shabad_card_view;

        public ShabadViewHolder(View convertView) {
            super(convertView);
            txtPlayListName = convertView.findViewById(R.id.txtPlayListName);
            txtCreated = convertView.findViewById(R.id.txtCreated);
            shabad_menu_IV = convertView.findViewById(R.id.shabad_menu_IV);
            shabad_thumbnail_IV = itemView.findViewById(R.id.shabad_thumbnail_IV);
            shabad_card_view = itemView.findViewById(R.id.shabad_card_view);
        }
    }

    public void fetchData(String playlistName, int pos, TextView mTextView) {
        mIGetShabadsList.getListholder(JBSehajBaniPreferences.getLoginId(mSharedPreferences), playlistName, mTextView);


    }
}