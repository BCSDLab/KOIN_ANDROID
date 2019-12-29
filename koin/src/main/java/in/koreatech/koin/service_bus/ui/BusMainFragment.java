package in.koreatech.koin.service_bus.ui;

import android.os.Bundle;
import android.util.Log;
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
import in.koreatech.koin.core.asynctasks.GenerateProgressTask;
import in.koreatech.koin.core.helpers.TimerRenewListener;
import in.koreatech.koin.core.networks.interactors.CityBusRestInteractor;
import in.koreatech.koin.core.util.BusTimerUtil;
import in.koreatech.koin.core.util.TimeUtil;
import in.koreatech.koin.service_bus.contracts.BusMainContract;
import in.koreatech.koin.service_bus.presenters.BusMainPresenter;


/**
 * @author yunjae na
 * @since 2018.12.02
 */
public class BusMainFragment extends BusBaseFragment implements BusMainContract.View, SwipeRefreshLayout.OnRefreshListener, TimerRenewListener {
    private final String TAG = BusMainFragment.class.getSimpleName();
    public static final int REFRESH_TIME = 60; // 1분 갱신


    private GenerateProgressTask generateProgressTask;
    private Unbinder mUnbinder;
    private boolean mIsCreate;
    private int mDepartureState; // 0 : 한기대 1 : 야우리 2 : 천안역
    private int mArrivalState; // 0 : 한기대 1 : 야우리 2 : 천안역
    private BusMainPresenter mBusMainPresenter;
    /* View Component */
    private View mView;


    private BusTimerUtil mCityNextBusTimerUtil;
    private BusTimerUtil mCitySoonBusTimerUtil;
    private BusTimerUtil mDaesungBusNextBusTimerUtil;
    private BusTimerUtil mDaesungBusSoonBusTimerUtil;
    private BusTimerUtil mShuttleBusNextBusTimerUtil;
    private BusTimerUtil mShuttleBusSoonBusTimerUtil;
    private BusTimerUtil mRefreshBusTimerUtil;

    @BindView(R.id.bus_main_swiperefreshlayout)
    SwipeRefreshLayout mBusSwipeRefreshLayout;

    // Spinner
    @BindView(R.id.bus_main_fragment_bus_departure_spinner)
    Spinner mBusDepartureSpinner;
    @BindView(R.id.bus_main_fragment_bus_arrival_spinner)
    Spinner mBusArrivalSpinner;


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
    TextView mDaesungDepartureTextview;
    @BindView(R.id.bus_main_fragment_daesung_arrival_textview)
    TextView mDaesungArrivalTextview;
    @BindView(R.id.bus_main_fragment_daesung_bus_type_textview)
    TextView mDaesungBusTypeTextView;
    @BindView(R.id.bus_main_fragment_daesung_soon_arrival_time_textview)
    TextView mDaesungSoonArrivalTimeTextview;
    @BindView(R.id.bus_main_fragment_daesung_next_arrival_time_textview)
    TextView mDaesungNextArrivalTimeTextView;
    @BindView(R.id.bus_main_fragment_daesung_soon_departure_time_textview)
    TextView mDaesungSoonDepartureTimeTextView;
    @BindView(R.id.bus_main_fragment_daesung_next_departure_time_textview)
    TextView mDaesungNextDepartureTimeTextview;

