package com.husky.test;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.husky.library.tools.TimeUtils;

/**
 * @author husky
 */
public class CountDownUtils extends CountDownTimer {

    TextView textView;

    public CountDownUtils(long millisInFuture, long countDownInterval, TextView textView) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
    }

    @Override
    public void onTick(long l) {
        textView.setText(TimeUtils.getDate(l,"MM:ss:SSS"));
    }

    @Override
    public void onFinish() {
        textView.setText("done");
        this.cancel();
    }
}
