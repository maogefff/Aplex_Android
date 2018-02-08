package com.aplex.webcan.flatui.view;

import com.aplex.webcan.R;
import com.aplex.webcan.R.styleable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aplex.webcan.flatui.Attributes;
import com.aplex.webcan.flatui.Attributes.AttributeChangeListener;
import com.aplex.webcan.flatui.FlatUI;




public class FlatTextView extends TextView implements AttributeChangeListener {
    private Attributes attributes;
    private int textColor = 2;
    private int backgroundColor;
    private int customBackgroundColor;
    private boolean hasOwnTextColor;

    public FlatTextView(Context context) {
        super(context);
        this.backgroundColor = Attributes.INVALID;
        this.customBackgroundColor = Attributes.INVALID;
        this.init((AttributeSet)null);
    }

    public FlatTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.backgroundColor = Attributes.INVALID;
        this.customBackgroundColor = Attributes.INVALID;
        this.init(attrs);
    }

    public FlatTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.backgroundColor = Attributes.INVALID;
        this.customBackgroundColor = Attributes.INVALID;
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        if(this.attributes == null) {
            this.attributes = new Attributes(this, this.getResources());
        }

        if(attrs != null) {
            String gradientDrawable = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textColor");
            int typeface = attrs.getStyleAttribute();
            int[] attributesArray = new int[]{16842904};
            TypedArray styleTextColorTypedArray = this.getContext().obtainStyledAttributes(typeface, attributesArray);
            int styleTextColor1 = styleTextColorTypedArray.getColor(0, -1);
            int styleTextColor2 = styleTextColorTypedArray.getColor(0, 1);
            this.hasOwnTextColor = gradientDrawable != null || styleTextColor1 == styleTextColor2;
            styleTextColorTypedArray.recycle();
            TypedArray a = this.getContext().obtainStyledAttributes(attrs, styleable.fl_FlatTextView);
            int customTheme = a.getResourceId(5, Attributes.DEFAULT_THEME);
            this.attributes.setThemeSilent(customTheme, this.getResources());
            this.attributes.setFontFamily(a.getString(3));
            this.attributes.setFontWeight(a.getString(4));
            this.attributes.setFontExtension(a.getString(2));
            this.attributes.setRadius(a.getDimensionPixelSize(1, Attributes.DEFAULT_RADIUS_PX));
            this.attributes.setBorderWidth(a.getDimensionPixelSize(0, 0));
            this.textColor = a.getInt(6, this.textColor);
            this.backgroundColor = a.getInt(7, this.backgroundColor);
            this.customBackgroundColor = a.getInt(8, this.customBackgroundColor);
            a.recycle();
        }

        GradientDrawable gradientDrawable1 = new GradientDrawable();
        if(this.backgroundColor != Attributes.INVALID) {
            gradientDrawable1.setColor(this.attributes.getColor(this.backgroundColor));
        } else if(this.customBackgroundColor != Attributes.INVALID) {
            gradientDrawable1.setColor(this.customBackgroundColor);
        } else {
            gradientDrawable1.setColor(0);
        }

        gradientDrawable1.setCornerRadius((float)this.attributes.getRadius());
        gradientDrawable1.setStroke(this.attributes.getBorderWidth(), this.attributes.getColor(this.textColor));
        this.setBackgroundDrawable(gradientDrawable1);
        if(!this.hasOwnTextColor) {
            this.setTextColor(this.attributes.getColor(this.textColor));
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