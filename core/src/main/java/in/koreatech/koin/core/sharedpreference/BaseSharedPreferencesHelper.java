package in.koreatech.koin.core.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSharedPreferencesHelper {

    protected SharedPreferences sharedPreferences;
    protected final GsonHelper gsonHelper;
    protected Context context;

    public BaseSharedPreferencesHelper() {
        gsonHelper = new GsonHelper();
        sharedPreferences = null;
    }

    public void init(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    public void clear() {
        SharedPreferences.Editor editor = editor();
        editor.clear();
        editor.commit();
    }

    public void putString(String key, String value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(key, value);
        e.apply();
    }

    public String getString(String key, String value) {
        if (sharedPreferences == null) {
            return null;
        }
        return sharedPreferences.getString(key, value);
    }

    public void putBoolean(String key, boolean value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putBoolean(key, value);
        e.apply();
    }

    public boolean getBoolean(String key, boolean value) {
        return sharedPreferences != null && sharedPreferences.getBoolean(key, value);
    }

    public void putInt(String key, int value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putInt(key, value);
        e.apply();
    }

    public int getInt(String key, int value) {
        if (sharedPreferences == null) {
            return -1;
        }
        return sharedPreferences.getInt(key, value);
    }

    public void putLong(String key, long value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putLong(key, value);
        e.apply();
    }

    public long getLong(String key, long value) {
        if (sharedPreferences == null) {
            return -1;
        }
        return sharedPreferences.getLong(key, value);
    }

    public void remove(String key) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.remove(key);
        e.apply();
    }

    public SharedPreferences.Editor editor() {
        return sharedPreferences.edit();
    }

    public void putStringArray(String key, List<String> value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(key, gsonHelper.objectToJSON(value).toString());

        e.commit();
    }

    public List<String> getStringArray(String key, String value) {
        if (sharedPreferences == null) {
            return null;
        }

        String arrayToString = sharedPreferences.getString(key, value);
        ArrayList<String> arrayList = gsonHelper.jsonToObject(arrayToString, ArrayList.class);

        return arrayList;
    }

}
