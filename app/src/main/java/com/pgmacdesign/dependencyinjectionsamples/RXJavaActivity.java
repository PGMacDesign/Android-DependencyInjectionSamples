package com.pgmacdesign.dependencyinjectionsamples;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * come back to for more info if needed:
 * https://github.com/kaushikgopal/RxJava-Android-Samples/blob/master/README.md
 * Realm Android Rx Tutorial -- (Rx Java) -- https://www.youtube.com/watch?v=XLH2v9deew0
 */
public class RXJavaActivity extends AppCompatActivity implements View.OnClickListener {

    /*
    NOTES:

        More research: (todo)
            RxMarbles: (http://rxmarbles.com/#delay)
            RxDocumentation: (https://github.com/ReactiveX/RxJava/wiki/How-To-Use-RxJava)
            Dan Lew's Blog post series (http://blog.danlew.net/2014/09/15/grokking-rxjava-part-1/)
            Vogella (http://www.vogella.com/tutorials/RxJava/article.html) 
            @RunChristinaRun (Christina Lee, She ran the RXJava talk on Realm)
        RX == Observables (Represent asynchronous data streams)
            + link queries (Allow to compose streams with operators)
            + schedulers (Allow to manage concurencies within streams)

        Observables:
            Streams of data
            Pull based (Except subjects)
            Create, store, pass around <-- important
            Abstract away threading, synchronization, concurrency, etc
            2 Phases:
                1) Put data in
                    Sample: Observable.just("some text");
                    Sample: Observable.just(new String[]{"Pat", "Laura", "Liam"});
                    Longer Sample with operators:
                        Observable.create(new OnSubscribe<String>(){
                            @Override
                            public void call(Subscribe<? super String> subscriber) {
                                subscriber.onNext("some text here");
                                subscriber.onCompleted();
                            }
                        }
                2) Get data out
            Observable Operators
                .OnNext
                .onComplete
                    This will end a stream (Stuff went as expected)
                    Safe for cleanup (Recycle objects)
                .onError
                    This will end a stream (Something went wrong)
                    NOTE! If this gets hit, this takes the place of onComplete so that will not get hit!

        Operators (At a higher level)
            -------> See RXMarbles for more operator samples <------
            TONS of operators, too many to list here.
            -Filter -- .filter() works if the filter procs (IE, .filter(4%2 = 0) <-- would proc)
            -merge
            -doOnNext -- do this when on Next triggers

        Link Queries:
            todo research

        Subscribe:
            Subscribers can take various #s of functions or none. (0 - 3). Make sure to always
            pass an error one though
            Use subscribeOn and observeOn here
            Can subscribe and unsubscribe too (when done / no longer needed)
            Thread management:
                subscribeOn()
                    Declare it ONLY ONCE
                    subscribeOn(Schedulers.io()) <--- This would be io thread, not Main
                    subscribeOn(Schedulers.Main()) <--- This would be io thread, not Main
                    It will default to the thread in which the observable itself was created (Main /
                    UI is normal, but could be computational too)
                    Observable will kick off execution on this thread no matter where declared
                observeOn()
                    Declare it as many times as needed
                    It affects all operators downstream
                        Any new observeOn call will change all stuff below it unless new observeOn is made

        Usages of RXJava in Android
            1) Bind to button clicks
            2) Cache hits on network calls
            3) Handle auth flow via a single stream

        Factory Analogy:
            Raw Material == Creation
            Converyor Belts == Operators / Transforms
            End Product == Output

        Schedulers:
            Scheduler mainThread = AndroidSchedulers.mainThread();
            Scheduler computation = Schedulers.computation();
            Scheduler io = Schedulers.io();

        //Local broadcast receiver (Not RX, but useful nonetheless)
            LocalBroadcastManager localBM = LocalBroadcastManager.getInstance(RXJavaActivity.this);

        intent,getStringExtra(smsUtility.KEY_VERIF_CODE) <-- to compare
        ContentObservable.fromLocalBroadcase(newLocalBroadcast())

        Timed Auth observable
            .observeOn(Schedulers.io())
            .doStuff
            .flatMap
            .observeOn(MainThtread)
            .subsribeOn(updateUI)


     */
    private TextView textView;
    private Button button1, button2, button3;
    private EditText edittext1, edittext2;

    private DisposableObserver dpo;

    private CompositeDisposable myDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        textView = (TextView) this.findViewById(R.id.textView);
        this.edittext1 = (EditText) this.findViewById(R.id.edittext1);
        this.edittext2 = (EditText) this.findViewById(R.id.edittext2);
        this.button1 = (Button) this.findViewById(R.id.button1);
        this.button2 = (Button) this.findViewById(R.id.button2);
        this.button3 = (Button) this.findViewById(R.id.button3);
        this.button1.setOnClickListener(this);
        this.button2.setOnClickListener(this);
        this.button3.setOnClickListener(this);

