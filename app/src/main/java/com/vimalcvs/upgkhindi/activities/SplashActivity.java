package com.vimalcvs.upgkhindi.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.vimalcvs.upgkhindi.databinding.ActivitySplashBinding;


public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ObjectAnimator.ofInt(binding.spaceTvContentBottom, "progress", 100).setDuration(SPLASH_TIME_OUT).start();
        new Handler().postDelayed(() -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();

        }, SPLASH_TIME_OUT);
    }
}