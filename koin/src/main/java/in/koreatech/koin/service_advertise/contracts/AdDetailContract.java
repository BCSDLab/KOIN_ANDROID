package in.koreatech.koin.service_advertise.contracts;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.service_advertise.presenters.AdDetailPresenter;

/**
 * Created by hansol on 2020.1.3...
 */
public interface AdDetailContract {
    interface View extends BaseView<AdDetailPresenter> {
        void showLoading();

        void hideLoading();

        void onAdDetailDataReceived(AdDetail adDetail);

        void showMessage(String msg);
    }

    interface Presenter extends BasePresenter {
        void getAdDetailInfo(int shopId);
    }
}
