package com.pgmacdesign.dependencyinjectionsamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pmacdowell on 2017-05-19.
 */

public class CircleDrawActivity extends AppCompatActivity implements View.OnClickListener {

    //private RelativeLayout circle_draw_button_rel_layout;
    private Button circle_draw_button_2, circle_draw_button_1;
    private LinearLayout circle_draw_button_layout;
    //private CircleDrawActivityV2 circular_fab;
    //private MultiColorCircleLayout multicolor;
    //private ProgressBar circle_draw_progress_bar;
    //private FloatingActionButton circle_draw_fab;
    private MultiColorCircle my_circle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_draw_activity);
        initUI();
    }

    private void initUI(){
        //circle_draw_button_rel_layout = (RelativeLayout) this.findViewById(
                //R.id.circle_draw_button_rel_layout);
        circle_draw_button_layout = (LinearLayout) this.findViewById(
                R.id.circle_draw_button_layout);
        circle_draw_button_1 = (Button) this.findViewById(
                R.id.circle_draw_button_1);
        circle_draw_button_2 = (Button) this.findViewById(
                R.id.circle_draw_button_2);
        /*
        multicolor = (MultiColorCircleLayout) this.findViewById(
                R.id.multicolor);
        circle_draw_progress_bar = (ProgressBar) this.findViewById(
                R.id.circle_draw_progress_bar);
        circle_draw_fab = (FloatingActionButton) this.findViewById(
                R.id.circle_draw_fab);
        circular_fab = (CircleDrawActivityV2) this.findViewById(R.id.circular_fab);


        //adjust pb
        circle_draw_progress_bar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(
                this, R.color.fuchsia), PorterDuff.Mode.MULTIPLY);
        circle_draw_fab.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        */
        my_circle = (MultiColorCircle) this.findViewById(R.id.my_circle);
        my_circle.setWidthOfCircleStroke(60);
        my_circle.setWidthOfBoarderStroke(2);
        my_circle.setColorOfBoarderStroke(ContextCompat.getColor(this, R.color.purple));
        MultiColorCircle.CustomStrokeObject s1 = new MultiColorCircle.CustomStrokeObject(
            50, 0, ContextCompat.getColor(this, R.color.blue)
        );
        MultiColorCircle.CustomStrokeObject s2 = new MultiColorCircle.CustomStrokeObject(
            30, 50, ContextCompat.getColor(this, R.color.red)
        );
        MultiColorCircle.CustomStrokeObject s3 = new MultiColorCircle.CustomStrokeObject(
            20, 80, ContextCompat.getColor(this, R.color.green)
        );
        List<MultiColorCircle.CustomStrokeObject> myList = new ArrayList<>();
        myList.add(s1);
        myList.add(s2);
        myList.add(s3);

        my_circle.setCircleStrokes(myList);

        YoYo.with(Techniques.FadeIn).duration(700).playOn(my_circle);

        circle_draw_button_1.setOnClickListener(this);
        circle_draw_button_2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.circle_draw_button_1:
                //this.circular_fab.setVisibility(View.VISIBLE);
                //circular_fab.
                break;

            case R.id.circle_draw_button_2:
                //this.circular_fab.setVisibility(View.GONE);
                break;
        }
    }


}
