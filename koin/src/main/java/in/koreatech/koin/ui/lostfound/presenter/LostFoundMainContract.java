package in.koreatech.koin.ui.lostfound.presenter;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.response.LostAndFoundPageResponse;

public interface LostFoundMainContract {
    interface View extends BaseView<LostFoundMainContract.Presenter> {
        void showLoading();

        void hideLoading();

        void showLostAndFoundPageResponse(LostAndFoundPageResponse lostAndFoundPageResponse);

        void showMessage(String message);

    }

    interface Presenter extends BasePresenter {
        void getLostItem(int page, int limit);
    }
}
