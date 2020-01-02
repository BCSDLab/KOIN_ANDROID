package in.koreatech.koin.service_advertise.presenters;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Advertising;
import in.koreatech.koin.core.networks.interactors.AdvertisingInteractor;
import in.koreatech.koin.service_advertise.contracts.AdvertisingContract;

/**
 * Created by hansol on 2020.1.1...
 */
public class AdvertisingPresenter implements BasePresenter {
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
            Advertising ads = (Advertising)object;
            adArrayList.clear();
            adArrayList.addAll(ads.ads);
            adView.onAdvertisingDataReceived();
        }

        @Override
        public void onFailure(Throwable throwable) {

        }
    }
}
