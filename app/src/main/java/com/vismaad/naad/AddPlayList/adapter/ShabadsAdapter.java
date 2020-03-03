package com.vismaad.naad.addPlayList.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.cardview.widget.CardView;

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
import com.vismaad.naad.R;
import com.vismaad.naad.player.ShabadPlayerActivity;
import com.vismaad.naad.player.service.MediaPlayerState;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;

import java.util.ArrayList;

import static com.vismaad.naad.Constants.PLAY_SONG;

/**
 * Created by satnamsingh on 20/06/18.
 */

public class ShabadsAdapter extends BaseAdapter {
    Context mContext;
    private ArrayList<Shabad> shabadList;
    LayoutInflater inflter;
    private boolean isSelected[];
    ArrayList<Integer> position1;
    private InterstitialAd mInterstitialAd;
    int count;
    private SharedPreferences mSharedPreferences;
    public ShabadsAdapter(Context mContext, ArrayList<Shabad> shabadList) {
        this.mContext = mContext;
        this.shabadList = shabadList;
        inflter = (LayoutInflater.from(mContext));
        isSelected = new boolean[shabadList.size()];
        position1 = new ArrayList<Integer>();
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

    public ArrayList<Integer> getPos() {
        return position1;
    }

    @Override
    public int getViewTypeCount() {
        if(shabadList.size()==0){
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
        final viewholder mViewholder;
        if (convertView == null) {
            convertView = inflter.inflate(R.layout.card_shabad, viewGroup, false);
            mViewholder = new viewholder();


            mViewholder.shabad_thumbnail_IV = convertView.findViewById(R.id.shabad_thumbnail_IV);
            mViewholder.shabad_title_TV = convertView.findViewById(R.id.shabad_title_TV);
            mViewholder.shabads_length_TV = convertView.findViewById(R.id.shabads_length_TV);
            mViewholder.shabad_menu_IV = convertView.findViewById(R.id.shabad_menu_IV);
            // mViewholder.row_list_checkbox_image = convertView.findViewById(R.id.row_list_checkbox_image);
            mViewholder.shabad_menu_IV.setVisibility(View.GONE);
            //mViewholder.row_list_checkedtextview = convertView.findViewById(R.id.row_list_checkedtextview);
            mViewholder.rl1 = (CardView) convertView.findViewById(R.id.rl1);

            mViewholder.shabads_raggi = convertView.findViewById(R.id.shabads_raggi);
            mViewholder.shabads_raggi.setVisibility(View.VISIBLE);
            mViewholder.row_list_checkedtextview.setVisibility(View.VISIBLE);
            mViewholder.rlplay =(RelativeLayout)convertView.findViewById(R.id.rlplay);



            convertView.setTag(mViewholder);
        } else {
            mViewholder = (viewholder) convertView.getTag();
        }


        final Shabad shabad = shabadList.get(position);
        mViewholder.shabad_title_TV.setText(shabad.getShabadEnglishTitle());
        mViewholder.shabads_length_TV.setText(shabad.getShabadLength());
       // mViewholder.shabads_raggi.setText(shabad.getRaagiName().toString());
        mViewholder.rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set the check text view
                boolean flag = mViewholder.row_list_checkedtextview.isChecked();
                mViewholder.row_list_checkedtextview.setChecked(!flag);
                isSelected[position] = !isSelected[position];
                if (mViewholder.row_list_checkedtextview.isChecked()) {
                    position1.add(position);
                    mViewholder.row_list_checkbox_image.setVisibility(View.VISIBLE);
                } else {
                    mViewholder.row_list_checkbox_image.setVisibility(View.GONE);
                }
            }
        });


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
                    mInterstitialAd.setAdUnitId(mContext.getResources().getString(R.string.ads_full));
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

        return convertView;

    }

    class viewholder {
        private ImageView shabad_thumbnail_IV, shabad_menu_IV, row_list_checkbox_image;
        private TextView shabad_title_TV, shabads_length_TV, shabads_raggi;
        CheckedTextView row_list_checkedtextview;
        RelativeLayout rlplay;
        CardView rl1;
    }
}
