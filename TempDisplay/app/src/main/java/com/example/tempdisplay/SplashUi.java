package com.example.tempdisplay;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

public class SplashUi extends Activity {
	private ImageView mLogo;
    private String TAG = "SplashUi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_welcome);
        mLogo = (ImageView) findViewById(R.id.logo);
        startAnimator();
    }

    private void startAnimator() {

        ObjectAnimator anim = ObjectAnimator.ofFloat(mLogo, "alpha", 0f, 1f).setDuration(1000);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(SplashUi.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }
}
