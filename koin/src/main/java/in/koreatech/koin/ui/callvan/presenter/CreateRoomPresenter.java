package in.koreatech.koin.ui.callvan.presenter;

import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.CallvanRoom;
import in.koreatech.koin.data.network.interactor.CallvanInteractor;

public class CreateRoomPresenter {

    private final CreateRoomContract.View createRoomView;

    private final CallvanInteractor callvanInteractor;

    public CreateRoomPresenter(CreateRoomContract.View createRoomView, CallvanInteractor callvanInteractor) {
        this.createRoomView = createRoomView;
        this.callvanInteractor = callvanInteractor;
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            UserInfoSharedPreferencesHelper.getInstance().saveCallvanRoomUid(((CallvanRoom) object).uid);

            createRoomView.onCreateRoomDataReceived(true);
        }

        @Override
        public void onFailure(Throwable throwable) {
            createRoomView.showMessage(throwable.getMessage());
        }
    };

    public void createCallvanRoom(CallvanRoom callvanRoom) {
        callvanInteractor.createRoom(callvanRoom, apiCallback);
    }

}

