package in.koreatech.koin.core.util;

import in.koreatech.koin.core.R;
import in.koreatech.koin.core.helpers.TimerRenewListener;

public class BusTimerUtil extends TimerUtil {

    private TimerRenewListener mTimerRenewListener;
    private int mTimerCode;

    public BusTimerUtil(int code) {
        this.mTimerCode = code;
    }

    @Override
    public void endTimer() {
        stopTimer();
        if (mEndTime <= 0)
            mTimerRenewListener.refreshTimer(mTimerCode);
    }


    public void setTimerRenewListener(TimerRenewListener timerRenewListener) {
        this.mTimerRenewListener = timerRenewListener;
    }


}
