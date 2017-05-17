package com.pgmacdesign.dependencyinjectionsamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.pgmacdesign.dependencyinjectionsamples.networkclasses.ProfantiyCheckerInterface;
import com.pgmacdesign.dependencyinjectionsamples.networkclasses.RetrofitClient;
import com.pgmacdesign.dependencyinjectionsamples.otherutils.L;
import com.pgmacdesign.dependencyinjectionsamples.otherutils.StringUtilities;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * come back to for more info if needed:
 * https://github.com/kaushikgopal/RxJava-Android-Samples/blob/master/README.md
 * Realm Android Rx Tutorial -- (Rx Java) -- https://www.youtube.com/watch?v=XLH2v9deew0
 */
public class RXJavaActivity extends AppCompatActivity implements View.OnClickListener {

    /*
    NOTES:

        More research: (todo)
            Blogs:
            https://code.tutsplus.com/tutorials/getting-started-with-rxjava-20-for-android--cms-28345
            https://code.tutsplus.com/tutorials/reactive-programming-operators-in-rxjava-20--cms-28396
https://code.tutsplus.com/tutorials/rxjava-for-android-apps-introducing-rxbinding-and-rxlifecycle--cms-28565
            RxMarbles: (http://rxmarbles.com/#delay)
            RxDocumentation: (https://github.com/ReactiveX/RxJava/wiki/How-To-Use-RxJava)
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

        Flowable Vs Observable:
            At this point, you may be wondering: why would I ever use Observables when I can just
            use Flowables and not have to worry about backpressure? The answer is that a Flowable
            incurs more of an overhead than a regular Observable, so in the interests of creating
            a high-performing app, you should stick with Observables unless you suspect that your
            application is struggling with backpressure.

        Singles
            Singles are useful when you just need to emit one value. In these scenarios,
            creating an Observable can feel like overkill, but a Single is designed to simply
            emit a single value and then complete, either by calling:
                onSuccess(): The Single emits its sole value.
                onError(): If the Single is unable to emit its item, then it’ll pass this method
                           the resulting Throwable

        Operators (At a higher level)
            -------> See RXMarbles for more operator samples <------
            TONS of operators, too many to list here.
            -Filter -- .filter() works if the filter procs (IE, .filter(4%2 = 0) <-- would proc)
            -merge
            -doOnNext -- do this when on Next triggers
            .fromArray() <--- This will set a group on to the observable. See test3() for sample
            .just() <-- This will set a single object to the observable. See test3() for sample
            The sample() Operator checks the Observable’s output at intervals specified by you,
                and then takes the most recent item that was emitted during that sampling period.
                For example, if you include .sample(5, SECONDS) in your project then the Observer
                will receive the last value that was emitted during each five-second interval.
            The throttleFirst() Operator takes the first value that was emitted during the
            sampling period. For example, if you include .throttlefirst(5, SECONDS) then the
                Observer will receive the first value that’s emitted during each
                five-second interval.

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
                    subscribeOn(Schedulers.computation()) <--- This would be comp thread, not Main
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

        //todo research into .publish() operator (with regards to views)
        The publish() operator converts a standard Observable into a connectable observable.
        While a regular observable starts emitting items as soon as the first observer
        subscribes to it, a connectable observable won’t emit anything until you explicitly
        instruct it to, by applying the connect() operator. This gives you a window of opportunity
        in which to subscribe multiple observers, without the observable starting to emit items
        as soon as the first subscription takes place.
        Once you’ve created all your subscriptions, simply apply the connect() operator
        and the observable will start emitting data to all its assigned observers.

     */

    private static final String TAG = "RXJavaActivity";
    private TextView textView;
    private Button button1, button2, button3, button4;
    private EditText edittext1, edittext2;

    private DisposableObserver dpo;

    private CompositeDisposable myDisposable;

    String curseWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        this.curseWord = null;

