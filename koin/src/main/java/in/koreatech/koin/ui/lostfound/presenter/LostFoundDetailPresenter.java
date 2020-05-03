package in.koreatech.koin.ui.lostfound.presenter;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.LostItem;
import in.koreatech.koin.data.network.interactor.LostAndFoundInteractor;
import in.koreatech.koin.data.network.interactor.LostAndFoundRestInteractor;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.data.network.response.GrantCheckResponse;

public class LostFoundDetailPresenter{
    private LostFoundDetailContract.View lostFoundDetailView;
    private LostAndFoundInteractor lostAndFoundInteractor;

    public LostFoundDetailPresenter(LostFoundDetailContract.View lostFoundDetailView, LostAndFoundInteractor lostAndFoundInteractor) {
        this.lostFoundDetailView = lostFoundDetailView;
        this.lostAndFoundInteractor = lostAndFoundInteractor;
        this.lostFoundDetailView.setPresenter(this);
    }

    /**
     * 응답 성공시 권한이 있는 경우 수정 및 삭제 가능하게 버튼을 활성화 시킨다.
     */
    private final ApiCallback grantCheckApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            GrantCheckResponse grantCheckResponse;
            if (object instanceof GrantCheckResponse) {
                grantCheckResponse = (GrantCheckResponse) object;
                lostFoundDetailView.showGranted(grantCheckResponse.isGrantEdit);
            } else {
                lostFoundDetailView.showGranted(false);
                lostFoundDetailView.showMessage(R.string.error_network);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            lostFoundDetailView.showGranted(false);
            lostFoundDetailView.showMessage(R.string.error_network);
        }
    };

    /**
     * 응답 성공시 받아온 정보를 넘겨준다
     */
    private final ApiCallback readLostAndFoundApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if (object instanceof LostItem) {
                LostItem lostItem = (LostItem) object;
                lostFoundDetailView.updateLostDetailData(lostItem);
            } else {
                lostFoundDetailView.showMessage(R.string.error_network);
            }
            lostFoundDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            lostFoundDetailView.showMessage(R.string.error_network);
            lostFoundDetailView.hideLoading();
        }
    };

    private final ApiCallback deleteLostFoundItemApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if (object instanceof DefaultResponse) {
                DefaultResponse defaultResponse = (DefaultResponse) object;
                if (defaultResponse.success) {
                    lostFoundDetailView.showSuccessDeleted();
                }
            } else {
                lostFoundDetailView.showMessage(R.string.lost_and_found_delete_fail);
            }
            lostFoundDetailView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            lostFoundDetailView.showMessage(R.string.lost_and_found_delete_fail);
            lostFoundDetailView.hideLoading();
        }
    };

    /**
     * 분실물 상세 페이지 정보를 받아온다.
     *
     * @param id id를 통해 정보를 받아온다.
     */
    public void getLostFoundDetailById(int id) {
        lostFoundDetailView.showLoading();
        lostAndFoundInteractor.readGrantedDetail(id, grantCheckApiCallback);
        lostAndFoundInteractor.readLostAndFoundDetail(id, readLostAndFoundApiCallback);
    }

    public void removeItem(int id) {
        lostFoundDetailView.showLoading();
        lostAndFoundInteractor.deleteLostAndFoundItem(id, deleteLostFoundItemApiCallback);
    }
}
