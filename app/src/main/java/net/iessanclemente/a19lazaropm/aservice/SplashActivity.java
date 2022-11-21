package net.iessanclemente.a19lazaropm.aservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Force to full screen
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);
        setContentView(R.layout.activity_splash);

        // Define animations for views in splash
        Animation animationDown = AnimationUtils.loadAnimation(
                this, R.anim.displacement_down);
        Animation animationUp = AnimationUtils.loadAnimation(
                this, R.anim.displacement_up);

        // Loads the views and apply the animations
        ImageView logoSplashImageView = findViewById(R.id.logoSplashImageView);
        logoSplashImageView.setAnimation(animationDown);
        TextView authorSplashTextView = findViewById(R.id.authorSplashTextView);
        authorSplashTextView.setAnimation(animationUp);

        // Handler for launch the next activity and finish splash, after 4 sec
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

                // Transition animation during intent
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(logoSplashImageView, "logoLoginTransition");
                pairs[1] = new Pair<View, String>(authorSplashTextView, "labelsLoginTransition");

                // goto LoginActivity with the transitions
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                        SplashActivity.this, pairs);
                startActivity(intent, activityOptions.toBundle());
                finish();
            }
        }, 4000);
    }
}