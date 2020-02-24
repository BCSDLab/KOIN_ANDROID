package in.koreatech.koin.data.sharedpreference;


import android.content.Context;
import android.util.Log;

import in.koreatech.koin.core.sharedpreference.BaseSharedPreferencesHelper;

public class TimeTableSharedPreferencesHelper extends BaseSharedPreferencesHelper {
    private final String TAG = "TimeTableSharedPreferencesHelper";
    private final String KOIN_TIMETABLE_SHARED_PREFERENCES = "in.koin.sharedpreferences.timetable";
    public static final String KEY_VERSION = ".version";
    public static final String KEY_LAST_LOGIN_TIMETABLE_PICTURE_NAME = ".path";
    public static final String SAVE_ANONYMOUS_USER_TIMETABLE = ".anonymous";
    public static final String SAVE_ANONYMOUS_LAST_BLOCK_ID = ".id";
    private static volatile TimeTableSharedPreferencesHelper instance = null;


    private Context context;

    public TimeTableSharedPreferencesHelper() {
        sharedPreferences = null;
    }

    public static TimeTableSharedPreferencesHelper getInstance() {
        if (instance == null) {
            synchronized (TimeTableSharedPreferencesHelper.class) {
                instance = new TimeTableSharedPreferencesHelper();
            }
        }
        return instance;
    }


    public void init(Context context) {
        super.init(context, context.getSharedPreferences(KOIN_TIMETABLE_SHARED_PREFERENCES, Context.MODE_PRIVATE));
    }


    public void saveTimeTableVersion(String version) {
        putString(KEY_VERSION, version);
    }


    public String loadTimeTableVersion() {
        Log.d(TAG, "loadTimeTableVersion: " + getString(KEY_VERSION, null));
        return getString(KEY_VERSION, null);
    }

    public void saveLastPathTimetableImage(String path) {
        putString(KEY_LAST_LOGIN_TIMETABLE_PICTURE_NAME, path);
    }

    public String loadLastPathTimetableImage() {
        return getString(KEY_LAST_LOGIN_TIMETABLE_PICTURE_NAME, null);
    }

    public void saveTimeTable(String semester, String jsonFile) {
        putString(semester, jsonFile);
    }

    public String loadSaveTimeTable(String semester) {
        return getString(semester, null);
    }

    public void saveTimeTableBlockID(int savedId) {
        putInt(SAVE_ANONYMOUS_LAST_BLOCK_ID, savedId);
    }

    public int loadSaveTimeTableBlockID() {
        return getInt(SAVE_ANONYMOUS_LAST_BLOCK_ID, -1);
    }
}

