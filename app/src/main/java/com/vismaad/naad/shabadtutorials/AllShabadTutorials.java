package com.vismaad.naad.shabadtutorials;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.vismaad.naad.R;
import com.vismaad.naad.rest.instance.RetrofitClient;
import com.vismaad.naad.rest.model.raagi.RaagiInfo;
import com.vismaad.naad.rest.model.raagi.ShabadTutorial;
import com.vismaad.naad.rest.service.RaagiService;
import com.vismaad.naad.rest.service.ShabadTutorialsService;
import com.vismaad.naad.youtubelinks.YoutubeScreen;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllShabadTutorials extends AppCompatActivity implements ShabadTutoralsAdapter.ItemListener{
    ProgressDialog  dialog;
    RecyclerView recyclerView;
    ShabadTutoralsAdapter mAdapter;
    ArrayList<ShabadTutorial> list=new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_shabad_tutorials);


        recyclerView = (RecyclerView) findViewById(R.id.grid);
        GridLayoutManager manager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        mAdapter= new ShabadTutoralsAdapter(this, list, this, true);
        recyclerView.setAdapter(mAdapter);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        dialog=new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();
        loadList();


    }

    public void loadList() {

        ShabadTutorialsService tutorialsService = RetrofitClient.getClient().create(ShabadTutorialsService.class);
        Call<List<ShabadTutorial>> raagiInfoCall = tutorialsService.shabad_tutorials("all");

        raagiInfoCall.enqueue(new Callback<List<ShabadTutorial>>() {
            @Override
            public void onResponse(Call<List<ShabadTutorial>> call, Response<List<ShabadTutorial>> response) {
                list.clear();
                Log.e("response","==>"+response.body().get(0).getName());
                for(ShabadTutorial raagiInfo: response.body()){
                    list.add(raagiInfo);
                }
                dialog.dismiss();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<ShabadTutorial>> call, Throwable t) {
                Log.e("response","==>"+t);
                // TODO - Failed Raagi Info Call
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onItemClick(ShabadTutorial item) {

        Intent intent=new Intent(this, YoutubeScreen.class);
        intent.putExtra("video_url",item.getUrl());
        intent.putExtra("title",item.getTitle()+" - "+item.getHarmoniumScale());
        startActivity(intent);

    }
}
