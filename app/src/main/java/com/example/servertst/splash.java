package com.example.servertst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class splash extends AppCompatActivity {

    Handler handler;
    ImageView imageView, i1, i2, i3, i4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        imageView = findViewById(R.id.mineral_logo);
        i1 = findViewById(R.id.chlorine);
        i2 = findViewById(R.id.zinc);
        i3 = findViewById(R.id.magnesium);
        i4 = findViewById(R.id.silicon);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(online())
                {
                    gomain();
                }
                else
                {
                    LinearLayout l = findViewById(R.id.linear_splash);
                    Snackbar snackbar = Snackbar
                            .make(l,"Check your Internet Connection!!!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recreate();
                                }
                            });
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.show();
                }
            }
        }, 5500);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        imageView.startAnimation(animation);
        i1.startAnimation(animation);
        i2.startAnimation(animation);
        i3.startAnimation(animation);
        i4.startAnimation(animation);

    }

    private boolean online() {
        ConnectivityManager connectivityManager =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void gomain(){


            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();

    }

}
