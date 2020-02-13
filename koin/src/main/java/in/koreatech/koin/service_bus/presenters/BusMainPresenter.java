package in.koreatech.koin.service_bus.presenters;

import android.util.Log;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Bus;
import in.koreatech.koin.core.networks.entity.BusType;
import in.koreatech.koin.core.networks.interactors.CityBusInteractor;
import in.koreatech.koin.core.networks.responses.BusResponse;
import in.koreatech.koin.core.util.TimeUtil;
import in.koreatech.koin.core.util.TimerUtil;
import in.koreatech.koin.service_bus.contracts.BusMainContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;


public class BusMainPresenter implements BasePresenter {
    private String TAG = BusMainPresenter.class.getSimpleName();
    private final BusMainContract.View busMainView;
    private final CityBusInteractor busInteractor;


    public BusMainPresenter(BusMainContract.View busMainView, CityBusInteractor busInteractor) {
        this.busMainView = busMainView;
        this.busInteractor = busInteractor;
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Bus busResponse = (Bus) object;
            Log.e("bus",Integer.toString(busResponse.getTerm().getTerm()));
            Log.e("bus",Integer.toString(busResponse.getBusResponse().getBusNumber()));
            busMainView.updateCityBusDepartInfo(busResponse.getBusResponse().getBusNumber(), busResponse.getBusResponse().getNextBusNumber());
            busMainView.updateCityBusTime(busResponse.getBusResponse().getRemainTime(), busResponse.getBusResponse().getNextRemainTime());
            busMainView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.d(TAG, throwable.getMessage());
            busMainView.updateFailCityBusDepartInfo();
            busMainView.hideLoading();
        }
    };

    public void getCityBus(int depart, int arrival) {
        busMainView.showLoading();
        String mDepart;
        String mArrival;

        if (depart == 0)
            mDepart = "koreatech";
        else if (depart == 1)
            mDepart = "station";
        else
            mDepart = "terminal";

        if (arrival == 0)
            mArrival = "koreatech";
        else if (arrival == 1)
            mArrival = "station";
        else
            mArrival = "terminal";

        busInteractor.readCityBusList(apiCallback, mDepart, mArrival);
    }

    public void getDaesungBus(int depart, int arrival) {
        // 0 => 한기대 2 => 터미널
        busMainView.showLoading();
        try {
            int soonArrival = (int) Bus.getRemainExpressTimeToLong(BusType.getValueOf(depart), BusType.getValueOf(arrival), true);
            int laterArrival = (int) Bus.getRemainExpressTimeToLong(BusType.getValueOf(depart), BusType.getValueOf(arrival), false);
            String soonDeparture = Bus.getNearExpressTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), true);
            String laterDeparture =  Bus.getNearExpressTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), false);
            busMainView.updateDaesungBusTime(soonArrival, laterArrival);
            busMainView.updateDaesungBusDepartInfo(soonDeparture, laterDeparture);
        } catch (ParseException e) {
            busMainView.updateFailDaesungBusDepartInfo();
        }
        busMainView.hideLoading();
    }

    public void getShuttleBus(int depart, int arrival) {
        // 0 : 한기대 1 : 야우리 2 : 천안역
        busMainView.showLoading();
        try {
            int soonArrival = (int) Bus.getRemainShuttleTimeToLong(BusType.getValueOf(depart), BusType.getValueOf(arrival), true);
            int laterArrival = (int) Bus.getRemainShuttleTimeToLong(BusType.getValueOf(depart), BusType.getValueOf(arrival), false);
            String soonDeparture = Bus.getNearShuttleTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), true);
            String laterDeparture =  Bus.getNearShuttleTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), false);
            busMainView.updateShuttleBusTime(soonArrival, laterArrival);
            busMainView.updateShuttleBusDepartInfo(soonDeparture, laterDeparture);
        } catch (ParseException e) {
            busMainView.updateFailCityBusDepartInfo();
        }
        busMainView.hideLoading();
    }
}
