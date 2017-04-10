package lab.zeroarst.android.customviewwithlayout;

import android.app.Application;
import android.content.Context;

/**
 * Override to do initial things.
 */
public class AppController extends Application {

    public static final String TAG = "AppController";

    private static Context mCtx;

    public static Context getContext() {
        return mCtx;
    }

    private static AppController mInstance;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mCtx = getApplicationContext();
    }
}
