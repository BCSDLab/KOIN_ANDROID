package in.koreatech.koin.core.helpers;

import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Auth;
import in.koreatech.koin.core.networks.entity.User;
import in.koreatech.koin.core.util.TimeUtil;

/**
 * Created by hyerim on 2018. 5. 31....
 */
public class TokenSessionLocalInteractor implements TokenSessionInteractor {

    private final SharedPreferencesHelper mSharedPreferencesUtil;

    public TokenSessionLocalInteractor() {
        this.mSharedPreferencesUtil = DefaultSharedPreferencesHelper.getInstance();
    }

    /**
     * 현재 토큰이 유효한지 검사
     * TODO : 유효성 검사 추가
     * 현재 토큰 저장만 함
     * @param auth
     * @param apiCallback
     */
    @Override
    public void validateCurrentSession(Auth auth, final ApiCallback apiCallback) {
        User user = mSharedPreferencesUtil.loadUser();

        if (user == null) {
            apiCallback.onFailure(new Exception(""));
        } else {
            mSharedPreferencesUtil.saveLastLoginDate(TimeUtil.getDeviceCreatedDateOnlyLong());
            mSharedPreferencesUtil.saveToken(auth.token);
            mSharedPreferencesUtil.saveUser(auth.user);

            apiCallback.onSuccess(null);
        }
    }

    @Override
    public void saveSession(Auth auth, String userPw, final ApiCallback apiCallback) {

        if (auth.user == null) {
            apiCallback.onFailure(new Exception(""));
        } else {
            mSharedPreferencesUtil.saveLastLoginDate(TimeUtil.getDeviceCreatedDateOnlyLong());
            mSharedPreferencesUtil.saveToken(auth.token);
            mSharedPreferencesUtil.saveUser(auth.user);
            mSharedPreferencesUtil.saveUserPw(userPw);

            apiCallback.onSuccess(null);
        }
    }

    @Override
    public void closeSession(ApiCallback apiCallback) {
        mSharedPreferencesUtil.clear();
        apiCallback.onSuccess(null);
    }
}
