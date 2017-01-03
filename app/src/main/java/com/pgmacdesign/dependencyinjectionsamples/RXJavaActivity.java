package com.pgmacdesign.dependencyinjectionsamples;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import rx.android.schedulers.AndroidSchedulers;

public class RXJavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);

        test1();
    }

    private static final String[] names = {"Pat", "Laura", "Arwen", "Caspian", "Liam"};

    /**
     * Notes:
     * 1) Always override the onError for observables b/c if it triggers and has not been
     * overridden, it will throw an exception and blow up the app/ script.
     *
     */
    //From: https://www.youtube.com/watch?v=k3D0cWyNno4
    private void test1(){
        //Use the from and just commands to,
        //"convert objects, lists, or arrays of objects into Observables that emit those objects:"
        //From an array
        Observable<String> o1 = Observable.fromArray(names);
        //From JUST one item
        Observable<String> o2 = Observable.just("Patrick");

        //Create a Schedule
        Looper looper = Looper.myLooper(); //May be wrong. look into
        rx.Scheduler mainThreadSchedule = AndroidSchedulers.mainThread();
        rx.Scheduler sss = AndroidSchedulers.from(looper);


        o1.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                Log.d("subscribe hit", "at 32");
            }
        });
        o1.subscribe();
    }
}
