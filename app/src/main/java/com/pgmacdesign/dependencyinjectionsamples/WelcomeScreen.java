package com.pgmacdesign.dependencyinjectionsamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.redbooth.WelcomeCoordinatorLayout;

/**
 * Created by pmacdowell on 2017-04-03.
 */

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen_tests);

        final WelcomeCoordinatorLayout coordinatorLayout
                = (WelcomeCoordinatorLayout)findViewById(R.id.welcome_screen_coordinator_layout);
        coordinatorLayout.addPage(R.layout.welcome_screen_layout_1, R.layout.welcome_screen_layout_2);
    }
}
