package com.aplex.webcan.flatui;
//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by Fernflower decompiler)
//


import com.aplex.webcan.R.array;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

public class FlatUI {
 public static final String androidStyleNameSpace = "http://schemas.android.com/apk/res/android";
 public static final int SAND;
 public static final int ORANGE;
 public static final int CANDY;
 public static final int BLOSSOM;
 public static final int GRAPE;
 public static final int DEEP;
 public static final int SKY;
 public static final int GRASS;
 public static final int DARK;
 public static final int SNOW;
 public static final int SEA;
 public static final int BLOOD;

 public FlatUI() {
 }

 public static void initDefaultValues(Context context) {
     Attributes.DEFAULT_BORDER_WIDTH_PX = (int)dipToPx(context, (float)Attributes.DEFAULT_BORDER_WIDTH_DP);
     Attributes.DEFAULT_RADIUS_PX = (int)dipToPx(context, (float)Attributes.DEFAULT_RADIUS_DP);
     Attributes.DEFAULT_SIZE_PX = (int)dipToPx(context, (float)Attributes.DEFAULT_SIZE_DP);
 }

 public static Typeface getFont(Context context, Attributes attributes) {
     String fontPath = "fonts/" + attributes.getFontFamily() + "_" + attributes.getFontWeight() + "." + attributes.getFontExtension();

     try {
         return Typeface.createFromAsset(context.getAssets(), fontPath);
     } catch (Exception var4) {
         Log.e("FlatUI", "Font file at " + fontPath + " cannot be found or the file is " + "not a valid font file. Please be sure that library assets are included " + "to project. If not, copy assets/fonts folder of the library to your " + "projects assets folder.");
         return null;
     }
 }

 public static Drawable getActionBarDrawable(Activity activity, int theme, boolean dark) {
     return getActionBarDrawable(activity, theme, dark, 0.0F);
 }

 public static Drawable getActionBarDrawable(Activity activity, int theme, boolean dark, float borderBottom) {
     int[] colors = activity.getResources().getIntArray(theme);
     int color1 = colors[2];
     int color2 = colors[1];
     if(dark) {
         color1 = colors[1];
         color2 = colors[0];
     }

     borderBottom = dipToPx(activity, borderBottom);
     PaintDrawable front = new PaintDrawable(color1);
     PaintDrawable bottom = new PaintDrawable(color2);
     Drawable[] d = new Drawable[]{bottom, front};
     LayerDrawable drawable = new LayerDrawable(d);
     drawable.setLayerInset(1, 0, 0, 0, (int)borderBottom);
     return drawable;
 }

 public static void setDefaultTheme(int theme) {
     Attributes.DEFAULT_THEME = theme;
 }

 private static float dipToPx(Context context, float dp) {
     DisplayMetrics metrics = context.getResources().getDisplayMetrics();
     return TypedValue.applyDimension(1, dp, metrics);
 }

 static {
     SAND = array.sand;
     ORANGE = array.orange;
     CANDY = array.candy;
     BLOSSOM = array.blossom;
     GRAPE = array.grape;
     DEEP = array.deep;
     SKY = array.sky;
     GRASS = array.grass;
     DARK = array.dark;
     SNOW = array.snow;
     SEA = array.sea;
     BLOOD = array.blood;
 }
}
