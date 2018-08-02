package com.vismaad.naad.navigation.home;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.vismaad.naad.R;
import com.vismaad.naad.navigation.home.adapter.RaagiInfoAdapter;
import com.vismaad.naad.navigation.home.presenter.HomePresenterImpl;
import com.vismaad.naad.navigation.home.view.HomeView;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by ivesingh on 2/2/18.
 */

public class HomeFragment extends Fragment implements HomeView {

    private HomePresenterImpl homePresenterImpl;
    private RecyclerView raagi_RV;
    private RecyclerView.LayoutManager layoutManager;
    private static int playerState = 0;
    RaagiInfoAdapter raagiInfoAdapter;
    private AdView mAdView;
    SearchView search;
    LinearLayout rootView;
    ACProgressFlower dialog;
    public HomeFragment() {

    }

    public static HomeFragment newInstance(int player_state) {
        playerState = player_state;
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        search.setQuery("", false);
        rootView.requestFocus();
    }

    @Override
    public void onStop() {
        super.onStop();
        // ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_home, container, false);
        MobileAds.initialize(getActivity(),
                getResources().getString(R.string.YOUR_ADMOB_APP_ID));

        // mAdView = findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        // mAdView.loadAd(adRequest);
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        homePresenterImpl = new HomePresenterImpl(this, getActivity());
        homePresenterImpl.init(view);
        dialog.show();
        homePresenterImpl.prepareRaagis();

        // EditText searchEditText = (EditText) search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        // searchEditText.setTextColor(getResources().getColor(R.color.black));
        //  searchEditText.setHintTextColor(getResources().getColor(R.color.gray));
        // search.setQueryHint(Html.fromHtml("<font color = #808080>" + getResources().getString(R.string.hintSearchMess) + "</font>"));
        //Drawable d = getResources().getDrawable(R.drawable.bg_white_rounded);
        // search.setBackground(d);
        // search.onActionViewExpanded();


        search.onActionViewExpanded();
        search.setActivated(true);
        search.setQueryHint("Search Raagis");
        Drawable d = getResources().getDrawable(R.drawable.bg_white_rounded);
        search.setBackground(d);
        search.setFocusable(false);
        search.clearFocus();

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                raagiInfoAdapter.getFilter().filter(query);

                return false;
            }
        });

        return view;
    }

    @Override
    public void init(View view) {
       // search = view.findViewById(R.id.search);
/*        Drawable d = getResources().getDrawable(R.drawable.bg_white_rounded);
        search.setBackground(d);
        EditText searchEditText = (EditText) search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.gray));*/

        dialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .bgColor(Color.TRANSPARENT)
                .bgAlpha(0)
                .bgCornerRadius(0)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCanceledOnTouchOutside(true);

        raagi_RV = view.findViewById(R.id.raagi_RV);
        layoutManager = new GridLayoutManager(getActivity(), 3);

        rootView = view.findViewById(R.id.root_layout);
        search = view.findViewById(R.id.search);
        EditText searchEditText = (EditText) search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.gray));
        search.setIconified(true);
    }

    @Override
    public void showRaagis(RaagiInfoAdapter raagiInfoAdapter) {
        dialog.dismiss();

        raagi_RV.setLayoutManager(layoutManager);
        this.raagiInfoAdapter = raagiInfoAdapter;

        // commented this line as it is not required
//        raagi_RV.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        raagi_RV.setItemAnimator(new DefaultItemAnimator());
        raagi_RV.setAdapter(raagiInfoAdapter);
        raagi_RV.setNestedScrollingEnabled(false);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      /*  inflater.inflate(R.menu.menusearch, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search a Shabad");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                raagiInfoAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                raagiInfoAdapter.getFilter().filter(query);
                return false;
            }
        });*/

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface OnItem1SelectedListener {
        void OnItem1SelectedListener(String item);
    }
}
