package com.vismaad.naad.utils;

import android.content.Context;

import android.graphics.Bitmap;

import android.renderscript.Allocation;

import android.renderscript.Element;

import android.renderscript.RenderScript;

import android.renderscript.ScriptIntrinsicBlur;
import androidx.annotation.NonNull;


import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class BlurTransform extends BitmapTransformation {

    private RenderScript rs;
    private Bitmap map;
    private Bitmap main;

    public BlurTransform(Context context) {
        super();
        rs = RenderScript.create(context);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap bitmap, int outWidth, int outHeight) {
        map = bitmap;
        getBlurredBitmap(bitmap, 25);
        return main;
    }


    private void getBlurredBitmap(Bitmap bitmap, int layer) {
        Bitmap blurredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        layer--;

        if (layer > 0) {
            Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
            Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setInput(input);
            script.setRadius(25);
            script.forEach(output);
            output.copyTo(blurredBitmap);

        }

        if (layer > 0) {
            map.recycle();
            main = blurredBitmap;
        }
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}

