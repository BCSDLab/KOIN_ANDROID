package in.koreatech.koin.service_dining.presenters;

import java.net.UnknownHostException;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import in.koreatech.koin.R;
import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.service_dining.contracts.DiningContract;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Dining;
import in.koreatech.koin.core.networks.interactors.DiningInteractor;

/**
 * Created by hyerim on 2018. 6. 21....
 */
public class DiningPresenter implements BasePresenter {

    private final DiningContract.View diningView;

    private final DiningInteractor diningInteractor;

    private boolean mDiningListApiCallCheck;

    private Resources mResources;

    public DiningPresenter(DiningContract.View diningView, DiningInteractor diningInteractor) {
        this.diningView = diningView;
        this.diningInteractor = diningInteractor;

    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            ArrayList<Dining> diningArrayList = (ArrayList<Dining>) object;
            diningView.onDiningListDataReceived(diningArrayList);
            mDiningListApiCallCheck = true;
            diningView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
           // diningView.showMessage(throwable.getMessage());
            mDiningListApiCallCheck = false;
            if(!(throwable instanceof UnknownHostException))
                diningView.showUserInterface();
            else
                diningView.showNetworkError();

            diningView.hideLoading();
        }
    };

    public void getDiningList(String date) {
        diningView.showLoading();
        diningInteractor.readDiningList(date, apiCallback);
    }
}
