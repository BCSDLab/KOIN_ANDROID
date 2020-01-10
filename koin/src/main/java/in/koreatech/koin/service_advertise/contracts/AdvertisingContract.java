package in.koreatech.koin.service_advertise.contracts;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Advertising;
import in.koreatech.koin.core.networks.entity.BokdukRoom;
import in.koreatech.koin.service_advertise.presenters.AdvertisingPresenter;

/**
 * Created by hansol on 2020.1.1...
 */
public interface AdvertisingContract {

    interface View extends BaseView<AdvertisingPresenter> {
        void onAdvertisingDataReceived(ArrayList<Advertising> adArrayList);
        void showMessage(String message);
    }

    interface Presenter extends BasePresenter {
        void getAdList();
        ArrayList<Advertising> displayProcessingEvent(boolean isChecked1, boolean isChecked2);
    }
}
