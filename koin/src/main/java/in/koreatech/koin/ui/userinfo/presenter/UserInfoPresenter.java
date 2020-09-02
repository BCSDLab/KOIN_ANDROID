package in.koreatech.koin.ui.userinfo.presenter;

import androidx.annotation.NonNull;

import com.google.firebase.perf.metrics.AddTrace;

import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.data.network.interactor.CallvanInteractor;
import in.koreatech.koin.data.network.interactor.CallvanRestInteractor;
import in.koreatech.koin.data.network.interactor.UserInteractor;
import in.koreatech.koin.data.network.interactor.UserRestInteractor;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserInfoPresenter{

    private final UserInfoContract.View userInfoView;
    private final UserInteractor userInteractor;
    private final CallvanInteractor callvanInteractor;

    public UserInfoPresenter(@NonNull UserInfoContract.View userInfoView) {
        this.userInfoView = checkNotNull(userInfoView, "userInfoView cannnot be null");
        this.userInfoView.setPresenter(this);
        this.userInteractor = new UserRestInteractor();
        this.callvanInteractor = new CallvanRestInteractor();
    }

    private final ApiCallback readUserApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            User user = (User) object;
            UserInfoSharedPreferencesHelper.getInstance().saveUser(user);

            userInfoView.onUserDataReceived();
        }

        @Override
        public void onFailure(Throwable throwable) {
            userInfoView.showMessage(throwable.getMessage());
        }
    };

    private final ApiCallback deleteUserApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            UserInfoSharedPreferencesHelper.getInstance().clear();
            userInfoView.onDeleteUserReceived();
        }

        @Override
        public void onFailure(Throwable throwable) {
            userInfoView.showMessage(throwable.getMessage());
        }
    };

    private final ApiCallback deleteParticipantApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            deleteUser(0);
        }

        @Override
        public void onFailure(Throwable throwable) {
            userInfoView.showMessage(throwable.getMessage());
        }
    };

    @AddTrace(name = "UserInfoPresenter_getUserData")
    public void getUserData() {
        this.userInteractor.readUser(readUserApiCallback);
    }

    public void deleteUser(int roomUid) {
        if (roomUid > 0) {
            updateDecreaseCurrentPeopleCount(roomUid);
        } else {
            this.userInteractor.deleteUser(deleteUserApiCallback);
        }
    }

    public void updateDecreaseCurrentPeopleCount(int roomUid) {
        callvanInteractor.deleteParticipant(roomUid, deleteParticipantApiCallback);
    }

}
