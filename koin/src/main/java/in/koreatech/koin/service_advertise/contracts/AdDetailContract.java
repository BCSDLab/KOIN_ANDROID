package in.koreatech.koin.service_advertise.contracts;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.service_advertise.presenters.AdDetailPresenter;

/**
 * Created by hansol on 2020.1.3...
 * Edited by seongyun on 2020. 02. 27...
 */
public interface AdDetailContract {
    interface View extends BaseView<AdDetailPresenter> {
        void showLoading();

        void hideLoading();

        void onAdDetailDataReceived(AdDetail adDetail);

        void onAdDetailDeleteCompleted(boolean inSuccess);

        void showMessage(String msg);
    }

    interface Presenter extends BasePresenter {
        void getAdDetailInfo(int shopId);
    }
}