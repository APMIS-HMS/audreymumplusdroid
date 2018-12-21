package ng.apmis.audreymumplus;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */

public class AudreyMumplus extends Application {

    private static final Object LOCK = new Object();
    private static AudreyMumplus sInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private final static String TAG = "AUDREY";

    public AudreyMumplus () {
        diskIO = null;
        mainThread = null;
        networkIO = null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mCtx = getApplicationContext();
    }

    private AudreyMumplus(Executor diskIO, Executor networkIO, Executor mainThread, Context context) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static AudreyMumplus getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AudreyMumplus(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor(), mCtx);
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor networkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
