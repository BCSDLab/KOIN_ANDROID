package in.koreatech.koin.ui.bus.presenter;

import java.text.ParseException;

import in.koreatech.koin.data.network.entity.Bus;
import in.koreatech.koin.constant.BusType;
import in.koreatech.koin.util.TimeUtil;


public class BusTimeTableSearchPresenter {
    private String TAG = "BusTimeTableSearchPresenter";
    private final BusTimeTableSearchContract.View busTimeTableSearchView;


    public BusTimeTableSearchPresenter(BusTimeTableSearchContract.View busTimeTableSearchView) {
        this.busTimeTableSearchView = busTimeTableSearchView;
    }


    public void getDaesungBus(int depart, int arrival, String date, String time) {
        busTimeTableSearchView.showLoading();
        String busTime;
        // 0 => 한기대 1 => 야우리
        String[] times = time.split(":");
        try {
            busTime = Bus.getNearExpressTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), Integer.parseInt(times[0]), Integer.parseInt(times[1]));
            busTimeTableSearchView.updateDaesungBusTime(busTime);
        } catch (ParseException e) {
            busTimeTableSearchView.updateFailDaesungBusDepartInfo();
        }

        busTimeTableSearchView.hideLoading();
    }

    public void getShuttleBus(int depart, int arrival, String date, String time) {
        busTimeTableSearchView.showLoading();
        String busTime;
        // 0 : 한기대 1 : 야우리 2 : 천안역
        String day = "";
        // 0 => 한기대 1 => 야우리
        try {
            day = TimeUtil.getDateDay(date, "yyyy-M-dd");
        } catch (Exception e) {
            busTimeTableSearchView.updateFailDaesungBusDepartInfo();
        }

        if (!day.isEmpty() && !time.isEmpty()) {
            String[] times = time.split(":");
            String[] dates = date.split("-");
            try {
                busTime = Bus.getNearShuttleTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]), Integer.parseInt(times[0]), Integer.parseInt(times[1]));
                busTimeTableSearchView.updateShuttleBusTime(busTime);
            } catch (ParseException e) {
                busTimeTableSearchView.updateFailDaesungBusDepartInfo();
            }
            busTimeTableSearchView.showBusTimeInfo();
            busTimeTableSearchView.hideLoading();
        }
    }
}