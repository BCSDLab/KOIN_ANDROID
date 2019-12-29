package in.koreatech.koin.presenters;

import androidx.annotation.NonNull;

import com.google.firebase.perf.metrics.AddTrace;
import in.koreatech.koin.contracts.UserInfoContract;
import in.koreatech.koin.core.helpers.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.User;
import in.koreatech.koin.core.networks.interactors.CallvanInteractor;
import in.koreatech.koin.core.networks.interactors.CallvanRestInteractor;
import in.koreatech.koin.core.networks.interactors.UserInteractor;
import in.koreatech.koin.core.networks.interactors.UserRestInteractor;

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
