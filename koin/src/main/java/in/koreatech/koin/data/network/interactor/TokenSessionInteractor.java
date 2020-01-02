package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Auth;

/**
 * Created by hyerim on 2018. 5. 31....
 */
public interface TokenSessionInteractor {
    void validateCurrentSession(Auth auth, final ApiCallback apiCallback);

    void saveSession(Auth auth, String userPw, final ApiCallback apiCallback);

    void closeSession(final ApiCallback apiCallback);
}
