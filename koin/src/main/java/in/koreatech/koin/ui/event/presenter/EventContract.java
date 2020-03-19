package in.koreatech.koin.ui.event.presenter;

public interface EventContract {
    interface View extends BaseView<AdvertisingPresenter> {
        void onAdvertisingDataReceived(ArrayList<Advertising> adArrayList);

        void onGrantCheckReceived(AdDetail adDetail);

        void showMessage(String message);

        void showLoading();

        void hideLoading();
    }

    void getAdList();

    ArrayList<Advertising> displayProcessingEvent(boolean isChecked1, boolean isChecked2);
}
