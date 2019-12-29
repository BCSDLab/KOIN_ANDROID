package in.koreatech.koin.presenters;

import androidx.annotation.NonNull;

import in.koreatech.koin.core.contracts.UserInfoEditContract;
import in.koreatech.koin.core.helpers.DefaultSharedPreferencesHelper;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.User;
import in.koreatech.koin.core.networks.interactors.UserInteractor;
import in.koreatech.koin.core.networks.interactors.UserRestInteractor;
import in.koreatech.koin.core.networks.responses.UserInfoEditResponse;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by yunjae on 2018. 8. 23....
 */
public class UserInfoEditPresenter implements UserInfoEditContract.Presenter {

    private final UserInfoEditContract.View mUserInfoEditView;

    private UserInteractor mUserInteractor;

    public UserInfoEditPresenter(@NonNull UserInfoEditContract.View userInfoEditView) {
        mUserInteractor = new UserRestInteractor();
        mUserInfoEditView = checkNotNull(userInfoEditView, "userInfoEditView cannnot be null");
        mUserInfoEditView.setPresenter(this);
    }

    private final ApiCallback updateUserApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            User user = (User) object;
            DefaultSharedPreferencesHelper.getInstance().saveUser(user);
            mUserInfoEditView.showConfirm();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mUserInfoEditView.showConfirmFail();
        }
    };

    //Nickname check apicallback
    private final ApiCallback checkUserNickNameApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            UserInfoEditResponse userInfoEditResponse = (UserInfoEditResponse) object;
            if (userInfoEditResponse.success != null)
                mUserInfoEditView.showCheckNickNameSuccess();
            else
                mUserInfoEditView.showCheckNickNameFail();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mUserInfoEditView.showCheckNickNameFail();
        }
    };

    @Override
    public void updateUserInfo(User user) {
        mUserInteractor.updateUser(updateUserApiCallback, user);
    }

    public void getUserCheckNickName(String nickname) {
        mUserInteractor.readCheckUserNickName(nickname, checkUserNickNameApiCallback);
    }


}
