package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.User;

/**
 * Created by hyerim on 2018. 5. 31....
 */
public interface UserInteractor {
    void readToken(String email, String password,  boolean isPasswordHash, final ApiCallback apiCallback);
    void createToken(String email, String password, final ApiCallback apiCallback );
    void createFindPassword(String email, final ApiCallback apiCallback );

    void readCheckUserNickName(String nickname, final ApiCallback apiCallback);

    void readUser(final ApiCallback apiCallback);
    void updateUser(final ApiCallback apiCallback, User user);
    void deleteUser(final ApiCallback apiCallback);
}
