package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.network.entity.Auth;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.util.TimeUtil;

/**
 * Created by hyerim on 2018. 5. 31....
 */
public class TokenSessionLocalInteractor implements TokenSessionInteractor {


    public TokenSessionLocalInteractor() {
    }

    /**
     * 현재 토큰이 유효한지 검사
     * TODO : 유효성 검사 추가
     * 현재 토큰 저장만 함
     *
     * @param auth
     * @param apiCallback
     */
    @Override
    public void validateCurrentSession(Auth auth, final ApiCallback apiCallback) {
        User user = UserInfoSharedPreferencesHelper.getInstance().loadUser();

        if (user == null) {
            apiCallback.onFailure(new Exception(""));
        } else {
            UserInfoSharedPreferencesHelper.getInstance().saveLastLoginDate(TimeUtil.getDeviceCreatedDateOnlyLong());
            UserInfoSharedPreferencesHelper.getInstance().saveToken(auth.token);
            UserInfoSharedPreferencesHelper.getInstance().saveUser(auth.user);

            apiCallback.onSuccess(null);
        }
    }

    @Override
    public void saveSession(Auth auth, String userPw, final ApiCallback apiCallback) {

        if (auth.user == null) {
            apiCallback.onFailure(new Exception(""));
        } else {
            UserInfoSharedPreferencesHelper.getInstance().saveLastLoginDate(TimeUtil.getDeviceCreatedDateOnlyLong());
            UserInfoSharedPreferencesHelper.getInstance().saveToken(auth.token);
            UserInfoSharedPreferencesHelper.getInstance().saveUser(auth.user);
            UserInfoSharedPreferencesHelper.getInstance().saveUserPw(userPw);

            apiCallback.onSuccess(null);
        }
    }

    @Override
    public void closeSession(ApiCallback apiCallback) {
        UserInfoSharedPreferencesHelper.getInstance().clear();
        apiCallback.onSuccess(null);
    }
}
