package com.vismaad.naad.newwork.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
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
import com.google.android.gms.ads.InterstitialAd;
import com.vismaad.naad.R;
import com.vismaad.naad.navigation.home.raagi_detail.RaagiDetailActivity;
import com.vismaad.naad.newwork.PopRagiAndShabad;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.welcome.signup.SignupActivity;

import java.util.List;

public class kathavaachaksInfoAdapter extends RecyclerView.Adapter<kathavaachaksInfoAdapter.ShabadViewHolder>  {

    private Context context;
    private List<PopRagiAndShabad.kathavaachaksInfo> shabadList;
    int pos = 0;
    private SharedPreferences mSharedPreferences;
    private InterstitialAd mInterstitialAd;
    int count;
    //PopRagiAndShabad.RaagisInfo shabad;

    public kathavaachaksInfoAdapter(Context context, List<PopRagiAndShabad.kathavaachaksInfo> shabadList) {
        this.context = context;
        this.shabadList = shabadList;
        mSharedPreferences = context.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public kathavaachaksInfoAdapter.ShabadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_raagi, parent, false);

        return new kathavaachaksInfoAdapter.ShabadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final kathavaachaksInfoAdapter.ShabadViewHolder holder, final int position) {
        pos = position;
        final PopRagiAndShabad.kathavaachaksInfo shabad = shabadList.get(position);
        String mVideoId = shabadList.get(position).getRaagiImageUrl();
        RequestOptions option = new RequestOptions().fitCenter()
                .override(Target.SIZE_ORIGINAL);

        holder.count.setText(""+shabadList.get(position).getShabadsCount());
        holder.title.setText(shabadList.get(position).getRaagiName());
        Glide.with(context)
                .load(mVideoId)
                .apply(option)
                .into(holder.imageView);
        holder.raagi_card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_intent(holder, shabad);
            }
        });
    }

    private void create_intent(final kathavaachaksInfoAdapter.ShabadViewHolder holder, PopRagiAndShabad.kathavaachaksInfo raagiInfo) {
        Pair<View, String> p1 = Pair.create((View) holder.imageView, "raagi_image");
        Pair<View, String> p2 = Pair.create((View) holder.title, "raagi_name");
        Pair<View, String> p3 = Pair.create((View) holder.count, "raagi_shabads_count");
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, p1, p2, p3);

        Intent intent = new Intent(context, RaagiDetailActivity.class);
        intent.putExtra("raagi_image_url", raagiInfo.getRaagiImageUrl());
        intent.putExtra("raagi_name", raagiInfo.getRaagiName());
        intent.putExtra("num_of_shabads", raagiInfo.getShabadsCount());
        intent.putExtra("total_shabads_length", raagiInfo.getMinutesOfShabads());
        intent.putExtra("STATUS", "KATHA");
        context.startActivity(intent, activityOptionsCompat.toBundle());
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

