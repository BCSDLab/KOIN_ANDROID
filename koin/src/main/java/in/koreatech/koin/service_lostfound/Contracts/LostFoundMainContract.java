package in.koreatech.koin.service_lostfound.Contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.LostItem;
import in.koreatech.koin.core.networks.responses.LostAndFoundPageResponse;

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
