package in.koreatech.koin.service_callvan.contracts;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.service_callvan.presenters.CreateRoomPresenter;

/**
 * Created by hyerim on 2018. 6. 25....
 */
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