        myDisposable = new CompositeDisposable();

        //test1(this);
    }

    private static final String[] names = {"Pat", "Laura", "Arwen", "Caspian", "Liam"};

    private void test3(){



        //checkProfanity();
    }

    private void test2(){
        dpo = new DisposableObserver() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

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

                .subscribeOn(Schedulers.io())//OR: Schedulers: .computation(), .newThread()., .io()
                //.debounce()
                //.concatWith()
                //.zipWith()
                //.subscribeOn(AndroidSchedulers.mainThread())
                .map(func)
                .retry(4)
                .doOnNext(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        // TODO: 2017-01-04 Cannot interact with main thread here
                        // TODO: this is called when onNext is called in the observable
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        // TODO: 2017-01-04 Cannot interact with main thread here
                        // TODO: this is called when onComplete is called in the observable.
                    }
                })
                .doOnEach(new Consumer<Notification<Object>>() {
                    @Override
                    public void accept(Notification<Object> objectNotification) throws Exception {
                        try {
                            Log.d("consumer notification", objectNotification.getValue().toString());
                        } catch (NullPointerException npe){}
                    }
                })
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
                .observeOn(AndroidSchedulers.mainThread())
                //.subscribeWith(observer1);
                .subscribe(dpo);
    }
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
                // TODO: 2017-01-04 Cannot call UI elements here
                Log.d("disposableObserver", "onComplete hit");
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
                textView.setText("complete");
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

                .subscribeOn(Schedulers.io())//OR: Schedulers: .computation(), .newThread()., .io()
                //.debounce()
                //.concatWith()
                //.zipWith()
                //.subscribeOn(AndroidSchedulers.mainThread())
                .map(func)
                .retry(4)
                .doOnNext(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        // TODO: 2017-01-04 Cannot interact with main thread here
                        // TODO: this is called when onNext is called in the observable
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        // TODO: 2017-01-04 Cannot interact with main thread here
                        // TODO: this is called when onComplete is called in the observable.
                    }
                })
                .doOnEach(new Consumer<Notification<Object>>() {
                    @Override
                    public void accept(Notification<Object> objectNotification) throws Exception {
                        try {
                            Log.d("consumer notification", objectNotification.getValue().toString());
                        } catch (NullPointerException npe){}
                    }
                })
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
                .observeOn(AndroidSchedulers.mainThread())
                //.subscribeWith(observer1);
                .subscribe(observer1);


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
    private void zipWithSampleCode(){
        /*
        BehaviorSubject<String> foo = BehaviorSubject.create();
            foo.subscribeOn(Subscriber.io()).observeOn(mainThread())
            .subscribe(string -> print(string));

            return  identifySelf()
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<self, config>(){
                        ...call(){
                        foo.onNext("identified self");
                        return getConfigFromNetwork();
                        }
                        })
         */
    }

    //From: http://blog.feedpresso.com/2016/01/25/why-you-should-use-rxjava-in-android-a-short-introduction-to-rxjava.html
    private void complicatedChainCall(){
        /*
        Observable.fromCallable(createNewUser())
            .subscribeOn(Schedulers.io())
            .flatMap(new Func1<User, Observable<Pair<Settings, List<Message>>>>() {
                @Override
                public Observable<Pair<Settings, List<Message>>> call(User user) {
                    return Observable.zip(
                            Observable.from(fetchUserSettings(user)),
                            Observable.from(fetchUserMessages(user))
                            , new Func2<Settings, List<Message>, Pair<Settings, List<Message>>>() {
                                @Override
                                public Pair<Settings, List<Message>> call(Settings settings, List<Message> messages) {
                                    return Pair.create(settings, messages);
                                }
                            });
                }
            })
            .doOnNext(new Action1<Pair<Settings, List<Message>>>() {
                @Override
                public void call(Pair<Settings, List<Message>> pair) {
                    System.out.println("Received settings" + pair.first);
                }
            })
            .flatMap(new Func1<Pair<Settings, List<Message>>, Observable<Message>>() {
                @Override
                public Observable<Message> call(Pair<Settings, List<Message>> settingsListPair) {
                    return Observable.from(settingsListPair.second);
                }
            })
            .subscribe(new Action1<Message>() {
                @Override
                public void call(Message message) {
                    System.out.println("New message " + message);
                }
            });

        */
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dpo.dispose();
        myDisposable.dispose();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                test3();
                break;

            case R.id.button2:

                break;

            case R.id.button3:

                break;

        }
    }
}
