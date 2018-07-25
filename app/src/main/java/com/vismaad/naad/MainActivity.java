package com.vismaad.naad;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.vismaad.naad.custom_views.HeaderView;


public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

//    @BindView(R.id.toolbar_header_view)
    protected HeaderView toolbarHeaderView;

//    @BindView(R.id.float_header_view)
    protected HeaderView floatHeaderView;

//    @BindView(R.id.appbar)
    protected AppBarLayout appBarLayout;

//    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private boolean isHideToolbarView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initUi();
    }

    private void initViews() {
        toolbarHeaderView = findViewById(R.id.toolbar_header_view);
        floatHeaderView = findViewById(R.id.float_header_view);
        appBarLayout = findViewById(R.id.appbar);
        toolbar = findViewById(R.id.toolbar);
    }

    private void initUi() {
        appBarLayout.addOnOffsetChangedListener(this);

        toolbarHeaderView.bindTo("Larry Page", "");
        floatHeaderView.bindTo("Larry Page", "Last seen today at 7.00PM");
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }
}