        textView = (TextView) this.findViewById(R.id.textView);
        this.edittext1 = (EditText) this.findViewById(R.id.edittext1);
        this.edittext2 = (EditText) this.findViewById(R.id.edittext2);
        this.button1 = (Button) this.findViewById(R.id.button1);
        this.button2 = (Button) this.findViewById(R.id.button2);
        this.button3 = (Button) this.findViewById(R.id.button3);
        this.button4 = (Button) this.findViewById(R.id.button4);
        this.button1.setOnClickListener(this);
        //this.button2.setOnClickListener(this);
        //this.button3.setOnClickListener(this);
        this.button4.setOnClickListener(this);

        myDisposable = new CompositeDisposable();

        test4();
        test5();
    }

    private static final String[] names = {"Pat", "Laura", "Arwen", "Caspian", "Liam"};

    /**
     * Test for intesger emitters
     */
    private void test3(){


        Integer[] intArray = {5, 6, 7, 8};

        Observable<Integer> observable6 = Observable.empty(); //Empty, Just for testing purposes
        /*
        This operator creates an Observable that emits an infinite sequence of ascending
        integers, with each emission separated by a time interval chosen by you
         */
        Observable<Long> observable5 = Observable.interval(1, TimeUnit.SECONDS);
        Observable<Integer> observable4 = Observable.range(5, 50); //Range 5 -> 50
        Observable<Integer[]> observable3 = Observable.just(intArray);
        Observable<Integer> observable2 = Observable.fromArray(intArray);
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
               @Override

               public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                   //Use onNext to emit each item in the stream//
                   e.onNext(1);
                   e.onNext(2);
                   e.onNext(3);
                   e.onNext(4);

                   //Once the Observable has emitted all items in the sequence, call onComplete//
                   e.onComplete();
               }
           }
        );
        Observer<Integer> observerInt = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe: "
                        + ", Thread == " + Thread.currentThread().getName());
            }

            @Override
            public void onNext(Integer value) {
                Log.e(TAG, "onNext: " + value
                        + ", Thread == " + Thread.currentThread().getName());
                if(Thread.currentThread().getName().equalsIgnoreCase("main")){
                    L.toast(RXJavaActivity.this, "TEST");
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: "
                        + ", Thread == " + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: All Done"
                        + ", Thread == " + Thread.currentThread().getName());
            }
        };

        Observer<Long> observerLong = new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe: "
                        + Thread.currentThread().getName());
            }

            @Override
            public void onNext(Long value) {
                try {

                } catch (Exception e){
                    e.printStackTrace();
                }
                edittext1.setText("someText1"); //<-- Fails!
                Log.e(TAG, "onNext: " + value + ", current thread == "
                        + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: "
                        + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: All Done!"
                        + Thread.currentThread().getName());
            }
        };

        //Create our subscription//
        //observable4.subscribe(observerInt);
        observable2
                .subscribeOn(Schedulers.computation())
                .subscribe(observerInt);

    }

    /**
     * Showing how to use flowables
     */
    private void test2(){
        Flowable<String> flowable = Flowable.fromArray(new String[] {"south", "north", "west", "east"});
        flowable.subscribe();

        Flowable<Integer> flowable1 = Flowable.range(0, 20);
        flowable1.subscribe();
    }

    /**
     * Showing how to use a single
     */
    private void test1(){
        Single.just("Hello World")
                .subscribe(getSingleObserver());

        //To convert a single into an observable:
        //mergeWith();
        //mergeWith(): Merges multiple Singles into a single Observable.
        //concatWith(): Chains the items emitted by multiple Singles together, to form an Observable emission.
        //toObservable(): Converts a Single into an Observable that emits the item that was originally emitted by the Single, and then completes.
    }
    private SingleObserver<String> getSingleObserver() {
        return new SingleObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe");
            }

            @Override
            public void onSuccess(String value) {
                Log.e(TAG, " onSuccess : " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ");
            }

        };

    }

    /**
     * Showing how to bind buttons and UI to the RX lifecycle
     */
    private void test4(){



        RxView.clicks(this.button3)
                .debounce(200, TimeUnit.MILLISECONDS) //Gap of 1/5 second between clicks allowed
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        L.m("button 3 hit");
                    }
                });

        RxTextView.textChanges(this.edittext1)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(@NonNull CharSequence charSequence) throws Exception {
                        L.m("(TEXT CHANGED) char == " + charSequence.toString());
                        curseWord = charSequence.toString();
                        if(StringUtilities.isNullOrEmpty(curseWord)){
                            curseWord = "";
                        }
                    }
                });
    }

    private void test5(){
        String[] strs = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        /*
        RxPermissions rxPermissions = new RxPermissions(RXJavaActivity.this);
        String[] strs = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        rxPermissions.request(strs)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {
                        if(granted){
                            L.m("granted perm");
                            L.toast(RXJavaActivity.this, "Success");
                        } else {
                            L.m("did not grant perm");
                            L.toast(RXJavaActivity.this, "Fail");
                        }
                    }
                });
        */

        RxView.clicks(this.button2)
                .debounce(50, TimeUnit.MILLISECONDS) //Gap of 50 milliseconds
                .compose(new RxPermissions(this).ensure(strs))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(granted -> {
                    if(granted == null){
                        granted = false;
                    }
                    if(granted){
                        L.toast(RXJavaActivity.this, "SUCCESS");
                    } else {
                        L.toast(RXJavaActivity.this, "FAILURE");
                    }
                });



    }

    private void test7(){
        RetrofitClient.Builder builder = new RetrofitClient.Builder(
                ProfantiyCheckerInterface.class,
                ProfantiyCheckerInterface.BASE_URL);
        builder.setTimeouts(60000, 60000);
        builder.setLogLevel(HttpLoggingInterceptor.Level.BODY);
        builder.setCustomAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.callIsJSONFormat();
        ProfantiyCheckerInterface profantiyCheckerInterface = builder.build().buildServiceClient();
        Observable<ResponseBody> observable = profantiyCheckerInterface.checkProfanity2(curseWord);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseBody -> handleResponse(responseBody),
                        error -> handleError(error));
    }

    private void handleResponse(ResponseBody rb){

    }

    private void handleError(Throwable t){
        t.printStackTrace();
        
    }

    private void test6(){
        //Great tutorial - https://medium.com/3xplore/handling-api-calls-using-retrofit-2-and-rxjava-2-1871c891b6ae

        RetrofitClient.Builder builder = new RetrofitClient.Builder(
                ProfantiyCheckerInterface.class,
                ProfantiyCheckerInterface.BASE_URL);
        builder.setTimeouts(60000, 60000);
        builder.setLogLevel(HttpLoggingInterceptor.Level.BODY);
        builder.setCustomAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.callIsJSONFormat();
        ProfantiyCheckerInterface profantiyCheckerInterface = builder.build().buildServiceClient();
        Observable<ResponseBody> observable = profantiyCheckerInterface.checkProfanity2(curseWord);
        observable.subscribeOn(Schedulers.newThread())
                .map(rb -> {
                    if(rb != null){
                        L.m("within map, rb is not null");
                    } else {
                        L.m("within map, rb is null");
                    }
                    return rb;
                })
                .observeOn(AndroidSchedulers.mainThread()) //From here down, on main thread
                .subscribe(rb -> {
                    if(rb != null){
                        String str = rb.string();
                        if(!StringUtilities.isNullOrEmpty(str)){
                            L.m("Response from server was: " + str);
                            L.toast(RXJavaActivity.this, "Is '" + curseWord + "' a curse word? == \n" + str);
                        } else {
                            L.toast(RXJavaActivity.this, "response was not null, but string was???");
                        }
                    } else {
                        L.toast(RXJavaActivity.this, "response was null");
                    }

                });
        /*
        RxView.clicks(this.button4)
                .subscribe();
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
                //Not tied to anything on purpose, using rx keybinding instead
                break;

            case R.id.button3:
                //Not tied to anything on purpose, using rx keybinding instead
                break;

            case R.id.button4:
                test6();
                break;
        }
    }
}
