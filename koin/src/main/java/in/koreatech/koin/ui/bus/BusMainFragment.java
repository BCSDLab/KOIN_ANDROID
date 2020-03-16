package in.koreatech.koin.ui.bus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.interactor.CityBusRestInteractor;
import in.koreatech.koin.data.network.interactor.TermRestInteractor;
import in.koreatech.koin.util.BusTimerUtil;
import in.koreatech.koin.util.TimeUtil;
import in.koreatech.koin.ui.bus.presenter.BusMainContract;
import in.koreatech.koin.ui.bus.presenter.BusMainPresenter;


public class BusMainFragment extends BusBaseFragment implements BusMainContract.View, SwipeRefreshLayout.OnRefreshListener, TimerRenewListener {
    private final String TAG = "BusMainFragment";
    public static final int REFRESH_TIME = 60; // 1분 갱신
    private Unbinder unbinder;
    private boolean isCreate;
    private int departureState; // 0 : 한기대 1 : 야우리 2 : 천안역
    private int arrivalState; // 0 : 한기대 1 : 야우리 2 : 천안역
    private BusMainPresenter busMainPresenter;
    /* View Component */
    private View view;
    private boolean isVacation;

    private BusTimerUtil cityNextBusTimerUtil;
    private BusTimerUtil citySoonBusTimerUtil;
    private BusTimerUtil daesungBusNextBusTimerUtil;
    private BusTimerUtil daesungBusSoonBusTimerUtil;
    private BusTimerUtil shuttleBusNextBusTimerUtil;
    private BusTimerUtil shuttleBusSoonBusTimerUtil;
    private BusTimerUtil refreshBusTimerUtil;

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
    TextView citybusSoonArrivalTimeTextview;
    @BindView(R.id.bus_main_fragment_citybus_next_arrival_time_textview)
    TextView citybusNextArrivalTimeTextView;
    @BindView(R.id.bus_main_fragment_citybus_soon_departure_time_textview)
    TextView citybusSoonDepartureTimeTextview;
    @BindView(R.id.bus_main_fragment_citybus_next_departure_time_textview)
    TextView citybusNextDepartureTimeTextview;
    @BindView(R.id.bus_main_fragment_city_bus_type_textview)
    TextView citybusTypeTextview;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.bus_main_fragment, container, false);
        unbinder = ButterKnife.bind(this, this.view);
        this.isCreate = true;
        init();
        return this.view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (refreshBusTimerUtil != null) {
            refreshBusTimerUtil.setEndTime(REFRESH_TIME); // 5분마다 갱신
            refreshBusTimerUtil.startTimer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.cityNextBusTimerUtil != null) {
            this.cityNextBusTimerUtil.stopTimer();
            this.citySoonBusTimerUtil.stopTimer();
            daesungBusNextBusTimerUtil.stopTimer();
            daesungBusSoonBusTimerUtil.stopTimer();
            shuttleBusNextBusTimerUtil.stopTimer();
            shuttleBusSoonBusTimerUtil.stopTimer();
            refreshBusTimerUtil.stopTimer();
        }
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
        this.busMainPresenter.getCityBus(this.departureState, this.arrivalState);
        this.busMainPresenter.getDaesungBus(this.departureState, this.arrivalState);
        this.busMainPresenter.getTermInfo();
        busSwipeRefreshLayout.setRefreshing(false);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
        if (this.cityNextBusTimerUtil != null) {
            this.cityNextBusTimerUtil.stopTimer();
            this.citySoonBusTimerUtil.stopTimer();
            daesungBusNextBusTimerUtil.stopTimer();
            daesungBusSoonBusTimerUtil.stopTimer();
            shuttleBusNextBusTimerUtil.stopTimer();
            shuttleBusSoonBusTimerUtil.stopTimer();
            refreshBusTimerUtil.stopTimer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void init() {
        busDepartureSpinner.setSelection(0);
        busArrivalSpinner.setSelection(1);
        this.departureState = 0;
        this.arrivalState = 1;
        busSwipeRefreshLayout.setOnRefreshListener(this);
        setPresenter(new BusMainPresenter(this, new CityBusRestInteractor(), new TermRestInteractor()));
        this.busMainPresenter.getCityBus(0, 1);
        citybusSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
        citybusNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        daesungNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        daesungSoonDepartureTimeTextView.setVisibility(View.INVISIBLE);
        shuttleNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        shuttleSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
        citybusTypeTextview.setVisibility(View.INVISIBLE);
        this.citySoonBusTimerUtil = new BusTimerUtil(0);
        this.citySoonBusTimerUtil.setTimerRenewListener(this);
        this.cityNextBusTimerUtil = new BusTimerUtil(1);
        this.cityNextBusTimerUtil.setTimerRenewListener(this);
        daesungBusSoonBusTimerUtil = new BusTimerUtil(2);
        daesungBusSoonBusTimerUtil.setTimerRenewListener(this);
        daesungBusNextBusTimerUtil = new BusTimerUtil(3);
        daesungBusNextBusTimerUtil.setTimerRenewListener(this);
        shuttleBusSoonBusTimerUtil = new BusTimerUtil(4);
        shuttleBusSoonBusTimerUtil.setTimerRenewListener(this);
        shuttleBusNextBusTimerUtil = new BusTimerUtil(5);
        shuttleBusNextBusTimerUtil.setTimerRenewListener(this);
        refreshBusTimerUtil = new BusTimerUtil(6);
        refreshBusTimerUtil.setTimerRenewListener(this);
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
        if (!this.isCreate) {
            this.busMainPresenter.getCityBus(this.departureState, this.arrivalState);
            this.busMainPresenter.getDaesungBus(this.departureState, this.arrivalState);
            this.busMainPresenter.getTermInfo();
        }
        this.isCreate = false;


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
        if (!this.isCreate) {
            this.busMainPresenter.getCityBus(this.departureState, this.arrivalState);
            this.busMainPresenter.getDaesungBus(this.departureState, this.arrivalState);
            this.busMainPresenter.getTermInfo();

        }
        this.isCreate = false;
    }

    @Override
    public void updateShuttleBusTime(int current, int next) {
        if (current >= 0) {
            shuttleBusSoonBusTimerUtil.setEndTime(current);
            shuttleBusSoonBusTimerUtil.setTextView(shuttleSoonArrivalTimeTextView);
            shuttleBusSoonBusTimerUtil.startTimer();
        } else {
            shuttleBusSoonBusTimerUtil.stopTimer();
            shuttleSoonArrivalTimeTextView.setText(R.string.bus_no_information);
        }

        if (next >= 0) {
            shuttleBusNextBusTimerUtil.setEndTime(next);
            shuttleBusNextBusTimerUtil.setTextView(shuttleNextArrivalTimeTextview);
            shuttleBusNextBusTimerUtil.startTimer();
        } else {
            shuttleBusNextBusTimerUtil.stopTimer();
            shuttleNextArrivalTimeTextview.setText(R.string.bus_no_information);
        }

    }

    @Override
    public void updateCityBusTime(int current, int next) {
        if (current > 0) {
            citybusSoonDepartureTimeTextview.setVisibility(View.VISIBLE);
            citybusSoonDepartureTimeTextview.setText("(" + TimeUtil.getAddTimeSecond(current) + ")분 출발");
            this.citySoonBusTimerUtil.setEndTime(current);
            this.citySoonBusTimerUtil.setTextView(citybusSoonArrivalTimeTextview);
            this.citySoonBusTimerUtil.startTimer();

        } else {
            citybusSoonArrivalTimeTextview.setText(R.string.bus_no_information);
            citybusSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
            this.citySoonBusTimerUtil.stopTimer();
        }

        if (next > 0) {
            citybusNextDepartureTimeTextview.setVisibility(View.VISIBLE);
            citybusNextDepartureTimeTextview.setText("(" + TimeUtil.getAddTimeSecond(next) + ")분 출발");
            this.cityNextBusTimerUtil.setEndTime(next);
            this.cityNextBusTimerUtil.setTextView(citybusNextArrivalTimeTextView);
            this.cityNextBusTimerUtil.startTimer();
        } else {
            citybusNextArrivalTimeTextView.setText(R.string.bus_no_information);
            citybusNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
            this.cityNextBusTimerUtil.stopTimer();
        }
    }

    @Override
    public void updateCityBusDepartInfo(int current, int next) {
        if (current == 0) {
            citybusTypeTextview.setVisibility(View.INVISIBLE);
        } else {
            citybusTypeTextview.setVisibility(View.VISIBLE);
            citybusTypeTextview.setText(Integer.toString(current) + "번 버스");
        }

//        if (next == 0) {
//            mCitybusNextTypeTextview.setVisibility(View.INVISIBLE);
//        } else {
//            mCitybusNextTypeTextview.setVisibility(View.VISIBLE);
//            mCitybusNextTypeTextview.setText(Integer.toString(current) + "번 버스");
//        }
    }

    @Override
    public void updateDaesungBusTime(int current, int next) {
        if (current > 0) {
            daesungBusSoonBusTimerUtil.setEndTime(current);
            daesungBusSoonBusTimerUtil.setTextView(daesungSoonArrivalTimeTextview);
            daesungBusSoonBusTimerUtil.startTimer();
        } else {
            daesungBusSoonBusTimerUtil.stopTimer();
            daesungSoonArrivalTimeTextview.setText(R.string.bus_no_information);
        }

        if (next > 0) {
            daesungBusNextBusTimerUtil.setEndTime(next);
            daesungBusNextBusTimerUtil.setTextView(daesungNextArrivalTimeTextView);
            daesungBusNextBusTimerUtil.startTimer();
        } else {
            daesungBusNextBusTimerUtil.stopTimer();
            daesungNextArrivalTimeTextView.setText(R.string.bus_no_information);
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
        daesungBusSoonBusTimerUtil.stopTimer();
        daesungBusNextBusTimerUtil.stopTimer();
        daesungNextArrivalTimeTextView.setText(R.string.bus_no_information);
        daesungSoonArrivalTimeTextview.setText(R.string.bus_no_information);
        daesungNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        daesungSoonDepartureTimeTextView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (this.busMainPresenter != null) {
            this.busMainPresenter.getCityBus(this.departureState, this.arrivalState);
            this.busMainPresenter.getDaesungBus(this.departureState, this.arrivalState);
            this.busMainPresenter.getTermInfo();
            busSwipeRefreshLayout.setRefreshing(false);

        }

    }

    @Override
    public void setPresenter(BusMainPresenter presenter) {
        this.busMainPresenter = presenter;
    }

    @Override
    public void refreshTimer(int code) {
        if (code == 0)
            this.busMainPresenter.getCityBus(this.departureState, this.arrivalState);
        else if (code == 2)
            this.busMainPresenter.getDaesungBus(this.departureState, this.arrivalState);
        else if (code == 4)
            this.busMainPresenter.getTermInfo();
        else if (code == 6) {
            this.busMainPresenter.getCityBus(this.departureState, this.arrivalState);
            this.busMainPresenter.getDaesungBus(this.departureState, this.arrivalState);
            this.busMainPresenter.getTermInfo();
            refreshBusTimerUtil.setEndTime(REFRESH_TIME);
            refreshBusTimerUtil.startTimer();
        }
    }

    @Override
    public void updateFailShuttleBusDepartInfo() {
        shuttleBusSoonBusTimerUtil.stopTimer();
        shuttleBusNextBusTimerUtil.stopTimer();
        shuttleNextArrivalTimeTextview.setText(R.string.bus_no_information);
        shuttleSoonArrivalTimeTextView.setText(R.string.bus_no_information);
        shuttleNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        shuttleSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateFailCityBusDepartInfo() {
        this.citySoonBusTimerUtil.stopTimer();
        this.cityNextBusTimerUtil.stopTimer();
        citybusNextArrivalTimeTextView.setText(R.string.bus_no_information);
        citybusSoonArrivalTimeTextview.setText(R.string.bus_no_information);
        citybusNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        citybusSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
    }

    /**
     * 받아온 학기 정보로 셔틀버스의 시간을 계산하여 update하는 함수
     *
     * @param term 학기정보 10 - 1학기, 11 - 1학기 여름방학, 20 - 2학기, 21 - 2학기 겨울방학
     */
    @Override
    public void updateShuttleBusInfo(int term) {
        if (term == 10 || term == 20)                                                //학기중
            isVacation = false;
        else                                                                        //방학중
            isVacation = true;
        this.busMainPresenter.getShuttleBus(this.departureState, this.arrivalState, isVacation);
    }

    @Override
    public void showLoading() {
        ((BusActivity) getActivity()).showProgressDialog(R.string.loading);

    }

    @Override
    public void hideLoading() {
        ((BusActivity) getActivity()).hideProgressDialog();
    }
}