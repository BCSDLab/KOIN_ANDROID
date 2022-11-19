package in.koreatech.koin.ui.main.presenter;

import android.util.Log;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import in.koreatech.koin.constant.BusType;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Bus;
import in.koreatech.koin.data.network.entity.RegularSemesterBus;
import in.koreatech.koin.data.network.entity.SeasonalSemesterBus;
import in.koreatech.koin.data.network.entity.Term;
import in.koreatech.koin.data.network.entity.VacationBus;
import in.koreatech.koin.data.network.interactor.BusInteractor;
import in.koreatech.koin.data.network.interactor.DiningInteractor;
import in.koreatech.koin.data.network.interactor.TermInteractor;
import in.koreatech.koin.data.response.bus.BusResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenter implements MainActivityContact.Presenter {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final String TAG = MainActivityPresenter.class.getSimpleName();

    private final MainActivityContact.View view;
    private final BusInteractor busInteractor;
    private final TermInteractor termInteractor;
    private final DiningInteractor diningInteractor;
    private final ApiCallback apiCallbackCitybus = new ApiCallback() {                     //시내버스의 시간을 받아오는 api callback
        @Override
        public void onSuccess(Object object) {
            try {
                BusResponse busResponse = (BusResponse) object;
                view.updateCityBusDepartInfo(busResponse.getNowBus().getBusNumber());
                view.updateCityBusTime((int) busResponse.getNowBus().getRemainTimeSecond());

                view.hideLoading();
            } catch (Throwable throwable) {
                Log.d(TAG, throwable.getMessage());
                Log.e("Citybus Failed", "");
                view.updateFailCityBusDepartInfo();
                view.hideLoading();
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.d(TAG, throwable.getMessage());
            Log.e("Citybus Failed", "");
            view.updateFailCityBusDepartInfo();
            view.hideLoading();
        }
    };

    private final ApiCallback apiCallbackDaesung = new ApiCallback() {                     //시내버스의 시간을 받아오는 api callback
        @Override
        public void onSuccess(Object object) {
            try {
                BusResponse busResponse = (BusResponse) object;
                LocalTime localTime = LocalTime.now().plusSeconds(busResponse.getNowBus().getRemainTimeSecond()).plusMinutes(1);;
                view.updateDaesungBusDepartInfo(localTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                view.updateDaesungBusTime((int) busResponse.getNowBus().getRemainTimeSecond());

                view.hideLoading();
            } catch (Throwable throwable) {
                Log.d(TAG, throwable.getMessage());
                Log.e("Daesung Failed", "");
                view.updateFailDaesungBusDepartInfo();
                view.hideLoading();
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.d(TAG, throwable.getMessage());
            Log.e("Daesung Failed", "");
            view.updateFailDaesungBusDepartInfo();
            view.hideLoading();
        }
    };

    private final ApiCallback apiCallbackShuttle = new ApiCallback() {                     //시내버스의 시간을 받아오는 api callback
        @Override
        public void onSuccess(Object object) {
            try {
                BusResponse busResponse = (BusResponse) object;
                LocalTime localTime = LocalTime.now().plusSeconds(busResponse.getNowBus().getRemainTimeSecond()).plusMinutes(1);
                view.updateShuttleBusDepartInfo(localTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                view.updateShuttleBusTime((int) busResponse.getNowBus().getRemainTimeSecond());

                view.hideLoading();
            } catch (Throwable throwable) {
                Log.d(TAG, throwable.getMessage());
                Log.e("Shuttle Failed", "");
                view.updateFailShuttleBusDepartInfo();
                view.hideLoading();
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.d(TAG, throwable.getMessage());
            Log.e("Shuttle Failed", "");
            view.updateFailShuttleBusDepartInfo();
            view.hideLoading();
        }
    };
    private final ApiCallback termApiCallback = new ApiCallback() {             //방학인지 학기중인지 정보를 받아오는 api callback
        @Override
        public void onSuccess(Object object) {
            Term term = (Term) object;
            view.updateShuttleBusInfo(term.getTerm());                   //셔틀버스의 남은 시간을 계산하여 업데이트
            view.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.d(TAG, throwable.getMessage());
            view.updateFailCityBusDepartInfo();
            view.hideLoading();
        }
    };

    public MainActivityPresenter(MainActivityContact.View view, BusInteractor busInteractor, TermInteractor termInteractor, DiningInteractor diningInteractor) {
        this.view = view;
        this.busInteractor = busInteractor;
        this.termInteractor = termInteractor;
        this.diningInteractor = diningInteractor;
    }

    /**
     * 학기 정보를 요청하는 함수
     * 학기에 따라 셔틀버스 시간표가 달라져 셔틀버스 정보를 얻어오기전에 먼저 실행되어야 한다.
     */
    public void getTermInfo() {
        termInteractor.readTerm(termApiCallback);
    }

    public void getCityBus(int depart, int arrival) {
        view.showLoading();
        String departString = getBusNodeString(depart);
        String arrivalString = getBusNodeString(arrival);

        busInteractor.readCityBusList(apiCallbackCitybus, departString, arrivalString);
    }

    public void getDaesungBus(int depart, int arrival) {
        view.showLoading();
        String departString = getBusNodeString(depart);
        String arrivalString = getBusNodeString(arrival);

        busInteractor.readDaesungBusList(apiCallbackDaesung, departString, arrivalString);
    }

    /**
     * 셔틀버스의 시간표를 계산해주는 함수
     * 시내버스와 학기정보와의 관계는 없다. 그러나 방학때는 셔틀버스의 시간이 달라짐으로 학기정보와 셔틀버스 시간은 관계가 있다.
     * isVacation으로 방학인지 학기중인지 구별
     *
     * @param depart  출발지
     * @param arrival 도착지
     * @param term    학기 정보
     */
    public void getShuttleBus(int depart, int arrival, int term) {
        view.showLoading();
        String departString = getBusNodeString(depart);
        String arrivalString = getBusNodeString(arrival);

        busInteractor.readShuttleBusList(apiCallbackShuttle, departString, arrivalString);
    }

    @Override
    public void getDiningList(String date) {
        compositeDisposable.add(
                diningInteractor.readDingingList(date)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> view.showLoading())
                        .doOnComplete(view::hideLoading)
                        .subscribe(view::onDiningListDataReceived, throwable -> view.showEmptyDining()));
    }

    private String getBusNodeString(int node) {
        if (node == 0)
            return BusType.KOREATECH.getDestination();
        else if (node == 1)
            return BusType.TERMINAL.getDestination();
        else
            return BusType.STATION.getDestination();
    }
}
