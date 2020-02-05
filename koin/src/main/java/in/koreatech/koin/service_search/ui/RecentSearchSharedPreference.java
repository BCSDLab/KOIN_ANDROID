package in.koreatech.koin.service_search.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import in.koreatech.koin.core.helpers.BaseSharedPreferencesHelper;

public class RecentSearchSharedPreference extends BaseSharedPreferencesHelper {
    public static final String TAG = RecentSearchSharedPreference.class.getName();
    private Context context;
    private final String KOIN_RECENT_SEARCH_SHARED_PREFERECE = "in.koin.sharedpreferences.search.recent";
    public static final String KEY_RECENT_SAVE = ".save";
    private static volatile RecentSearchSharedPreference instance = null;


    public static RecentSearchSharedPreference getInstance() {
        if (instance == null) {
            synchronized (RecentSearchSharedPreference.class) {
                instance = new RecentSearchSharedPreference();
            }
        }
        return instance;
    }

    public void init(Context context) {
        super.init(context, context.getSharedPreferences(KOIN_RECENT_SEARCH_SHARED_PREFERECE, Context.MODE_PRIVATE));
    }

    public void saveRecentSearch(ArrayList<String> recentSearch) {
        if (recentSearch == null) return;
        Log.e(TAG, "saveRecentSearch: " + gsonHelper.ArrayListToJSONString(recentSearch));
        putString(KEY_RECENT_SAVE, gsonHelper.ArrayListToJSONString(recentSearch));

    }

    public ArrayList<String> getRecentSearch() {
        String recentString = sharedPreferences.getString(KEY_RECENT_SAVE, "");
        String[] recentSearchs = gsonHelper.jsonToObject(recentString, String[].class);
        Log.d(TAG, "recentSearch: " + recentString);
        if (recentSearchs == null) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(recentSearchs));
    }
}
