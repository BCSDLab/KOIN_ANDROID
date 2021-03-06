package in.koreatech.koin.ui.dining.presenter;

import android.content.res.Resources;

import java.net.UnknownHostException;
import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Dining;
import in.koreatech.koin.data.network.interactor.DiningInteractor;

public class DiningPresenter {

    private final DiningContract.View diningView;

    private final DiningInteractor diningInteractor;

    private boolean diningListApiCallCheck;

    private Resources resources;

    public DiningPresenter(DiningContract.View diningView, DiningInteractor diningInteractor) {
        this.diningView = diningView;
        this.diningInteractor = diningInteractor;

    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            ArrayList<Dining> diningArrayList = (ArrayList<Dining>) object;
            diningView.onDiningListDataReceived(diningArrayList);
            diningListApiCallCheck = true;
            diningView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
           // diningView.showMessage(throwable.getMessage());
            diningListApiCallCheck = false;
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
