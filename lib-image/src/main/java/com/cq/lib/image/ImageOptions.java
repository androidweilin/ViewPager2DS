package com.cq.lib.image;

/**
 * Create by guojiel 2019.3.26
 * 图片加载参数Options类
 */
public class ImageOptions {

    private static final int CacheInMemory = 0b01;
    private static final int CacheInDisk = 0b10;

    public static ImageOptions getDefault(){
        return ImageOptions.create(R.drawable.default_img, R.drawable.default_img);
    }

    public static ImageOptions create(int placeHolder, int errorHolder){
        return new ImageOptions().setPlaceHolder(placeHolder).setErrorHolder(errorHolder);
    }

    public static ImageOptions create(int peHolder){
        return create(peHolder, peHolder);
    }

    /**
     * 占位图
     */
    private int placeHolder;
    /**
     * 错误图片占位图
     */
    private int errorHolder;
    /**
     * 缓存Flag
     */
    private int mCacheFlag = CacheInMemory | CacheInDisk;

    private ImageOptions(){
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public ImageOptions setPlaceHolder(int placeHolder) {
        this.placeHolder = placeHolder;
        return this;
    }

    public int getErrorHolder() {
        return errorHolder;
    }

    public ImageOptions setErrorHolder(int errorHolder) {
        this.errorHolder = errorHolder;
        return this;
    }

    public boolean isCacheInMemory() {
        return (mCacheFlag & CacheInMemory) == CacheInMemory;
    }

    public ImageOptions skipMemoryCache() {
        mCacheFlag &= ~CacheInMemory;
        return this;
    }

    public boolean isCacheInDisk() {
        return (mCacheFlag & CacheInDisk) == CacheInDisk;
    }

    public ImageOptions skipDiskCache() {
        mCacheFlag &= ~CacheInDisk;
        return this;
    }

}
