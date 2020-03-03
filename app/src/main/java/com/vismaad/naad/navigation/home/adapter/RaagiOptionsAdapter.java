package com.vismaad.naad.navigation.home.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vismaad.naad.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parth Vora on 2/3/18.
 */

public class RaagiOptionsAdapter extends RecyclerView.Adapter<RaagiOptionsAdapter.OptionsViewHolder>{

    private Context context;
    private List<String> optionsList = new ArrayList<>();

    public RaagiOptionsAdapter(Context context, List<String> optionsList){
        this.context = context;
        this.optionsList = optionsList;
    }

    @Override
    public OptionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_options, parent, false);
        return new OptionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OptionsViewHolder holder, final int position) {
        holder.optionsLabel.setText(optionsList.get(position));
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    class OptionsViewHolder extends RecyclerView.ViewHolder{

        private TextView optionsLabel;

        public OptionsViewHolder(View itemView) {
            super(itemView);
            optionsLabel = itemView.findViewById(R.id.option_title);
        }
    }
}