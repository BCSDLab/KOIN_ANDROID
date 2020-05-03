package in.koreatech.koin.ui.lostfound.presenter;

import androidx.annotation.StringRes;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.LostItem;

public interface LostFoundEditContract {
    interface View extends BaseView<LostFoundEditPresenter> {
        void showLoading();

        void hideLoading();

        void showSuccessUpdate(LostItem lostItem);

        void showSuccessCreate(LostItem lostItem);

        void showMessage(@StringRes int message);
    }
}
