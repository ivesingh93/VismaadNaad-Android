package com.vismaad.naad.addPlayList;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;

import com.vismaad.naad.addPlayList.adapter.PopularShabdsAdapter;
import com.vismaad.naad.R;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.playlist.JBPopularShabads;
import com.vismaad.naad.rest.service.PopularShabads;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPopularShabadsList extends AppCompatActivity {
    RecyclerView list_popular_shabads;
    ACProgressFlower dialog;
    ArrayList<JBPopularShabads> popularShabadsList = new ArrayList();
    PopularShabdsAdapter mPopularShabdsAdapter;
    LinearLayout ln_pop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular_shabads);
        initial();
        //GridLayoutManager popManager = new GridLayoutManager(AllPopularShabadsList.this, popularShabadsList.size(), GridLayoutManager.HORIZONTAL, false);
        //  list_popular_shabads.setLayoutManager(popManager);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        list_popular_shabads.setLayoutManager(mLayoutManager);
        list_popular_shabads.setItemAnimator(new DefaultItemAnimator());

        mPopularShabdsAdapter = new PopularShabdsAdapter(AllPopularShabadsList.this, popularShabadsList);
        list_popular_shabads.setAdapter(mPopularShabdsAdapter);

        loadPopularList();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initial() {
        list_popular_shabads = (RecyclerView) findViewById(R.id.list_popular_shabads);
        ln_pop = (LinearLayout) findViewById(R.id.ln_pop);
        ln_pop.setVisibility(View.VISIBLE);
        dialog = new ACProgressFlower.Builder(AllPopularShabadsList.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .bgColor(Color.TRANSPARENT)
                .bgAlpha(0)
                .bgCornerRadius(0)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCanceledOnTouchOutside(true);
    }

    public void loadPopularList() {
        dialog.show();
        PopularShabads mPopularShabads = RetrofitClient.getClient().create(PopularShabads.class);
        //Call<List<JBPopularShabads>> raagiInfoCall = mPopularShabads.popular_shabads();
        Call<List<JBPopularShabads>> popularShabads = mPopularShabads.popular_shabads();
        popularShabads.enqueue(new Callback<List<JBPopularShabads>>() {
            @Override
            public void onResponse(Call<List<JBPopularShabads>> call,
                                   Response<List<JBPopularShabads>> response) {
                popularShabadsList.clear();
                for (JBPopularShabads raagiInfo : response.body()) {
                    popularShabadsList.add(raagiInfo);
                }
                dialog.dismiss();
                mPopularShabdsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<JBPopularShabads>> call, Throwable t) {
                // TODO - Failed Raagi Info Call
                dialog.dismiss();
            }
        });


    }
}
