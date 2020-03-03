package com.vismaad.naad.addPlayList.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vismaad.naad.R;
import com.vismaad.naad.rest.model.playlist.JBPopularShabads;
import com.vismaad.naad.rest.model.raagi.ShabadTutorial;

import java.util.ArrayList;

public class PopularShabdsAdapter extends RecyclerView.Adapter<PopularShabdsAdapter.MyViewHolder> {

    ArrayList<JBPopularShabads> mValues;
    Context mContext;
    //protected PopularShabdsAdapter.ItemListener mListener;
    boolean fromAllShabad = false;

    public PopularShabdsAdapter(Context context, ArrayList<JBPopularShabads> values) {

        mValues = values;
        mContext = context;
        // mListener = itemListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        JBPopularShabads item;
        ImageView list_image;
        TextView title, listen;
        View v, shabad_card_view;
        ;

        public MyViewHolder(View v) {
            super(v);
            this.v = v;

            title = (TextView) v.findViewById(R.id.title);
            listen = (TextView) v.findViewById(R.id.listen);
            list_image = (ImageView) v.findViewById(R.id.list_image);
            shabad_card_view = v.findViewById(R.id.shabad_card_view);
        }

        public void setData(final JBPopularShabads item) {
            this.item = item;
            title.setText(item.getSathaayi_title());
            listen.setText(String.valueOf(item.getListeners()));

            Glide.with(mContext)
                    .load(item.getImage_url())
                    .into(list_image);

            shabad_card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

        }


    }

   /* private void create_intent(Shabad shabad) {

        Intent intent = new Intent(mContext, ShabadPlayerActivity.class);
        intent.putExtra(PLAY_SONG, true);
        intent.putParcelableArrayListExtra("shabads", shabadList);
        intent.putExtra("current_shabad", shabad);
        intent.putExtra(MediaPlayerState.SHABAD_DURATION, (long) 0);

        mContext.startActivity(intent);
    }*/

    @Override
    public PopularShabdsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        view = LayoutInflater.from(mContext).inflate(R.layout.popular_adapter, parent, false);

        return new PopularShabdsAdapter.MyViewHolder(view);
    }


    public void onBindViewHolder(PopularShabdsAdapter.MyViewHolder Vholder, int position) {

        Vholder.setData(mValues.get(position));


    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(ShabadTutorial item);
    }
}
