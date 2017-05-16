package com.pgmacdesign.dependencyinjectionsamples;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_butter_knife, button_rx_java, button_dagger_2, button_custom_tabs,
            button_dart, welcome_screen;
    private PendingIntent testPendingIntent;

    private static final String DAGGER_TUTORIAL_URL = "https://github.com/google/dagger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, MainActivity.class);
        testPendingIntent = PendingIntent.getActivity(
                this, 222, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        initUI();
    }

    private void initUI(){
        this.button_butter_knife = (Button) this.findViewById(R.id.button_butter_knife);
        this.button_rx_java = (Button) this.findViewById(R.id.button_rx_java);
        this.button_dagger_2 = (Button) this.findViewById(R.id.button_dagger_2);
        this.button_custom_tabs = (Button) this.findViewById(R.id.button_custom_tabs);
        this.button_dart = (Button) this.findViewById(R.id.button_dart);
        this.welcome_screen = (Button) this.findViewById(R.id.welcome_screen);

        this.button_dart.setOnClickListener(this);
        this.button_custom_tabs.setOnClickListener(this);
        this.button_dagger_2.setOnClickListener(this);
        this.button_rx_java.setOnClickListener(this);
        this.button_butter_knife.setOnClickListener(this);
        this.welcome_screen.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){

            case R.id.button_custom_tabs:

                try {
                    Bitmap bitmap = BitmapFactory.decodeResource(
                            this.getResources(), R.drawable.pause_icon);
                    Bitmap bitmap2 = BitmapFactory.decodeResource(
                            this.getResources(), R.drawable.square_icon);
                    CustomTabsIntent.Builder myBuilder = new CustomTabsIntent.Builder();
                    myBuilder.addMenuItem("Custom Button Here", testPendingIntent);
                    myBuilder.setActionButton(bitmap, "myButton", testPendingIntent, true);
                    myBuilder.setCloseButtonIcon(bitmap2);
                    myBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.Red));
                    CustomTabsIntent customTabsIntent = myBuilder.build();
                    customTabsIntent.launchUrl(this, Uri.parse(DAGGER_TUTORIAL_URL));
                } catch (ActivityNotFoundException anfe){
                    //If this is hit, it means they have disabled Chrome
                    Toast.makeText(this,
                            "You have disabled Chrome, you must enable it to use this feature",
                            Toast.LENGTH_LONG).show();

                    //Use a backup web intent here if you need to
                }
                break;

            case R.id.button_butter_knife:
                intent = new Intent(this, ButterKnifeActivity.class);
                startActivity(intent);
                break;

            case R.id.button_rx_java:
                intent = new Intent(this, RXJavaActivity.class);
                startActivity(intent);
                break;

            case R.id.button_dagger_2:
                intent = new Intent(this, DaggerActivity.class);
                startActivity(intent);
                break;

            case R.id.button_dart:
                intent = new Intent(this, DartActivity.class);
                startActivity(intent);
                break;

            case R.id.welcome_screen:
                intent = new Intent(this, WelcomeScreen.class);
                startActivity(intent);
                break;

        }
    }
}
