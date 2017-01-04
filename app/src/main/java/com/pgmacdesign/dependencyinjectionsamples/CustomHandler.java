package com.pgmacdesign.dependencyinjectionsamples;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

import java.util.concurrent.Semaphore;

/**
 * Referenced from: https://medium.com/@deividi/using-handlerscheduler-from-rxandroid-a09d35fc40a0#.25cmhz312
 * Created by pmacdowell on 2017-01-04.
 */

public class CustomHandler extends Handler {

    public CustomHandler(String name) {
        this(name, Process.THREAD_PRIORITY_BACKGROUND);
    }

    protected CustomHandler(String handlerName, int handlerPriority) {
        super(startHandlerThread(handlerName, handlerPriority));
    }

    private static Looper startHandlerThread(String name, int priority) {
        final Semaphore semaphore = new Semaphore(0);
        HandlerThread handlerThread = new HandlerThread(name, priority) {
            protected void onLooperPrepared() {
                semaphore.release();
            }
        };
        handlerThread.start();
        semaphore.acquireUninterruptibly();
        return handlerThread.getLooper();
    }

    public void quit() {
        getLooper().quit();
    }

}