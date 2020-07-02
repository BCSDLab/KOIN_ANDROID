package in.koreatech.koin.ui.bus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.interactor.CityBusRestInteractor;
import in.koreatech.koin.data.network.interactor.TermRestInteractor;
import in.koreatech.koin.ui.bus.presenter.BusMainContract;
import in.koreatech.koin.ui.bus.presenter.BusMainPresenter;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.ui.main.MainActivity;
import in.koreatech.koin.util.TimeUtil;
import in.koreatech.koin.util.timer.CountTimer;
import in.koreatech.koin.util.timer.TimerManger;


public class BusMainFragment extends KoinBaseFragment implements BusMainContract.View, SwipeRefreshLayout.OnRefreshListener, CountTimer.OnTimerListener {
    private final String TAG = "BusMainFragment";
    public static final int REFRESH_TIME = 60000; // 1분 갱신
    public static final String CITY_NEXT_BUS = "CITY_NEXT_BUS";
    public static final String CITY_SOON_BUS = "CITY_SOON_BUS";
    public static final String DAESUNG_NEXT_BUS = "DAESUNG_NEXT_BUS";
    public static final String DAESUNG_SOON_BUS = "DAESUNG_SOON_BUS";
    public static final String SHUTTLE_NEXT_BUS = "SHUTTLE_NEXT_BUS";
    public static final String SHUTTLE_SOON_BUS = "SHUTTLE_SOON_BUS";
    public static final String REFRESH = "REFRESH";

