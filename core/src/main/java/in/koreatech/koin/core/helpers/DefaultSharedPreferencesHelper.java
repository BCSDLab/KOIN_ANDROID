package in.koreatech.koin.core.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.koreatech.koin.core.constants.AuthorizeConstant;
import in.koreatech.koin.core.networks.entity.Auth;
import in.koreatech.koin.core.networks.entity.User;

/**
 * Created by hyerim on 2018. 5. 31....
 */
public class DefaultSharedPreferencesHelper extends BaseSharedPreferencesHelper implements SharedPreferencesHelper {
    private final String TAG = DefaultSharedPreferencesHelper.class.getSimpleName();
    private final String KOIN_SHARED_PREFERENCES = "in.koin.sharedpreferences";
    public static final String KEY_TOKEN = ".token";
    public static final String KEY_USER = ".user";
    public static final String KEY_USER_PW = ".userPw";
    public static final String KEY_CALLVAN_ROOM = ".callvan.room";
    public static final String KEY_LAST_LOGIN_DATE = ".date.login";

    private static volatile DefaultSharedPreferencesHelper instance = null;


    public static DefaultSharedPreferencesHelper getInstance() {
        if (instance == null) {
            synchronized (DefaultSharedPreferencesHelper.class) {
                instance = new DefaultSharedPreferencesHelper();
            }
        }
        return instance;
    }


    public void init(Context context) {
        super.init(context, context.getSharedPreferences(KOIN_SHARED_PREFERENCES, Context.MODE_PRIVATE));
    }

    @Override
    public void saveUser(User user) {
        SharedPreferences.Editor editor = editor();
        editor.putString(KEY_USER, gsonHelper.objectToJSON(user).toString());
        editor.apply();
    }

    @Override
    public User loadUser() {
        String userStr = sharedPreferences.getString(KEY_USER, "");
        User user = gsonHelper.jsonToObject(userStr, User.class);
        Log.d(TAG, "user: " + userStr);
        return user;
    }

    @Override
    public void saveUserPw(String userPw) {
        putString(KEY_USER_PW, userPw);
    }

    @Override
    public String loadUserPw() {
        return getString(KEY_USER_PW, null);
    }

    @Override
    public void saveToken(String token) {
        putString(KEY_TOKEN, token);
    }

    @Override
    public String loadToken() {
        return getString(KEY_TOKEN, null);
    }

    @Override
    public void saveCallvanRoomUid(int roomUid) {
        putInt(KEY_CALLVAN_ROOM, roomUid);
    }

    @Override
    public int loadCallvanRoomUid() {
        return getInt(KEY_CALLVAN_ROOM, 0);
    }

    @Override
    public void removeCallvanRoomUid() {
        remove(KEY_CALLVAN_ROOM);
    }

    @Override
    public void saveLastLoginDate(Long lastLoginDate) {
        putLong(KEY_LAST_LOGIN_DATE, lastLoginDate);
    }

    @Override
    public Long loadLastLoginDate() {
        return getLong(KEY_LAST_LOGIN_DATE, 0);
    }

    @Override
    public void clear() {
        SharedPreferences.Editor editor = editor();
        editor.clear();
        editor.commit();
    }

    // 권한 확인
    public AuthorizeConstant checkAuthorize() throws NullPointerException
    {
        String userStr = sharedPreferences.getString(KEY_USER, "");
        if(userStr.isEmpty())
            return AuthorizeConstant.ANONYMOUS;
        else
            return AuthorizeConstant.MEMBER;
    }

}
