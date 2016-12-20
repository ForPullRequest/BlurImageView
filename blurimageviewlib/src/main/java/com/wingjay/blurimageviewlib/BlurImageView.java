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

//import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
//import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
//  private Drawable failDrawable = new Drawable() {
//    Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//    @Override
//    public void draw(@NonNull Canvas canvas) {
//      canvas.drawColor(greyColor);
//      String failString = "load failure";
//      canvas.translate((canvas.getWidth() - textPaint.measureText(failString)) / 2,
//          canvas.getHeight()/2);
//      textPaint.setColor(Color.DKGRAY);
//      textPaint.setTextSize(30);
//      canvas.drawText(failString, 0, failString.length(), textPaint);
//    }
//
//    @Override
//    public void setAlpha(int alpha) {}
//
//    @Override
//    public void setColorFilter(ColorFilter colorFilter) {}
//
//    @Override
//    public int getOpacity() {
//      return PixelFormat.UNKNOWN;
//    }
//  };
//  private ImageLoader imageLoader;
//  private DisplayImageOptions displayImageOptions;

  private ImageView imageView;

  public ImageView getImageView(){
    return imageView;
  }
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

//  private void init() {
////    initUIL();
////    imageLoader = ImageLoader.getInstance();
//
//    initChildView();
////    initDisplayImageOptions();
//  }

//  private void initUIL() {
//    ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(mContext);
//    config.threadPriority(Thread.NORM_PRIORITY - 2);
//    config.denyCacheImageMultipleSizesInMemory();
//    config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//    config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
//    config.tasksProcessingOrder(QueueProcessingType.LIFO);
//    config.writeDebugLogs(); // Remove for release app
//
//    // Initialize ImageLoader with configuration.
//    ImageLoader.getInstance().init(config.build());
//  }

