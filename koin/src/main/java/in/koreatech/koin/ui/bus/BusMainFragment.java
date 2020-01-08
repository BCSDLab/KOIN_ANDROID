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


    private CustomProgressDialog customProgressDialog;
    private Unbinder mUnbinder;
    private boolean mIsCreate;
    private int departureState; // 0 : 한기대 1 : 야우리 2 : 천안역
    private int mArrivalState; // 0 : 한기대 1 : 야우리 2 : 천안역
    private BusMainPresenter busMainPresenter;
    /* View Component */
    private View mView;


    private BusTimerUtil cityNextBusTimerUtil;
    private BusTimerUtil citySoonBusTimerUtil;
    private BusTimerUtil daesungBusNextBusTimerUtil;
    private BusTimerUtil daesungBusSoonBusTimerUtil;
    private BusTimerUtil mShuttleBusNextBusTimerUtil;
    private BusTimerUtil mShuttleBusSoonBusTimerUtil;
    private BusTimerUtil mRefreshBusTimerUtil;

    @BindView(R.id.bus_main_swiperefreshlayout)
    SwipeRefreshLayout busSwipeRefreshLayout;

    // Spinner
    @BindView(R.id.bus_main_fragment_bus_departure_spinner)
    Spinner busDepartureSpinner;
    @BindView(R.id.bus_main_fragment_bus_arrival_spinner)
    Spinner busArrivalSpinner;


    // Shuttle Bus
    @BindView(R.id.bus_main_fragment_shuttle_departure_textview)
    TextView mShuttleDepatureTextview;
    @BindView(R.id.bus_main_fragment_shuttle_arrival_textview)
    TextView mShuttleArrivalTextview;
    @BindView(R.id.bus_main_fragment_shuttle_soon_arrival_time_textview)
    TextView mShuttleSoonArrivalTimeTextView;
    @BindView(R.id.bus_main_fragment_shuttle_next_arrival_time_textview)
    TextView mShuttleNextArrivalTimeTextview;
    @BindView(R.id.bus_main_fragment_shuttle_soon_departure_time_textview)
    TextView mShuttleSoonDepartureTimeTextview;
    @BindView(R.id.bus_main_fragment_shuttle_next_departure_time_textview)
    TextView mShuttleNextDepartureTimeTextview;

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
//    @BindView(R.id.bus_main_fragment_next_city_bus_type_textview)
//    TextView citybusNextTypeTextview;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.bus_main_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        mIsCreate = true;
        init();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mRefreshBusTimerUtil != null) {
            mRefreshBusTimerUtil.setEndTime(REFRESH_TIME); // 5분마다 갱신
            mRefreshBusTimerUtil.startTimer();
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
            mShuttleBusNextBusTimerUtil.stopTimer();
            mShuttleBusSoonBusTimerUtil.stopTimer();
            mRefreshBusTimerUtil.stopTimer();
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
        mArrivalState = 1;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onRefresh() {
        busMainPresenter.getCityBus(departureState, mArrivalState);
        busMainPresenter.getDaesungBus(departureState, mArrivalState);
        busMainPresenter.getShuttleBus(departureState, mArrivalState);
        busSwipeRefreshLayout.setRefreshing(false);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null)
            mUnbinder.unbind();
        if (cityNextBusTimerUtil != null) {
            cityNextBusTimerUtil.stopTimer();
            citySoonBusTimerUtil.stopTimer();
            daesungBusNextBusTimerUtil.stopTimer();
            daesungBusSoonBusTimerUtil.stopTimer();
            mShuttleBusNextBusTimerUtil.stopTimer();
            mShuttleBusSoonBusTimerUtil.stopTimer();
            mRefreshBusTimerUtil.stopTimer();
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
        mArrivalState = 1;
        busSwipeRefreshLayout.setOnRefreshListener(this);
        setPresenter(new BusMainPresenter(this, new CityBusRestInteractor()));
        busMainPresenter.getCityBus(0, 1);
        citybusSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
        citybusNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        daesungNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        daesungSoonDepartureTimeTextView.setVisibility(View.INVISIBLE);
        mShuttleNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        mShuttleSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
        citybusTypeTextview.setVisibility(View.INVISIBLE);
        citySoonBusTimerUtil = new BusTimerUtil(0);
        citySoonBusTimerUtil.setTimerRenewListener(this);
        cityNextBusTimerUtil = new BusTimerUtil(1);
        cityNextBusTimerUtil.setTimerRenewListener(this);
        daesungBusSoonBusTimerUtil = new BusTimerUtil(2);
        daesungBusSoonBusTimerUtil.setTimerRenewListener(this);
        daesungBusNextBusTimerUtil = new BusTimerUtil(3);
        daesungBusNextBusTimerUtil.setTimerRenewListener(this);
        mShuttleBusSoonBusTimerUtil = new BusTimerUtil(4);
        mShuttleBusSoonBusTimerUtil.setTimerRenewListener(this);
        mShuttleBusNextBusTimerUtil = new BusTimerUtil(5);
        mShuttleBusNextBusTimerUtil.setTimerRenewListener(this);
        mRefreshBusTimerUtil = new BusTimerUtil(6);
        mRefreshBusTimerUtil.setTimerRenewListener(this);
    }


    public void setDepartureText() {
        citybusDepartureTextview.setText(getResources().getStringArray(R.array.bus_place)[departureState]);
        mShuttleDepatureTextview.setText(getResources().getStringArray(R.array.bus_place)[departureState]);
        daesungDepartureTextview.setText(getResources().getStringArray(R.array.bus_place)[departureState]);
    }

    public void setArrivalText() {
        citybusArrivalTextview.setText(getResources().getStringArray(R.array.bus_place)[mArrivalState]);
        mShuttleArrivalTextview.setText(getResources().getStringArray(R.array.bus_place)[mArrivalState]);
        daesungArrivalTextview.setText(getResources().getStringArray(R.array.bus_place)[mArrivalState]);
    }

    public void setDaesungTypeSet() {
        if (departureState == 0 && mArrivalState == 1) {
            daesungBusTypeTextView.setVisibility(View.VISIBLE);
            daesungBusTypeTextView.setText(R.string.daesung_depature_place_university_to_yawoori);
        } else if (departureState == 1 && mArrivalState == 0) {
            daesungBusTypeTextView.setVisibility(View.VISIBLE);
            daesungBusTypeTextView.setText(R.string.daesung_depature_place_yawoori_to_university);
        } else
            daesungBusTypeTextView.setVisibility(View.GONE);
    }

    public void setSpinner() {

        busDepartureSpinner.setSelection(departureState);
        busArrivalSpinner.setSelection(mArrivalState);
    }

    @OnItemSelected(R.id.bus_main_fragment_bus_departure_spinner)
    public void onItemSelectedBuDepartureSpinner(Spinner spinner, int position) {
        if (position != mArrivalState)
            departureState = position;
        else {
            mArrivalState = departureState;
            departureState = position;
        }
        setArrivalText();
        setDepartureText();
        setDaesungTypeSet();
        setSpinner();
        if (!mIsCreate) {
            busMainPresenter.getCityBus(departureState, mArrivalState);
            busMainPresenter.getDaesungBus(departureState, mArrivalState);
            busMainPresenter.getShuttleBus(departureState, mArrivalState);
        }
        mIsCreate = false;


    }

    @OnItemSelected(R.id.bus_main_fragment_bus_arrival_spinner)
    public void onItemSelectedBusArrivalSpinner(Spinner spinner, int position) {

        if (position != departureState)
            mArrivalState = position;
        else {
            departureState = mArrivalState;
            mArrivalState = position;

        }
        setArrivalText();
        setDepartureText();
        setDaesungTypeSet();
        setSpinner();
        if (!mIsCreate) {
            busMainPresenter.getCityBus(departureState, mArrivalState);
            busMainPresenter.getDaesungBus(departureState, mArrivalState);
            busMainPresenter.getShuttleBus(departureState, mArrivalState);

        }
        mIsCreate = false;
    }

    @Override
    public void updateShuttleBusTime(int current, int next) {
        if (current >= 0) {
            mShuttleBusSoonBusTimerUtil.setEndTime(current);
            mShuttleBusSoonBusTimerUtil.setTextView(mShuttleSoonArrivalTimeTextView);
            mShuttleBusSoonBusTimerUtil.startTimer();
        } else {
            mShuttleBusSoonBusTimerUtil.stopTimer();
            mShuttleSoonArrivalTimeTextView.setText(R.string.bus_no_information);
        }

        if (next >= 0) {
            mShuttleBusNextBusTimerUtil.setEndTime(next);
            mShuttleBusNextBusTimerUtil.setTextView(mShuttleNextArrivalTimeTextview);
            mShuttleBusNextBusTimerUtil.startTimer();
        } else {
            mShuttleBusNextBusTimerUtil.stopTimer();
            mShuttleNextArrivalTimeTextview.setText(R.string.bus_no_information);
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
        if (mShuttleSoonDepartureTimeTextview != null) {
            mShuttleSoonDepartureTimeTextview.setVisibility(current.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            mShuttleSoonDepartureTimeTextview.setText("(" + current + ")분 출발");

            mShuttleNextDepartureTimeTextview.setVisibility(next.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            mShuttleNextDepartureTimeTextview.setText("(" + next + ")분 출발");
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
            busMainPresenter.getCityBus(departureState, mArrivalState);
            busMainPresenter.getDaesungBus(departureState, mArrivalState);
            busMainPresenter.getShuttleBus(departureState, mArrivalState);
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
            busMainPresenter.getCityBus(departureState, mArrivalState);
        else if (code == 2)
            busMainPresenter.getDaesungBus(departureState, mArrivalState);
        else if (code == 4)
            busMainPresenter.getShuttleBus(departureState, mArrivalState);
        else if (code == 6) {
            busMainPresenter.getCityBus(departureState, mArrivalState);
            busMainPresenter.getDaesungBus(departureState, mArrivalState);
            busMainPresenter.getShuttleBus(departureState, mArrivalState);
            mRefreshBusTimerUtil.setEndTime(REFRESH_TIME);
            mRefreshBusTimerUtil.startTimer();
        }
    }

    @Override
    public void updateFailShuttleBusDepartInfo() {
        mShuttleBusSoonBusTimerUtil.stopTimer();
        mShuttleBusNextBusTimerUtil.stopTimer();
        mShuttleNextArrivalTimeTextview.setText(R.string.bus_no_information);
        mShuttleSoonArrivalTimeTextView.setText(R.string.bus_no_information);
        mShuttleNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        mShuttleSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
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
//        if (customProgressDialog == null) {
//            customProgressDialog = new CustomProgressDialog(getContext(), "로딩중");
//            customProgressDialog.execute();
//        }

    }

    @Override
    public void hideLoading() {
//        if (customProgressDialog != null) {
//            customProgressDialog.cancel(true);
//            customProgressDialog = null;
//        }
    }
}