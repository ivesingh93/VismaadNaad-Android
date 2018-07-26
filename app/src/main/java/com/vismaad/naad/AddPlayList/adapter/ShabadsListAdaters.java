package com.vismaad.naad.AddPlayList.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.vismaad.naad.AddPlayList.model.JBShabadsList;
import com.vismaad.naad.R;
import com.vismaad.naad.player.ShabadPlayerActivity;
import com.vismaad.naad.player.service.MediaPlayerState;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;

import java.util.ArrayList;
import java.util.Random;

import static com.vismaad.naad.Constants.PLAY_SONG;

/**
 * Created by satnamsingh on 25/06/18.
 */

public class ShabadsListAdaters extends BaseAdapter {
    Context mContext;
    private ArrayList<Shabad> shabadList;
    LayoutInflater inflter;
    private boolean isSelected[];
    //ArrayList<Integer> position1;
    private InterstitialAd mInterstitialAd;
    int count;
    private SharedPreferences mSharedPreferences;
    public ShabadsListAdaters(Context mContext, ArrayList<Shabad> shabadList) {
        this.mContext = mContext;
        this.shabadList = shabadList;
        inflter = (LayoutInflater.from(mContext));
        isSelected = new boolean[shabadList.size()];
        // position1 = new ArrayList<Integer>();
        mSharedPreferences = mContext.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ShabadsListAdaters.viewholder mViewholder;
        if (convertView == null) {
            convertView = inflter.inflate(R.layout.card_shabad, viewGroup, false);
            mViewholder = new ShabadsListAdaters.viewholder();


            mViewholder.shabad_thumbnail_IV = convertView.findViewById(R.id.shabad_thumbnail_IV);
            mViewholder.shabad_title_TV = convertView.findViewById(R.id.shabad_title_TV);
            mViewholder.shabads_length_TV = convertView.findViewById(R.id.shabads_length_TV);
            //mViewholder.shabad_menu_IV = convertView.findViewById(R.id.shabad_menu_IV);
            //mViewholder.row_list_checkbox_image = convertView.findViewById(R.id.row_list_checkbox_image);
            //mViewholder.shabad_menu_IV.setVisibility(View.GONE);
            //mViewholder.row_list_checkedtextview = convertView.findViewById(R.id.row_list_checkedtextview);
            mViewholder.rl1 = (CardView) convertView.findViewById(R.id.rl1);
            mViewholder.shabads_raggi = convertView.findViewById(R.id.shabads_raggi);
            mViewholder.shabads_raggi.setVisibility(View.VISIBLE);
            mViewholder.rlplay = convertView.findViewById(R.id.rlplay);
            // mViewholder.row_list_checkbox_image.setVisibility(View.VISIBLE);
            mViewholder.row_list_checkedtextview.setVisibility(View.VISIBLE);

            convertView.setTag(mViewholder);
        } else {
            mViewholder = (ShabadsListAdaters.viewholder) convertView.getTag();
        }


        final Shabad shabad = shabadList.get(position);
        mViewholder.shabad_title_TV.setText(shabad.getShabadEnglishTitle());
        //Log.i("length", ""+shabad.getTo_char());
        mViewholder.shabads_length_TV.setText(shabad.getShabadLength());
        mViewholder.shabads_raggi.setText(shabad.getRaagiName());
  /*      mViewholder.rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set the check text view
                boolean flag = mViewholder.row_list_checkedtextview.isChecked();
                mViewholder.row_list_checkedtextview.setChecked(!flag);
                isSelected[position] = !isSelected[position];
                if (mViewholder.row_list_checkedtextview.isChecked()) {
                    Log.i("SELECTED-POS", "" + position);
                    position1.add(position);
                    // mViewholder.checkedImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tick_ico));
                    // mViewholder.rl1.setBackgroundColor(Color.parseColor("#F16585"));
                    mViewholder.row_list_checkbox_image.setVisibility(View.VISIBLE);
                } else {
                    //holder.checkedImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tick_unselelcted_ico));
                    //mViewholder.rl1.setBackgroundResource(0);
                    mViewholder.row_list_checkbox_image.setVisibility(View.GONE);
                }
            }
        });*/

        mViewholder.rlplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (JBSehajBaniPreferences.getAdsCount(mSharedPreferences) > 0) {
                    count = JBSehajBaniPreferences.getAdsCount(mSharedPreferences);
                }

                count++;

                JBSehajBaniPreferences.setAdsCount(mSharedPreferences, count);
                if (count % 5 == 0) {
                    MobileAds.initialize(mContext,
                            mContext.getResources().getString(R.string.YOUR_ADMOB_APP_ID));

                    mInterstitialAd = new InterstitialAd(mContext);
                    mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    mInterstitialAd.setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            mInterstitialAd.show();
                        }
                    });


                }
                Intent intent = new Intent(mContext, ShabadPlayerActivity.class);
                intent.putExtra(PLAY_SONG, true);
                intent.putParcelableArrayListExtra("shabads", shabadList);
                intent.putExtra("current_shabad", shabad);
                intent.putExtra(MediaPlayerState.SHABAD_DURATION, (long) 0);
                mContext.startActivity(intent);
            }
        });
       /* mViewholder.txtAddress.setText("Address: " + mArrayList.get(position).getAddress1());
        mViewholder.txtLocationZip.setText("City: " + mArrayList.get(position).getCity() + " , "
                + "State: " + mArrayList.get(position).getState());*/


        return convertView;

    }

    class viewholder {
        private ImageView shabad_thumbnail_IV;
        //private ImageView shabad_thumbnail_IV, shabad_menu_IV, row_list_checkbox_image;
        private TextView shabad_title_TV, shabads_length_TV, shabads_raggi;
        CheckedTextView row_list_checkedtextview;
        CardView rl1;
        RelativeLayout rlplay;
    }

}
