/*
 * Copyright (c) 2026 Mohammed Husain Kapadia (MHK). All Rights Reserved.
 * Project: VicePhone - GTA 6 Duo Companion (Nexconn Build Challenge 2026)
 */
package com.example.kapslobby;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView tvTitle = findViewById(R.id.tvSplashTitle);
        TextView tvSub = findViewById(R.id.tvSplashSub);

        // Smooth Zoom + Fade-in Animation
        tvTitle.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(1100)
                .start();

        tvSub.animate()
                .alpha(1f)
                .setStartDelay(500)
                .setDuration(900)
                .start();

        // 2.2 seconds baad MainActivity par redirect
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 2200);
    }
}