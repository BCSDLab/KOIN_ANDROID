package in.koreatech.koin.ui.userinfo.presenter;

import androidx.annotation.NonNull;

import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.data.network.interactor.UserInteractor;
import in.koreatech.koin.data.network.interactor.UserRestInteractor;
import in.koreatech.koin.data.network.response.UserInfoEditResponse;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by yunjae on 2018. 8. 23....
 */
public class UserInfoEditPresenter implements UserInfoEditContract.Presenter {

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

    @Override
    public void updateUserInfo(User user) {
        this.userInteractor.updateUser(updateUserApiCallback, user);
    }

    public void getUserCheckNickName(String nickname) {
        this.userInteractor.readCheckUserNickName(nickname, checkUserNickNameApiCallback);
    }


}
