package com.wingjay.blurimageviewlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * This imageView can show blur image.
 * Then it will be expanded to automatically display one image with two styles
 * one is small and blurry, another is the origin image,So here are two urls for these two images.
 */
public class BlurImageView extends RelativeLayout {

    public final static int DEFAULT_BLUR_RADIUS = 8;
    private Context mContext;

    private int mBlurRadius = DEFAULT_BLUR_RADIUS;

    private String mBlurImageUrl, mOriginImageUrl;
    //default image's color
    private int greyColor = Color.parseColor("#66CCCCCC");
    private Drawable defaultDrawable = new ColorDrawable(greyColor);

    private ImageView imageView;

//  private LoadingCircleProgressView loadingCircleProgressView;

//  private boolean enableProgress = true;//TODO progress开关

    public BlurImageView(Context context) {
        this(context, null);
    }

    public BlurImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlurImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context.getApplicationContext();
        initChildView();
    }

    private void initChildView() {
        imageView = new ImageView(mContext);
        imageView.setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageDrawable(defaultDrawable);
        addView(imageView);

        //progress 初始化
//    loadingCircleProgressView = new LoadingCircleProgressView(mContext);
//    LayoutParams progressBarLayoutParams =
//        new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//    progressBarLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//    loadingCircleProgressView.setLayoutParams(progressBarLayoutParams);
//    loadingCircleProgressView.setVisibility(GONE);
//    addView(loadingCircleProgressView);
    }

    /**
     * @return
     * @deprecated
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * This method will fetch bitmap from resource and make it blurry, display
     *
     * @param blurImageRes the image resource id which is needed to be blurry
     */
    public void setBlurImageByRes(int blurImageRes) {
        buildDrawingCache();
        Bitmap blurBitmap = FastBlurUtil.doBlur(getDrawingCache(), mBlurRadius, true);
        imageView.setImageBitmap(blurBitmap);
    }

    /**
     * This image won't be blurry.
     *
     * @param originImageRes The origin image resource id.
     */
    public void setOriginImageByRes(int originImageRes) {
        Bitmap originBitmap = BitmapFactory.decodeResource(mContext.getResources(), originImageRes);
        imageView.setImageBitmap(originBitmap);
    }

    /**
     * @param blurImageUrl
     */
    public BlurImageView setBlurImageByUrl(String blurImageUrl) {
        mBlurImageUrl = blurImageUrl;
        cancelImageRequestForSafety();
        return this;
//    imageLoaderListener.loadBlur(blurImageUrl, imageView);
    }

    /**
     * @param originImageUrl
     */
    public BlurImageView setOriginImageByUrl(String originImageUrl) {
        mOriginImageUrl = originImageUrl;
        cancelImageRequestForSafety();
        return this;
//        imageLoaderListener.loadOrigin(originImageUrl, imageView, BlurImageView.getBlurBitmap(placeholder));
    }

    private boolean isRecycle = true;

    public void setRecycle(boolean recycle) {
        isRecycle = recycle;
    }

    /**
     * 在{@link #setBlurBitmap}后执行 不然会持续寻找
     * 在{@link #setImageLoaderListener}后执行
     */
    public void showOrigin() {
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (hasBlur) {
                    hasBlur = false;
                    if (imageLoaderListener != null)
                        imageLoaderListener.loadOrigin(mOriginImageUrl, imageView, getBlurBitmap(blurBitmap));
                } else if (isRecycle) {
                    showOrigin();
                } else {
                    throw new IllegalArgumentException("setBlurBitmap should be invoked before");
                }
            }
        }, 200);
    }

    /**
     * 在{@link #setBlurImageByUrl}、{@link #setBlurLoaderListener}后执行
     */
    public void showBlur() {
        if (blurLoaderListener != null)
            blurLoaderListener.loadBlur(mBlurImageUrl, imageView);
    }

    /**
     * 需要传入bitmap进行blur操作
     *
     * @param loadedBitmap
     * @return
     */
    public Bitmap getBlurBitmap(Bitmap loadedBitmap) {
        // make this bitmap mutable
        loadedBitmap = loadedBitmap.copy(loadedBitmap.getConfig(), true);
        return FastBlurUtil.doBlur(loadedBitmap, mBlurRadius, true);
    }

    /**
     * 需在showOrigin之前
     *
     * @param blurRadius
     * @return
     */
    public BlurImageView setBlurRadius(int blurRadius) {
        if (blurRadius < 0) {
            throw new IllegalArgumentException("blurRadius must not be less than 0");
        }
        mBlurRadius = blurRadius;
        return this;
    }

    /**
     * 不一定需要
     */
    public void cancelImageRequestForSafety() {
        if (cancelLoaderListener != null)
            cancelLoaderListener.cancel(imageView);
    }

    public void clear() {
        cancelImageRequestForSafety();
        imageView.setImageBitmap(null);
        blurBitmap = null;
    }

    private Bitmap blurBitmap;
    private boolean hasBlur;//是否有blur资源

    public BlurImageView setBlurBitmap(Bitmap blurBitmap) {
        this.blurBitmap = blurBitmap;
        hasBlur = true;
        return this;
    }

    private BlurLoaderListener blurLoaderListener;

    public interface BlurLoaderListener {
        void loadBlur(String bUrl, ImageView imageView);
    }

    public void setBlurLoaderListener(BlurLoaderListener blurLoaderListener) {
        this.blurLoaderListener = blurLoaderListener;
    }

    private ImageLoaderListener imageLoaderListener;

    /**
     * 加载图片的监听
     */
    public interface ImageLoaderListener {
        //将oUrl加载到imageView并把blurBitmap作为placeholder
        void loadOrigin(String oUrl, ImageView imageView, Bitmap blurBitmap);
    }

    public BlurImageView setImageLoaderListener(ImageLoaderListener imageLoaderListener) {
        this.imageLoaderListener = imageLoaderListener;
        return this;
    }

    private CancelLoaderListener cancelLoaderListener;

    /**
     * 取消加载图片的监听
     */
    public interface CancelLoaderListener {
        void cancel(ImageView imageView);
    }

    public BlurImageView setCancelLoaderListener(CancelLoaderListener cancelLoaderListener) {
        this.cancelLoaderListener = cancelLoaderListener;
        return this;
    }

    /**
     * If you disable progress, then it won't show a loading progress view when you're loading image.
     * Default the progress view is enabled.
     */
//  public void disableProgress() {
//    this.enableProgress = false;
//  }

    /**
     * 设置progress的数值
     *
     * @param current
     * @param total
     */
    private void setLoadingProgressRatio(int current, int total) {
//    if (current < total) {
//      if (loadingCircleProgressView.getVisibility() == GONE) {
//        loadingCircleProgressView.setVisibility(VISIBLE);
//      }
//      loadingCircleProgressView.setCurrentProgressRatio((float) current / total);
//    } else {
//      loadingCircleProgressView.setVisibility(GONE);
//    }
    }

    /**
     * @param bgColor
     * @deprecated
     */
    public void setProgressBarBgColor(int bgColor) {
//    this.loadingCircleProgressView.setProgressBgColor(bgColor);
    }

    /**
     * @param color
     * @deprecated
     */
    public void setProgressBarColor(int color) {
//    this.loadingCircleProgressView.setProgressColor(color);
    }
}
