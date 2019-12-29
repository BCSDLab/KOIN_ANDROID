package in.koreatech.koin.service_lostfound.Contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.LostItem;

public interface LostFoundEditContract {
    interface View extends BaseView<LostFoundEditContract.Presenter> {
        void showLoading();

        void hideLoading();

        void showSuccessUpdate(LostItem lostItem);

        void showSuccessCreate(LostItem lostItem);

        void showMessage(String message);

    }

    interface Presenter extends BasePresenter {
        void updateLostItem(int id, LostItem lostItem);

        void createLostItem(LostItem lostItem);
    }
}
