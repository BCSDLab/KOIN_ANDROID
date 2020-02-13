package in.koreatech.koin.service_advertise.contracts;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.service_advertise.presenters.AdvertisingCreatingPresenter;

public interface AdvertisingCreatingContract  {
    interface View extends BaseView<AdvertisingCreatingPresenter> {
        void showLoading();

        void hideLoading();

        void onClickEditButton();//create, edited button

        void onAdvertisingDataReceived(Article article);

        void goToAdvertisingActivity(Article article);

        void showUploadImage(String url, String uploadImageId);

        void showFailUploadImage(String uploadImageId);

        void showMessage(String msg);
    }
}
