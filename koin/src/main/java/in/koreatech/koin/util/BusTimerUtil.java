package in.koreatech.koin.util;

import in.koreatech.koin.core.helper.TimerRenewListener;

public class BusTimerUtil extends TimerUtil {

    private TimerRenewListener timerRenewListener;
    private int timerCode;

    public BusTimerUtil(int code) {
        this.timerCode = code;
    }

    @Override
    public void endTimer() {
        stopTimer();
        if (endTime <= 0)
            timerRenewListener.refreshTimer(timerCode);
    }


    public void setTimerRenewListener(TimerRenewListener timerRenewListener) {
        this.timerRenewListener = timerRenewListener;
    }


}
