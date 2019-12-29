package in.koreatech.koin.service_callvan.presenters;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.service_callvan.contracts.CallvanRoomContract;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.CallvanRoom;
import in.koreatech.koin.core.networks.interactors.CallvanInteractor;

/**
 * Created by hyerim on 2018. 6. 18....
 */
public class CallvanRoomPresenter implements BasePresenter {
    private final CallvanRoomContract.View roomView;

    private final CallvanInteractor callvanInteractor;

    public CallvanRoomPresenter(CallvanRoomContract.View roomView, CallvanInteractor callvanInteractor) {
        this.roomView = roomView;
        this.callvanInteractor = callvanInteractor;
    }

    private final ApiCallback listApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            ArrayList<CallvanRoom> roomArrayList = (ArrayList<CallvanRoom>) object;
            roomView.onCallvanRoomListDataReceived(roomArrayList);
        }

        @Override
        public void onFailure(Throwable throwable) {
            roomView.showMessage(throwable.getMessage());
        }
    };

    private final ApiCallback roomApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            CallvanRoom room = (CallvanRoom) object;
            roomView.onCallvanRoomDataReceived(room);
        }

        @Override
        public void onFailure(Throwable throwable) {
            roomView.showMessage(throwable.getMessage());
        }
    };

    private final ApiCallback createParticipantApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            roomView.onCallvanRoomIncreasePeopleReceived((int)object);
        }

        @Override
        public void onFailure(Throwable throwable) {
            roomView.showMessage(throwable.getMessage());
        }
    };

    private final ApiCallback deleteParticipantApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            roomView.onCallvanRoomDecreasePeopleReceived((boolean) object);
        }

        @Override
        public void onFailure(Throwable throwable) {
            roomView.showMessage(throwable.getMessage());
        }
    };

    public void readRoom(int mJoinedRoomUid) {
        callvanInteractor.readRoom(mJoinedRoomUid, roomApiCallback);
    }

    public void readRoomList() {
        callvanInteractor.readRoomList(listApiCallback);
    }

    public void updateDecreaseCurrentPeopleCount(int roomUid) {
        callvanInteractor.deleteParticipant(roomUid, deleteParticipantApiCallback);
    }

    public void updateIncreaseCurrentPeopleCount(int roomUid) {
        callvanInteractor.createParticipant(roomUid, createParticipantApiCallback);
    }

}
