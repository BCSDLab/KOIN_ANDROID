package in.koreatech.koin.ui.lostfound.presenter;

import androidx.annotation.StringRes;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.response.LostAndFoundPageResponse;

public interface LostFoundMainContract {
    interface View extends BaseView<LostFoundMainPresenter> {
        void showLoading();

        void hideLoading();

        void showLostAndFoundPageResponse(LostAndFoundPageResponse lostAndFoundPageResponse);

        void showMessage(@StringRes int message);

    }
}