    // City Bus
    @BindView(R.id.bus_main_fragment_citybus_departure_textview)
    TextView mCitybusDepartureTextview;
    @BindView(R.id.bus_main_fragment_citybus_arrival_textview)
    TextView mCitybusArrivalTextview;
    @BindView(R.id.bus_main_fragment_citybus_soon_arrival_time_textview)
    TextView mCitybusSoonArrivalTimeTextview;
    @BindView(R.id.bus_main_fragment_citybus_next_arrival_time_textview)
    TextView mCitybusNextArrivalTimeTextView;
    @BindView(R.id.bus_main_fragment_citybus_soon_departure_time_textview)
    TextView mCitybusSoonDepartureTimeTextview;
    @BindView(R.id.bus_main_fragment_citybus_next_departure_time_textview)
    TextView mCitybusNextDepartureTimeTextview;
    @BindView(R.id.bus_main_fragment_city_bus_type_textview)
    TextView mCitybusTypeTextview;
//    @BindView(R.id.bus_main_fragment_next_city_bus_type_textview)
//    TextView mCitybusNextTypeTextview;


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
        if (mCityNextBusTimerUtil != null) {
            mCityNextBusTimerUtil.stopTimer();
            mCitySoonBusTimerUtil.stopTimer();
            mDaesungBusNextBusTimerUtil.stopTimer();
            mDaesungBusSoonBusTimerUtil.stopTimer();
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
        mDepartureState = 0;
        mArrivalState = 1;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onRefresh() {
        mBusMainPresenter.getCityBus(mDepartureState, mArrivalState);
        mBusMainPresenter.getDaesungBus(mDepartureState, mArrivalState);
        mBusMainPresenter.getShuttleBus(mDepartureState, mArrivalState);
        mBusSwipeRefreshLayout.setRefreshing(false);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null)
            mUnbinder.unbind();
        if (mCityNextBusTimerUtil != null) {
            mCityNextBusTimerUtil.stopTimer();
            mCitySoonBusTimerUtil.stopTimer();
            mDaesungBusNextBusTimerUtil.stopTimer();
            mDaesungBusSoonBusTimerUtil.stopTimer();
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
        mBusDepartureSpinner.setSelection(0);
        mBusArrivalSpinner.setSelection(1);
        mDepartureState = 0;
        mArrivalState = 1;
        mBusSwipeRefreshLayout.setOnRefreshListener(this);
        setPresenter(new BusMainPresenter(this, new CityBusRestInteractor()));
        mBusMainPresenter.getCityBus(0, 1);
        mCitybusSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
        mCitybusNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        mDaesungNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        mDaesungSoonDepartureTimeTextView.setVisibility(View.INVISIBLE);
        mShuttleNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        mShuttleSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
        mCitybusTypeTextview.setVisibility(View.INVISIBLE);
        mCitySoonBusTimerUtil = new BusTimerUtil(0);
        mCitySoonBusTimerUtil.setTimerRenewListener(this);
        mCityNextBusTimerUtil = new BusTimerUtil(1);
        mCityNextBusTimerUtil.setTimerRenewListener(this);
        mDaesungBusSoonBusTimerUtil = new BusTimerUtil(2);
        mDaesungBusSoonBusTimerUtil.setTimerRenewListener(this);
        mDaesungBusNextBusTimerUtil = new BusTimerUtil(3);
        mDaesungBusNextBusTimerUtil.setTimerRenewListener(this);
        mShuttleBusSoonBusTimerUtil = new BusTimerUtil(4);
        mShuttleBusSoonBusTimerUtil.setTimerRenewListener(this);
        mShuttleBusNextBusTimerUtil = new BusTimerUtil(5);
        mShuttleBusNextBusTimerUtil.setTimerRenewListener(this);
        mRefreshBusTimerUtil = new BusTimerUtil(6);
        mRefreshBusTimerUtil.setTimerRenewListener(this);
    }


    public void setDepartureText() {
        mCitybusDepartureTextview.setText(getResources().getStringArray(R.array.bus_place)[mDepartureState]);
        mShuttleDepatureTextview.setText(getResources().getStringArray(R.array.bus_place)[mDepartureState]);
        mDaesungDepartureTextview.setText(getResources().getStringArray(R.array.bus_place)[mDepartureState]);
    }

    public void setArrivalText() {
        mCitybusArrivalTextview.setText(getResources().getStringArray(R.array.bus_place)[mArrivalState]);
        mShuttleArrivalTextview.setText(getResources().getStringArray(R.array.bus_place)[mArrivalState]);
        mDaesungArrivalTextview.setText(getResources().getStringArray(R.array.bus_place)[mArrivalState]);
    }

    public void setDaesungTypeSet() {
        if (mDepartureState == 0 && mArrivalState == 1) {
            mDaesungBusTypeTextView.setVisibility(View.VISIBLE);
            mDaesungBusTypeTextView.setText(R.string.daesung_depature_place_university_to_yawoori);
        } else if (mDepartureState == 1 && mArrivalState == 0) {
            mDaesungBusTypeTextView.setVisibility(View.VISIBLE);
            mDaesungBusTypeTextView.setText(R.string.daesung_depature_place_yawoori_to_university);
        } else
            mDaesungBusTypeTextView.setVisibility(View.GONE);
    }

    public void setSpinner() {

        mBusDepartureSpinner.setSelection(mDepartureState);
        mBusArrivalSpinner.setSelection(mArrivalState);
    }

    @OnItemSelected(R.id.bus_main_fragment_bus_departure_spinner)
    public void onItemSelectedBuDepartureSpinner(Spinner spinner, int position) {
        if (position != mArrivalState)
            mDepartureState = position;
        else {
            mArrivalState = mDepartureState;
            mDepartureState = position;
        }
        setArrivalText();
        setDepartureText();
        setDaesungTypeSet();
        setSpinner();
        if (!mIsCreate) {
            mBusMainPresenter.getCityBus(mDepartureState, mArrivalState);
            mBusMainPresenter.getDaesungBus(mDepartureState, mArrivalState);
            mBusMainPresenter.getShuttleBus(mDepartureState, mArrivalState);
        }
        mIsCreate = false;


    }

    @OnItemSelected(R.id.bus_main_fragment_bus_arrival_spinner)
    public void onItemSelectedBusArrivalSpinner(Spinner spinner, int position) {

        if (position != mDepartureState)
            mArrivalState = position;
        else {
            mDepartureState = mArrivalState;
            mArrivalState = position;

        }
        setArrivalText();
        setDepartureText();
        setDaesungTypeSet();
        setSpinner();
        if (!mIsCreate) {
            mBusMainPresenter.getCityBus(mDepartureState, mArrivalState);
            mBusMainPresenter.getDaesungBus(mDepartureState, mArrivalState);
            mBusMainPresenter.getShuttleBus(mDepartureState, mArrivalState);

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
            mCitybusSoonDepartureTimeTextview.setVisibility(View.VISIBLE);
            mCitybusSoonDepartureTimeTextview.setText("(" + TimeUtil.getAddTimeSecond(current) + ")분 출발");
            mCitySoonBusTimerUtil.setEndTime(current);
            mCitySoonBusTimerUtil.setTextView(mCitybusSoonArrivalTimeTextview);
            mCitySoonBusTimerUtil.startTimer();

        } else {
            mCitybusSoonArrivalTimeTextview.setText(R.string.bus_no_information);
            mCitybusSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
            mCitySoonBusTimerUtil.stopTimer();
        }

        if (next > 0) {
            mCitybusNextDepartureTimeTextview.setVisibility(View.VISIBLE);
            mCitybusNextDepartureTimeTextview.setText("(" + TimeUtil.getAddTimeSecond(next) + ")분 출발");
            mCityNextBusTimerUtil.setEndTime(next);
            mCityNextBusTimerUtil.setTextView(mCitybusNextArrivalTimeTextView);
            mCityNextBusTimerUtil.startTimer();
        } else {
            mCitybusNextArrivalTimeTextView.setText(R.string.bus_no_information);
            mCitybusNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
            mCityNextBusTimerUtil.stopTimer();
        }
    }

    @Override
    public void updateCityBusDepartInfo(int current, int next) {
        if (current == 0) {
            mCitybusTypeTextview.setVisibility(View.INVISIBLE);
        } else {
            mCitybusTypeTextview.setVisibility(View.VISIBLE);
            mCitybusTypeTextview.setText(Integer.toString(current) + "번 버스");
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
            mDaesungBusSoonBusTimerUtil.setEndTime(current);
            mDaesungBusSoonBusTimerUtil.setTextView(mDaesungSoonArrivalTimeTextview);
            mDaesungBusSoonBusTimerUtil.startTimer();
        } else {
            mDaesungBusSoonBusTimerUtil.stopTimer();
            mDaesungSoonArrivalTimeTextview.setText(R.string.bus_no_information);
        }

        if (next > 0) {
            mDaesungBusNextBusTimerUtil.setEndTime(next);
            mDaesungBusNextBusTimerUtil.setTextView(mDaesungNextArrivalTimeTextView);
            mDaesungBusNextBusTimerUtil.startTimer();
        } else {
            mDaesungBusNextBusTimerUtil.stopTimer();
            mDaesungNextArrivalTimeTextView.setText(R.string.bus_no_information);
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
        if (mDaesungSoonDepartureTimeTextView != null) {
            mDaesungSoonDepartureTimeTextView.setVisibility(current.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            mDaesungSoonDepartureTimeTextView.setText("(" + current + ")분 출발");

            mDaesungNextDepartureTimeTextview.setVisibility(next.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            mDaesungNextDepartureTimeTextview.setText("(" + next + ")분 출발");
        }

    }

    @Override
    public void updateFailDaesungBusDepartInfo() {
        mDaesungBusSoonBusTimerUtil.stopTimer();
        mDaesungBusNextBusTimerUtil.stopTimer();
        mDaesungNextArrivalTimeTextView.setText(R.string.bus_no_information);
        mDaesungSoonArrivalTimeTextview.setText(R.string.bus_no_information);
        mDaesungNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        mDaesungSoonDepartureTimeTextView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mBusMainPresenter != null) {
            mBusMainPresenter.getCityBus(mDepartureState, mArrivalState);
            mBusMainPresenter.getDaesungBus(mDepartureState, mArrivalState);
            mBusMainPresenter.getShuttleBus(mDepartureState, mArrivalState);
            mBusSwipeRefreshLayout.setRefreshing(false);

        }

    }

    @Override
    public void setPresenter(BusMainPresenter presenter) {
        this.mBusMainPresenter = presenter;
    }

    @Override
    public void refreshTimer(int code) {
        if (code == 0)
            mBusMainPresenter.getCityBus(mDepartureState, mArrivalState);
        else if (code == 2)
            mBusMainPresenter.getDaesungBus(mDepartureState, mArrivalState);
        else if (code == 4)
            mBusMainPresenter.getShuttleBus(mDepartureState, mArrivalState);
        else if (code == 6) {
            mBusMainPresenter.getCityBus(mDepartureState, mArrivalState);
            mBusMainPresenter.getDaesungBus(mDepartureState, mArrivalState);
            mBusMainPresenter.getShuttleBus(mDepartureState, mArrivalState);
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
        mCitySoonBusTimerUtil.stopTimer();
        mCityNextBusTimerUtil.stopTimer();
        mCitybusNextArrivalTimeTextView.setText(R.string.bus_no_information);
        mCitybusSoonArrivalTimeTextview.setText(R.string.bus_no_information);
        mCitybusNextDepartureTimeTextview.setVisibility(View.INVISIBLE);
        mCitybusSoonDepartureTimeTextview.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoading() {
//        if (generateProgressTask == null) {
//            generateProgressTask = new GenerateProgressTask(getContext(), "로딩중");
//            generateProgressTask.execute();
//        }

    }

    @Override
    public void hideLoading() {
//        if (generateProgressTask != null) {
//            generateProgressTask.cancel(true);
//            generateProgressTask = null;
//        }
    }
}