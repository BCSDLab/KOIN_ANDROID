package in.koreatech.koin.ui.userinfo.presenter;

import androidx.annotation.NonNull;

import in.koreatech.koin.R;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.data.network.interactor.UserInteractor;
import in.koreatech.koin.data.network.interactor.UserRestInteractor;
import in.koreatech.koin.data.network.response.UserInfoEditResponse;
import in.koreatech.koin.util.FilterUtil;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserInfoEditPresenter{

    private final UserInfoEditContract.View userInfoEditView;

    private UserInteractor userInteractor;

    public UserInfoEditPresenter(@NonNull UserInfoEditContract.View userInfoEditView) {
        this.userInteractor = new UserRestInteractor();
        this.userInfoEditView = checkNotNull(userInfoEditView, "userInfoEditView cannnot be null");
        this.userInfoEditView.setPresenter(this);
    }

    private final ApiCallback updateUserApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            User user = (User) object;
            UserInfoSharedPreferencesHelper.getInstance().saveUser(user);
            userInfoEditView.showConfirm();
        }

        @Override
        public void onFailure(Throwable throwable) {
            userInfoEditView.showConfirmFail();
        }
    };

    //Nickname check apicallback
    private final ApiCallback checkUserNickNameApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            UserInfoEditResponse userInfoEditResponse = (UserInfoEditResponse) object;
            if (userInfoEditResponse.success != null)
                userInfoEditView.showCheckNickNameSuccess();
            else
                userInfoEditView.showCheckNickNameFail();
        }

        @Override
        public void onFailure(Throwable throwable) {
            userInfoEditView.showCheckNickNameFail();
        }
    };

    public void updateUserInfo(User user) {
        this.userInteractor.updateUser(updateUserApiCallback, user);
    }

    public void getUserCheckNickName(String nickname) {
        if(!FilterUtil.isNickNameValidate(nickname)){
            userInfoEditView.showMessage(R.string.user_info_check_nickname_warning);
            return;
        }
        this.userInteractor.readCheckUserNickName(nickname, checkUserNickNameApiCallback);
    }


}
