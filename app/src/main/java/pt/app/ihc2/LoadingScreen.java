package pt.app.ihc2;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class LoadingScreen extends Activity {

    private ProgressBar pb;
    //int counter = 0;
    private ObjectAnimator pboa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        init();

        Handler hander = new Handler();
        hander.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoadingScreen.this, Guidelines.class));
                finish();
            }
        }, 5000);
    }

    @SuppressLint("ObjectAnimatorBinding")
    public void init() {
        pb = findViewById(R.id.pbid);
        pboa = ObjectAnimator.ofInt(pb, "progressbar", 0, 100);
    }

//    public void prog() {
//        pb = (ProgressBar) findViewById(R.id.pbid);
//
//        final Timer t = new Timer();
//        TimerTask tt = new TimerTask() {
//            @Override
//            public void run() {
//                counter++;
//                pb.setProgress(counter);
//
//                if (counter == 100) {
//                    t.cancel();
//                }
//            }
//        };
//
//        t.schedule(tt, 0, 10);
//
//    }
}