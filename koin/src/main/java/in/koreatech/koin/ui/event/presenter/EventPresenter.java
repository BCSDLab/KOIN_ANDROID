package in.koreatech.koin.ui.event.presenter;

public class EventPresenter {
    private final AdvertisingContract.View adView;
    private final AdvertisingInteractor advertisingInteractor;
    private ArrayList<Advertising> adArrayList;

    public AdvertisingPresenter(AdvertisingContract.View adView, AdvertisingInteractor advertisingInteractor) {
        this.adView = adView;
        this.advertisingInteractor = advertisingInteractor;
        adArrayList = new ArrayList<>();
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Advertising ads = (Advertising) object;
            adArrayList.clear();
            adArrayList.addAll(ads.ads);
            adView.onAdvertisingDataReceived(adArrayList);
        }

        @Override
        public void onFailure(Throwable throwable) {
            adView.showMessage("홍보 게시물을 받아오지 못했습니다");
        }
    };

    @Override
    public void getAdList() {
        adArrayList.clear();
        advertisingInteractor.readAdList(apiCallback);
    }

    @Override
    public ArrayList<Advertising> displayProcessingEvent(boolean isChecked1, boolean isChecked2) {
        ArrayList<Advertising> subAdDate = new ArrayList<>();
        if (isChecked1 && isChecked2) { //전체
            subAdDate.addAll(adArrayList);
        }
        if (!isChecked1 && !isChecked2) { //아무것도 없음
            return subAdDate;
        }

        Date date = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

        if (isChecked1 && !isChecked2) {
            for (int i = 0; i < adArrayList.size(); i++) {
                try {
                    if ((date.compareTo(formatDate.parse(adArrayList.get(i).endDate)) == 1)) {
                        subAdDate.add(adArrayList.get(i));
                    }
                } catch (ParseException e) {
                    adView.showMessage("에러");
                }
            }
        }
        if (!isChecked1 && isChecked2) {
            for (int i = 0; i < adArrayList.size(); i++) {
                try {
                    if ((date.compareTo(formatDate.parse(adArrayList.get(i).endDate)) == -1)
                            && (date.compareTo(formatDate.parse(adArrayList.get(i).startDate)) == 1)) {
                        subAdDate.add(adArrayList.get(i));
                    }
                } catch (ParseException e) {
                    adView.showMessage("에러");
                }
            }
        }

        return subAdDate;
    }

    private final ApiCallback grantCheckApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            adView.onGrantCheckReceived((AdDetail) object);
            adView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            adView.showMessage(throwable.getMessage());
            adView.hideLoading();
        }
    };

    public void getAdGrantCheck(int articleUid) {
        adView.showLoading();
        advertisingInteractor.updateGrantCheck(articleUid, grantCheckApiCallback);
        Log.d("AdverstisingActivity", "clicked");
    }
}
