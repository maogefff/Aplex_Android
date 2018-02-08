package com.aplex.webcan.flatui.view;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by Fernflower decompiler)
//


import com.aplex.webcan.R;
import com.aplex.webcan.R.styleable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.aplex.webcan.flatui.Attributes;
import com.aplex.webcan.flatui.Attributes.AttributeChangeListener;
import com.aplex.webcan.flatui.FlatUI;
import com.aplex.webcan.flatui.TouchEffectAnimator;




public class FlatButton extends Button implements AttributeChangeListener {
 private Attributes attributes;
 private int bottom = 0;
 private TouchEffectAnimator touchEffectAnimator;

 public FlatButton(Context context) {
     super(context);
     this.init((AttributeSet)null);
 }

 public FlatButton(Context context, AttributeSet attrs) {
     super(context, attrs);
     this.init(attrs);
 }

 public FlatButton(Context context, AttributeSet attrs, int defStyle) {
     super(context, attrs, defStyle);
     this.init(attrs);
 }

 public boolean onTouchEvent(MotionEvent event) {
     if(this.attributes.hasTouchEffect() && this.touchEffectAnimator != null) {
         this.touchEffectAnimator.onTouchEvent(event);
     }

     return super.onTouchEvent(event);
 }

 @SuppressLint("WrongCall")
protected void onDraw(Canvas canvas) {
     if(this.attributes.hasTouchEffect() && this.touchEffectAnimator != null) {
         this.touchEffectAnimator.onDraw(canvas);
     }

     super.onDraw(canvas);
 }

 private void init(AttributeSet attrs) {
     int paddingTop = this.getPaddingTop();
     int paddingRight = this.getPaddingRight();
     int paddingLeft = this.getPaddingLeft();
     int paddingBottom = this.getPaddingBottom();
     if(this.attributes == null) {
         this.attributes = new Attributes(this, this.getResources());
     }

     if(attrs != null) {
         TypedArray normalFront = this.getContext().obtainStyledAttributes(attrs, styleable.fl_FlatButton);
         int normalBack = normalFront.getResourceId(5, Attributes.DEFAULT_THEME);
         this.attributes.setThemeSilent(normalBack, this.getResources());
         this.attributes.setTouchEffect(normalFront.getInt(6, 0));
         this.attributes.setFontFamily(normalFront.getString(2));
         this.attributes.setFontWeight(normalFront.getString(3));
         this.attributes.setFontExtension(normalFront.getString(1));
         this.attributes.setTextAppearance(normalFront.getInt(4, 0));
         this.attributes.setRadius(normalFront.getDimensionPixelSize(0, Attributes.DEFAULT_RADIUS_PX));
         this.bottom = normalFront.getDimensionPixelSize(7, this.bottom);
         normalFront.recycle();
     }

     if(this.attributes.hasTouchEffect()) {
         boolean normalFront2 = this.attributes.getTouchEffect() == 2;
         this.touchEffectAnimator = new TouchEffectAnimator(this);
         this.touchEffectAnimator.setHasRippleEffect(normalFront2);
         this.touchEffectAnimator.setEffectColor(this.attributes.getColor(1));
         this.touchEffectAnimator.setClipRadius(this.attributes.getRadius());
     }

     ShapeDrawable normalFront1 = new ShapeDrawable(new RoundRectShape(this.attributes.getOuterRadius(), (RectF)null, (float[])null));
     normalFront1.getPaint().setColor(this.attributes.getColor(2));
     ShapeDrawable normalBack1 = new ShapeDrawable(new RoundRectShape(this.attributes.getOuterRadius(), (RectF)null, (float[])null));
     normalBack1.getPaint().setColor(this.attributes.getColor(1));
     normalBack1.setPadding(0, 0, 0, this.bottom);
     Drawable[] d = new Drawable[]{normalBack1, normalFront1};
     LayerDrawable normal = new LayerDrawable(d);
     ShapeDrawable pressedFront = new ShapeDrawable(new RoundRectShape(this.attributes.getOuterRadius(), (RectF)null, (float[])null));
     pressedFront.getPaint().setColor(this.attributes.getColor(1));
     ShapeDrawable pressedBack = new ShapeDrawable(new RoundRectShape(this.attributes.getOuterRadius(), (RectF)null, (float[])null));
     pressedBack.getPaint().setColor(this.attributes.getColor(0));
     if(this.bottom != 0) {
         pressedBack.setPadding(0, 0, 0, this.bottom / 2);
     }

     Drawable[] d2 = new Drawable[]{pressedBack, pressedFront};
     LayerDrawable pressed = new LayerDrawable(d2);
     ShapeDrawable disabledFront = new ShapeDrawable(new RoundRectShape(this.attributes.getOuterRadius(), (RectF)null, (float[])null));
     disabledFront.getPaint().setColor(this.attributes.getColor(3));
     disabledFront.getPaint().setAlpha(160);
     ShapeDrawable disabledBack = new ShapeDrawable(new RoundRectShape(this.attributes.getOuterRadius(), (RectF)null, (float[])null));
     disabledBack.getPaint().setColor(this.attributes.getColor(2));
     Drawable[] d3 = new Drawable[]{disabledBack, disabledFront};
     LayerDrawable disabled = new LayerDrawable(d3);
     StateListDrawable states = new StateListDrawable();
     if(!this.attributes.hasTouchEffect()) {
         states.addState(new int[]{16842919, 16842910}, pressed);
     }

     states.addState(new int[]{16842908, 16842910}, pressed);
     states.addState(new int[]{16842910}, normal);
     states.addState(new int[]{-16842910}, disabled);
     this.setBackgroundDrawable(states);
     this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
     if(this.attributes.getTextAppearance() == 1) {
         this.setTextColor(this.attributes.getColor(0));
     } else if(this.attributes.getTextAppearance() == 2) {
         this.setTextColor(this.attributes.getColor(3));
     } else {
         this.setTextColor(-1);
     }

     if(!this.isInEditMode()) {
         Typeface typeface = FlatUI.getFont(this.getContext(), this.attributes);
         if(typeface != null) {
             this.setTypeface(typeface);
         }
     }

 }

 public Attributes getAttributes() {
     return this.attributes;
 }

 public void onThemeChange() {
     this.init((AttributeSet)null);
 }
}
