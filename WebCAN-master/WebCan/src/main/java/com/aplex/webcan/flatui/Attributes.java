package com.aplex.webcan.flatui;
//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by Fernflower decompiler)
//



import com.aplex.webcan.R.array;

import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;

public class Attributes {
 public static int INVALID = -1;
 public static int DEFAULT_THEME;
 public static final int DEFAULT_TOUCH_EFFECT = 0;
 public static final int EASE_TOUCH_EFFECT = 1;
 public static final int RIPPLE_TOUCH_EFFECT = 2;
 public static final String DEFAULT_FONT_FAMILY = "roboto";
 public static final String DEFAULT_FONT_WEIGHT = "light";
 public static final String DEFAULT_FONT_EXTENSION = "ttf";
 public static final int DEFAULT_TEXT_APPEARANCE = 0;
 public static int DEFAULT_RADIUS_DP;
 public static int DEFAULT_BORDER_WIDTH_DP;
 public static int DEFAULT_SIZE_DP;
 public static int DEFAULT_RADIUS_PX;
 public static int DEFAULT_BORDER_WIDTH_PX;
 public static int DEFAULT_SIZE_PX;
 private int[] colors;
 private int theme = -1;
 private int touchEffect = 0;
 private String fontFamily = "roboto";
 private String fontWeight = "light";
 private String fontExtension = "ttf";
 private int textAppearance = 0;
 private int radius;
 private int size;
 private int borderWidth;
 private Attributes.AttributeChangeListener attributeChangeListener;

 public Attributes(Attributes.AttributeChangeListener attributeChangeListener, Resources resources) {
     this.radius = DEFAULT_RADIUS_PX;
     this.size = DEFAULT_SIZE_PX;
     this.borderWidth = DEFAULT_BORDER_WIDTH_PX;
     this.attributeChangeListener = attributeChangeListener;
     this.setThemeSilent(DEFAULT_THEME, resources);
 }

 public int getTheme() {
     return this.theme;
 }

 public void setTheme(int theme, Resources resources) {
     this.setThemeSilent(theme, resources);
     this.attributeChangeListener.onThemeChange();
 }

 public void setThemeSilent(int theme, Resources resources) {
     try {
         this.theme = theme;
         this.colors = resources.getIntArray(theme);
     } catch (NotFoundException var4) {
         this.colors = new int[]{Color.parseColor("#732219"), Color.parseColor("#a63124"), Color.parseColor("#d94130"), Color.parseColor("#f2b6ae")};
     }

 }

 public void setColors(int[] colors) {
     this.colors = colors;
     this.attributeChangeListener.onThemeChange();
 }

 public int getColor(int colorPos) {
     return this.colors[colorPos];
 }

 public String getFontFamily() {
     return this.fontFamily;
 }

 public void setFontFamily(String fontFamily) {
     if(fontFamily != null && !fontFamily.equals("") && !fontFamily.equals("null")) {
         this.fontFamily = fontFamily;
     }

 }

 public String getFontWeight() {
     return this.fontWeight;
 }

 public void setFontWeight(String fontWeight) {
     if(fontWeight != null && !fontWeight.equals("") && !fontWeight.equals("null")) {
         this.fontWeight = fontWeight;
     }

 }

 public String getFontExtension() {
     return this.fontExtension;
 }

 public void setFontExtension(String fontExtension) {
     if(fontExtension != null && !fontExtension.equals("") && !fontExtension.equals("null")) {
         this.fontExtension = fontExtension;
     }

 }

 public int getRadius() {
     return this.radius;
 }

 public float[] getOuterRadius() {
     return new float[]{(float)this.radius, (float)this.radius, (float)this.radius, (float)this.radius, (float)this.radius, (float)this.radius, (float)this.radius, (float)this.radius};
 }

 public void setRadius(int radius) {
     this.radius = radius;
 }

 public int getSize() {
     return this.size;
 }

 public void setSize(int size) {
     this.size = size;
 }

 public int getBorderWidth() {
     return this.borderWidth;
 }

 public void setBorderWidth(int borderWidth) {
     this.borderWidth = borderWidth;
 }

 public int getTextAppearance() {
     return this.textAppearance;
 }

 public void setTextAppearance(int textAppearance) {
     this.textAppearance = textAppearance;
 }

 public int getTouchEffect() {
     return this.touchEffect;
 }

 public void setTouchEffect(int touchEffect) {
     this.touchEffect = touchEffect;
 }

 public boolean hasTouchEffect() {
     return this.touchEffect != 0;
 }

 static {
     DEFAULT_THEME = array.blood;
     DEFAULT_RADIUS_DP = 4;
     DEFAULT_BORDER_WIDTH_DP = 2;
     DEFAULT_SIZE_DP = 10;
     DEFAULT_RADIUS_PX = 8;
     DEFAULT_BORDER_WIDTH_PX = 4;
     DEFAULT_SIZE_PX = 20;
 }

 public interface AttributeChangeListener {
     void onThemeChange();
 }
}
