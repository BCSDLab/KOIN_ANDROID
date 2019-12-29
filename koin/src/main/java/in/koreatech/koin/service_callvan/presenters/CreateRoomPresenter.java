package in.koreatech.koin.service_callvan.presenters;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.service_callvan.contracts.CreateRoomContract;
import in.koreatech.koin.core.helpers.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.CallvanRoom;
import in.koreatech.koin.core.networks.interactors.CallvanInteractor;

/**
 * Created by hyerim on 2018. 6. 25....
 */
public class CreateRoomPresenter implements BasePresenter {

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

