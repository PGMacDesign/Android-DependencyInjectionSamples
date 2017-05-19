package com.pgmacdesign.dependencyinjectionsamples;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pgmacdesign.dependencyinjectionsamples.otherutils.L;

/**
 * Created by pmacdowell on 2017-05-19.
 */

public class MultiColorCircleLayout extends RelativeLayout {

    private Context context;
    private LayoutInflater mInflater;
    private LinearLayout multicolor_circle_colors_layout;
    private ImageView multicolor_circle_colors_layout_left_side,
            multicolor_circle_colors_layout_right_side, simple_circle;

    public MultiColorCircleLayout(Context context) {
        super(context);
        this.context = context;
        mInflater = LayoutInflater.from(context);
        initCustom();
    }

    public MultiColorCircleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mInflater = LayoutInflater.from(context);
        initCustom();
    }

    public MultiColorCircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mInflater = LayoutInflater.from(context);
        initCustom();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MultiColorCircleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        mInflater = LayoutInflater.from(context);
        initCustom();
    }

    private void initCustom(){

        L.m("initCustom in multi-color circle layout");
        View view = mInflater.inflate(R.layout.multi_color_circle_layout, this, true);

        multicolor_circle_colors_layout = (LinearLayout) view.findViewById(
                R.id.multicolor_circle_colors_layout);
        multicolor_circle_colors_layout_left_side = (ImageView) view.findViewById(
                R.id.multicolor_circle_colors_layout_left_side);
        multicolor_circle_colors_layout_right_side = (ImageView) view.findViewById(
                R.id.multicolor_circle_colors_layout_right_side);
        simple_circle = (ImageView) view.findViewById(
                R.id.simple_circle);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        L.m("on draw hit in multi-color circle layout");
    }
}
