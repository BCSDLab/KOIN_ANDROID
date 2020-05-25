package in.koreatech.koin.data.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.core.sharedpreference.BaseSharedPreferencesHelper;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.util.FormValidatorUtil;

public class UserInfoSharedPreferencesHelper extends BaseSharedPreferencesHelper {
    private final String TAG = UserInfoSharedPreferencesHelper.class.getSimpleName();
    private final String KOIN_SHARED_PREFERENCES = "in.koin.sharedpreferences";
    public static final String KEY_TOKEN = ".token";
    public static final String KEY_USER = ".user";
    public static final String KEY_USER_PW = ".userPw";
    public static final String KEY_CALLVAN_ROOM = ".callvan.room";
    public static final String KEY_LAST_LOGIN_DATE = ".date.login";

    private static volatile UserInfoSharedPreferencesHelper instance = null;


    public static UserInfoSharedPreferencesHelper getInstance() {
        if (instance == null) {
            synchronized (UserInfoSharedPreferencesHelper.class) {
                instance = new UserInfoSharedPreferencesHelper();
            }
        }
        return instance;
    }


    public void init(Context context) {
        super.init(context, context.getSharedPreferences(KOIN_SHARED_PREFERENCES, Context.MODE_PRIVATE));
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = editor();
        editor.putString(KEY_USER, gsonHelper.objectToJSON(user).toString());
        editor.apply();
    }

    public User loadUser() {
        String userStr = sharedPreferences.getString(KEY_USER, "");
        if (FormValidatorUtil.validateStringIsEmpty(userStr))
            return null;
        User user = gsonHelper.jsonToObject(userStr, User.class);
        Log.d(TAG, "user: " + userStr);
        return user;
    }

    public void saveUserPw(String userPw) {
        putString(KEY_USER_PW, userPw);
    }

    public String loadUserPw() {
        return getString(KEY_USER_PW, null);
    }

    public void saveToken(String token) {
        putString(KEY_TOKEN, token);
    }

    public String loadToken() {
        return getString(KEY_TOKEN, null);
    }

    public void saveCallvanRoomUid(int roomUid) {
        putInt(KEY_CALLVAN_ROOM, roomUid);
    }

    public int loadCallvanRoomUid() {
        return getInt(KEY_CALLVAN_ROOM, 0);
    }

    public void removeCallvanRoomUid() {
        remove(KEY_CALLVAN_ROOM);
    }

    public void saveLastLoginDate(Long lastLoginDate) {
        putLong(KEY_LAST_LOGIN_DATE, lastLoginDate);
    }

    public Long loadLastLoginDate() {
        return getLong(KEY_LAST_LOGIN_DATE, 0);
    }

    // 권한 확인
    public AuthorizeConstant checkAuthorize() throws NullPointerException {
        String userStr = sharedPreferences.getString(KEY_USER, "");
        if (userStr.isEmpty())
            return AuthorizeConstant.ANONYMOUS;
        else
            return AuthorizeConstant.MEMBER;
    }

}
