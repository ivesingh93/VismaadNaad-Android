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
import android.widget.RelativeLayout;
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
import com.vismaad.naad.newwork.RadioPlayer;
import com.vismaad.naad.player.ShabadPlayerActivity;
import com.vismaad.naad.player.service.MediaPlayerState;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.welcome.signup.SignupActivity;

import java.util.ArrayList;
import java.util.List;

import static com.vismaad.naad.Constants.PLAY_SONG;

public class RadioStationsAdapter extends RecyclerView.Adapter<RadioStationsAdapter.ShabadViewHolder> {

    private Context context;
    private List<PopRagiAndShabad.RadioChannels> shabadList;
    int pos = 0;
    private SharedPreferences mSharedPreferences;
    private InterstitialAd mInterstitialAd;
    int count;
    //PopRagiAndShabad.RaagisInfo shabad;

    public RadioStationsAdapter(Context context, List<PopRagiAndShabad.RadioChannels> shabadList) {
        this.context = context;
        this.shabadList = shabadList;
        mSharedPreferences = context.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public RadioStationsAdapter.ShabadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_raagi, parent, false);

        return new RadioStationsAdapter.ShabadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RadioStationsAdapter.ShabadViewHolder holder, final int position) {
        pos = position;
        final PopRagiAndShabad.RadioChannels shabad = shabadList.get(position);

        RequestOptions option = new RequestOptions().fitCenter()
                .override(Target.SIZE_ORIGINAL);

        //   holder.count.setText(""+shabadList.get(position).getShabadsCount());

        holder.count.setVisibility(View.GONE);
        holder.title.setText(shabadList.get(position).getName());
        Glide.with(context)
                .load(shabadList.get(position).getImageUrl())
                .apply(option)
                .into(holder.imageView);
        holder.raagi_card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_intent(shabadList.get(position).getName(), shabadList.get(position).getLink(), shabadList.get(position).getImageUrl());
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_intent(shabadList.get(position).getName(), shabadList.get(position).getLink(), shabadList.get(position).getImageUrl());
            }
        });

    }

    private void create_intent(String name, String link, String imageLink) {

        Intent intent = new Intent(context, RadioPlayer.class);
        intent.putExtra("radio", "radio");
        intent.putExtra("RADIO_NAME", name);
        intent.putExtra("NAME", link);
        intent.putExtra("IMAGE", imageLink);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return shabadList.size();
    }


    class ShabadViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title, count;
        CardView raagi_card_layout;

        public ShabadViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.raagi_name_TV);
            count = (TextView) itemView.findViewById(R.id.count);
            imageView = (ImageView) itemView.findViewById(R.id.raagi_thumbnail_IV);
            raagi_card_layout = (CardView) itemView.findViewById(R.id.raagi_card_layout);
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