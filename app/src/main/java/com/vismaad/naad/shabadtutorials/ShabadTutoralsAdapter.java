package com.vismaad.naad.shabadtutorials;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.vismaad.naad.R;
import com.vismaad.naad.rest.model.raagi.ShabadTutorial;

import java.util.ArrayList;

public class ShabadTutoralsAdapter extends RecyclerView.Adapter<ShabadTutoralsAdapter.MyViewHolder> {

    ArrayList<ShabadTutorial> mValues;
    Context mContext;
    protected ItemListener mListener;
    boolean fromAllShabad=false;

    public ShabadTutoralsAdapter(Context context, ArrayList<ShabadTutorial> values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener = itemListener;
    }

    public ShabadTutoralsAdapter(Context context, ArrayList<ShabadTutorial> values, ItemListener itemListener, boolean fromAllShabad) {

        mValues = values;
        mContext = context;
        mListener = itemListener;
        this.fromAllShabad = fromAllShabad;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ShabadTutorial item;
        ImageView imageView;
        TextView title, count;
        View v;

        public MyViewHolder(View v) {

            super(v);
            this.v = v;

            title = (TextView) v.findViewById(R.id.raagi_name_TV);
            count = (TextView) v.findViewById(R.id.shabads_count_TV);
            imageView = (ImageView) v.findViewById(R.id.raagi_thumbnail_IV);


        }

        public void setData(final ShabadTutorial item) {
            this.item = item;
            title.setText(item.getTitle() + " - " + item.getHarmoniumScale());
            count.setVisibility(View.GONE);

//            https://img.youtube.com/vi/lfZpTLyPUMc/default.jpg
            String mVideoId, mVideoUrl = item.getUrl();
            if (mVideoUrl.contains("&"))
                mVideoId = mVideoUrl.substring(mVideoUrl.indexOf("=") + 1, mVideoUrl.indexOf("&"));
            else
                mVideoId = mVideoUrl.substring(mVideoUrl.indexOf("=") + 1);
            RequestOptions option = new RequestOptions().fitCenter()
                    .override(Target.SIZE_ORIGINAL);

            Glide.with(mContext)
                    .load("https://img.youtube.com/vi/" + mVideoId + "/mqdefault.jpg")
                    .apply(option)
                    .into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(item);
                    }
                }
            });

        }


    }

    @Override
    public ShabadTutoralsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (fromAllShabad)
            view = LayoutInflater.from(mContext).inflate(R.layout.card_shabad_tutorials, parent, false);
        else
            view = LayoutInflater.from(mContext).inflate(R.layout.card_raagi, parent, false);

        return new MyViewHolder(view);
    }


    public void onBindViewHolder(ShabadTutoralsAdapter.MyViewHolder Vholder, int position) {

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