//  private void initDisplayImageOptions() {
//    displayImageOptions = new DisplayImageOptions.Builder()
//        .cacheOnDisk(true)
//        .considerExifParams(true)
//        .bitmapConfig(Bitmap.Config.RGB_565)
//        .build();
//  }

  private Drawable defaultDrawable = new ColorDrawable(greyColor);
  private void initChildView() {
    imageView = new ImageView(mContext);
    imageView.setLayoutParams(
        new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    imageView.setImageDrawable(defaultDrawable);
    //progress 初始化
//    loadingCircleProgressView = new LoadingCircleProgressView(mContext);
//    LayoutParams progressBarLayoutParams =
//        new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//    progressBarLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//    loadingCircleProgressView.setLayoutParams(progressBarLayoutParams);
//    loadingCircleProgressView.setVisibility(GONE);

    addView(imageView);
//    addView(loadingCircleProgressView);
  }

//  private SimpleImageLoadingListener blurLoadingListener = new SimpleImageLoadingListener() {
//    @Override
//    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//      imageView.setImageDrawable(failDrawable);//TODO 错误图像
//    }
//
//    @Override
//    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//      imageView.setImageBitmap(getBlurBitmap(loadedImage));//TODO blur图像的加载完成
//    }
//  };

//  private SimpleImageLoadingListener fullLoadingListener = new SimpleImageLoadingListener() {
//    @Override
//    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//      imageView.setImageDrawable(failDrawable);
//      Log.e("Image Load error", "cannot load Small image, please check url or network status");
//    }
//
//    @Override
//    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//      imageView.setImageBitmap(getBlurBitmap(loadedImage));
//      imageLoader.displayImage(mOriginImageUrl, imageView, displayImageOptions,
//          new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//              setLoadingProgressRatio(5, 100);//TODO
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//              setLoadingProgressRatio(5, 100);//TODO
//              Log.e("Image Load error", "cannot load Big image, please check url or network status");
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//              setLoadingProgressRatio(100, 100);//TODO
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//              Log.w("Image Load cancel", "the image loading process is cancelled");
//              setLoadingProgressRatio(100, 100);//TODO
//            }
//          },
//          new ImageLoadingProgressListener() {
//            @Override
//            public void onProgressUpdate(String imageUri, View view, int current, int total) {
//              if (!enableProgress) {
//                return;
//              }
//              setLoadingProgressRatio(current, total);//TODO
//            }
//          });
//    }
//  };

  /**
   * 设置progress的数值
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
   * This method will fetch bitmap from resource and make it blurry, display
   * @param blurImageRes the image resource id which is needed to be blurry
   */
  public void setBlurImageByRes(int blurImageRes) {
    buildDrawingCache();
    Bitmap blurBitmap = FastBlurUtil.doBlur(getDrawingCache(), mBlurRadius, true);
    imageView.setImageBitmap(blurBitmap);
  }

  /**
   * This image won't be blurry.
   * @param originImageRes The origin image resource id.
   */
  public void setOriginImageByRes(int originImageRes) {
    Bitmap originBitmap = BitmapFactory.decodeResource(mContext.getResources(), originImageRes);
    imageView.setImageBitmap(originBitmap);
  }

  /**
   * @deprecated
   * @param blurImageUrl
     */
  public void setBlurImageByUrl(String blurImageUrl) {
    mBlurImageUrl = blurImageUrl;
    cancelImageRequestForSafety();
//    imageLoaderListener.loadBlur(blurImageUrl, imageView);
  }

  /**
   * @deprecated
   * @param originImageUrl
     */
  public void setOriginImageByUrl(String originImageUrl, Bitmap blurBitmap) {
    mOriginImageUrl = originImageUrl;
    cancelImageRequestForSafety();
    imageLoaderListener.loadOrigin(originImageUrl, imageView, blurBitmap);
  }

  /**
   * This will load two Images literally. The small size blurry one and the big size original one.
   * @param blurImageUrl This is a small image url and will be loaded fast and will be blurry
   *                     automatically.
   * @param originImageUrl After show the blurry image, it will load the origin image automatically
   *                       and replace the blurry one after finish loading.
   */
  public BlurImageView init(String blurImageUrl, String originImageUrl) {
    mBlurImageUrl = blurImageUrl;
    mOriginImageUrl = originImageUrl;
//    cancelImageRequestForSafety();
//    imageLoader.loadImage(blurImageUrl, displayImageOptions, fullLoadingListener);
    //start loader
    return this;
  }

  public void start(String originImageUrl, Bitmap blurBitmap){
    cancelImageRequestForSafety();
//    imageLoaderListener.loadBlur(mBlurImageUrl, imageView);
    imageLoaderListener.loadOrigin(originImageUrl, imageView, blurBitmap);
  }

  /**
   * 需要传入bitmap进行blur操作
   * @param loadedBitmap
   * @return
     */
  public static Bitmap getBlurBitmap(Bitmap loadedBitmap) {
    // make this bitmap mutable
    loadedBitmap = loadedBitmap.copy(loadedBitmap.getConfig(), true);
    return FastBlurUtil.doBlur(loadedBitmap, DEFAULT_BLUR_RADIUS, true);
  }

  public void setBlurRadius(int blurRadius) {
    if (blurRadius < 0) {
      throw new IllegalArgumentException("blurRadius must not be less than 0");
    }
    mBlurRadius = blurRadius;
  }

  /**
   * 不一定需要
   */
  public void cancelImageRequestForSafety() {
//    imageLoader.cancelDisplayTask(imageView);
    cancelLoaderListener.cancel(imageView);
  }

  public void clear() {
    cancelImageRequestForSafety();
    imageView.setImageBitmap(null);
  }

  /**
   * If you disable progress, then it won't show a loading progress view when you're loading image.
   * Default the progress view is enabled.
   */
//  public void disableProgress() {
//    this.enableProgress = false;
//  }

  /**
   * @deprecated
   * @param bgColor
     */
  public void setProgressBarBgColor(int bgColor) {
//    this.loadingCircleProgressView.setProgressBgColor(bgColor);
  }

  /**
   * @deprecated
   * @param color
   */
  public void setProgressBarColor(int color) {
//    this.loadingCircleProgressView.setProgressColor(color);
  }

  /**
   * @deprecated
   * @param failDrawable
     */
  public void setFailDrawable(Drawable failDrawable) {
//    this.failDrawable = failDrawable;
  }

  /**
   * @deprecated
   * @param defaultDrawable
     */
  public void setDefaultDrawable(Drawable defaultDrawable) {
//    this.defaultDrawable = defaultDrawable;
  }

  public BlurImageView setImageLoaderListener(ImageLoaderListener imageLoaderListener) {
    this.imageLoaderListener = imageLoaderListener;
    return this;
  }

  private ImageLoaderListener imageLoaderListener;

  public BlurImageView setCancelLoaderListener(CancelLoaderListener cancelLoaderListener) {
    this.cancelLoaderListener = cancelLoaderListener;
    return this;
  }

  private CancelLoaderListener cancelLoaderListener;

  /**
   * 加载图片的监听
   */
  public interface ImageLoaderListener{
//    void loadBlur(String bUri, ImageView imageView);
    void loadOrigin(String oUri, ImageView imageView, Bitmap blurBitmap);
  }

  /**
   * 取消加载图片的监听
   */
  public interface CancelLoaderListener{
    void cancel(ImageView imageView);
  }
}
