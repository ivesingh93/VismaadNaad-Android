package com.vismaad.naad.navigation.home.raagi_detail.adapter;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.vismaad.naad.AddPlayList.AddPlayList;
import com.vismaad.naad.R;
import com.vismaad.naad.player.ShabadPlayerActivity;
import com.vismaad.naad.player.service.MediaPlayerState;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.welcome.signup.SignupActivity;

import java.util.ArrayList;
import java.util.List;

import static com.vismaad.naad.Constants.PLAY_SONG;

/**
 * Created by ivesingh on 2/3/18.
 */

public class ShabadAdapter extends RecyclerView.Adapter<ShabadAdapter.ShabadViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Shabad> shabadList;
    private List<Shabad> mFilteredList;
    int pos = 0;
    private SharedPreferences mSharedPreferences;
    private InterstitialAd mInterstitialAd;
    int count;


    public ShabadAdapter(Context context, ArrayList<Shabad> shabadList) {
        this.context = context;
        this.shabadList = shabadList;
        this.mFilteredList = shabadList;
        // mFilteredList = shabadList;
        mSharedPreferences = context.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);


    }

    @Override
    public ShabadAdapter.ShabadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_shabad, parent, false);

        return new ShabadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShabadAdapter.ShabadViewHolder holder, final int position) {
        pos = position;
        final Shabad shabad = mFilteredList.get(position);
        Log.i("name", shabad.getShabadEnglishTitle());
        holder.shabad_title_TV.setText(shabad.getShabadEnglishTitle());
        holder.shabads_length_TV.setText(shabad.getShabadLength());
        // Log.i("RAGGI_NAME", "" + shabadList.get(position).getRaagiName());

        holder.shabad_menu_IV.setVisibility(View.GONE);
        holder.shabad_thumbnail_IV.setText((position + 1) + ".");
        holder.listen.setText(shabad.getListeners() + "");

        holder.shabad_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_intent(holder, shabad);
            }
        });



        /*
        holder.shabad_menu_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.shabad_menu_IV, shabad);
            }
        });


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

        holder.shabad_menu_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.shabad_menu_IV, shabad);
            }
        });

        holder.rlplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_intent(holder, shabad);
            }
        });

        holder.rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_intent(holder, shabad);
            }
        });

        */

    }

    private void create_intent(final ShabadAdapter.ShabadViewHolder holder, Shabad shabad) {
        if (JBSehajBaniPreferences.getAdsCount(mSharedPreferences) > 0) {
            count = JBSehajBaniPreferences.getAdsCount(mSharedPreferences);
        }

        count++;

        JBSehajBaniPreferences.setAdsCount(mSharedPreferences, count);
        if (count % 5 == 0) {
            MobileAds.initialize(context,
                    context.getResources().getString(R.string.YOUR_ADMOB_APP_ID));

            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.ads_full));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    mInterstitialAd.show();
                }
            });
        }
        Intent intent = new Intent(context, ShabadPlayerActivity.class);
        intent.putExtra(PLAY_SONG, true);
        intent.putParcelableArrayListExtra("shabads", shabadList);
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

   /* private void showPopupMenu(final View view, final Shabad shabad) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.raagi_card, popup.getMenu());
        popup.getMenu().getItem(2).setVisible(false);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_favorite:
                        //Toast.makeText(context, "Add Favorite" + shabad.getRaagiName(), Toast.LENGTH_SHORT).show();

                        //context.sendBroadcast(new Intent("start.fragment.action"));

                        // ((Activity) context).finish();
                        // Intent intent = new Intent("BROADCAST_RANDOM_NUMBER");
                        //  intent.putExtra("SHABAD_ID", )
                        // LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        // Log.i("RAGGI_NAME", ragginame);

                        if (!JBSehajBaniPreferences.getLoginId(mSharedPreferences).equalsIgnoreCase("")) {
                            Intent mIntent = new Intent(context, AddPlayList.class);
                            mIntent.putExtra("RAGGI_NAME", shabad.getRaagiName());
                            mIntent.putExtra("SHABAD_ID", shabad.getShabadId());
                            mIntent.putExtra("SHABAD_NAME", shabad.getShabadEnglishTitle());
                            context.startActivity(mIntent);
                        } else {
                            createDialog();
                        }


                        break;
                    case R.id.play_now:
                        Toast.makeText(context, "Play now", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
        popup.show();
    }*/

    @Override
    public int getItemCount() {
        return mFilteredList.size();
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
    }

    class ShabadViewHolder extends RecyclerView.ViewHolder {

        private ImageView shabad_menu_IV;
        private TextView shabad_title_TV, shabads_length_TV, shabad_thumbnail_IV, listen;
        private RelativeLayout rlplay;
        private CardView rl1;
        private View shabad_card_view;

        public ShabadViewHolder(View itemView) {
            super(itemView);
            shabad_thumbnail_IV = itemView.findViewById(R.id.shabad_thumbnail_IV);
            shabad_title_TV = itemView.findViewById(R.id.shabad_title_TV);
            shabads_length_TV = itemView.findViewById(R.id.shabads_length_TV);
            shabad_menu_IV = itemView.findViewById(R.id.shabad_menu_IV);
            listen = itemView.findViewById(R.id.listen);
            rlplay = itemView.findViewById(R.id.rlplay);
            //  rl1=itemView.findViewById(R.id.rl1);
            shabad_card_view = itemView.findViewById(R.id.shabad_card_view);
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

}