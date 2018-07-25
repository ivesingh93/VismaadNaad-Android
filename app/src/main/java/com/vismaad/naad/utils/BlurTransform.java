package com.vismaad.naad.utils;

import android.content.Context;

import android.graphics.Bitmap;

import android.renderscript.Allocation;

import android.renderscript.Element;

import android.renderscript.RenderScript;

import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.util.Log;


import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.squareup.picasso.Transformation;

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
        // Create another bitmap that will hold the results of the filter.
        Bitmap blurredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        layer--;

        if (layer > 0) {
//            Log.e("Blyr Transform", "Layer count: " + layer);

            // Allocate memory for Renderscript to work with
            Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
            Allocation output = Allocation.createTyped(rs, input.getType());
            // Load up an instance of the specific script that we want to use.
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setInput(input);

            // Set the blur radius
            script.setRadius(25);

            // Start the ScriptIntrinisicBlur
            script.forEach(output);

            // Copy the output to the blurred bitmap
            output.copyTo(blurredBitmap);

//            getBlurredBitmap(blurredBitmap, layer);
        }

        if (layer > 0) {
            map.recycle();
//            Log.e("Blur Transform", "Blur return at blurred bitmap layer : " + layer);
            main = blurredBitmap;
        }
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}

