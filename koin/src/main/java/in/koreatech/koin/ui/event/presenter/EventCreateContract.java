package in.koreatech.koin.ui.event.presenter;

public interface EventCreateContract  {
    interface View extends BaseView<AdvertisingCreatingPresenter> {
        void showLoading();

        void hideLoading();

        void onClickEditButton();//create, edited button

        void onAdDetailDataReceived(AdDetail adDetail);

        void goToAdvertisingActivity(AdDetail adDetail);

        void showUploadImage(String url, String uploadImageId);

        void showFailUploadImage(String uploadImageId);

        void showMessage(String msg);
    }
}
