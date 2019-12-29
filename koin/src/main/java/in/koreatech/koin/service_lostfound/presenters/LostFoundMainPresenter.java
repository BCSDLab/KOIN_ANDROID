package in.koreatech.koin.service_lostfound.presenters;

import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.interactors.LostAndFoundInteractor;
import in.koreatech.koin.core.networks.interactors.LostAndFoundRestInteractor;
import in.koreatech.koin.core.networks.responses.LostAndFoundPageResponse;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_lostfound.Contracts.LostFoundMainContract;

/**
 * @author yunjaeNa
 * @since 2019-09-04
 * <p>
 * 분실물 리스트 메인 Presenter
 */
public class LostFoundMainPresenter implements LostFoundMainContract.Presenter {
    private LostAndFoundInteractor lostAndFoundInteractor;
    private LostFoundMainContract.View lostFoundMainView;

    public LostFoundMainPresenter(LostFoundMainContract.View lostFoundMainView) {
        this.lostAndFoundInteractor = new LostAndFoundRestInteractor();
        this.lostFoundMainView = lostFoundMainView;
        lostFoundMainView.setPresenter(this);
    }

    final ApiCallback lostItemApiCallback = new ApiCallback() {
        LostAndFoundPageResponse lostAndFoundPageResponse;

        @Override
        public void onSuccess(Object object) {
            if (object instanceof LostAndFoundPageResponse) {
                lostAndFoundPageResponse = (LostAndFoundPageResponse) object;
                lostFoundMainView.showLostAndFoundPageResponse(lostAndFoundPageResponse);
                lostFoundMainView.hideLoading();
            } else {
                lostFoundMainView.hideLoading();
                lostFoundMainView.showMessage("리스트를 받아오지 못했습니다.");
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            lostFoundMainView.hideLoading();
        }
    };

    @Override
    public void getLostItem(int page, int limit) {
        lostFoundMainView.showLoading();
        lostAndFoundInteractor.readLostAndFoundList(limit, page, lostItemApiCallback);
    }
}
