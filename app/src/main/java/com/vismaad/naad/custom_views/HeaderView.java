package com.vismaad.naad.custom_views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vismaad.naad.R;


/**
 * Created by anton on 11/12/15.
 */

public class HeaderView extends LinearLayout {

//    @BindView(R.id.name)
    TextView raagiName;

//    @BindView(R.id.last_seen)
    TextView shabadCount;

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        raagiName = findViewById(R.id.raagi_name_TV);
        shabadCount = findViewById(R.id.shabads_count_TV);
//        ButterKnife.bind(this);
    }

    public void bindTo(String name, String lastSeen) {
        this.raagiName.setText(name);
        this.shabadCount.setText(lastSeen);
    }

    public void setTextSize(float size) {
        raagiName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }
}
