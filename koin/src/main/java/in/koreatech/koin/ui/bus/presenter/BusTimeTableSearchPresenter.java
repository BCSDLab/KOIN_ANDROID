package in.koreatech.koin.ui.bus.presenter;

import android.util.Log;

import java.text.ParseException;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Bus;
import in.koreatech.koin.constant.BusType;
import in.koreatech.koin.data.network.entity.RegularSemesterBus;
import in.koreatech.koin.data.network.entity.SeasonalSemesterBus;
import in.koreatech.koin.data.network.entity.Term;
import in.koreatech.koin.data.network.entity.VacationBus;
import in.koreatech.koin.data.network.interactor.TermInteractor;
import in.koreatech.koin.data.network.interactor.TermRestInteractor;
import in.koreatech.koin.util.TimeUtil;


public class BusTimeTableSearchPresenter {
    private String TAG = "BusTimeTableSearchPresenter";
    private final BusTimeTableSearchContract.View busTimeTableSearchView;
    private final TermInteractor termInteractor;


    public BusTimeTableSearchPresenter(BusTimeTableSearchContract.View busTimeTableSearchView, TermInteractor termInteractor) {
        this.busTimeTableSearchView = busTimeTableSearchView;
        this.termInteractor = termInteractor;
        this.busTimeTableSearchView.setPresenter(this);
    }

    private final ApiCallback termApiCallback = new ApiCallback() {             //방학인지 학기중인지 정보를 받아오는 api callback
        @Override
        public void onSuccess(Object object) {
            Term term = (Term) object;
            busTimeTableSearchView.updateTermInfo(term.getTerm());                   //셔틀버스의 남은 시간을 계산하여 업데이트
            busTimeTableSearchView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            busTimeTableSearchView.hideLoading();
        }
    };


    public void getDaesungBus(int depart, int arrival, String date, String time) {
        busTimeTableSearchView.showLoading();
        String busTime;
        // 0 => 한기대 1 => 야우리
        String[] times = time.split(":");
        try {
            busTime = Bus.getNearExpressTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), Integer.parseInt(times[0]), Integer.parseInt(times[1]));
            busTimeTableSearchView.updateDaesungBusTime(busTime);
        } catch (ParseException | ArrayIndexOutOfBoundsException e) {
            busTimeTableSearchView.updateFailDaesungBusDepartInfo();
        }

        busTimeTableSearchView.hideLoading();
    }

    public void getShuttleBus(int depart, int arrival, String date, String time, int term) {
        busTimeTableSearchView.showLoading();
        String busTime;
        Bus currentSemesterBus;
        // 0 : 한기대 1 : 야우리 2 : 천안역

        switch (term % 10) {
            case 0:
                currentSemesterBus = new RegularSemesterBus();
            case 1:
                currentSemesterBus = new SeasonalSemesterBus();
            default:
                currentSemesterBus = new VacationBus();
        }

        if (!time.isEmpty()) {
            String[] times = time.split(":");
            String[] dates = date.split("-");
            try {
                busTime = currentSemesterBus.getNearShuttleTimeToString(BusType.getValueOf(depart), BusType.getValueOf(arrival), Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]), Integer.parseInt(times[0]), Integer.parseInt(times[1]));
                busTimeTableSearchView.updateShuttleBusTime(busTime);
            } catch (ParseException e) {
                busTimeTableSearchView.updateFailDaesungBusDepartInfo();
            }
            busTimeTableSearchView.showBusTimeInfo();
            busTimeTableSearchView.hideLoading();
        }
    }

    /**
     * 학기 정보를 요청하는 함수
     * 학기에 따라 셔틀버스 시간표가 달라져 셔틀버스 정보를 얻어오기전에 먼저 실행되어야 한다.
     */
    public void getTermInfo() {
        termInteractor.readTerm(termApiCallback);
    }
}