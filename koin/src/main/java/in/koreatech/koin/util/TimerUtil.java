package in.koreatech.koin.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;


import java.util.Timer;
import java.util.TimerTask;

import in.koreatech.koin.R;

public abstract class TimerUtil {
    private final String TAG = "TimerUtil";
    protected TimerListener timerListener;
    public static final int SEND_CODE = 0;
    public static final int END_CODE = 1;
    protected Handler handler;
    protected Timer timer;
    protected TextView textview;
    protected int endTime;
    protected TimerTask timerTask;
    protected String strTime;

    public TimerUtil() {
        handler = new Handler();
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setTextView(TextView textView) {
        this.textview = textView;
    }

    public void setTimerListener(TimerListener timerListener) {
        this.timerListener = timerListener;
    }

    public void startTimer() {
        stopTimer();
        timer = new Timer();
        handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                {
                    if (msg.what == END_CODE) {
                        endTimer();
                    }
                    else {
                        /*if (textview != null) textview.setText(R.string.bus_no_information);
                        if (timerListener != null) timerListener.onTimerUpdated("운행정보없음");
                    } else {*/
                        if (textview != null) textview.setText(strTime);
                        if (timerListener != null) timerListener.onTimerUpdated(strTime);
                    }
                }
            }
        };
        timerTask = new TimerTask() {
            public void run() {
                try {
                    endTime--;
                    int hour = endTime / 3600;
                    int min = (endTime / 60) % 60;
                    int sec = endTime % 60;
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

                    if (handler == null) return;
                    if (endTime > 0) {
                        handler.obtainMessage(SEND_CODE).sendToTarget();
                    } else {
                        handler.obtainMessage(END_CODE).sendToTarget();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    public String getStrTime() {
        return strTime;
    }

    public abstract void endTimer();

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timerTask.cancel();
            if (handler != null)
                handler = null;
        }

    }
}
