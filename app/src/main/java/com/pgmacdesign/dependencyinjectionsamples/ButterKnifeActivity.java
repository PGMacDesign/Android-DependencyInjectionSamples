package com.pgmacdesign.dependencyinjectionsamples;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * For more info, see - http://jakewharton.github.io/butterknife/
 * Created by pmacdowell on 2017-01-03.
 */
public class ButterKnifeActivity extends AppCompatActivity {

    //UI
    @BindView(R.id.butter_knife_layout_1) RelativeLayout butter_knife_layout_1;
    @BindView(R.id.butter_knife_layout_2) RelativeLayout butter_knife_layout_2;
    @BindView(R.id.butter_knife_layout_3) RelativeLayout butter_knife_layout_3;
    @BindView(R.id.butter_knife_layout_4) RelativeLayout butter_knife_layout_4;
    @BindView(R.id.butter_knife_button_1) Button butter_knife_button_1;
    @BindView(R.id.butter_knife_button_2) Button butter_knife_button_2;
    @BindView(R.id.butter_knife_button_3) Button butter_knife_button_3;
    @BindView(R.id.butter_knife_button_4) Button butter_knife_button_4;
    @BindView(R.id.butter_knife_tv_1) TextView butter_knife_tv_1;
    @BindView(R.id.butter_knife_tv_2) TextView butter_knife_tv_2;
    @BindView(R.id.butter_knife_tv_3) TextView butter_knife_tv_3;
    @BindView(R.id.butter_knife_tv_4) TextView butter_knife_tv_4;

    //Collections of Views
    @BindViews({R.id.butter_knife_button_1, R.id.butter_knife_button_2,
            R.id.butter_knife_button_3, R.id.butter_knife_button_4})
    List<Button> buttonList;

    //Resources
    @BindString(R.string.app_name) String app_name;
    @BindDrawable(R.drawable.pause_icon) Drawable pause_icon;
    @BindColor(R.color.Red) int red;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butter_knife_samples);
        ButterKnife.bind(this);

        messWithViews();
    }

    private void messWithViews(){
        //Adjust group
        for(Button btn : buttonList){
            if(btn != null) {
                btn.setTransformationMethod(null);
            }
        }


    }

    //Set listeners
    @OnClick(R.id.butter_knife_button_1)
    public void button1Hit(View view){ //View from the button
        butter_knife_button_1.setTextColor(red);
    }
    @OnClick(R.id.butter_knife_button_2)
    public void button2Hit(){ //Sample empty param passed in
        Log.d("stuff!", "Stuff!");
    }
    @OnClick(R.id.butter_knife_button_3)
    public void button3Hit(Button button){ //Pass in the actual button
        Log.d("stuff!", "Stuff!");
    }
    @OnClick({R.id.butter_knife_button_4, R.id.butter_knife_tv_1,
            R.id.butter_knife_tv_2, R.id.butter_knife_tv_3, R.id.butter_knife_tv_4})
    public void button4Hit(){
        Log.d("stuff!", "Stuff!");
    }
}
