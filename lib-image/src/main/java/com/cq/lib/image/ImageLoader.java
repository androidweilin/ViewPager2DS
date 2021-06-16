package com.cq.lib.image;

import android.content.Context;
import android.widget.ImageView;

import com.cq.lib.image.gilde.GlideImageLoaderStrategy;


/**
 * 图片加载器封装类
 */
public class ImageLoader {

    private static ImageLoader instance;

    public static ImageLoader getInstance(){
        if(instance == null) {
            synchronized (ImageLoader.class) {
                if(instance == null){
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    private final ImageLoaderStrategy mImageLoaderStrategy = new GlideImageLoaderStrategy();

    public void displayImage(ImageConfig ic){
        mImageLoaderStrategy.displayImage(ic.getContext(), ic);
    }

    public void displayImage(String uri, ImageView imageView){
        if(imageView == null || uri == null)
            return;
        displayImage(ImageConfig.displayConfig(imageView, uri));
    }

    public void downloadImage(Context context, ImageConfig ic){
        if(context == null || ic == null)
            return;
        mImageLoaderStrategy.downloadImage(context, ic);
    }

    public void resumeRequest(Context context){
        mImageLoaderStrategy.resumeRequest(context);
    }

    public void pauseRequest(Context context){
        mImageLoaderStrategy.pauseRequest(context);
    }

}
