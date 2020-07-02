package in.koreatech.koin.util.timer;

import android.widget.TextView;

import java.util.HashMap;

public class TimerManger {
    private HashMap<String, CountTimer> timerTaskHashMap;

    public TimerManger() {
        timerTaskHashMap = new HashMap<>();
    }

    public void addTimer(String name, long millisInFuture, CountTimer.OnTimerListener onTimerListener) {
        stopTimer(name);
        CountTimer countTimer = new CountTimer(name, millisInFuture, 1000);
        countTimer.setTimerListener(onTimerListener);
        timerTaskHashMap.put(name, countTimer);
    }

    public void addTimer(String name, long millisInFuture, TextView textView, TimerText timerText, CountTimer.OnTimerListener onTimerListener) {
        stopTimer(name);
        CountTimer countTimer = new CountTimer(name, millisInFuture, 1000);
        countTimer.setTimerListener(
                (timerName, millisUntilFinished) -> {
                    textView.post(() -> textView.setText(timerText.format(millisUntilFinished)));
                    onTimerListener.onCountEvent(timerName, millisUntilFinished);
                });
        timerTaskHashMap.put(name, countTimer);
    }

    public void startTimer(String name) {
        if (timerTaskHashMap.containsKey(name)) {
            timerTaskHashMap.get(name).start();
        }
    }


    public void stopTimer(String name) {
        if (timerTaskHashMap.containsKey(name)) {
            timerTaskHashMap.get(name).cancel();
            timerTaskHashMap.remove(name);
        }
    }

    public void stopAllTimer() {
        for (String key : timerTaskHashMap.keySet()) {
            timerTaskHashMap.get(key).cancel();
        }
        timerTaskHashMap.clear();
    }

    @FunctionalInterface
    public interface TimerText {
        String format(long milliSeconds);
    }
}
