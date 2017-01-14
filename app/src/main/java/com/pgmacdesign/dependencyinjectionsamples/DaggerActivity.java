package com.pgmacdesign.dependencyinjectionsamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

public class DaggerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger);


    }




    //Simple class for testing
    class SimplePojo {
        private String name;
        private int age;

        @Inject String str;
    }
}
