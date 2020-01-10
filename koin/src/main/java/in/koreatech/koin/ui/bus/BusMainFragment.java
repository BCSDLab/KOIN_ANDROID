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
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.helper.TimerRenewListener;
import in.koreatech.koin.data.network.interactor.CityBusRestInteractor;
import in.koreatech.koin.util.BusTimerUtil;
import in.koreatech.koin.util.TimeUtil;
import in.koreatech.koin.ui.bus.presenter.BusMainContract;
import in.koreatech.koin.ui.bus.presenter.BusMainPresenter;


/**
 * @author yunjae na
 * @since 2018.12.02
 */
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
        this.unbinder = ButterKnife.bind(this, this.view);
        this.isCreate = true;
        init();
        return this.view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.refreshBusTimerUtil != null) {
            this.refreshBusTimerUtil.setEndTime(REFRESH_TIME); // 5분마다 갱신
            this.refreshBusTimerUtil.startTimer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cityNextBusTimerUtil != null) {
            cityNextBusTimerUtil.stopTimer();
            citySoonBusTimerUtil.stopTimer();
            daesungBusNextBusTimerUtil.stopTimer();
            daesungBusSoonBusTimerUtil.stopTimer();
            this.shuttleBusNextBusTimerUtil.stopTimer();
            this.shuttleBusSoonBusTimerUtil.stopTimer();
            this.refreshBusTimerUtil.stopTimer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        departureState = 0;
        this.arrivalState = 1;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onRefresh() {
        busMainPresenter.getCityBus(departureState, this.arrivalState);
        busMainPresenter.getDaesungBus(departureState, this.arrivalState);
        busMainPresenter.getShuttleBus(departureState, this.arrivalState);
        busSwipeRefreshLayout.setRefreshing(false);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.unbinder != null)
            this.unbinder.unbind();
        if (cityNextBusTimerUtil != null) {
            cityNextBusTimerUtil.stopTimer();
            citySoonBusTimerUtil.stopTimer();
            daesungBusNextBusTimerUtil.stopTimer();
            daesungBusSoonBusTimerUtil.stopTimer();
            this.shuttleBusNextBusTimerUtil.stopTimer();
            this.shuttleBusSoonBusTimerUtil.stopTimer();
            this.refreshBusTimerUtil.stopTimer();
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
        departureState = 0;
        this.arrivalState = 1;
        busSwipeRefreshLayout.setOnRefreshListener(this);
        setPresenter(new BusMainPresenter(this, new CityBusRestInteractor()));
        busMainPresenter.getCityBus(0, 1);
        citybusSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
        citybusNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        daesungNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        daesungSoonDepartureTimeTextView.setVisibility(View.INVISIBLE);
        this.shuttleNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        this.shuttleSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
        citybusTypeTextview.setVisibility(View.INVISIBLE);
        citySoonBusTimerUtil = new BusTimerUtil(0);
        citySoonBusTimerUtil.setTimerRenewListener(this);
        cityNextBusTimerUtil = new BusTimerUtil(1);
        cityNextBusTimerUtil.setTimerRenewListener(this);
        daesungBusSoonBusTimerUtil = new BusTimerUtil(2);
        daesungBusSoonBusTimerUtil.setTimerRenewListener(this);
        daesungBusNextBusTimerUtil = new BusTimerUtil(3);
        daesungBusNextBusTimerUtil.setTimerRenewListener(this);
        this.shuttleBusSoonBusTimerUtil = new BusTimerUtil(4);
        this.shuttleBusSoonBusTimerUtil.setTimerRenewListener(this);
        this.shuttleBusNextBusTimerUtil = new BusTimerUtil(5);
        this.shuttleBusNextBusTimerUtil.setTimerRenewListener(this);
        this.refreshBusTimerUtil = new BusTimerUtil(6);
        this.refreshBusTimerUtil.setTimerRenewListener(this);
    }


    public void setDepartureText() {
        citybusDepartureTextview.setText(getResources().getStringArray(R.array.bus_place)[departureState]);
        this.shuttleDepatureTextview.setText(getResources().getStringArray(R.array.bus_place)[departureState]);
        daesungDepartureTextview.setText(getResources().getStringArray(R.array.bus_place)[departureState]);
    }

    public void setArrivalText() {
        citybusArrivalTextview.setText(getResources().getStringArray(R.array.bus_place)[this.arrivalState]);
        this.shuttleArrivalTextview.setText(getResources().getStringArray(R.array.bus_place)[this.arrivalState]);
        daesungArrivalTextview.setText(getResources().getStringArray(R.array.bus_place)[this.arrivalState]);
    }

    public void setDaesungTypeSet() {
        if (departureState == 0 && this.arrivalState == 1) {
            daesungBusTypeTextView.setVisibility(View.VISIBLE);
            daesungBusTypeTextView.setText(R.string.daesung_depature_place_university_to_yawoori);
        } else if (departureState == 1 && this.arrivalState == 0) {
            daesungBusTypeTextView.setVisibility(View.VISIBLE);
            daesungBusTypeTextView.setText(R.string.daesung_depature_place_yawoori_to_university);
        } else
            daesungBusTypeTextView.setVisibility(View.GONE);
    }

    public void setSpinner() {

        busDepartureSpinner.setSelection(departureState);
        busArrivalSpinner.setSelection(this.arrivalState);
    }

    @OnItemSelected(R.id.bus_main_fragment_bus_departure_spinner)
    public void onItemSelectedBuDepartureSpinner(Spinner spinner, int position) {
        if (position != this.arrivalState)
            departureState = position;
        else {
            this.arrivalState = departureState;
            departureState = position;
        }
        setArrivalText();
        setDepartureText();
        setDaesungTypeSet();
        setSpinner();
        if (!this.isCreate) {
            busMainPresenter.getCityBus(departureState, this.arrivalState);
            busMainPresenter.getDaesungBus(departureState, this.arrivalState);
            busMainPresenter.getShuttleBus(departureState, this.arrivalState);
        }
        this.isCreate = false;


    }

    @OnItemSelected(R.id.bus_main_fragment_bus_arrival_spinner)
    public void onItemSelectedBusArrivalSpinner(Spinner spinner, int position) {

        if (position != departureState)
            this.arrivalState = position;
        else {
            departureState = this.arrivalState;
            this.arrivalState = position;

        }
        setArrivalText();
        setDepartureText();
        setDaesungTypeSet();
        setSpinner();
        if (!this.isCreate) {
            busMainPresenter.getCityBus(departureState, this.arrivalState);
            busMainPresenter.getDaesungBus(departureState, this.arrivalState);
            busMainPresenter.getShuttleBus(departureState, this.arrivalState);

        }
        this.isCreate = false;
    }

    @Override
    public void updateShuttleBusTime(int current, int next) {
        if (current >= 0) {
            this.shuttleBusSoonBusTimerUtil.setEndTime(current);
            this.shuttleBusSoonBusTimerUtil.setTextView(this.shuttleSoonArrivalTimeTextView);
            this.shuttleBusSoonBusTimerUtil.startTimer();
        } else {
            this.shuttleBusSoonBusTimerUtil.stopTimer();
            this.shuttleSoonArrivalTimeTextView.setText(R.string.bus_no_information);
        }

        if (next >= 0) {
            this.shuttleBusNextBusTimerUtil.setEndTime(next);
            this.shuttleBusNextBusTimerUtil.setTextView(this.shuttleNextArrivalTimeTextview);
            this.shuttleBusNextBusTimerUtil.startTimer();
        } else {
            this.shuttleBusNextBusTimerUtil.stopTimer();
            this.shuttleNextArrivalTimeTextview.setText(R.string.bus_no_information);
        }

    }

    @Override
    public void updateCityBusTime(int current, int next) {
        if (current > 0) {
            citybusSoonDepartureTimeTextview.setVisibility(View.VISIBLE);
            citybusSoonDepartureTimeTextview.setText("(" + TimeUtil.getAddTimeSecond(current) + ")분 출발");
            citySoonBusTimerUtil.setEndTime(current);
            citySoonBusTimerUtil.setTextView(citybusSoonArrivalTimeTextview);
            citySoonBusTimerUtil.startTimer();

        } else {
            citybusSoonArrivalTimeTextview.setText(R.string.bus_no_information);
            citybusSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
            citySoonBusTimerUtil.stopTimer();
        }

        if (next > 0) {
            citybusNextDepartureTimeTextview.setVisibility(View.VISIBLE);
            citybusNextDepartureTimeTextview.setText("(" + TimeUtil.getAddTimeSecond(next) + ")분 출발");
            cityNextBusTimerUtil.setEndTime(next);
            cityNextBusTimerUtil.setTextView(citybusNextArrivalTimeTextView);
            cityNextBusTimerUtil.startTimer();
        } else {
            citybusNextArrivalTimeTextView.setText(R.string.bus_no_information);
            citybusNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
            cityNextBusTimerUtil.stopTimer();
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
//            citybusNextTypeTextview.setVisibility(View.INVISIBLE);
//        } else {
//            citybusNextTypeTextview.setVisibility(View.VISIBLE);
//            citybusNextTypeTextview.setText(Integer.toString(current) + "번 버스");
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
        if (this.shuttleSoonDepartureTimeTextview != null) {
            this.shuttleSoonDepartureTimeTextview.setVisibility(current.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            this.shuttleSoonDepartureTimeTextview.setText("(" + current + ")분 출발");

            this.shuttleNextDepartureTimeTextview.setVisibility(next.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            this.shuttleNextDepartureTimeTextview.setText("(" + next + ")분 출발");
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
        if (busMainPresenter != null) {
            busMainPresenter.getCityBus(departureState, this.arrivalState);
            busMainPresenter.getDaesungBus(departureState, this.arrivalState);
            busMainPresenter.getShuttleBus(departureState, this.arrivalState);
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
            busMainPresenter.getCityBus(departureState, this.arrivalState);
        else if (code == 2)
            busMainPresenter.getDaesungBus(departureState, this.arrivalState);
        else if (code == 4)
            busMainPresenter.getShuttleBus(departureState, this.arrivalState);
        else if (code == 6) {
            busMainPresenter.getCityBus(departureState, this.arrivalState);
            busMainPresenter.getDaesungBus(departureState, this.arrivalState);
            busMainPresenter.getShuttleBus(departureState, this.arrivalState);
            this.refreshBusTimerUtil.setEndTime(REFRESH_TIME);
            this.refreshBusTimerUtil.startTimer();
        }
    }

    @Override
    public void updateFailShuttleBusDepartInfo() {
        this.shuttleBusSoonBusTimerUtil.stopTimer();
        this.shuttleBusNextBusTimerUtil.stopTimer();
        this.shuttleNextArrivalTimeTextview.setText(R.string.bus_no_information);
        this.shuttleSoonArrivalTimeTextView.setText(R.string.bus_no_information);
        this.shuttleNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        this.shuttleSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateFailCityBusDepartInfo() {
        citySoonBusTimerUtil.stopTimer();
        cityNextBusTimerUtil.stopTimer();
        citybusNextArrivalTimeTextView.setText(R.string.bus_no_information);
        citybusSoonArrivalTimeTextview.setText(R.string.bus_no_information);
        citybusNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        citybusSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoading() {
        ((BusActivity) getActivity()).showProgressDialog("로딩 중");

    }

    @Override
    public void hideLoading() {
        ((BusActivity) getActivity()).hideProgressDialog();
    }
}