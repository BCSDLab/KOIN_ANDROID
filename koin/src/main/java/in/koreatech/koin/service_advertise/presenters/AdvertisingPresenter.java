package in.koreatech.koin.service_advertise.presenters;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.service_advertise.contracts.AdvertisingContract;

/**
 * Created by hansol on 2020.1.1...
 */
public class AdvertisingPresenter implements BasePresenter {
    AdvertisingContract.View adView;

    public AdvertisingPresenter(AdvertisingContract.View adView) {
        this.adView = adView;
    }
}
