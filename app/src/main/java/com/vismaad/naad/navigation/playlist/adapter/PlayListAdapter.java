package com.vismaad.naad.navigation.playlist.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.vismaad.naad.AddPlayList.SelectPlayListShabads;
import com.vismaad.naad.AddPlayList.ShabadsPlayList;
import com.vismaad.naad.R;
import com.vismaad.naad.navigation.fetchplaylist.presenter.GetPlayListPresenterCompl;
import com.vismaad.naad.navigation.home.adapter.RaagiInfoAdapter;
import com.vismaad.naad.navigation.home.raagi_detail.RaagiDetailActivity;
import com.vismaad.naad.navigation.playlist.delete.PlayListDeletePresenterCompl;
import com.vismaad.naad.navigation.playlist.view.IPlayListView;
import com.vismaad.naad.rest.model.raagi.RaagiInfo;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satnamsingh on 14/06/18.
 */

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayLListViewHolder> implements IPlayListView {

    private Context context;
    private Activity activity;
    private List<String> PlayListList;
    PlayListDeletePresenterCompl mPlayListDeletePresenterCompl;
    private SharedPreferences mSharedPreferences;
    private GestureDetector mGestureDetector;
    private PlayListAdapter.CustomGestureDetector customGestureDetector;
    String strRaggiName;
    boolean isHas;

    public PlayListAdapter(Activity context, List<String> PlayListList, boolean isHas) {
        this.context = context;
        this.activity = context;
        this.PlayListList = PlayListList;
        customGestureDetector = new PlayListAdapter.CustomGestureDetector();
        mGestureDetector = new GestureDetector(context, customGestureDetector);
        this.strRaggiName = strRaggiName;
        this.isHas = isHas;
    }

    @Override
    public PlayListAdapter.PlayLListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_raagi, parent,
                false);
        mPlayListDeletePresenterCompl = new PlayListDeletePresenterCompl(this);
        mSharedPreferences = context.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
        return new PlayListAdapter.PlayLListViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return PlayListList.size();
    }


    @Override
    public void onBindViewHolder(final PlayListAdapter.PlayLListViewHolder holder, final int position) {

        RequestOptions option = new RequestOptions().fitCenter()
                .override(Target.SIZE_ORIGINAL);

        Glide.with(context)
                .load(R.drawable.folder).apply(option)
                .into(holder.raagi_thumbnail_IV);

        holder.raagi_name_TV.setText(PlayListList.get(position));
        holder.shabads_count_TV.setVisibility(View.GONE);
        holder.raagi_thumbnail_IV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                customGestureDetector.setCustomGestureDetector(PlayListList.get(position));
                mGestureDetector.onTouchEvent(event);
                return true;
//                return animateTheCard(event, holder, raagiInfo);
            }
        });


        holder.raagi_menu_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.raagi_menu_IV, PlayListList, position);
            }
        });
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

                        notifyDataSetChanged();
                    }

                    Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    class PlayLListViewHolder extends RecyclerView.ViewHolder {

        private TextView raagi_name_TV, shabads_count_TV;
        private ImageView raagi_thumbnail_IV, raagi_menu_IV;
        private CardView raagi_card_layout;

        public PlayLListViewHolder(View itemView) {
            super(itemView);
            raagi_thumbnail_IV = itemView.findViewById(R.id.raagi_thumbnail_IV);
            raagi_menu_IV = itemView.findViewById(R.id.raagi_menu_IV);
            raagi_name_TV = itemView.findViewById(R.id.raagi_name_TV);
            shabads_count_TV = itemView.findViewById(R.id.shabads_count_TV);
            raagi_card_layout = itemView.findViewById(R.id.raagi_card_layout);
        }
    }

    private void showPopupMenu(View view, final List<String> raagiInfo, final int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.remove_menu, popup.getMenu());
        popup.getMenu().getItem(1).setVisible(true);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_remove:
                        if (Utils.isNetworkAvailable(context) == true) {
                            mPlayListDeletePresenterCompl.doDeletePlayList1(JBSehajBaniPreferences.getLoginId(mSharedPreferences), raagiInfo.get(position));
                        }
                        break;
                    case R.id.more_options:
                        Toast.makeText(context, "Play now", Toast.LENGTH_SHORT).show();
                        break;

                }
                return false;
            }
        });
        popup.show();
    }

    class CustomGestureDetector implements GestureDetector.OnGestureListener,
            GestureDetector.OnDoubleTapListener {

        String playlistName;

        public void setCustomGestureDetector(String playlistName) {
            this.playlistName = playlistName;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            create_intent(strRaggiName, playlistName);
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    }

    private void create_intent(String strRaggiName, String playlistName) {

        if (isHas == false) {
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context);
            Intent intent = new Intent(context, SelectPlayListShabads.class);
            intent.putExtra("RAGGI_NAME", strRaggiName);
            intent.putExtra("PLAYLIST_NAME", playlistName);
            context.startActivity(intent, activityOptionsCompat.toBundle());
        } else {
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context);
            Intent intent = new Intent(context, ShabadsPlayList.class);
            intent.putExtra("RAGGI_NAME", strRaggiName);
            intent.putExtra("PLAYLIST_NAME", playlistName);
            context.startActivity(intent, activityOptionsCompat.toBundle());

        }
    }
}
