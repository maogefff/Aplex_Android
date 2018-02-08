package com.aplex.webcan.flatui;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by Fernflower decompiler)
//


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Path.Direction;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;

public class TouchEffectAnimator {
 private final int EASE_ANIM_DURATION = 200;
 private final int RIPPLE_ANIM_DURATION = 300;
 private final int MAX_RIPPLE_ALPHA = 255;
 private View mView;
 private int mClipRadius;
 private boolean hasRippleEffect = false;
 private int animDuration = 200;
 private int requiredRadius;
 private float mDownX;
 private float mDownY;
 private float mRadius;
 private int mCircleAlpha = 255;
 private int mRectAlpha = 0;
 private Paint mCirclePaint = new Paint();
 private Paint mRectPaint = new Paint();
 private Path mCirclePath = new Path();
 private Path mRectPath = new Path();
 private boolean isTouchReleased = false;
 private boolean isAnimatingFadeIn = false;
 private AnimationListener animationListener = new AnimationListener() {
     public void onAnimationStart(Animation animation) {
         TouchEffectAnimator.this.isAnimatingFadeIn = true;
     }

     public void onAnimationEnd(Animation animation) {
         TouchEffectAnimator.this.isAnimatingFadeIn = false;
         if(TouchEffectAnimator.this.isTouchReleased) {
             TouchEffectAnimator.this.fadeOutEffect();
         }

     }

     public void onAnimationRepeat(Animation animation) {
     }
 };

 public TouchEffectAnimator(View mView) {
     this.mView = mView;
 }

 public void setHasRippleEffect(boolean hasRippleEffect) {
     this.hasRippleEffect = hasRippleEffect;
     if(hasRippleEffect) {
         this.animDuration = 300;
     }

 }

 public void setAnimDuration(int animDuration) {
     this.animDuration = animDuration;
 }

 public void setEffectColor(int effectColor) {
     this.mCirclePaint.setColor(effectColor);
     this.mCirclePaint.setAlpha(this.mCircleAlpha);
     this.mRectPaint.setColor(effectColor);
     this.mRectPaint.setAlpha(this.mRectAlpha);
 }

 public void setClipRadius(int mClipRadius) {
     this.mClipRadius = mClipRadius;
 }

 public void onTouchEvent(MotionEvent event) {
     if(event.getActionMasked() == 3) {
         this.isTouchReleased = true;
         if(!this.isAnimatingFadeIn) {
             this.fadeOutEffect();
         }
     }

     if(event.getActionMasked() == 1) {
         this.isTouchReleased = true;
         if(!this.isAnimatingFadeIn) {
             this.fadeOutEffect();
         }
     } else if(event.getActionMasked() == 0) {
         this.requiredRadius = this.mView.getWidth() > this.mView.getHeight()?this.mView.getWidth():this.mView.getHeight();
         this.requiredRadius = (int)((double)this.requiredRadius * 1.2D);
         this.isTouchReleased = false;
         this.mDownX = event.getX();
         this.mDownY = event.getY();
         this.mCircleAlpha = 255;
         this.mRectAlpha = 0;
         TouchEffectAnimator.ValueGeneratorAnim valueGeneratorAnim = new TouchEffectAnimator.ValueGeneratorAnim(new TouchEffectAnimator.InterpolatedTimeCallback() {
             public void onTimeUpdate(float interpolatedTime) {
                 if(TouchEffectAnimator.this.hasRippleEffect) {
                     TouchEffectAnimator.this.mRadius = (float)TouchEffectAnimator.this.requiredRadius * interpolatedTime;
                 }

                 TouchEffectAnimator.this.mRectAlpha = (int)(interpolatedTime * 255.0F);
                 TouchEffectAnimator.this.mView.invalidate();
             }
         });
         valueGeneratorAnim.setInterpolator(new DecelerateInterpolator());
         valueGeneratorAnim.setDuration((long)this.animDuration);
         valueGeneratorAnim.setAnimationListener(this.animationListener);
         this.mView.startAnimation(valueGeneratorAnim);
     }

 }

 public void onDraw(Canvas canvas) {
     if(this.hasRippleEffect) {
         this.mCirclePath.reset();
         this.mCirclePaint.setAlpha(this.mCircleAlpha);
         this.mCirclePath.addRoundRect(new RectF(0.0F, 0.0F, (float)this.mView.getWidth(), (float)this.mView.getHeight()), (float)this.mClipRadius, (float)this.mClipRadius, Direction.CW);
         canvas.clipPath(this.mCirclePath);
         canvas.drawCircle(this.mDownX, this.mDownY, this.mRadius, this.mCirclePaint);
     }

     this.mRectPath.reset();
     if(this.hasRippleEffect && this.mCircleAlpha != 255) {
         this.mRectAlpha = this.mCircleAlpha / 2;
     }

     this.mRectPaint.setAlpha(this.mRectAlpha);
     canvas.drawRoundRect(new RectF(0.0F, 0.0F, (float)this.mView.getWidth(), (float)this.mView.getHeight()), (float)this.mClipRadius, (float)this.mClipRadius, this.mRectPaint);
 }

 private void fadeOutEffect() {
     TouchEffectAnimator.ValueGeneratorAnim valueGeneratorAnim = new TouchEffectAnimator.ValueGeneratorAnim(new TouchEffectAnimator.InterpolatedTimeCallback() {
         public void onTimeUpdate(float interpolatedTime) {
             TouchEffectAnimator.this.mCircleAlpha = (int)(255.0F - 255.0F * interpolatedTime);
             TouchEffectAnimator.this.mRectAlpha = TouchEffectAnimator.this.mCircleAlpha;
             TouchEffectAnimator.this.mView.invalidate();
         }
     });
     valueGeneratorAnim.setDuration((long)this.animDuration);
     this.mView.startAnimation(valueGeneratorAnim);
 }

 interface InterpolatedTimeCallback {
     void onTimeUpdate(float var1);
 }

 class ValueGeneratorAnim extends Animation {
     private TouchEffectAnimator.InterpolatedTimeCallback interpolatedTimeCallback;

     ValueGeneratorAnim(TouchEffectAnimator.InterpolatedTimeCallback interpolatedTimeCallback) {
         this.interpolatedTimeCallback = interpolatedTimeCallback;
     }

     protected void applyTransformation(float interpolatedTime, Transformation t) {
         this.interpolatedTimeCallback.onTimeUpdate(interpolatedTime);
     }
 }
}
