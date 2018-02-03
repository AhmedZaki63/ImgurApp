package com.example.ahmed.imgurapp.Util;

import android.content.Context;
import android.util.AttributeSet;


public class DynamicHeightImageView extends android.support.v7.widget.AppCompatImageView {
    private double heightRatio;

    public DynamicHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightImageView(Context context) {
        super(context);
    }

    public double getHeightRatio() {
        return heightRatio;
    }

    public void setHeightRatio(double ratio) {
        if (ratio != heightRatio) {
            heightRatio = ratio;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (heightRatio > 0.0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * heightRatio);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
