package in.koreatech.koin.core.contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.User;

public interface UserInfoEditContract {
    interface View extends BaseView<Presenter> {
        void showCheckNickNameSuccess(); // 닉네임 사용가능

        void showCheckNickNameFail(); //닉네임 사용 불가능

        void showConfirm(); // 변경 확인

        void showConfirmFail(); //변경 불가
    }

    interface Presenter extends BasePresenter {
        void updateUserInfo(User user);
        void getUserCheckNickName(String s);
    }
}
