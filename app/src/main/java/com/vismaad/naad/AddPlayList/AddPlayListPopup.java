package com.vismaad.naad.AddPlayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vismaad.naad.AddPlayList.model.JBPlaylistCount;
import com.vismaad.naad.R;
import com.vismaad.naad.navigation.playlist.presenter.IPlayListPresenter;
import com.vismaad.naad.navigation.playlist.presenter.PlayListPresenterCompl;
import com.vismaad.naad.navigation.playlist.view.IPlayListView;
import com.vismaad.naad.sharedprefrences.JBSehajBaniPreferences;
import com.vismaad.naad.sharedprefrences.SehajBaniPreferences;
import com.vismaad.naad.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by satnamsingh on 04/07/18.
 */

public class AddPlayListPopup extends Activity implements IPlayListView {

    Button btnCancel, btnSubmit;
    EditText editName;
    private SharedPreferences mSharedPreferences;
    ACProgressFlower dialog;
    IPlayListPresenter playListPresenterCompl;
    ArrayList<JBPlaylistCount> playListArrayList;
    Bundle extras;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        initial();
    }

    private void initial() {
        extras = getIntent().getExtras();
        mSharedPreferences = AddPlayListPopup.this.getSharedPreferences(
                SehajBaniPreferences.Atree_PREFERENCES, Context.MODE_PRIVATE);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        editName = (EditText) findViewById(R.id.editName);
        dialog = new ACProgressFlower.Builder(AddPlayListPopup.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .bgColor(Color.TRANSPARENT)
                .bgAlpha(0)
                .bgCornerRadius(0)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCanceledOnTouchOutside(true);
        if (extras != null) {
            //playListArrayList = extras.getParcelable("PLAY_LIST_ARRAYLIST");
            playListArrayList = (ArrayList<JBPlaylistCount>) getIntent().getSerializableExtra("PLAY_LIST_ARRAYLIST");
        }

        playListPresenterCompl = new PlayListPresenterCompl(this);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                if (Utils.isNetworkAvailable(AddPlayListPopup.this) == true) {
                    if (editName.getText().toString().equalsIgnoreCase("")) {
                        Utils.showSnackBar(AddPlayListPopup.this, "Please enter name");
                    } else {
                        if (playListArrayList.contains(editName.getText().toString())) {
                            Toast toast = Toast.makeText(AddPlayListPopup.this, "PlayList name already exist choose different name.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            dialog.show();
                            playListPresenterCompl.doCreatePlayList1(JBSehajBaniPreferences.getLoginId(mSharedPreferences)
                                    , editName.getText().toString());
                        }


                    }
                } else {
                    Utils.showSnackBar(AddPlayListPopup.this, "No internet connection");
                }

            }
        });
    }

    @Override
    public void onResult(String code, int pageID) {
        dialog.dismiss();
        if (pageID == 1) {
            if (!code.equalsIgnoreCase("")) {
                try {
                    JSONObject json = (JSONObject) new JSONTokener(code).nextValue();
                    int responseCode = (int) json.get("ResponseCode");
                    String msg = (String) json.get("Message");
                    if (responseCode == 200) {

                        finish();
                    }

                    Toast toast = Toast.makeText(AddPlayListPopup.this, msg, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
