package com.aplex.webcan.flatui.view;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by Fernflower decompiler)
//

import com.aplex.webcan.R;
import com.aplex.webcan.R.styleable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.aplex.webcan.flatui.Attributes;
import com.aplex.webcan.flatui.Attributes.AttributeChangeListener;
import com.aplex.webcan.flatui.FlatUI;




public class FlatAutoCompleteTextView extends AutoCompleteTextView implements AttributeChangeListener {
 private Attributes attributes;
 private int style = 0;
 private boolean hasOwnTextColor;
 private boolean hasOwnHintColor;

 public FlatAutoCompleteTextView(Context context) {
     super(context);
     this.init((AttributeSet)null);
 }

 public FlatAutoCompleteTextView(Context context, AttributeSet attrs) {
     super(context, attrs);
     this.init(attrs);
 }

 public FlatAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
     super(context, attrs, defStyle);
     this.init(attrs);
 }

 private void init(AttributeSet attrs) {
     if(this.attributes == null) {
         this.attributes = new Attributes(this, this.getResources());
     }

     if(attrs != null) {
         this.hasOwnTextColor = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textColor") != null;
         this.hasOwnHintColor = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textColorHint") != null;
         TypedArray backgroundDrawable = this.getContext().obtainStyledAttributes(attrs, styleable.fl_FlatAutoCompleteTextView);
         int typeface = backgroundDrawable.getResourceId(6, Attributes.DEFAULT_THEME);
         this.attributes.setThemeSilent(typeface, this.getResources());
         this.attributes.setFontFamily(backgroundDrawable.getString(3));
         this.attributes.setFontWeight(backgroundDrawable.getString(4));
         this.attributes.setFontExtension(backgroundDrawable.getString(2));
         this.attributes.setTextAppearance(backgroundDrawable.getInt(5, 0));
         this.attributes.setRadius(backgroundDrawable.getDimensionPixelSize(1, Attributes.DEFAULT_RADIUS_PX));
         this.attributes.setBorderWidth(backgroundDrawable.getDimensionPixelSize(0, Attributes.DEFAULT_BORDER_WIDTH_PX));
         this.style = backgroundDrawable.getInt(7, 0);
         backgroundDrawable.recycle();
     }

     GradientDrawable backgroundDrawable1 = new GradientDrawable();
     backgroundDrawable1.setCornerRadius((float)this.attributes.getRadius());
     if(this.style == 0) {
         if(!this.hasOwnTextColor) {
             this.setTextColor(this.attributes.getColor(3));
         }

         backgroundDrawable1.setColor(this.attributes.getColor(2));
         backgroundDrawable1.setStroke(0, this.attributes.getColor(2));
     } else if(this.style == 1) {
         if(!this.hasOwnTextColor) {
             this.setTextColor(this.attributes.getColor(2));
         }

         backgroundDrawable1.setColor(-1);
         backgroundDrawable1.setStroke(this.attributes.getBorderWidth(), this.attributes.getColor(2));
     } else if(this.style == 2) {
         if(!this.hasOwnTextColor) {
             this.setTextColor(this.attributes.getColor(1));
         }

         backgroundDrawable1.setColor(0);
         backgroundDrawable1.setStroke(this.attributes.getBorderWidth(), this.attributes.getColor(2));
     }

     this.setBackgroundDrawable(backgroundDrawable1);
     if(!this.hasOwnHintColor) {
         this.setHintTextColor(this.attributes.getColor(3));
     }

     if(this.attributes.getTextAppearance() == 1) {
         this.setTextColor(this.attributes.getColor(0));
     } else if(this.attributes.getTextAppearance() == 2) {
         this.setTextColor(this.attributes.getColor(3));
     }

     if(!this.isInEditMode()) {
         Typeface typeface1 = FlatUI.getFont(this.getContext(), this.attributes);
         if(typeface1 != null) {
             this.setTypeface(typeface1);
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
