package com.aplex.webcan.flatui.view;

import com.aplex.webcan.R;
import com.aplex.webcan.R.styleable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import com.aplex.webcan.flatui.Attributes;
import com.aplex.webcan.flatui.Attributes.AttributeChangeListener;

public class FlatToggleButton extends ToggleButton implements AttributeChangeListener {
    private Attributes attributes;
    private int space = 14;
    private int padding;

    public FlatToggleButton(Context context) {
        super(context);
        this.init((AttributeSet)null);
    }

    public FlatToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    public FlatToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        if(this.attributes == null) {
            this.attributes = new Attributes(this, this.getResources());
        }

        if(attrs != null) {
            TypedArray uncheckedEnabledFrontCore = this.getContext().obtainStyledAttributes(attrs, styleable.fl_FlatToggleButton);
            int uncheckedEnabledFront = uncheckedEnabledFrontCore.getResourceId(1, Attributes.DEFAULT_THEME);
            this.attributes.setThemeSilent(uncheckedEnabledFront, this.getResources());
            this.attributes.setRadius(uncheckedEnabledFrontCore.getDimensionPixelSize(0, Attributes.DEFAULT_RADIUS_PX));
            this.space = uncheckedEnabledFrontCore.getDimensionPixelSize(2, this.space);
            this.padding = this.space / 10;
            uncheckedEnabledFrontCore.recycle();
        }

        ShapeDrawable uncheckedEnabledFrontCore1 = new ShapeDrawable(new RoundRectShape(this.attributes.getOuterRadius(), (RectF)null, (float[])null));
        uncheckedEnabledFrontCore1.getPaint().setColor(this.attributes.getColor(2));
        InsetDrawable uncheckedEnabledFront1 = new InsetDrawable(uncheckedEnabledFrontCore1, this.padding);
        ShapeDrawable uncheckedEnabledBack = new ShapeDrawable(new RoundRectShape(this.attributes.getOuterRadius(), (RectF)null, (float[])null));
        uncheckedEnabledBack.getPaint().setColor(Color.parseColor("#f2f2f2"));
        uncheckedEnabledBack.setIntrinsicWidth(this.space / 2 * 5);
        uncheckedEnabledBack.setIntrinsicHeight(this.space);
        uncheckedEnabledBack.setPadding(0, 0, this.space / 2 * 5, 0);
        Drawable[] d1 = new Drawable[]{uncheckedEnabledBack, uncheckedEnabledFront1};
        LayerDrawable uncheckedEnabled = new LayerDrawable(d1);
        ShapeDrawable checkedEnabledFrontCore = new ShapeDrawable(new RoundRectShape(this.attributes.getOuterRadius(), (RectF)null, (float[])null));
        checkedEnabledFrontCore.getPaint().setColor(this.attributes.getColor(2));
        InsetDrawable checkedEnabledFront = new InsetDrawable(checkedEnabledFrontCore, this.padding);
        ShapeDrawable checkedEnabledBack = new ShapeDrawable(new RoundRectShape(this.attributes.getOuterRadius(), (RectF)null, (float[])null));
        checkedEnabledBack.getPaint().setColor(this.attributes.getColor(3));
        checkedEnabledBack.setPadding(this.space / 2 * 5, 0, 0, 0);
        Drawable[] d2 = new Drawable[]{checkedEnabledBack, checkedEnabledFront};
        LayerDrawable checkedEnabled = new LayerDrawable(d2);
        ShapeDrawable uncheckedDisabledFrontCore = new ShapeDrawable(new RoundRectShape(this.attributes.getOuterRadius(), (RectF)null, (float[])null));
        uncheckedDisabledFrontCore.getPaint().setColor(Color.parseColor("#d2d2d2"));
        InsetDrawable uncheckedDisabledFront = new InsetDrawable(uncheckedDisabledFrontCore, this.padding);
        ShapeDrawable uncheckedDisabledBack = new ShapeDrawable(new RoundRectShape(this.attributes.getOuterRadius(), (RectF)null, (float[])null));
        uncheckedDisabledBack.getPaint().setColor(Color.parseColor("#f2f2f2"));
        uncheckedDisabledBack.setPadding(0, 0, this.space / 2 * 5, 0);
        Drawable[] d3 = new Drawable[]{uncheckedDisabledBack, uncheckedDisabledFront};
        LayerDrawable uncheckedDisabled = new LayerDrawable(d3);
        ShapeDrawable checkedDisabledFrontCore = new ShapeDrawable(new RoundRectShape(this.attributes.getOuterRadius(), (RectF)null, (float[])null));
        checkedDisabledFrontCore.getPaint().setColor(this.attributes.getColor(3));
        InsetDrawable checkedDisabledFront = new InsetDrawable(checkedDisabledFrontCore, this.padding);
        ShapeDrawable checkedDisabledBack = new ShapeDrawable(new RoundRectShape(this.attributes.getOuterRadius(), (RectF)null, (float[])null));
        checkedDisabledBack.getPaint().setColor(Color.parseColor("#f2f2f2"));
        checkedDisabledBack.setPadding(this.space / 2 * 5, 0, 0, 0);
        Drawable[] d4 = new Drawable[]{checkedDisabledBack, checkedDisabledFront};
        LayerDrawable checkedDisabled = new LayerDrawable(d4);
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{-16842912, 16842910}, new InsetDrawable(uncheckedEnabled, this.padding * 2));
        states.addState(new int[]{16842912, 16842910}, new InsetDrawable(checkedEnabled, this.padding * 2));
        states.addState(new int[]{-16842912, -16842910}, new InsetDrawable(uncheckedDisabled, this.padding * 2));
        states.addState(new int[]{16842912, -16842910}, new InsetDrawable(checkedDisabled, this.padding * 2));
        this.setBackgroundDrawable(states);
        this.setText("");
        this.setTextOff("");
        this.setTextOn("");
        this.setTextSize(0.0F);
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void onThemeChange() {
        this.init((AttributeSet)null);
    }
}
