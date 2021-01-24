package in.koreatech.koin.util;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import in.koreatech.koin.BuildConfig;

public class FirebaseEventUtil {
    private static FirebaseEventUtil instance = null;
    public final String DATE_FORMAT = "%d일 %d시간 %d분 %d초";
    public final String STORE_CALL_ELAPSE_TIME = "store_call_elapse_time";
    public final String START = "start";
    public final String END = "end";
    public final String DURATION = "duration";
    public final String DURATION_FORMAT = "duration_format";

    private FirebaseAnalytics firebaseAnalytics;
    private HashMap<String, Date> storeCallTrackHashMap;

    private FirebaseEventUtil(@NonNull Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
    }

    public static FirebaseEventUtil getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (FirebaseEventUtil.class) {
                instance = new FirebaseEventUtil(context);
            }
        }
        return instance;
    }

    public void startTrackStoreCallTime() {
        if (storeCallTrackHashMap == null)
            storeCallTrackHashMap = new HashMap<>();

        storeCallTrackHashMap.put(START, getCurrentDate());

    }

    public void endTrackStoreCallTime() {
        if (storeCallTrackHashMap == null || !storeCallTrackHashMap.containsKey(START))
            return;

        Date startDate = storeCallTrackHashMap.get(START);
        Date endDate = getCurrentDate();
        long duration = endDate.getTime() - startDate.getTime();
        if(duration > 0) {
            Bundle bundle = new Bundle();
            bundle.putLong(START, startDate.getTime());
            bundle.putLong(END, endDate.getTime());
            bundle.putLong(DURATION, duration);
            bundle.putString(DURATION_FORMAT, longToDateString(duration));
            track(STORE_CALL_ELAPSE_TIME, bundle);
        }
        storeCallTrackHashMap = null;
    }

    private void track(String name, Bundle bundle) {
        if (!BuildConfig.IS_DEBUG)
            firebaseAnalytics.logEvent(name, bundle);
    }


    private Date getCurrentDate() {
        return new Date();
    }

    private String longToDateString(long different) {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return String.format(Locale.KOREA, DATE_FORMAT, elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
    }
}
