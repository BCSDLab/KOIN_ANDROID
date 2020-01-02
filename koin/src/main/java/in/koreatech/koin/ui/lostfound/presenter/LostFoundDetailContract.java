package in.koreatech.koin.ui.lostfound.presenter;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.LostItem;

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
