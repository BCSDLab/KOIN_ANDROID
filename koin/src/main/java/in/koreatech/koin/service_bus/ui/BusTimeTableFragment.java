package in.koreatech.koin.service_bus.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import in.koreatech.koin.R;
import in.koreatech.koin.core.networks.interactors.TermRestInteractor;
import in.koreatech.koin.service_bus.contracts.BusTimeTableContract;
import in.koreatech.koin.service_bus.presenters.BusMainPresenter;
import in.koreatech.koin.service_bus.presenters.BusTimeTablePresenter;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableCheonanShuttleFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableCheonanShuttleStartEndDojungStationlFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableCheonanShuttleStartEndExpressTrainlFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableCheonanShuttleStartEndShinbanddonglFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableCheonanShuttleStartEndTerminalFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableCheonanShuttleStartEndTrainStationFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableChungjuShuttleFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableChungjuShuttleStartEndBunpyeongdongFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableChungjuShuttleStartEndGymFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableChungjuShuttleStartEndShinnamdongFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableChungjuShuttleStartEndYongamFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableCityBusFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableDaejeonShuttleFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableSeoulShuttleFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableUnitoYawooriDaesungFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.BusTimeTableYawooritoUniDaesungFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.seasonbustimetable.BusTimeTableSeasonChungjuShuttleGymEveningFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.seasonbustimetable.BusTimeTableSeasonChungjuShuttleGymMorningFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.seasonbustimetable.BusTimeTableSeasonChungjuShuttleYongamEveningFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.seasonbustimetable.BusTimeTableSeasonChungjuShuttleYongamMorningFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.seasonbustimetable.BusTimeTableSeasonShuttleStationFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.seasonbustimetable.BusTimeTableSeasonShuttleTerminalFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.seasonbustimetable.BusTimeTableVacaionCheonanShuttleFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.seasonbustimetable.BusTimeTableVacationChungjuShuttleYongamEveningFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.seasonbustimetable.BusTimeTableVacationChungjuShuttleYongamMorningFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.seasonbustimetable.BusTimeTableVacationSeoulShuttleFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.seasonbustimetable.BusTimeTableVacationShuttleDojungFragment;
import in.koreatech.koin.service_bus.ui.bustimetable.seasonbustimetable.BusTimeTableVacationShuttleFragment;


/**
 * @author yunjae na
 * @since 2018.12.02
 */
public class BusTimeTableFragment extends BusBaseFragment implements BusTimeTableContract.View {
    private final String TAG = BusTimeTableFragment.class.getSimpleName();


    private Unbinder mUnbinder;
    private FragmentManager mFragmentManger;
    private BusTimeTablePresenter busTimeTablePresenter;
    private boolean isVacation;

    @BindView(R.id.bus_timetable_fragment_spinner)
    Spinner mBusTimetableShuttleBusSpinner;
    @BindView(R.id.bus_timetable_bustype_shuttle)
    AppCompatButton mBusTimetableTypeShuttle;
    @BindView(R.id.bus_timetable_bustype_daesung)
    AppCompatButton mBusTimetableTypeDaesung;
    @BindView(R.id.bus_timetable_bustype_city)
    AppCompatButton mBusTimetableTypeCity;
    @BindView(R.id.bus_timetable_fragment_cheonan_start_endspinner)
    Spinner mBusTimetableCheonanStartEndSpinner;
    @BindView(R.id.bus_timetable_fragment_chungju_spinner)
    Spinner mBusTimetableChungjuSpinner;
    @BindView(R.id.bus_timetable_fragment_daesung_spinner)
    Spinner mBusTimetableDaesungSpinner;

    /* View Component */
    private View mView;

    public View getView() {
        return mView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.bus_timetable_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        init();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @OnClick(R.id.bus_timetable_bustype_shuttle)
    public void onClickBustypeShuttle() {
        FragmentTransaction fragmentTransaction = mFragmentManger.beginTransaction();
        fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableCheonanShuttleStartEndTrainStationFragment());
        mBusTimetableCheonanStartEndSpinner.setVisibility(View.VISIBLE);
        mBusTimetableDaesungSpinner.setVisibility(View.GONE);
        mBusTimetableChungjuSpinner.setVisibility(View.GONE);
        mBusTimetableShuttleBusSpinner.setVisibility(View.VISIBLE);
        mBusTimetableCheonanStartEndSpinner.setSelection(0);
        mBusTimetableShuttleBusSpinner.setSelection(0);

        fragmentTransaction.commit();
    }

