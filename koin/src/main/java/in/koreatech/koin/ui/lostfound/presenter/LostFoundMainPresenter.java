package in.koreatech.koin.ui.lostfound.presenter;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.interactor.LostAndFoundInteractor;
import in.koreatech.koin.data.network.interactor.LostAndFoundRestInteractor;
import in.koreatech.koin.data.network.response.LostAndFoundPageResponse;

/**
 * 분실물 리스트 메인 Presenter
 */
public class LostFoundMainPresenter{
    private LostAndFoundInteractor lostAndFoundInteractor;
    private LostFoundMainContract.View lostFoundMainView;

    public LostFoundMainPresenter(LostFoundMainContract.View lostFoundMainView, LostAndFoundInteractor lostAndFoundInteractor) {
        this.lostAndFoundInteractor = new LostAndFoundRestInteractor();
        this.lostFoundMainView = lostFoundMainView;
        this.lostAndFoundInteractor = lostAndFoundInteractor;
        this.lostFoundMainView.setPresenter(this);
    }

    private final ApiCallback lostItemApiCallback = new ApiCallback() {
        LostAndFoundPageResponse lostAndFoundPageResponse;

        @Override
        public void onSuccess(Object object) {
            if (object instanceof LostAndFoundPageResponse) {
                lostAndFoundPageResponse = (LostAndFoundPageResponse) object;
                lostFoundMainView.showLostAndFoundPageResponse(lostAndFoundPageResponse);
                lostFoundMainView.hideLoading();
            } else {
                lostFoundMainView.showMessage("리스트를 받아오지 못했습니다.");
                lostFoundMainView.hideLoading();
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            lostFoundMainView.showMessage("리스트를 받아오지 못했습니다.");
            lostFoundMainView.hideLoading();
        }
    };

    public void getLostItem(int page, int limit) {
        lostFoundMainView.showLoading();
        lostAndFoundInteractor.readLostAndFoundList(limit, page, lostItemApiCallback);
    }
}
