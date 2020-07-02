package in.koreatech.koin.util.timer;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class CountTimer {
    public static final String TAG = "CountTimer";
    private String name;
    private OnTimerListener onTimerListener;
    private long millisInFuture;
    private long countDownInterval;
    private Timer timer;
    private Handler handler;
    private boolean isRunning;

    private CountTimer(long millisInFuture, long countDownInterval) {
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
        handler = new Handler(Looper.getMainLooper());
        timer = new Timer();
        isRunning = false;
    }

    public CountTimer(String name, long millisInFuture, long countDownInterval) {
        this(millisInFuture, countDownInterval);
        this.name = name;
    }

    public void start() {
        if (isRunning)
            timer.cancel();
        isRunning = true;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (millisInFuture == 0) {
                    timer.cancel();
                    isRunning = false;
                }
                handler.post(() -> {
                    if (onTimerListener != null)
                        onTimerListener.onCountEvent(name, millisInFuture);
                });
                millisInFuture--;
                Log.e(TAG, "run: " + millisInFuture);
            }
        }, 0, countDownInterval);
    }

    public void cancel() {
        timer.cancel();
        isRunning = false;
    }

    public void setTimerListener(OnTimerListener onTimerListener) {
        this.onTimerListener = onTimerListener;
    }

    public interface OnTimerListener {
        void onCountEvent(String name, long millisUntilFinished);
    }
}
