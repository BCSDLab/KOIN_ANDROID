package in.koreatech.koin.ui.userinfo.presenter;

import androidx.annotation.StringRes;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.User;

public interface UserInfoEditContract {
    interface View extends BaseView<UserInfoEditPresenter> {
        void showCheckNickNameSuccess(); // 닉네임 사용가능

        void showCheckNickNameFail(); //닉네임 사용 불가능

        void showMessage(String message);

        void showMessage(@StringRes int message);

        void showConfirm(); // 변경 확인

        void showConfirmFail(); //변경 불가
    }
}
