package com.application.imail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashScreenActivity extends Activity {

    // Splash screen waktu
    private static int SPLASH_TIME_OUT = 3000;
//    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        loading = (ProgressBar) findViewById(R.id.progressBar);
//        // Untuk merubah warna progress bar
//        loading.getIndeterminateDrawable().setColorFilter(Color.parseColor("#51d8c7"), PorterDuff.Mode.MULTIPLY);

        new Handler().postDelayed(new Runnable() {
            //Biasanya splash screen menampilkan logo / nama aplikasi
            @Override
            public void run() {
                // Method ini berfungsi untuk mengeksekusi waktu
                // Pindah layout ke MainActivity
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);
                // tutup activity
                finish();
            }
        }, SPLASH_TIME_OUT);



    }
}


