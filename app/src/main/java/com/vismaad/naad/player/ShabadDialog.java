package com.vismaad.naad.player;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.vismaad.naad.Constants;
import com.vismaad.naad.R;
import com.vismaad.naad.player.presenter.ShabadPlayerPresenterImpl;
import com.vismaad.naad.player.service.App;

/**
 * Created by ivesingh on 2/17/18.
 */

public class ShabadDialog {

    private Activity activity;
    private ShabadPlayerPresenterImpl shabadPlayerPresenterImpl;
    private ArrayAdapter<CharSequence> color_S_AA;
    private ImageButton zoom_out_IB, zoom_in_IB;
    private Spinner color_S;
    private CheckBox teeka_CB, punjabi_CB, english_CB;
    private boolean teeka, punjabi, english;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int fontSize;

    public ShabadDialog(Activity activity, ShabadPlayerPresenterImpl shabadPlayerPresenterImpl) {
        this.activity = activity;
        this.shabadPlayerPresenterImpl = shabadPlayerPresenterImpl;
        sharedPreferences = activity.getSharedPreferences("shabadSettingsData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        teeka = App.getPrefranceDataBoolean(Constants.TEEKA_CB);
        punjabi = App.getPrefranceDataBoolean(Constants.PUNJABI_CB);
        english = App.getPrefranceDataBoolean(Constants.ENGLISH_CB);
        fontSize = App.getPreferanceInt(Constants.FONT_SIZE);
        if (fontSize == 0) {
            fontSize = 20;
        }
    }

    public void create_dialog_box() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        View view = activity.getLayoutInflater().inflate(R.layout.settings_shabad, null);
        process_settings(view);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void process_settings(View view) {
        zoom_out_IB = view.findViewById(R.id.zoom_out_IB);
        zoom_in_IB = view.findViewById(R.id.zoom_in_IB);
        color_S = view.findViewById(R.id.color_S);
        teeka_CB = view.findViewById(R.id.teeka_CB);
        punjabi_CB = view.findViewById(R.id.punjabi_CB);
        english_CB = view.findViewById(R.id.english_CB);
        teeka_CB.setChecked(teeka);
        punjabi_CB.setChecked(punjabi);
        english_CB.setChecked(english);
        shabadPlayerPresenterImpl.prepareTranslation(isTeeka(), isPunjabi(), isEnglish());
        color_S_AA = ArrayAdapter.createFromResource(activity, R.array.colors, R.layout.support_simple_spinner_dropdown_item);
        color_S_AA.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        color_S.setAdapter(color_S_AA);
        color_S.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                shabadPlayerPresenterImpl.changeShabadView(i);
                editor.putInt("background_color_position", i);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        color_S.setSelection(getSharedPreferences().getInt("background_color_position", 0));

        zoom_out_IB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo decrement the zoom level of the lyrics font size
                if (fontSize >= 16) {
                    setFontSize(fontSize-1);
                }
            }
        });

        zoom_in_IB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo increment the zoom level of the lyrics font size
                if (fontSize <= 25) {
                    setFontSize(fontSize+1);
                }
            }
        });
    }

    private void setFontSize(int size) {
//        Log.e("FONT SIZE", "size: " + size);
        if (size >= 16 && size <= 25) {
            App.setPreferencesInt(Constants.FONT_SIZE, size);
            shabadPlayerPresenterImpl.setTranslationSize(App.getPreferanceInt(Constants.FONT_SIZE));
            fontSize = size;
        }
    }

    public boolean isTeeka() {
        return teeka;
    }

    public void setTeeka(boolean teeka) {
        this.teeka = teeka;
    }

    public boolean isPunjabi() {
        return punjabi;
    }

    public void setPunjabi(boolean punjabi) {
        this.punjabi = punjabi;
    }

    public boolean isEnglish() {
        return english;
    }

    public void setEnglish(boolean english) {
        this.english = english;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
}