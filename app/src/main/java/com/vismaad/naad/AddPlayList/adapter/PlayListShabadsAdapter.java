package com.vismaad.naad.AddPlayList.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.vismaad.naad.AddPlayList.AddPlayList;
import com.vismaad.naad.AddPlayList.ShabadsPlayList;
import com.vismaad.naad.AddPlayList.remove.presenter.ShabadsRemovePresenterCompl;
import com.vismaad.naad.R;
import com.vismaad.naad.addshabads.model.AddShabadsList;
import com.vismaad.naad.navigation.playlist.view.IPlayListView;
import com.vismaad.naad.player.ShabadPlayerActivity;
import com.vismaad.naad.player.service.MediaPlayerState;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.welcome.signup.SignupActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import static com.vismaad.naad.Constants.PLAY_SONG;

/**
 * Created by satnamsingh on 05/07/18.
 */

public class PlayListShabadsAdapter extends RecyclerView.Adapter<PlayListShabadsAdapter.ShabadViewHolder>
        implements com.vismaad.naad.addshabads.view.IPlayListView ,Filterable {

    private Context context;
    private ArrayList<Shabad> shabadList;
    // private List<Shabad> mFilteredList;
    //  int pos = 0;
    private SharedPreferences mSharedPreferences;
    ShabadsRemovePresenterCompl mShabadsRemovePresenterCompl;
    ArrayList<AddShabadsList> mAddShabadsLists;
    AddShabadsList mAddShabadsList;
    String playlist;
    int position1;
    private InterstitialAd mInterstitialAd;
    int count;
    private ArrayList<Shabad> mFilteredList;

    public PlayListShabadsAdapter(Context context, ArrayList<Shabad> shabadList, String playlist) {
        this.context = context;
        this.shabadList = shabadList;
        //  this.mFilteredList = shabadList;
        // mFilteredList = shabadList;
        mSharedPreferences = context.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
        mShabadsRemovePresenterCompl = new ShabadsRemovePresenterCompl(this);
        this.mFilteredList = shabadList;
        mAddShabadsLists = new ArrayList<AddShabadsList>();
        this.playlist = playlist;
    }

    @Override
    public PlayListShabadsAdapter.ShabadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_shabad, parent, false);

        return new PlayListShabadsAdapter.ShabadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlayListShabadsAdapter.ShabadViewHolder holder, final int position) {
        // pos = position;
        final Shabad shabad = mFilteredList.get(position);
        holder.shabads_raggi.setVisibility(View.VISIBLE);
        holder.shabad_title_TV.setText(shabad.getShabadEnglishTitle());
        holder.shabads_length_TV.setText(shabad.getShabadLength());
        holder.shabads_raggi.setText(shabad.getRaagiName());
        // Log.i("RAGGI_NAME", "" + shabadList.get(position).getRaagiName());

        holder.shabad_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_intent(holder, shabad);
            }
        });

        holder.shabad_menu_IV.setVisibility(View.VISIBLE);

        holder.shabad_menu_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position1 = position;
                showPopupMenu(holder.shabad_menu_IV, shabad.getRaagiName(), shabad, position);
            }
        });
        /*
        holder.shabad_thumbnail_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_intent(holder, shabad);
            }
        });

        holder.shabad_title_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_intent(holder, shabad);
            }
        });

        holder.shabads_length_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_intent(holder, shabad);
            }
        });





        holder.rlplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_intent(holder, shabad);
            }
        });
        */

    }

    private void create_intent(final PlayListShabadsAdapter.ShabadViewHolder holder, Shabad shabad) {
        count++;
        if (count % 5 == 0) {
            MobileAds.initialize(context,
                    context.getResources().getString(R.string.YOUR_ADMOB_APP_ID));

            MobileAds.initialize(context,
                    "ca-app-pub-3940256099942544~3347511713");

            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    mInterstitialAd.show();
                }
            });


        }

        Intent intent = new Intent(context, ShabadPlayerActivity.class);
        intent.putExtra(PLAY_SONG, true);
        intent.putParcelableArrayListExtra("shabads", mFilteredList);
        intent.putExtra("current_shabad", shabad);
        intent.putExtra(MediaPlayerState.SHABAD_DURATION, (long) 0);
