package in.koreatech.koin.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import in.koreatech.koin.core.R;


import java.util.Timer;
import java.util.TimerTask;

public abstract class TimerUtil {
    private final String TAG = "TimerUtil";
    public static final int SEND_CODE = 0;
    public static final int END_CODE = 1;
    protected Handler mHandler;
    protected Timer mTimer;
    protected TextView mTextview;
    protected int mEndTime;
    protected TimerTask mTimerTask;
    protected String strTime;

    public TimerUtil() {
        mHandler = new Handler();

    }

    public void setEndTime(int endTime) {
        this.mEndTime = endTime;
    }

    public void setTextView(TextView textView) {
        this.mTextview = textView;
    }

    public void startTimer() {
        stopTimer();
        mTimer = new Timer();
        mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                {
                    if (msg.what == END_CODE) {
                        endTimer();
                    }
                    if (mTextview != null) {
                        if (msg.what == END_CODE)
                            mTextview.setText(R.string.bus_no_information);
                        else
                            mTextview.setText(strTime);
                    }
                }
            }
        };
        mTimerTask = new TimerTask() {
            public void run() {
                try {
                    mEndTime--;
                    int hour = mEndTime / 3600;
                    int min = (mEndTime / 60) % 60;
                    int sec = mEndTime % 60;
                    if (hour > 0)
                        strTime = String.format("%s시간 %s분 %s초 남음", hour, min, sec);
                    else {
                        if (min > 1)
                            strTime = String.format("%s분 %s초 남음", min, sec);
                        else {
                            strTime = String.format("약 %s분 남음", min);
                            if (min == 0) strTime = String.format("곧 도착 예정");
                        }
                    }

                    if (mHandler == null) return;
                    if (mEndTime > 0) {
                        mHandler.obtainMessage(SEND_CODE).sendToTarget();
                    } else {
                        mHandler.obtainMessage(END_CODE).sendToTarget();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }


    public abstract void endTimer();

    public void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimerTask.cancel();
            if (mHandler != null)
                mHandler = null;
        }

    }
}
