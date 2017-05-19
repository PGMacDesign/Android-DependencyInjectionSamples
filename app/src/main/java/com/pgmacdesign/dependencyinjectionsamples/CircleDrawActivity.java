package com.pgmacdesign.dependencyinjectionsamples;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by pmacdowell on 2017-05-19.
 */

public class CircleDrawActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout circle_draw_button_rel_layout;
    private Button circle_draw_button_2, circle_draw_button_1;
    private LinearLayout circle_draw_button_layout;
    private CircleDrawActivityV2 circular_fab;
    //private MultiColorCircleLayout multicolor;
    private ProgressBar circle_draw_progress_bar;
    private FloatingActionButton circle_draw_fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_draw_activity);
        initUI();
    }

    private void initUI(){
        circle_draw_button_rel_layout = (RelativeLayout) this.findViewById(
                R.id.circle_draw_button_rel_layout);
        circle_draw_button_layout = (LinearLayout) this.findViewById(
                R.id.circle_draw_button_layout);
        circle_draw_button_1 = (Button) this.findViewById(
                R.id.circle_draw_button_1);
        circle_draw_button_2 = (Button) this.findViewById(
                R.id.circle_draw_button_2);
        //multicolor = (MultiColorCircleLayout) this.findViewById(
                //R.id.multicolor);
        circle_draw_progress_bar = (ProgressBar) this.findViewById(
                R.id.circle_draw_progress_bar);
        circle_draw_fab = (FloatingActionButton) this.findViewById(
                R.id.circle_draw_fab);
        circular_fab = (CircleDrawActivityV2) this.findViewById(R.id.circular_fab);

        //adjust pb
        circle_draw_progress_bar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(
                this, R.color.fuchsia), PorterDuff.Mode.MULTIPLY);
        circle_draw_fab.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));


        circle_draw_button_1.setOnClickListener(this);
        circle_draw_button_2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.circle_draw_button_1:
                this.circular_fab.setVisibility(View.VISIBLE);
                //circular_fab.
                break;

            case R.id.circle_draw_button_2:
                this.circular_fab.setVisibility(View.GONE);
                break;
        }
    }


}
