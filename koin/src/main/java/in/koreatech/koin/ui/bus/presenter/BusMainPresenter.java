package in.koreatech.koin.ui.bus.presenter;

import android.util.Log;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Bus;
import in.koreatech.koin.constant.BusType;
import in.koreatech.koin.data.network.entity.Term;
import in.koreatech.koin.data.network.entity.VacationBus;
import in.koreatech.koin.data.network.interactor.CityBusInteractor;
import in.koreatech.koin.data.network.interactor.TermInteractor;
import in.koreatech.koin.data.network.response.BusResponse;

import java.text.ParseException;


public class BusMainPresenter {
    private String TAG = "BusMainPresenter";
    private final BusMainContract.View busMainView;
    private final CityBusInteractor busInteractor;
    private final TermInteractor termInteractor;


    public BusMainPresenter(BusMainContract.View busMainView, CityBusInteractor busInteractor, TermInteractor termInteractor) {
        this.busMainView = busMainView;
        this.busInteractor = busInteractor;
        this.termInteractor = termInteractor;
    }

    private final ApiCallback apiCallback = new ApiCallback() {                     //시내버스의 시간을 받아오는 api callback
        @Override
        public void onSuccess(Object object) {
            BusResponse busResponse = (BusResponse) object;
            busMainView.updateCityBusDepartInfo(busResponse.busNumber, busResponse.nextBusNumber);
            busMainView.updateCityBusTime(busResponse.remainTime, busResponse.nextRemainTime);

            busMainView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.d(TAG, throwable.getMessage());
            busMainView.updateFailCityBusDepartInfo();
            busMainView.hideLoading();
        }
    };
    private final ApiCallback termApiCallback = new ApiCallback() {             //방학인지 학기중인지 정보를 받아오는 api callback
        @Override
        public void onSuccess(Object object) {
            Term term = (Term) object;
            busMainView.updateShuttleBusInfo(term.getTerm());                   //셔틀버스의 남은 시간을 계산하여 업데이트
            busMainView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.d(TAG, throwable.getMessage());
            busMainView.updateFailCityBusDepartInfo();
            busMainView.hideLoading();
        }
    };

    /**
     * 학기 정보를 요청하는 함수
     * 학기에 따라 셔틀버스 시간표가 달라져 셔틀버스 정보를 얻어오기전에 먼저 실행되어야 한다.
     */
    public void getTermInfo() {
        termInteractor.readTerm(termApiCallback);
    }

    public void getCityBus(int depart, int arrival) {
        busMainView.showLoading();
        String departString;
        String arrivalString;

        if (depart == 0)
            departString = "koreatech";
        else if (depart == 1)
            departString = "station";
        else
            departString = "terminal";

        if (arrival == 0)
            arrivalString = "koreatech";
        else if (arrival == 1)
            arrivalString = "station";
        else
            arrivalString = "terminal";

        busInteractor.readCityBusList(apiCallback, departString, arrivalString);
    }

    public void getDaesungBus(int depart, int arrival) {
        // 0 => 한기대 2 => 터미널
        busMainView.showLoading();
        try {
            int soonArrival = (int) Bus.getRemainExpressTimeToLong(BusType.getValueOf(depart), BusType.getValueOf(arrival), true);
            int laterArrival = (int) Bus.getRemainExpressTimeToLong(BusType.getValueOf(depart), BusType.getValueOf(arrival), false);
            String soonDeparture = Bus.getNearExpressTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), true);
            String laterDeparture = Bus.getNearExpressTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), false);
            busMainView.updateDaesungBusTime(soonArrival, laterArrival);
            busMainView.updateDaesungBusDepartInfo(soonDeparture, laterDeparture);
        } catch (ParseException e) {
            busMainView.updateFailDaesungBusDepartInfo();
        }

        busMainView.hideLoading();
    }

    /**
     * 셔틀버스의 시간표를 계산해주는 함수
     * 시내버스와 학기정보와의 관계는 없다. 그러나 방학때는 셔틀버스의 시간이 달라짐으로 학기정보와 셔틀버스 시간은 관계가 있다.
     * isVacation으로 방학인지 학기중인지 구별
     *
     * @param depart     출발지
     * @param arrival    도착지
     * @param isVacation 방학여부
     */
    public void getShuttleBus(int depart, int arrival, boolean isVacation) {
        // 0 : 한기대 1 : 야우리 2 : 천안역
        busMainView.showLoading();
            if (!isVacation) {                                                     //학기중
                try {
                    int soonArrival = (int) Bus.getRemainShuttleTimeToLong(BusType.getValueOf(depart), BusType.getValueOf(arrival), true);
                    int laterArrival = (int) Bus.getRemainShuttleTimeToLong(BusType.getValueOf(depart), BusType.getValueOf(arrival), false);
                    String soonDeparture = Bus.getNearShuttleTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), true);
                    String laterDeparture = Bus.getNearShuttleTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), false);
                    busMainView.updateShuttleBusTime(soonArrival, laterArrival);
                    busMainView.updateShuttleBusDepartInfo(soonDeparture, laterDeparture);
                } catch (ParseException e) {
                    busMainView.updateFailCityBusDepartInfo();
                }
            } else {                                                                //방학중
                try {
                    int soonArrival = (int) VacationBus.getRemainShuttleTimeToLong(BusType.getValueOf(depart), BusType.getValueOf(arrival), true);
                    int laterArrival = (int) VacationBus.getRemainShuttleTimeToLong(BusType.getValueOf(depart), BusType.getValueOf(arrival), false);
                    String soonDeparture = VacationBus.getNearShuttleTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), true);
                    String laterDeparture = VacationBus.getNearShuttleTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), false);
                    busMainView.updateShuttleBusTime(soonArrival, laterArrival);
                    busMainView.updateShuttleBusDepartInfo(soonDeparture, laterDeparture);
                } catch (ParseException e) {
                    busMainView.updateFailCityBusDepartInfo();
                }
            }
            busMainView.hideLoading();
        }
    }
