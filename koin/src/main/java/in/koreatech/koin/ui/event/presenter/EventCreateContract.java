package in.koreatech.koin.ui.event.presenter;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Event;

public interface EventCreateContract  {
    interface View extends BaseView<EventCreatePresenter> {
        void showLoading();

        void hideLoading();

        void onClickEditButton();//create, edited button

        void onEventDataReceived(Event event);

        void goToEventActivity(Event event);

        void showUploadImage(String url, String uploadImageId);

        void showFailUploadImage(String uploadImageId);

        void showMessage(String msg);
    }
}