    @BindView(R.id.bus_main_swiperefreshlayout)
    SwipeRefreshLayout busSwipeRefreshLayout;
    // Spinner
    @BindView(R.id.bus_main_fragment_bus_departure_spinner)
    Spinner busDepartureSpinner;
    @BindView(R.id.bus_main_fragment_bus_arrival_spinner)
    Spinner busArrivalSpinner;
    // Shuttle Bus
    @BindView(R.id.bus_main_fragment_shuttle_departure_textview)
    TextView shuttleDepatureTextview;
    @BindView(R.id.bus_main_fragment_shuttle_arrival_textview)
    TextView shuttleArrivalTextview;
    @BindView(R.id.bus_main_fragment_shuttle_soon_arrival_time_textview)
    TextView shuttleSoonArrivalTimeTextView;
    @BindView(R.id.bus_main_fragment_shuttle_next_arrival_time_textview)
    TextView shuttleNextArrivalTimeTextview;
    @BindView(R.id.bus_main_fragment_shuttle_soon_departure_time_textview)
    TextView shuttleSoonDepartureTimeTextview;
    @BindView(R.id.bus_main_fragment_shuttle_next_departure_time_textview)
    TextView shuttleNextDepartureTimeTextview;
    //Daesung Bus
    @BindView(R.id.bus_main_fragment_daesung_departure_textview)
    TextView daesungDepartureTextview;
    @BindView(R.id.bus_main_fragment_daesung_arrival_textview)
    TextView daesungArrivalTextview;
    @BindView(R.id.bus_main_fragment_daesung_bus_type_textview)
    TextView daesungBusTypeTextView;
    @BindView(R.id.bus_main_fragment_daesung_soon_arrival_time_textview)
    TextView daesungSoonArrivalTimeTextview;
    @BindView(R.id.bus_main_fragment_daesung_next_arrival_time_textview)
    TextView daesungNextArrivalTimeTextView;
    @BindView(R.id.bus_main_fragment_daesung_soon_departure_time_textview)
    TextView daesungSoonDepartureTimeTextView;
    @BindView(R.id.bus_main_fragment_daesung_next_departure_time_textview)
    TextView daesungNextDepartureTimeTextview;
    // City Bus
    @BindView(R.id.bus_main_fragment_citybus_departure_textview)
    TextView citybusDepartureTextview;
    @BindView(R.id.bus_main_fragment_citybus_arrival_textview)
    TextView citybusArrivalTextview;
    @BindView(R.id.bus_main_fragment_citybus_soon_arrival_time_textview)
    TextView citybusSoonArrivalTimeTextView;
    @BindView(R.id.bus_main_fragment_citybus_next_arrival_time_textview)
    TextView citybusNextArrivalTimeTextView;
    @BindView(R.id.bus_main_fragment_citybus_soon_departure_time_textview)
    TextView citybusSoonDepartureTimeTextview;
    @BindView(R.id.bus_main_fragment_citybus_next_departure_time_textview)
    TextView citybusNextDepartureTimeTextview;
    @BindView(R.id.bus_main_fragment_city_bus_type_textview)
    TextView citybusTypeTextview;
    private Unbinder unbinder;
    private int departureState; // 0 : 한기대 1 : 야우리 2 : 천안역
    private int arrivalState; // 0 : 한기대 1 : 야우리 2 : 천안역
    private BusMainPresenter busMainPresenter;
    /* View Component */
    private View view;
    private int term;
    private TimerManger timerManger;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.bus_main_fragment, container, false);
        unbinder = ButterKnife.bind(this, this.view);
        init();
        return this.view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setTimer(REFRESH, REFRESH_TIME);
        refreshAllBusTime();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timerManger != null)
            timerManger.stopAllTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.departureState = 0;
        this.arrivalState = 1;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onRefresh() {
        refreshAllBusTime();
        busSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    public void init() {
        timerManger = new TimerManger();
        busDepartureSpinner.setSelection(0);
        busArrivalSpinner.setSelection(1);
        this.departureState = 0;
        this.arrivalState = 1;
        busSwipeRefreshLayout.setOnRefreshListener(this);
        citybusSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
        citybusNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        daesungNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        daesungSoonDepartureTimeTextView.setVisibility(View.INVISIBLE);
        shuttleNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        shuttleSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
        citybusTypeTextview.setVisibility(View.INVISIBLE);
        setPresenter(new BusMainPresenter(this, new CityBusRestInteractor(), new TermRestInteractor()));
    }


    public void setDepartureText() {
        citybusDepartureTextview.setText(getResources().getStringArray(R.array.bus_place)[this.departureState]);
        shuttleDepatureTextview.setText(getResources().getStringArray(R.array.bus_place)[this.departureState]);
        daesungDepartureTextview.setText(getResources().getStringArray(R.array.bus_place)[this.departureState]);
    }

    public void setArrivalText() {
        citybusArrivalTextview.setText(getResources().getStringArray(R.array.bus_place)[this.arrivalState]);
        shuttleArrivalTextview.setText(getResources().getStringArray(R.array.bus_place)[this.arrivalState]);
        daesungArrivalTextview.setText(getResources().getStringArray(R.array.bus_place)[this.arrivalState]);
    }

    public void setDaesungTypeSet() {
        if (this.departureState == 0 && this.arrivalState == 1) {
            daesungBusTypeTextView.setVisibility(View.VISIBLE);
            daesungBusTypeTextView.setText(R.string.daesung_depature_place_university_to_yawoori);
        } else if (this.departureState == 1 && this.arrivalState == 0) {
            daesungBusTypeTextView.setVisibility(View.VISIBLE);
            daesungBusTypeTextView.setText(R.string.daesung_depature_place_yawoori_to_university);
        } else
            daesungBusTypeTextView.setVisibility(View.GONE);
    }

    public void setSpinner() {
        busDepartureSpinner.setSelection(this.departureState);
        busArrivalSpinner.setSelection(this.arrivalState);
    }

    @OnItemSelected(R.id.bus_main_fragment_bus_departure_spinner)
    public void onItemSelectedBuDepartureSpinner(Spinner spinner, int position) {
        if (position != this.arrivalState)
            this.departureState = position;
        else {
            this.arrivalState = this.departureState;
            this.departureState = position;
        }
        setArrivalText();
        setDepartureText();
        setDaesungTypeSet();
        setSpinner();
        refreshAllBusTime();
    }

    @OnItemSelected(R.id.bus_main_fragment_bus_arrival_spinner)
    public void onItemSelectedBusArrivalSpinner(Spinner spinner, int position) {
        if (position != this.departureState)
            this.arrivalState = position;
        else {
            this.departureState = this.arrivalState;
            this.arrivalState = position;

        }
        setArrivalText();
        setDepartureText();
        setDaesungTypeSet();
        setSpinner();
        refreshAllBusTime();
    }

    @Override
    public void updateShuttleBusTime(long current, long next) {
        if (current >= 0) {
            setTimer(SHUTTLE_SOON_BUS, current * 1000);
        } else {
            stopTimer(SHUTTLE_SOON_BUS);
        }

        if (next >= 0) {
            setTimer(SHUTTLE_NEXT_BUS, next * 1000);
        } else {
            stopTimer(SHUTTLE_NEXT_BUS);
        }

    }

    @Override
    public void updateCityBusTime(long current, long next) {
        if (current > 0) {
            citybusSoonDepartureTimeTextview.setVisibility(View.VISIBLE);
            citybusSoonDepartureTimeTextview.setText("(" + TimeUtil.getAddTimeSecond((int) current) + ")분 출발");
            setTimer(CITY_SOON_BUS, current);
        } else {
            stopTimer(CITY_SOON_BUS);
        }

        if (next > 0) {
            citybusNextDepartureTimeTextview.setVisibility(View.VISIBLE);
            citybusNextDepartureTimeTextview.setText("(" + TimeUtil.getAddTimeSecond((int) next) + ")분 출발");
            setTimer(CITY_NEXT_BUS, next);
        } else {
            stopTimer(CITY_NEXT_BUS);
        }
    }

    @Override
    public void updateCityBusDepartInfo(long current, long next) {
        if (current == 0) {
            citybusTypeTextview.setVisibility(View.INVISIBLE);
        } else {
            citybusTypeTextview.setVisibility(View.VISIBLE);
            citybusTypeTextview.setText(current + "번 버스");
        }
    }

    @Override
    public void updateDaesungBusTime(long current, long next) {
        if (current > 0) {
            setTimer(DAESUNG_SOON_BUS, current * 1000);
        } else {
            daesungSoonArrivalTimeTextview.setText(R.string.bus_no_information);
            stopTimer(DAESUNG_SOON_BUS);
        }

        if (next > 0) {
            setTimer(DAESUNG_NEXT_BUS, next * 1000);
        } else {
            daesungNextArrivalTimeTextView.setText(R.string.bus_no_information);
            stopTimer(DAESUNG_NEXT_BUS);
        }
    }

    @Override
    public void updateShuttleBusDepartInfo(String current, String next) {
        if (shuttleSoonDepartureTimeTextview != null) {
            shuttleSoonDepartureTimeTextview.setVisibility(current.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            shuttleSoonDepartureTimeTextview.setText("(" + current + ")분 출발");
            shuttleNextDepartureTimeTextview.setVisibility(next.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            shuttleNextDepartureTimeTextview.setText("(" + next + ")분 출발");
        }
    }


    @Override
    public void updateDaesungBusDepartInfo(String current, String next) {
        if (daesungSoonDepartureTimeTextView != null) {
            daesungSoonDepartureTimeTextView.setVisibility(current.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            daesungSoonDepartureTimeTextView.setText("(" + current + ")분 출발");
            daesungNextDepartureTimeTextview.setVisibility(next.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            daesungNextDepartureTimeTextview.setText("(" + next + ")분 출발");
        }

    }

    @Override
    public void updateFailDaesungBusDepartInfo() {
        timerManger.stopTimer(DAESUNG_SOON_BUS);
        timerManger.stopTimer(DAESUNG_NEXT_BUS);
    }

    @Override
    public void setPresenter(BusMainPresenter presenter) {
        this.busMainPresenter = presenter;
    }

    @Override
    public void updateFailShuttleBusDepartInfo() {
        stopTimer(SHUTTLE_SOON_BUS);
        stopTimer(SHUTTLE_NEXT_BUS);
    }

    @Override
    public void updateFailCityBusDepartInfo() {
        stopTimer(CITY_NEXT_BUS);
        stopTimer(CITY_SOON_BUS);
    }

    public void refreshAllBusTime() {
        if (this.busMainPresenter != null && timerManger != null) {
            timerManger.stopAllTimer();
            setTimer(REFRESH, REFRESH_TIME);
            this.busMainPresenter.getCityBus(this.departureState, this.arrivalState);
            this.busMainPresenter.getDaesungBus(this.departureState, this.arrivalState);
            this.busMainPresenter.getTermInfo();
        }
    }

    /**
     * 받아온 학기 정보로 셔틀버스의 시간을 계산하여 update하는 함수
     *
     * @param term 학기정보 10 - 1학기, 11 - 1학기 여름계절학기, 12 - 1학기 방학,  20 - 2학기, 21 - 2학기 겨울방학 , 22 - 2학기 방학
     */
    @Override
    public void updateShuttleBusInfo(int term) {
        this.term = term;
        this.busMainPresenter.getShuttleBus(this.departureState, this.arrivalState, term);
    }

    @Override
    public void showLoading() {
        ((MainActivity) getActivity()).showProgressDialog(R.string.loading);

    }

    @Override
    public void hideLoading() {
        ((MainActivity) getActivity()).hideProgressDialog();
    }

    @Override
    public void onCountEvent(String name, long millisUntilFinished) {
        if (millisUntilFinished <= 0) {
            refreshTimerByName(name);
        }
    }

    private void setTimer(String name, long millisUntilFinished) {
        if (timerManger == null) return;
        if (getTimerArrivalTextViewByName(name) != null)
            timerManger.addTimer(name, millisUntilFinished, getTimerArrivalTextViewByName(name), this::timerMillisecondFormat, this);
        else
            timerManger.addTimer(name, millisUntilFinished, this);
        timerManger.startTimer(name);
    }

    private void stopTimer(String name) {
        if (timerManger == null) return;
        timerManger.stopTimer(name);
        getTimerArrivalTextViewByName(name).setText(R.string.bus_no_information);
        getTimerDepartureTextViewByName(name).setVisibility(View.INVISIBLE);
    }


    private String timerMillisecondFormat(long millisecond) {
        String timeString = "";
        long hour = (millisecond / (1000 * 60 * 60)) % 24;
        long min = (millisecond / (1000 * 60)) % 60;
        long sec = (millisecond % 1000) % 60;
        if (hour > 0)
            timeString = String.format(Locale.KOREA, "%d시간 %d분 %d초 남음", hour, min, sec);
        else {
            if (min > 1)
                timeString = String.format(Locale.KOREA, "%d분 %d초 남음", min, sec);
            else {
                timeString = String.format("약 %d분 남음", min);
                if (min == 0) timeString = "곧 도착 예정";
            }
        }
        return timeString;
    }

    private TextView getTimerArrivalTextViewByName(String name) {
        TextView textView = null;
        switch (name) {
            case SHUTTLE_NEXT_BUS:
                textView = shuttleNextArrivalTimeTextview;
                break;
            case SHUTTLE_SOON_BUS:
                textView = shuttleSoonArrivalTimeTextView;
                break;
            case DAESUNG_NEXT_BUS:
                textView = daesungNextArrivalTimeTextView;
                break;
            case DAESUNG_SOON_BUS:
                textView = daesungSoonArrivalTimeTextview;
                break;
            case CITY_NEXT_BUS:
                textView = citybusNextArrivalTimeTextView;
                break;
            case CITY_SOON_BUS:
                textView = citybusSoonArrivalTimeTextView;
                break;
        }
        return textView;
    }


    private TextView getTimerDepartureTextViewByName(String name) {
        TextView textView = null;
        switch (name) {
            case SHUTTLE_NEXT_BUS:
                textView = shuttleNextDepartureTimeTextview;
                break;
            case SHUTTLE_SOON_BUS:
                textView = shuttleSoonDepartureTimeTextview;
                break;
            case DAESUNG_NEXT_BUS:
                textView = daesungNextDepartureTimeTextview;
                break;
            case DAESUNG_SOON_BUS:
                textView = daesungSoonDepartureTimeTextView;
                break;
            case CITY_NEXT_BUS:
                textView = citybusNextDepartureTimeTextview;
                break;
            case CITY_SOON_BUS:
                textView = citybusSoonDepartureTimeTextview;
                break;
        }
        return textView;
    }

    private void refreshTimerByName(String name) {
        switch (name) {
            case SHUTTLE_SOON_BUS:
                this.busMainPresenter.getShuttleBus(this.departureState, this.arrivalState, term);
                break;
            case DAESUNG_SOON_BUS:
                this.busMainPresenter.getDaesungBus(this.departureState, this.arrivalState);
                break;
            case CITY_SOON_BUS:
                this.busMainPresenter.getCityBus(this.departureState, this.arrivalState);
                break;
            case REFRESH:
                refreshAllBusTime();
                setTimer(REFRESH, REFRESH_TIME);
                break;
        }
    }
}