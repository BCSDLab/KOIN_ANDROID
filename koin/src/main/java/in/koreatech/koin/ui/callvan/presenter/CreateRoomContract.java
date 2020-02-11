package in.koreatech.koin.ui.callvan.presenter;

import in.koreatech.koin.core.contract.BaseView;

public interface CreateRoomContract {
    interface View extends BaseView<CreateRoomPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void onClickCreateRoomStartPlaceLayout();

        void onClickCreateRoomEndPlaceLayout();

//        void onClickCreateRoomStartYearLayout();
//
//        void onClickCreateRoomStartMonthLayout();
//
//        void onClickCreateRoomStartDayLayout();
//
//        void onClickCreateRoomStartHourLayout();
//
//        void onClickCreateRoomStartMinuteLayout();

        void onClickCreateRoomButton();

        void createRoomToServer(final String startPlaceResult, final String endPlaceResult, final String startTime, final int maxPeople);

        void onCreateRoomDataReceived(boolean isSuccess);

    }
}
