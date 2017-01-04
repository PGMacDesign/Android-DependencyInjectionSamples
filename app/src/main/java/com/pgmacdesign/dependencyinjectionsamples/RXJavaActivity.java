package com.pgmacdesign.dependencyinjectionsamples;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RXJavaActivity extends AppCompatActivity {

    private TextView textView;
    private DisposableObserver dpo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        textView = (TextView) this.findViewById(R.id.textView);
        test1(this);
    }

    private static final String[] names = {"Pat", "Laura", "Arwen", "Caspian", "Liam"};

    /**
     * Notes:
     * 1) Always override the onError for observables b/c if it triggers and has not been
     * overridden, it will throw an exception and blow up the app/ script.
     * 2) Certain operators have different schedulers. If you do not specify the scheduler
     * you want to use, it will pick one on its own and it may not work exactly the way you
     * want it to.
     *
     */
    //From: https://www.youtube.com/watch?v=k3D0cWyNno4
    private void test1(final Context context){

        dpo = new DisposableObserver() {
            @Override
            public void onNext(Object o) {
                Log.d("disposableObserver", "onNext hit");
                if(o == null){
                    return;
                }
                Log.d("disposableObserver:", "onNext Start " + o.toString());

                try {
                    java.net.URL myUrl;
                    String url = "http://www.purgomalum.com/service/containsprofanity?text=" + o.toString();
                    myUrl = new URL(url);

                    HttpURLConnection urlConnection = (HttpURLConnection) myUrl.openConnection();
                    try {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        ByteArrayOutputStream result = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = in.read(buffer)) != -1) {
                            result.write(buffer, 0, length);
                        }
                        String str = result.toString();
                        Log.d("RESULT of " + o.toString(), str);
                    } finally {
                        urlConnection.disconnect();
                    }

                } catch (MalformedURLException mle){
                } catch (IOException ioe){
                }
                Log.d("disposableObserver:", "onNext Finish");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("disposableObserver", "onError hit");
                e.printStackTrace();

            }

            @Override
            public void onComplete() {
                Log.d("disposableObserver", "onComplete hit");
                textView.setText("FINISHED");
            }
        };

        Observer observer1 = new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d("observer1", "onSubscribe hit");
            }

            @Override
            public void onNext(Object o) {
                if(o == null){
                    return;
                }
                Log.d("observer1:", "onNext Start " + o.toString());
                Log.d("observer1:", "onNext Finish");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("observer1", "onError hit");
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d("observer1", "onComplete hit");
            }
        };

        Function<String, Object> func = new Function<String, Object>() {
            @Override
            public Object apply(String s) throws Exception {
                // TODO: 2017-01-04 THIS IS WHERE BACKGROUND THREAD STUFF GOES!
                Log.d("in func:", "s = " + s);
                String str = tryAddingNetwork(s);
                if(str == null){
                    return s;
                } else {
                    return str;
                }
            }
        };

        //Checking for curse words via purgomalum.net
        Observable.just("one", "two", "shit", "four", "five")
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(func)
                /*
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        Log.d("filter hit", "string s = " + s);
                        if(s == null){
                            return false;
                        } else {
                            return true;
                        }
                    }
                })
                */
                .subscribe(observer1);
        //ddddddddddddddddddddddd
    }

    private String tryAddingNetwork(Object o){
        try {
            java.net.URL myUrl;
            String url = "http://www.purgomalum.com/service/containsprofanity?text=" + o.toString();
            myUrl = new URL(url);

            HttpURLConnection urlConnection = (HttpURLConnection) myUrl.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                String str = result.toString();
                Log.d("RESULT of " + o.toString(), str);
                return str;
            } finally {
                urlConnection.disconnect();
            }

        } catch (MalformedURLException mle){
        } catch (IOException ioe){
        }

        return null;
    }
    private void emailChangeListener(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dpo.dispose();
    }
}
