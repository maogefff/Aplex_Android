package com.aplex.webcan.flatui.view;
import com.aplex.webcan.R;
import com.aplex.webcan.R.styleable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.aplex.webcan.flatui.Attributes;
import com.aplex.webcan.flatui.Attributes.AttributeChangeListener;
import com.aplex.webcan.flatui.FlatUI;




public class FlatCheckBox extends CheckBox implements AttributeChangeListener {
    private Attributes attributes;
    private int dotMargin = 2;

    public FlatCheckBox(Context context) {
        super(context);
        this.init((AttributeSet)null);
    }

    public FlatCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    public FlatCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        if(this.attributes == null) {
            this.attributes = new Attributes(this, this.getResources());
        }

        if(attrs != null) {
            TypedArray uncheckedEnabled = this.getContext().obtainStyledAttributes(attrs, styleable.fl_FlatCheckBox);
            int checkedOutside = uncheckedEnabled.getResourceId(6, Attributes.DEFAULT_THEME);
            this.attributes.setThemeSilent(checkedOutside, this.getResources());
            this.attributes.setFontFamily(uncheckedEnabled.getString(3));
            this.attributes.setFontWeight(uncheckedEnabled.getString(4));
            this.attributes.setFontExtension(uncheckedEnabled.getString(2));
            this.attributes.setSize(uncheckedEnabled.getDimensionPixelSize(5, Attributes.DEFAULT_SIZE_PX));
            this.attributes.setRadius(uncheckedEnabled.getDimensionPixelSize(0, Attributes.DEFAULT_RADIUS_PX));
            this.attributes.setBorderWidth(this.attributes.getSize() / 10);
            this.dotMargin = uncheckedEnabled.getDimensionPixelSize(1, this.dotMargin);
            uncheckedEnabled.recycle();
        }

        GradientDrawable uncheckedEnabled1 = new GradientDrawable();
        uncheckedEnabled1.setCornerRadius((float)this.attributes.getRadius());
        uncheckedEnabled1.setSize(this.attributes.getSize(), this.attributes.getSize());
        uncheckedEnabled1.setColor(0);
        uncheckedEnabled1.setStroke(this.attributes.getBorderWidth(), this.attributes.getColor(2));
        GradientDrawable checkedOutside1 = new GradientDrawable();
        checkedOutside1.setCornerRadius((float)this.attributes.getRadius());
        checkedOutside1.setSize(this.attributes.getSize(), this.attributes.getSize());
        checkedOutside1.setColor(0);
        checkedOutside1.setStroke(this.attributes.getBorderWidth(), this.attributes.getColor(2));
        PaintDrawable checkedCore = new PaintDrawable(this.attributes.getColor(2));
        checkedCore.setCornerRadius((float)this.attributes.getRadius());
        checkedCore.setIntrinsicHeight(this.attributes.getSize());
        checkedCore.setIntrinsicWidth(this.attributes.getSize());
        InsetDrawable checkedInside = new InsetDrawable(checkedCore, this.attributes.getBorderWidth() + this.dotMargin, this.attributes.getBorderWidth() + this.dotMargin, this.attributes.getBorderWidth() + this.dotMargin, this.attributes.getBorderWidth() + this.dotMargin);
        Drawable[] checkedEnabledDrawable = new Drawable[]{checkedOutside1, checkedInside};
        LayerDrawable checkedEnabled = new LayerDrawable(checkedEnabledDrawable);
        GradientDrawable uncheckedDisabled = new GradientDrawable();
        uncheckedDisabled.setCornerRadius((float)this.attributes.getRadius());
        uncheckedDisabled.setSize(this.attributes.getSize(), this.attributes.getSize());
        uncheckedDisabled.setColor(0);
        uncheckedDisabled.setStroke(this.attributes.getBorderWidth(), this.attributes.getColor(3));
        GradientDrawable checkedOutsideDisabled = new GradientDrawable();
        checkedOutsideDisabled.setCornerRadius((float)this.attributes.getRadius());
        checkedOutsideDisabled.setSize(this.attributes.getSize(), this.attributes.getSize());
        checkedOutsideDisabled.setColor(0);
        checkedOutsideDisabled.setStroke(this.attributes.getBorderWidth(), this.attributes.getColor(3));
        PaintDrawable checkedCoreDisabled = new PaintDrawable(this.attributes.getColor(3));
        checkedCoreDisabled.setCornerRadius((float)this.attributes.getRadius());
        checkedCoreDisabled.setIntrinsicHeight(this.attributes.getSize());
        checkedCoreDisabled.setIntrinsicWidth(this.attributes.getSize());
        InsetDrawable checkedInsideDisabled = new InsetDrawable(checkedCoreDisabled, this.attributes.getBorderWidth() + this.dotMargin, this.attributes.getBorderWidth() + this.dotMargin, this.attributes.getBorderWidth() + this.dotMargin, this.attributes.getBorderWidth() + this.dotMargin);
        Drawable[] checkedDisabledDrawable = new Drawable[]{checkedOutsideDisabled, checkedInsideDisabled};
        LayerDrawable checkedDisabled = new LayerDrawable(checkedDisabledDrawable);
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{-16842912, 16842910}, uncheckedEnabled1);
        states.addState(new int[]{16842912, 16842910}, checkedEnabled);
        states.addState(new int[]{-16842912, -16842910}, uncheckedDisabled);
        states.addState(new int[]{16842912, -16842910}, checkedDisabled);
        this.setButtonDrawable(states);
        this.setPadding(this.attributes.getSize() / 4 * 5, 0, 0, 0);
        this.setTextColor(this.attributes.getColor(2));
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
