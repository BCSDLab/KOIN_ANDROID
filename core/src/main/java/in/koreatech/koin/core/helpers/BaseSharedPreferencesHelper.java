package in.koreatech.koin.core.helpers;

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

    public void putString(String _key, String _value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(_key, _value);
        e.apply();
    }

    public String getString(String _key, String _value) {
        if (sharedPreferences == null) {
            return null;
        }
        return sharedPreferences.getString(_key, _value);
    }

    public void putBoolean(String _key, boolean _value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putBoolean(_key, _value);
        e.apply();
    }

    public boolean getBoolean(String _key, boolean _value) {
        return sharedPreferences != null && sharedPreferences.getBoolean(_key, _value);
    }

    public void putInt(String _key, int _value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putInt(_key, _value);
        e.apply();
    }

    public int getInt(String _key, int _value) {
        if (sharedPreferences == null) {
            return -1;
        }
        return sharedPreferences.getInt(_key, _value);
    }

    public void putLong(String _key, long _value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putLong(_key, _value);
        e.apply();
    }

    public long getLong(String _key, long _value) {
        if (sharedPreferences == null) {
            return -1;
        }
        return sharedPreferences.getLong(_key, _value);
    }

    public void remove(String _key) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.remove(_key);
        e.apply();
    }

    public SharedPreferences.Editor editor() {
        return sharedPreferences.edit();
    }

    public void putStringArray(String _key, List<String> _value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(_key, gsonHelper.objectToJSON(_value).toString());

        e.commit();
    }

    public List<String> getStringArray(String _key, String _value) {
        if (sharedPreferences == null) {
            return null;
        }

        String arrayToString = sharedPreferences.getString(_key, _value);
        ArrayList<String> arrayList = gsonHelper.jsonToObject(arrayToString, ArrayList.class);

        return arrayList;
    }

}
