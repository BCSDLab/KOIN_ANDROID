package in.koreatech.koin.service_lostfound.Contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.LostItem;

public interface LostFoundDetailContract {
    interface View extends BaseView<LostFoundDetailContract.Presenter> {
        void showLoading();

        void hideLoading();

        void updateLostDetailData(LostItem lostAndFound);

        void showMessage(String message);

        void showGranted(boolean isGrant);

        void showSuccessDeleted();
    }

    interface Presenter extends BasePresenter {
        void getLostFoundDetailById(int id);

        void removeItem(int id);
    }
}
