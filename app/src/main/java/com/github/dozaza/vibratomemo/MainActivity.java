package com.github.dozaza.vibratomemo;

import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private RelativeLayout mainLayout;

    private RelativeLayout touchMeLayout;

    private RelativeLayout photoLayout;

    private TextView countDownTextView;

    private int imageId = 1;

    private int imageRestSecond = 10;

    private Handler handler = new CountDownTextViewHandler();

    RelativeLayout.OnTouchListener OnMainLayoutTouchListener = new RelativeLayout.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                hidePhoto();
                showTouchMe();
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                showPhoto();
                hideTouchMe();

                if (imageRestSecond == 10) {
                    // 开启一个线程修改倒计时, 并且在满10秒后切换图片
                    new Thread(new CountDownChangeThread()).start();
                }
            }

            return true;
        }
    };

    // 更改倒计时
    private class CountDownChangeThread implements Runnable {
        @Override
        public void run() {
            while(imageRestSecond > 0) {
                try {
                    Thread.sleep(1000);
                    imageRestSecond -= 1;
                    Message message = new Message();
                    message.what = imageRestSecond;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class CountDownTextViewHandler extends Handler {
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 0:
                    // 表示满了10秒，然后切换图片
                    hidePhoto();
                    showTouchMe();
                    imageId = getNextImageId(imageId);
                    imageRestSecond = 10;
                    break;
                default:
                    break;
            }
            countDownTextView.setText(imageRestSecond + "");

            super.handleMessage(msg);
        }
    }

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

        mainLayout = findViewById(R.id.main_layout);
        touchMeLayout = findViewById(R.id.touch_me);
        photoLayout = findViewById(R.id.photo);
        countDownTextView = findViewById(R.id.count_down);

        mainLayout.setOnTouchListener(OnMainLayoutTouchListener);
    }

}
