package in.koreatech.koin.ui.lostfound.presenter;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.LostItem;
import in.koreatech.koin.data.network.interactor.LostAndFoundInteractor;
import in.koreatech.koin.data.network.interactor.LostAndFoundRestInteractor;

public class LostFoundEditPresenter{
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
            lostFoundEditView.showMessage(R.string.error_network);
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
            lostFoundEditView.showMessage(R.string.error_network);
        }
    };
    public void updateLostItem(int id, LostItem lostItem) {
        lostFoundEditView.showLoading();
        lostAndFoundInteractor.editCotentEdit(id, lostItem, updateLostFoundItemApiCallback);
    }

    public void createLostItem(LostItem lostItem) {
        lostFoundEditView.showLoading();
        lostAndFoundInteractor.createLostAndFoundItem(lostItem, createLostFoundItemApiCallback);
    }
}
