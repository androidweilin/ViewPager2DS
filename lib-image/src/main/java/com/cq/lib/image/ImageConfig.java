package com.cq.lib.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 *
 * 图片配置类
 * {@link #view} 要展示的ImageView
 * {@link #url} 要加载的图片地址
 * {@link #listener} 图片加载监听器 图片下载监听器
 * {@link #options} 图片加载配置参数
 */
public class ImageConfig {

    public static ImageConfig downloadConfig(String url, Listener listener) {
       ImageConfig ic = new ImageConfig();
        ic.setUrl(url);
        ic.setListener(listener);
        ic.setOptions(ImageOptions.getDefault());
        return ic;
    }

    public static ImageConfig displayConfig(ImageView iv, String url) {
        return displayConfig(iv, url, ImageOptions.getDefault());
    }

    public static ImageConfig displayConfig(ImageView iv, String url, ImageOptions io) {
       ImageConfig ic = new ImageConfig();
        ic.setView(iv);
        ic.setUrl(url);
        ic.setOptions(io);
        return ic;
    }

    protected ImageConfig() {
    }

    private ImageView view;
    private String url;
    private ImageOptions options;
    private Listener listener;

    public ImageView getView() {
        return view;
    }

    private void setView(ImageView view) {
        this.view = view;
    }

    public String getUrl() {
        return url;
    }

    public void setUrlNoChange(String url){
        this.url = url;
    }

    private void setUrl(String url) {
        //解决域名带_下划线不展示图片问题
        if (url != null && url.regionMatches(true, 6, "//", 0, 2)) {
            int authorityStart = 8;
            int first = findFirstOf(url, "/?", authorityStart, url.length());
            String au = url.substring(authorityStart, first);
            if (au.contains("_")) {
                this.url = "http" + url.substring(5);
            } else {
                this.url = url;
            }
        } else {
            this.url = url;
        }
    }

    private int findFirstOf(String string, String chars, int start, int end) {
        for (int i = start; i < end; i++) {
            char c = string.charAt(i);
            if (chars.indexOf(c) != -1) {
                return i;
            }
        }
        return end;
    }

    public ImageOptions getOptions() {
        return options;
    }

    private void setOptions(ImageOptions options) {
        this.options = options;
    }

    public Listener getListener() {
        return listener;
    }

    public ImageConfig setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public Context getContext() {
        return view.getContext();
    }

    public interface Listener {
        void onSuccess(Drawable drawable);

        void onError(Exception ex);
    }

}
