package com.cq.lib.image.gilde;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.cq.lib.image.ImageConfig;
import com.cq.lib.image.ImageLoaderStrategy;
import com.cq.lib.image.ImageOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.URL;


/**
 * Glide图片加载器实现
 */
public class GlideImageLoaderStrategy implements ImageLoaderStrategy {

    @Override
    public void displayImage(Context context, ImageConfig ic) {
        if (!isMainThread()) {
            Log.e("ImageLoader", "displayImage()需要在UI线程调用...");
            return;
        }
        RequestManager rm = null;
        try {
            rm = Glide.with(context);
        } catch (Exception ignore) {
        }
        if (rm == null) {
            return;
        }
        ImageOptions               options  = ic.getOptions();
        final ImageConfig.Listener listener = ic.getListener();
        GlideUrl glideUrl = new GlideUrl(ic.getUrl(), new LazyHeaders.Builder()
                .addHeader("device-type", "android")
                .build());
        rm.load(glideUrl)
                .placeholder(options.getPlaceHolder())
                .error(options.getErrorHolder())
                .skipMemoryCache(!options.isCacheInMemory())
                .diskCacheStrategy(options.isCacheInDisk() ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                .dontAnimate()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        if (listener != null) {
                            listener.onError(e);
                        }

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        if (listener != null) {
                            listener.onSuccess(resource);
                        }

                        return false;
                    }
                }).into(ic.getView());
    }

    @Override
    public void downloadImage(Context context, ImageConfig ic) {
        if (!isMainThread()) {
            Log.e("ImageLoader", "downloadImage()需要在UI线程调用...");
            return;
        }
        RequestManager rm = null;
        try {
            rm = Glide.with(context);
        } catch (Exception ignore) {
        }
        if (rm == null) {
            return;
        }
        ImageOptions               options  = ic.getOptions();
        final ImageConfig.Listener listener = ic.getListener();
        if (listener == null) {
            return;
        }
        rm.load(ic.getUrl())
                .placeholder(options.getPlaceHolder())
                .error(options.getErrorHolder())
                .skipMemoryCache(options.isCacheInMemory())
                .diskCacheStrategy(options.isCacheInDisk() ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                .dontAnimate().into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource,
                                        @Nullable Transition<? super Drawable> transition) {
                listener.onSuccess(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                listener.onError(new Exception("loadError"));
            }
        });


    }

    @Override
    public void resumeRequest(Context context) {
        try {
            Glide.with(context).resumeRequests();
        } catch (Exception ignore) {
        }
    }

    @Override
    public void pauseRequest(Context context) {
        try {
            Glide.with(context).pauseRequests();
        } catch (Exception ignore) {
        }
    }

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

}
