package com.gameotaku.app.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.ImageView;

import com.gameotaku.app.BootstrapApplication;
import com.gameotaku.app.Injector;
import com.gameotaku.app.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

import javax.inject.Inject;

/**
 * Created by Vincent on 8/29/14.
 */
public class AjustImageUtils {
    private static AjustImageUtils instance;
    @Inject
    Context mContext;

    public AjustImageUtils() {
        Injector.inject(this);
    }

    public static AjustImageUtils getInstance() {
        if (instance == null) {
            instance = new AjustImageUtils();
        }
        return instance;
    }

    public String ajustThumb(String url, int pixed) {
        String suffix = "?imageMogr2/thumbnail/";
        String multiple = "x";
        int size = getPixels(pixed);
        return url + suffix + size + multiple + size;
    }

    private int getPixels(int dipValue) {
        Resources r = mContext.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }

    public Drawable loadImage(long item_id, String url, String prefix) {
        String icon_file = StorageUtils.ICON_ROOT + prefix + "_" + item_id;
        File icon = new File(icon_file);

        //如果已经有缓存了
        if (icon.exists()) {
            return Drawable.createFromPath(icon_file);
        }
        return mContext.getResources().getDrawable(R.drawable.icon);
    }

    public void loadImage(long item_id, String url, ImageView imageView, String prefix, int pixed) {

        String icon_file = StorageUtils.ICON_ROOT + prefix + "_" + item_id;
        File icon = new File(icon_file);

        //如果已经有缓存了
        if (icon.exists()) {
            Picasso.with(BootstrapApplication.getInstance())
                    .load(icon)
                    .fit().centerCrop()
                    .into(imageView);
        } else {
            Picasso.with(BootstrapApplication.getInstance())
                    .load(ajustThumb(url, pixed))
                    .transform(RoundedTransformation.getInstance())
                    .fit().centerCrop()
                    .into(imageView);

            Picasso.with(BootstrapApplication.getInstance())
                    .load(ajustThumb(url, pixed))
                    .transform(RoundedTransformation.getInstance())
                    .into(new CacheTarget(icon_file));
        }

    }

    private class CacheTarget implements Target {
        private String icon_file;

        public CacheTarget(String icon_file) {
            this.icon_file = icon_file;
        }

        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    File picFile = new File(StorageUtils.ICON_ROOT);
                    if (!picFile.exists()) {
                        picFile.mkdirs();
                    }

                    File imageFile = new File(icon_file);
                    try {
                        imageFile.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(imageFile);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                        ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            if (placeHolderDrawable != null) {
            }
        }
    }
}
