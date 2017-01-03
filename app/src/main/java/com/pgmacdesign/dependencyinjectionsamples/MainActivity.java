package com.pgmacdesign.dependencyinjectionsamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_butter_knife, button_rx_java;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI(){
        this.button_butter_knife = (Button) this.findViewById(R.id.button_butter_knife);
        this.button_rx_java = (Button) this.findViewById(R.id.button_rx_java);

        this.button_rx_java.setOnClickListener(this);
        this.button_butter_knife.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.button_butter_knife:
                intent = new Intent(this, ButterKnifeActivity.class);
                startActivity(intent);
                break;

            case R.id.button_rx_java:
                intent = new Intent(this, RXJavaActivity.class);
                startActivity(intent);
                break;

        }
    }
}
