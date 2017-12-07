package com.github.dozaza.vibratomemo;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {

    private RelativeLayout mainLayout;

    private RelativeLayout touchMeLayout;

    private RelativeLayout photoLayout;

    private int imageId = 1;

    private boolean firstTimeSeePhoto = true;

    RelativeLayout.OnTouchListener OnMainLayoutTouchListener = new RelativeLayout.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                hidePhoto();
                showTouchMe();
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (firstTimeSeePhoto) {
                    // 开启一个后台线程，用于计时10秒
                    mainLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 以下为图片在前台展示满10秒之后，才执行的代码
                            firstTimeSeePhoto = true;
                            hidePhoto();
                            showTouchMe();
                            imageId = getNextImageId(imageId);
                        }
                    }, 10 * 1000);
                }

                firstTimeSeePhoto = false;
                showPhoto();
                hideTouchMe();
            }

            return true;
        }
    };

    private void showPhoto() {
        if (imageId == 2) {
            Vibrator vb = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
            if (vb != null) {
                vb.vibrate(10 * 1000);
            }
        } else if (imageId == 4) {
            Vibrator vb = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
            if (vb != null) {
                long[] pattern = {500L, 500L};
                vb.vibrate(pattern, 0);
            }
        }
        photoLayout.setVisibility(View.VISIBLE);
        getSubPhotoLayout(imageId).setVisibility(View.VISIBLE);
    }

    private void hidePhoto() {
        photoLayout.setVisibility(View.INVISIBLE);
        getSubPhotoLayout(imageId).setVisibility(View.INVISIBLE);
        Vibrator vb = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        if (vb != null) {
            vb.cancel();
        }
    }

    private void showTouchMe() {
        touchMeLayout.setVisibility(View.VISIBLE);
    }

    private void hideTouchMe() {
        touchMeLayout.setVisibility(View.INVISIBLE);
    }

    private int getNextImageId(int imageId) {
        return 5 == imageId ? 1 : imageId + 1;
    }

    // 用于获取子图片的layout，来进行显示/隐藏
    private RelativeLayout getSubPhotoLayout(int imageId) {
        if (imageId == 1) {
            return (RelativeLayout) photoLayout.findViewById(R.id.photo01);
        } else if (imageId == 2) {
            return (RelativeLayout) photoLayout.findViewById(R.id.photo02);
        } else if (imageId == 3) {
            return (RelativeLayout) photoLayout.findViewById(R.id.photo03);
        } else if (imageId == 4) {
            return (RelativeLayout) photoLayout.findViewById(R.id.photo04);
        } else {
            return (RelativeLayout) photoLayout.findViewById(R.id.photo05);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        touchMeLayout = (RelativeLayout) findViewById(R.id.touch_me);
        photoLayout = (RelativeLayout) findViewById(R.id.photo);

        mainLayout.setOnTouchListener(OnMainLayoutTouchListener);
    }

}
