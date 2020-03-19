package in.koreatech.koin.ui.event.presenter;

public class EventCreatePresenter {
    private AdvertisingCreatingContract.View adCreatingView;
    private AdDetailInterator adDetailInterator;
    private MarketUsedInteractor marketUsedInteractor;

    private String uploadImageId;

    public AdvertisingCreatingPresenter(AdvertisingCreatingContract.View adCreatingView, AdDetailInterator adDetailInterator) {
        this.adCreatingView = adCreatingView;
        this.adDetailInterator = adDetailInterator;
        this.marketUsedInteractor = new MarketUsedRestInteractor();
    }

    private final ApiCallback advertisingApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            adCreatingView.onAdDetailDataReceived((AdDetail) object);
            adCreatingView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            adCreatingView.showMessage(throwable.getMessage());
            adCreatingView.hideLoading();
        }
    };

    private final ApiCallback uploadImageApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Image img = (Image) object;

            if (img != null) {
                adCreatingView.showUploadImage(img.getUrls().get(0), uploadImageId);
            } else {
                adCreatingView.showFailUploadImage(uploadImageId);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            adCreatingView.showFailUploadImage(uploadImageId);
        }
    };

    public void createAdDetail(AdDetail adDetail) {
        adCreatingView.showLoading();
        adDetailInterator.createAdDetail(adDetail, advertisingApiCallback);
    }

    public void uploadImage(File file, String uid) {
        uploadImageId = uid;
        if(file == null) {
            adCreatingView.showFailUploadImage(uid);
            return;
        }

        marketUsedInteractor.uploadImage(file, uploadImageApiCallback);
    }

    public void updateAdDetail(int articleId, AdDetail adDetail) {
        adCreatingView.showLoading();
        adDetailInterator.updateAdDetail(articleId, adDetail, advertisingApiCallback);
    }
}
