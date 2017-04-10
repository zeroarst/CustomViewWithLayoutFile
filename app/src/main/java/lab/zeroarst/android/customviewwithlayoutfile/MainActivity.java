package lab.zeroarst.android.customviewwithlayoutfile;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import lab.zeroarst.android.customviewwithlayout.R;

public class MainActivity extends AppCompatActivity {

    private LoaderLayout mLoaderLayout;

    private static final int MSG_JUST_WAIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoaderLayout = (LoaderLayout) findViewById(R.id.lo_loader);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Customize style.
        mLoaderLayout.setMessageTextColor(Color.WHITE);
        mLoaderLayout.showMessage("Click the button to launch loader");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLoaderLayout.showLoader(true, true);

                // wait for the loader to run for a while
                HandlerThread ht = new HandlerThread("just wait");
                ht.start();

                Handler handler = new Handler(ht.getLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case MSG_JUST_WAIT:
                                try {
                                    Thread.sleep(msg.arg1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLoaderLayout.hideLoader(true);
                                    }
                                });
                                break;
                        }
                    }
                };

                Message msg = new Message();
                msg.what = MSG_JUST_WAIT;
                msg.arg1 = 3000; // ms
                handler.sendMessage(msg);
            }
        });
    }
}
