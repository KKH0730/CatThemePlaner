package com.planer.catthemeplaner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class SplashActivity extends AppCompatActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splashGif = findViewById(R.id.splash_gif_view);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(splashGif);
        Glide.with(this).load(R.raw.cat)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(splashGif);


        startLoading();

    }

    private void startLoading(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
