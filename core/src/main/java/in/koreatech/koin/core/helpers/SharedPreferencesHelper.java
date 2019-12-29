package in.koreatech.koin.core.helpers;

import in.koreatech.koin.core.networks.entity.User;

/**
 * Created by hyerim on 2018. 5. 31....
 */
public interface SharedPreferencesHelper {
    void saveUser(User user);

    User loadUser();

    void saveUserPw(String userPw);

    String loadUserPw();

    void saveToken(String token);

    String loadToken();

    void saveCallvanRoomUid(int roomUid);

    int loadCallvanRoomUid();

    void removeCallvanRoomUid();

    void clear();

    void saveLastLoginDate(Long lastLoginDate);

    Long loadLastLoginDate();
}
