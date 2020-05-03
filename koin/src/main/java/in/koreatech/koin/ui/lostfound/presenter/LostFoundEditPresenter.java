package in.koreatech.koin.ui.lostfound.presenter;

import java.util.regex.Pattern;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.LostItem;
import in.koreatech.koin.data.network.interactor.LostAndFoundInteractor;
import in.koreatech.koin.data.network.interactor.LostAndFoundRestInteractor;
import in.koreatech.koin.util.FilterUtil;
import in.koreatech.koin.util.FormValidatorUtil;

public class LostFoundEditPresenter {
    private LostFoundEditContract.View lostFoundEditView;
    private LostAndFoundInteractor lostAndFoundInteractor;

    public LostFoundEditPresenter(LostFoundEditContract.View lostFoundEditView) {
        this.lostFoundEditView = lostFoundEditView;
        this.lostFoundEditView.setPresenter(this);
        lostAndFoundInteractor = new LostAndFoundRestInteractor();
    }

    private final ApiCallback createLostFoundItemApiCallback = new ApiCallback() {
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

    private final ApiCallback updateLostFoundItemApiCallback = new ApiCallback() {
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
        if (FormValidatorUtil.validateStringIsEmpty(lostItem.getTitle())) {
            lostFoundEditView.showMessage(R.string.lost_and_found_title_check);
            return;
        } else if (lostItem.isPhoneOpen() && !FilterUtil.isPhoneValidate(lostItem.getPhone())) {
            lostFoundEditView.showMessage(R.string.lost_and_found_phone_check);
            return;
        } else if (FormValidatorUtil.validateHTMLStringIsEmpty(lostItem.getContent())) {
            lostFoundEditView.showMessage(R.string.lost_and_found_content_check);
            return;
        }
        lostFoundEditView.showLoading();
        lostAndFoundInteractor.editCotentEdit(id, lostItem, updateLostFoundItemApiCallback);
    }

    public void createLostItem(LostItem lostItem) {
        if (FormValidatorUtil.validateStringIsEmpty(lostItem.getTitle())) {
            lostFoundEditView.showMessage(R.string.lost_and_found_title_check);
            return;
        } else if (lostItem.isPhoneOpen() && !FilterUtil.isPhoneValidate(lostItem.getPhone())) {
            lostFoundEditView.showMessage(R.string.lost_and_found_phone_check);
            return;
        } else if (FormValidatorUtil.validateHTMLStringIsEmpty(lostItem.getContent())) {
            lostFoundEditView.showMessage(R.string.lost_and_found_content_check);
            return;
        }

        lostFoundEditView.showLoading();
        lostAndFoundInteractor.createLostAndFoundItem(lostItem, createLostFoundItemApiCallback);
    }
}