//        intent.putExtra("shabad_english_title", shabad.getShabadEnglishTitle());
//        intent.putExtra("shabad_length", shabad.getShabadLength());
//        intent.putExtra("sathaayi_id", shabad.getSathaayiId());
//        intent.putExtra("starting_id", shabad.getStartingId());
//        intent.putExtra("ending_id", shabad.getEndingId());
//        intent.putExtra("shabad_url", shabad.getShabadUrl());
//        intent.putExtra("raagi_name", shabad.getRaagiName());

        //TODO - Animate shared elements

        context.startActivity(intent);
    }

    private void showPopupMenu(final View view, final String ragginame, Shabad shabad, final int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.remove_menu, popup.getMenu());
        //popup.getMenu().getItem(1).setVisible(false);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_remove:

                        mAddShabadsList = new AddShabadsList();
                        mAddShabadsList.setId(mFilteredList.get(position).getShabadId());
                        mAddShabadsList.setPlaylist_name(playlist);
                        mAddShabadsList.setUserName(JBSehajBaniPreferences.getLoginId(mSharedPreferences));
                        mAddShabadsLists.add(mAddShabadsList);
                        mShabadsRemovePresenterCompl.removeShabads(mAddShabadsLists);

                       /* if (!JBSehajBaniPreferences.getLoginId(mSharedPreferences).equalsIgnoreCase("")) {
                            Intent mIntent = new Intent(context, AddPlayList.class);
                            mIntent.putExtra("RAGGI_NAME", ragginame);
                            context.startActivity(mIntent);
                        } else {
                            createDialog();
                        }*/


                        break;

                }

                return false;
            }
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        Log.i("size----", "" + mFilteredList.size());
        return mFilteredList.size();
    }

    @Override
    public void onResult(String code, int pageID) {
        if (pageID == 1) {
            //dialog.dismiss();
            if (!code.equalsIgnoreCase("")) {
                try {
                    JSONObject json = (JSONObject) new JSONTokener(code).nextValue();
                    int responseCode = (int) json.get("ResponseCode");
                    String msg = (String) json.get("Message");
                    if (responseCode == 200) {
                        //mShabadsListAdaters.notifyDataSetChanged();
                        Log.i("position", "" + position1);
                        mFilteredList.remove(position1);
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

   /* @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = shabadList;
                } else {
                    List<Shabad> filteredList = new ArrayList<>();
                    for (Shabad row : shabadList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getShabadEnglishTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Shabad>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }*/

    class ShabadViewHolder extends RecyclerView.ViewHolder {

        private ImageView shabad_thumbnail_IV, shabad_menu_IV;
        private TextView shabad_title_TV, shabads_length_TV,shabads_raggi;
        private View shabad_card_view;


        public ShabadViewHolder(View itemView) {
            super(itemView);
            shabad_thumbnail_IV = itemView.findViewById(R.id.shabad_thumbnail_IV);
            shabad_title_TV = itemView.findViewById(R.id.shabad_title_TV);
            shabads_length_TV = itemView.findViewById(R.id.shabads_length_TV);
            shabad_menu_IV = itemView.findViewById(R.id.shabad_menu_IV);
            shabad_card_view = itemView.findViewById(R.id.shabad_card_view);
            shabads_raggi  = itemView.findViewById(R.id.shabads_raggi);

        }
    }

    public void createDialog() {
        // dialog.show();
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_login);
        dialog.setTitle("Alert!");

        dialog.show();

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        final EditText editName = (EditText) dialog.findViewById(R.id.editName);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
                //Intent mIntent = new Intent(context, NavigationActivity.class);
                // context.startActivity(mIntent);
                //((Activity)context).finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(context, SignupActivity.class);
                context.startActivity(mIntent);
                ((Activity) context).finish();
                dialog.dismiss();
            }
        });

    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = shabadList;
                } else {
                    ArrayList<Shabad> filteredList = new ArrayList<>();
                    for (Shabad row : shabadList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getShabadEnglishTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Shabad>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