    @OnClick(R.id.bus_timetable_bustype_daesung)
    public void onClickBustypeDaesung() {
        FragmentTransaction fragmentTransaction = mFragmentManger.beginTransaction();
        fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableUnitoYawooriDaesungFragment());
        mBusTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
        mBusTimetableDaesungSpinner.setVisibility(View.VISIBLE);
        mBusTimetableChungjuSpinner.setVisibility(View.GONE);
        mBusTimetableShuttleBusSpinner.setVisibility(View.GONE);
        mBusTimetableDaesungSpinner.setSelection(0);

        fragmentTransaction.commit();
    }

    @OnClick(R.id.bus_timetable_bustype_city)
    public void onClickBustypeCity() {
        FragmentTransaction fragmentTransaction = mFragmentManger.beginTransaction();
        fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableCityBusFragment());
        mBusTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
        mBusTimetableDaesungSpinner.setVisibility(View.GONE);
        mBusTimetableChungjuSpinner.setVisibility(View.GONE);
        mBusTimetableShuttleBusSpinner.setVisibility(View.GONE);

        fragmentTransaction.commit();

    }


    @OnItemSelected(R.id.bus_timetable_fragment_spinner)
    public void onBusArrivalSpinnerSelect(Spinner spinner, int position) {
        FragmentTransaction fragmentTransaction = mFragmentManger.beginTransaction();
        if (!isVacation) {                                                              //학기중
            switch (position) {
                case 0:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableCheonanShuttleStartEndTrainStationFragment());
                    mBusTimetableCheonanStartEndSpinner.setVisibility(View.VISIBLE);
                    mBusTimetableChungjuSpinner.setVisibility(View.GONE);
                    mBusTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 1:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableCheonanShuttleFragment());
                    mBusTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    mBusTimetableChungjuSpinner.setVisibility(View.GONE);
                    mBusTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 2:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableChungjuShuttleStartEndGymFragment());
                    mBusTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    mBusTimetableChungjuSpinner.setVisibility(View.VISIBLE);
                    mBusTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 3:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableChungjuShuttleFragment());
                    mBusTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    mBusTimetableChungjuSpinner.setVisibility(View.GONE);
                    mBusTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 4:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableSeoulShuttleFragment());
                    mBusTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    mBusTimetableChungjuSpinner.setVisibility(View.GONE);
                    break;
                case 5:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableDaejeonShuttleFragment());
                    mBusTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    mBusTimetableChungjuSpinner.setVisibility(View.GONE);
                    mBusTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
            }
            fragmentTransaction.commit();
        } else {                                                                        //방학중
            switch (position) {
                case 0:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableSeasonShuttleTerminalFragment());
                    mBusTimetableCheonanStartEndSpinner.setSelection(0);
                    mBusTimetableChungjuSpinner.setSelection(0);
                    mBusTimetableDaesungSpinner.setSelection(0);
                    mBusTimetableCheonanStartEndSpinner.setVisibility(View.VISIBLE);
                    mBusTimetableChungjuSpinner.setVisibility(View.GONE);
                    mBusTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 1:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableVacationShuttleFragment());
                    mBusTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    mBusTimetableChungjuSpinner.setVisibility(View.GONE);
                    mBusTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 2:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableVacationShuttleDojungFragment());
                    mBusTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    mBusTimetableChungjuSpinner.setVisibility(View.GONE);
                    mBusTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 3:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableSeasonChungjuShuttleYongamMorningFragment());
                    mBusTimetableCheonanStartEndSpinner.setSelection(0);
                    mBusTimetableChungjuSpinner.setSelection(0);
                    mBusTimetableDaesungSpinner.setSelection(0);
                    mBusTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    mBusTimetableChungjuSpinner.setVisibility(View.VISIBLE);
                    mBusTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 4:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableVacationChungjuShuttleYongamMorningFragment());
                    mBusTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    mBusTimetableChungjuSpinner.setVisibility(View.GONE);
                    mBusTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 5:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableVacationChungjuShuttleYongamEveningFragment());
                    mBusTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    mBusTimetableChungjuSpinner.setVisibility(View.GONE);
                    mBusTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 6:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableVacationSeoulShuttleFragment());
                    mBusTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    mBusTimetableChungjuSpinner.setVisibility(View.GONE);
                    mBusTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 7:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableVacaionCheonanShuttleFragment());
                    mBusTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    mBusTimetableChungjuSpinner.setVisibility(View.GONE);
                    mBusTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;


            }
            fragmentTransaction.commit();
        }


    }


    @OnItemSelected(R.id.bus_timetable_fragment_cheonan_start_endspinner)
    public void onBusArrivalCheonanSpinnerSelect(Spinner spinner, int position) {
        FragmentTransaction fragmentTransaction = mFragmentManger.beginTransaction();
        if (!isVacation) {                                                                     //학기중
            switch (position) {
                case 0:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableCheonanShuttleStartEndTrainStationFragment());
                    break;
                case 1:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableCheonanShuttleStartEndTerminalFragment());
                    break;
                case 2:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableCheonanShuttleStartEndDojungStationlFragment());
                    break;
                case 3:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableCheonanShuttleStartEndExpressTrainlFragment());
                    break;
                case 4:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableCheonanShuttleStartEndShinbanddonglFragment());
                    break;
            }
            fragmentTransaction.commit();
        } else {                                                                                //방학중
            switch (position) {
                case 0:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableSeasonShuttleTerminalFragment());
                    break;
                case 1:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableSeasonShuttleStationFragment());
                    break;
            }

            fragmentTransaction.commit();
        }


    }


    @OnItemSelected(R.id.bus_timetable_fragment_chungju_spinner)
    public void onBusArrivalChungjuSpinnerSelect(Spinner spinner, int position) {
        FragmentTransaction fragmentTransaction = mFragmentManger.beginTransaction();
        if (!isVacation) {                                                            //학기중
            switch (position) {
                case 0:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableChungjuShuttleStartEndGymFragment());
                    break;
                case 1:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableChungjuShuttleStartEndYongamFragment());
                    break;
                case 2:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableChungjuShuttleStartEndShinnamdongFragment());
                    break;
                case 3:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableChungjuShuttleStartEndBunpyeongdongFragment());
                    break;
            }
            fragmentTransaction.commit();
        } else {                                                                    //방학중
            switch (position) {
                case 0:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableSeasonChungjuShuttleYongamMorningFragment());
                    break;
                case 1:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableSeasonChungjuShuttleGymMorningFragment());
                    break;
                case 2:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableSeasonChungjuShuttleYongamEveningFragment());
                    break;
                case 3:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableSeasonChungjuShuttleGymEveningFragment());
                    break;
            }

            fragmentTransaction.commit();
        }

    }

    @OnItemSelected(R.id.bus_timetable_fragment_daesung_spinner)
    public void onBusArrivalDaesungSpinnerSelect(Spinner spinner, int position) {
        FragmentTransaction fragmentTransaction = mFragmentManger.beginTransaction();
        switch (position) {
            case 0:
                fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableUnitoYawooriDaesungFragment());
                break;
            case 1:
                fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableYawooritoUniDaesungFragment());
                break;

        }

        mBusTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
        mBusTimetableChungjuSpinner.setVisibility(View.GONE);
        mBusTimetableDaesungSpinner.setVisibility(View.VISIBLE);
        fragmentTransaction.commit();
    }


    public void init() {
        mFragmentManger = getChildFragmentManager();
        setPresenter(new BusTimeTablePresenter(this, new TermRestInteractor()));
        busTimeTablePresenter.getTerm();
        mBusTimetableChungjuSpinner.setVisibility(View.GONE);
        mBusTimetableShuttleBusSpinner.setVisibility(View.VISIBLE);
        mBusTimetableCheonanStartEndSpinner.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = mFragmentManger.beginTransaction();
        fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableCheonanShuttleStartEndTrainStationFragment());
        fragmentTransaction.commit();
    }

    /**
     * spinner의 내용을 바꿔주는 함수
     *
     * @param arrayId spinner안에 들어갈 배열
     * @param spinner 내용이 바뀔 spinner
     */
    public void changeSpinnerItem(int arrayId, Spinner spinner) {
        String[] items = getResources().getStringArray(arrayId);
        ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(spinnerAdapter);
    }

    /**
     * 학기 정보에 따라 spinner의 내용을 설정하는 함수
     *
     * @param term 학기 정보 10 - 1학기, 11 - 1학기 여름방학, 20 - 2학기, 21 - 2학기 겨울방학
     */

    @Override
    public void setBusTimeTableSpinner(int term) {
        if (term == 10 || term == 20) {
            isVacation = false;
            changeSpinnerItem(R.array.bus_stop_place, mBusTimetableShuttleBusSpinner);
            changeSpinnerItem(R.array.bus_stop_cheonan_start_end, mBusTimetableCheonanStartEndSpinner);
            changeSpinnerItem(R.array.bus_stop_chungju, mBusTimetableChungjuSpinner);
        } else {
            isVacation = true;
            changeSpinnerItem(R.array.bus_stop_place_season, mBusTimetableShuttleBusSpinner);
            changeSpinnerItem(R.array.bus_stop_cheonan_start_end_season, mBusTimetableCheonanStartEndSpinner);
            changeSpinnerItem(R.array.bus_stop_chungju_season, mBusTimetableChungjuSpinner);
        }


    }


    @Override
    public void setPresenter(BusTimeTablePresenter presenter) {
        this.busTimeTablePresenter = presenter;
    }
}