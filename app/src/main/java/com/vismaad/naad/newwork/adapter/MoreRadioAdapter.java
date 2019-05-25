package com.vismaad.naad.newwork.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.vismaad.naad.R;
import com.vismaad.naad.navigation.home.raagi_detail.RaagiDetailActivity;
import com.vismaad.naad.newwork.PopRagiAndShabad;
import com.vismaad.naad.player.ShabadPlayerActivity;
import com.vismaad.naad.player.service.MediaPlayerState;
import com.vismaad.naad.rest.model.raagi.MoreRadio;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.welcome.signup.SignupActivity;

import java.util.List;

import static com.vismaad.naad.Constants.PLAY_SONG;

public class MoreRadioAdapter extends RecyclerView.Adapter<MoreRadioAdapter.ShabadViewHolder>  {

    private Context context;
    private List<MoreRadio> shabadList;
    int pos = 0;
    private SharedPreferences mSharedPreferences;
    private InterstitialAd mInterstitialAd;
    int count;
    //PopRagiAndShabad.RaagisInfo shabad;

    public MoreRadioAdapter(Context context, List<MoreRadio> shabadList) {
        this.context = context;
        this.shabadList = shabadList;
        mSharedPreferences = context.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public MoreRadioAdapter.ShabadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_shabad_new, parent, false);

        return new MoreRadioAdapter.ShabadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoreRadioAdapter.ShabadViewHolder holder, final int position) {
        pos = position;
        final MoreRadio shabad = shabadList.get(position);

        RequestOptions option = new RequestOptions().fitCenter()
                .override(Target.SIZE_ORIGINAL);

        //   holder.count.setText(""+shabadList.get(position).getShabadsCount());

        holder.img_listn.setVisibility(View.GONE);
        holder.shabad_menu_IV.setVisibility(View.GONE);
        holder.shabads_length_TV.setVisibility(View.GONE);
        holder.listen.setVisibility(View.GONE);



        holder.shabad_title_TV.setText(shabad.getName());
        Glide.with(context)
                .load(shabad.getImageUrl())
                .apply(option)
                .into(holder.shabad_thumbnail_IV);
        holder.shabad_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_intent(holder, shabadList);
            }
        });
    }

    private void create_intent(final MoreRadioAdapter.ShabadViewHolder holder, List<MoreRadio> shabadList) {
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
        //intent.putParcelableArrayListExtra("shabads", mFilteredList);
       // intent.putExtra("current_shabad", raagiInfo.);
       // intent.putExtra(MediaPlayerState.SHABAD_DURATION, (long) 0);

        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return shabadList.size();
    }



    class ShabadViewHolder extends RecyclerView.ViewHolder {

        private ImageView shabad_menu_IV,shabad_thumbnail_IV,img_listn;
        private TextView shabad_title_TV, shabads_length_TV, shabads_raggi,listen;
        private View shabad_card_view;


        public ShabadViewHolder(View itemView) {
            super(itemView);
            listen = itemView.findViewById(R.id.listen);
            shabad_thumbnail_IV = itemView.findViewById(R.id.shabad_thumbnail_IV);
            shabad_title_TV = itemView.findViewById(R.id.shabad_title_TV);
            shabads_length_TV = itemView.findViewById(R.id.shabads_length_TV);
            shabad_menu_IV = itemView.findViewById(R.id.shabad_menu_IV);
            shabad_card_view = itemView.findViewById(R.id.shabad_card_view);
            shabads_raggi = itemView.findViewById(R.id.shabads_raggi);

            img_listn= itemView.findViewById(R.id.img_listn);

        }
    }

    public void createDialog() {
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
                dialog.dismiss();
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
