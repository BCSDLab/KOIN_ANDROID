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

/**
 * Created by hyerim on 2018. 7. 2....
 */
public class UserInfoPresenter implements UserInfoContract.Presenter {

    private final UserInfoContract.View mUserInfoView;
    private final UserInteractor mUserInteractor;
    private final CallvanInteractor mCallvanInteractor;

    public UserInfoPresenter(@NonNull UserInfoContract.View userInfoView) {
        mUserInfoView = checkNotNull(userInfoView, "userInfoView cannnot be null");
        mUserInfoView.setPresenter(this);
        this.mUserInteractor = new UserRestInteractor();
        this.mCallvanInteractor = new CallvanRestInteractor();
    }

    private final ApiCallback readUserApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            User user = (User) object;
            UserInfoSharedPreferencesHelper.getInstance().saveUser(user);

            mUserInfoView.onUserDataReceived();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mUserInfoView.showMessage(throwable.getMessage());
        }
    };

    private final ApiCallback deleteUserApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            UserInfoSharedPreferencesHelper.getInstance().clear();
            mUserInfoView.onDeleteUserReceived();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mUserInfoView.showMessage(throwable.getMessage());
        }
    };

    private final ApiCallback deleteParticipantApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            deleteUser(0);
        }

        @Override
        public void onFailure(Throwable throwable) {
            mUserInfoView.showMessage(throwable.getMessage());
        }
    };

    @AddTrace(name ="UserInfoPresenter_getUserData")
    public void getUserData() {
        mUserInteractor.readUser(readUserApiCallback);
    }

    public void deleteUser(int roomUid) {
        if (roomUid > 0) {
            updateDecreaseCurrentPeopleCount(roomUid);
        } else {
            mUserInteractor.deleteUser(deleteUserApiCallback);
        }
    }

    public void updateDecreaseCurrentPeopleCount(int roomUid) {
        mCallvanInteractor.deleteParticipant(roomUid, deleteParticipantApiCallback );
    }

}
