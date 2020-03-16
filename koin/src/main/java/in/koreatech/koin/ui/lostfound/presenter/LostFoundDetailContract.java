package in.koreatech.koin.ui.lostfound.presenter;

        import androidx.annotation.StringRes;

        import in.koreatech.koin.core.contract.BaseView;
        import in.koreatech.koin.data.network.entity.LostItem;

public interface LostFoundDetailContract {
    interface View extends BaseView<LostFoundDetailPresenter> {
        void showLoading();

        void hideLoading();

        void updateLostDetailData(LostItem lostAndFound);

        void showMessage(String message);

        void showMessage(@StringRes int message);

        void showGranted(boolean isGrant);

        void showSuccessDeleted();
    }
}
