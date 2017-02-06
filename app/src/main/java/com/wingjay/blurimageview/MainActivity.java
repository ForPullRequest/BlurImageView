package com.wingjay.blurimageview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wingjay.blurimageviewlib.BlurImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.fast_blur_btn)
    Button fastBlurBtn;

    @Bind(R.id.full_blur_image_view)
    BlurImageView blurImageView;

    @Bind(R.id.image_indicator)
    TextView imageIndicator;

    @Bind(R.id.about_author)
    TextView aboutAuthor;

    boolean alreadyLoad = false;

    int[] mediumSmRes = {
            R.drawable.medium_sm_1,
            R.drawable.medium_sm_2,
            R.drawable.medium_sm_3,
            R.drawable.medium_sm_4
    };

    int[] mediumNmRes = {
            R.drawable.medium_nm_1,
            R.drawable.medium_nm_2,
            R.drawable.medium_nm_3,
            R.drawable.medium_nm_4
    };

    String[] mediumSmUrl = {
            "http://i04.pictn.sogoucdn.com/244b7c50678e3525",
            "http://i03.pictn.sogoucdn.com/2195e99c4ad9fdc3",
            "http://i04.pictn.sogoucdn.com/e6be687457646d09",
            "http://i01.pictn.sogoucdn.com/30cbe73b9d45e8ba"
    };

    String[] mediumNmUrl = {
            "http://pic1.win4000.com/wallpaper/3/568b3bfab5256.jpg",
            "http://pic1.win4000.com/wallpaper/9/53a12312b1fbf.jpg",
            "http://tupian.enterdesk.com/2012/1031/zyz/02/star%20%286%29.jpg",
            "http://tupian.enterdesk.com/2015/xll/02/26/4/rili9.jpg"
    };

    int[] blurImageViewProgressBgColor = {
            Color.BLACK,
            Color.BLACK,
            Color.parseColor("#E29C45"),
            Color.parseColor("#E29C45"),
    };

    int[] blurImageViewProgressColor = {
            Color.WHITE,
            Color.parseColor("#789262"),
            Color.parseColor("#7BCFA6"),
            Color.parseColor("#519A73"),
    };


    int currentIndex = 0;

    int getResIndex() {
        if (currentIndex > 3) {
            currentIndex = currentIndex - 4;
        }
        return currentIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String aboutAuthorString = "<u>Find me here: wingjay (https://github.com/wingjay)</u>";
        aboutAuthor.setText(Html.fromHtml(aboutAuthorString));
        aboutAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/wingjay"));
                startActivity(browserIntent);
            }
        });

        blurImageView.setImageLoaderListener(new BlurImageView.ImageLoaderListener() {
            @Override
            public void loadOrigin(String oUrl, ImageView imageView, Bitmap blurBitmap) {
                Log.e("loadOrigin", oUrl);
                //blurBitmap为对源资源模糊后的bitmap
                Drawable drawable = new BitmapDrawable(blurBitmap);
                //skipMemoryCache为了重复出现blur
                //鉴于Glide必须通过placeholder的方式设置这两张图的出现方式 不然会闪屏
                //不得已使用串行的方式加载两张图
//                imageView.setImageDrawable(drawable);
                Glide.with(MainActivity.this).load(oUrl).placeholder(drawable).skipMemoryCache(true).crossFade().into(imageView);
            }
        });
        customizeBlurImageView();

        Glide.with(MainActivity.this).load(mediumSmUrl[getResIndex()]).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap blurBitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                //设置模糊的源资源
                blurImageView.setBlurBitmap(blurBitmap);
            }
        });
        //显示清晰图片
        blurImageView.setOriginImageByUrl(mediumNmUrl[getResIndex()])
                .showOrigin();
    }

    @OnClick(R.id.fast_blur_btn)
    void doFastBlur() {
        currentIndex++;
        blurImageView.clear();
        Glide.with(MainActivity.this).load(mediumSmUrl[getResIndex()]).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap blurBitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                //设置模糊的源资源
                blurImageView.setBlurBitmap(blurBitmap);
            }
        });
        //显示清晰图片
        blurImageView.setOriginImageByUrl(mediumNmUrl[getResIndex()])
                .showOrigin();
//        if (!alreadyLoad) {
//            alreadyLoad = true;
//
//            fastBlurBtn.setText("Click and Clear current image");
//            imageIndicator.setText((getResIndex() + 1) + "/" + mediumNmUrl.length);
//        } else {
//            alreadyLoad = false;
//            blurImageView.clear();
//
//            currentIndex++;
//            doFastBlur();
////            fastBlurBtn.setText("Click to load new Image");
//        }
    }

    private void customizeBlurImageView() {
        blurImageView.setProgressBarBgColor(blurImageViewProgressBgColor[getResIndex()]);
        blurImageView.setProgressBarColor(blurImageViewProgressColor[getResIndex()]);
    }

    @Override
    protected void onDestroy() {
        blurImageView.cancelImageRequestForSafety();
        super.onDestroy();
    }

}
