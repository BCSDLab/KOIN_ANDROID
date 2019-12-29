package in.koreatech.koin.service_lostfound.presenters;

import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.LostItem;
import in.koreatech.koin.core.networks.interactors.LostAndFoundInteractor;
import in.koreatech.koin.core.networks.interactors.LostAndFoundRestInteractor;
import in.koreatech.koin.service_lostfound.Contracts.LostFoundEditContract;

public class LostFoundEditPresenter implements LostFoundEditContract.Presenter {
    private LostFoundEditContract.View lostFoundEditView;
    private LostAndFoundInteractor lostAndFoundInteractor;

    public LostFoundEditPresenter(LostFoundEditContract.View lostFoundEditView) {
        this.lostFoundEditView = lostFoundEditView;
        this.lostFoundEditView.setPresenter(this);
        lostAndFoundInteractor = new LostAndFoundRestInteractor();

    }

    final ApiCallback createLostFoundItemApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if (object instanceof LostItem) {
                LostItem lostItem = (LostItem) object;
                lostFoundEditView.showSuccessCreate(lostItem);
            }
            lostFoundEditView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            lostFoundEditView.hideLoading();
            lostFoundEditView.showMessage("네트워크 환경을 확인해주세요");
        }
    };

    final ApiCallback updateLostFoundItemApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if (object instanceof LostItem) {
                LostItem lostItem = (LostItem) object;
                lostFoundEditView.showSuccessUpdate(lostItem);
            }
            lostFoundEditView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            lostFoundEditView.hideLoading();
            lostFoundEditView.showMessage("네트워크 환경을 확인해주세요");
        }
    };

    @Override
    public void updateLostItem(int id, LostItem lostItem) {
        lostFoundEditView.showLoading();
        lostAndFoundInteractor.editCotentEdit(id, lostItem, updateLostFoundItemApiCallback);
    }

    @Override
    public void createLostItem(LostItem lostItem) {
        lostFoundEditView.showLoading();
        lostAndFoundInteractor.createLostAndFoundItem(lostItem, createLostFoundItemApiCallback);
    }
}
