package in.koreatech.koin.ui.userinfo.presenter;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.User;

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
