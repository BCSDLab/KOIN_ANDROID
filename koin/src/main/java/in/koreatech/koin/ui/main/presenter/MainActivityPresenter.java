package in.koreatech.koin.ui.main.presenter;

import android.util.Log;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;

import in.koreatech.koin.constant.BusType;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Bus;
import in.koreatech.koin.data.network.entity.Dining;
import in.koreatech.koin.data.network.entity.RegularSemesterBus;
import in.koreatech.koin.data.network.entity.SeasonalSemesterBus;
import in.koreatech.koin.data.network.entity.Term;
import in.koreatech.koin.data.network.entity.VacationBus;
import in.koreatech.koin.data.network.interactor.CityBusInteractor;
import in.koreatech.koin.data.network.interactor.DiningInteractor;
import in.koreatech.koin.data.network.interactor.TermInteractor;
import in.koreatech.koin.data.network.response.BusResponse;

public class MainActivityPresenter implements MainActivityContact.Presenter {
    private static final String TAG = MainActivityPresenter.class.getSimpleName();

    private final MainActivityContact.View view;
    private final CityBusInteractor busInteractor;
    private final TermInteractor termInteractor;
    private final DiningInteractor diningInteractor;

    private boolean diningListApiCallCheck;

    private final ApiCallback apiCallback = new ApiCallback() {                     //시내버스의 시간을 받아오는 api callback
        @Override
        public void onSuccess(Object object) {
            BusResponse busResponse = (BusResponse) object;
            view.updateCityBusDepartInfo(busResponse.busNumber);
            view.updateCityBusTime(busResponse.remainTime);

            view.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.d(TAG, throwable.getMessage());
            Log.e("Citybus Failed", "");
            view.updateFailCityBusDepartInfo();
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

    private final ApiCallback diningApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            ArrayList<Dining> diningArrayList = (ArrayList<Dining>) object;
            view.onDiningListDataReceived(diningArrayList);
            diningListApiCallCheck = true;
            view.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            // diningView.showMessage(throwable.getMessage());
            diningListApiCallCheck = false;
            if(!(throwable instanceof UnknownHostException))
                view.showEmptyDining();
            else
                view.showNetworkError();

            view.hideLoading();
        }
    };

    public MainActivityPresenter(MainActivityContact.View view, CityBusInteractor busInteractor, TermInteractor termInteractor, DiningInteractor diningInteractor) {
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
        String departString;
        String arrivalString;

        if (depart == 0)
            departString = BusType.KOREATECH.getDestination();
        else if (depart == 1)
            departString = BusType.TERMINAL.getDestination();
        else
            departString = BusType.STATION.getDestination();

        if (arrival == 0)
            arrivalString = BusType.KOREATECH.getDestination();
        else if (arrival == 1)
            arrivalString = BusType.TERMINAL.getDestination();
        else
            arrivalString = BusType.STATION.getDestination();

        busInteractor.readCityBusList(apiCallback, departString, arrivalString);
    }

    public void getDaesungBus(int depart, int arrival) {
        // 0 => 한기대 2 => 터미널
        view.showLoading();
        try {
            int soonArrival = (int) Bus.getRemainExpressTimeToLong(BusType.getValueOf(depart), BusType.getValueOf(arrival), true);
            String soonDeparture = Bus.getNearExpressTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), true);
            view.updateDaesungBusTime(soonArrival);
            view.updateDaesungBusDepartInfo(soonDeparture);
        } catch (ParseException e) {
            Log.e("Daesung Failed", "");
            view.updateFailDaesungBusDepartInfo();
        }

        view.hideLoading();
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
        // 0 : 한기대 1 : 야우리 2 : 천안역
        view.showLoading();
        Bus currentSemesterBus;
        switch (term % 10) {
            case 0:
                currentSemesterBus = new RegularSemesterBus();
            case 1:
                currentSemesterBus = new SeasonalSemesterBus();
            default:
                currentSemesterBus = new VacationBus();
        }
        try {
            int soonArrival = (int) currentSemesterBus.getRemainShuttleTimeToLong(BusType.getValueOf(depart), BusType.getValueOf(arrival), true);
            int laterArrival = (int) currentSemesterBus.getRemainShuttleTimeToLong(BusType.getValueOf(depart), BusType.getValueOf(arrival), false);
            String soonDeparture = currentSemesterBus.getNearShuttleTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), true);
            String laterDeparture = currentSemesterBus.getNearShuttleTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), false);
            view.updateShuttleBusTime(soonArrival);
            view.updateShuttleBusDepartInfo(soonDeparture);
        } catch (ParseException e) {
            Log.e("CityBus Failed", "getShuttlebus");
            view.updateFailCityBusDepartInfo();
        }

        view.hideLoading();
    }

    @Override
    public void getDiningList(String date) {
        view.showLoading();
        diningInteractor.readDiningList(date, diningApiCallback);
    }
}
