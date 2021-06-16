package com.cq.lib.image;

import android.content.Context;

/**
 *
 * 图片加载器策略接口
 */
public interface ImageLoaderStrategy {

    void displayImage(Context holder, ImageConfig ic);

    void downloadImage(Context holder, ImageConfig ic);

    void resumeRequest(Context context);

    void pauseRequest(Context context);

}
