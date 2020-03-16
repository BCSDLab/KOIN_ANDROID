package in.koreatech.koin.ui.bus;

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
import in.koreatech.koin.data.network.interactor.TermRestInteractor;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableCheonanShuttleFragment;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableCheonanShuttleStartEndDojungStationlFragment;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableCheonanShuttleStartEndExpressTrainlFragment;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableCheonanShuttleStartEndShinbanddonglFragment;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableCheonanShuttleStartEndTerminalFragment;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableCheonanShuttleStartEndTrainStationFragment;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableChungjuShuttleFragment;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableChungjuShuttleStartEndBunpyeongdongFragment;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableChungjuShuttleStartEndGymFragment;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableChungjuShuttleStartEndShinnamdongFragment;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableChungjuShuttleStartEndYongamFragment;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableCityBusFragment;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableDaejeonShuttleFragment;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableSeoulShuttleFragment;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableUnitoYawooriDaesungFragment;
import in.koreatech.koin.ui.bus.bustimetable.BusTimeTableYawooritoUniDaesungFragment;
import in.koreatech.koin.ui.bus.bustimetable.seasonbustimetable.BusTimeTableSeasonChungjuShuttleGymEveningFragment;
import in.koreatech.koin.ui.bus.bustimetable.seasonbustimetable.BusTimeTableSeasonChungjuShuttleGymMorningFragment;
import in.koreatech.koin.ui.bus.bustimetable.seasonbustimetable.BusTimeTableSeasonChungjuShuttleYongamEveningFragment;
import in.koreatech.koin.ui.bus.bustimetable.seasonbustimetable.BusTimeTableSeasonChungjuShuttleYongamMorningFragment;
import in.koreatech.koin.ui.bus.bustimetable.seasonbustimetable.BusTimeTableSeasonShuttleStationFragment;
import in.koreatech.koin.ui.bus.bustimetable.seasonbustimetable.BusTimeTableSeasonShuttleTerminalFragment;
import in.koreatech.koin.ui.bus.bustimetable.seasonbustimetable.BusTimeTableVacaionCheonanShuttleFragment;
import in.koreatech.koin.ui.bus.bustimetable.seasonbustimetable.BusTimeTableVacationChungjuShuttleYongamEveningFragment;
import in.koreatech.koin.ui.bus.bustimetable.seasonbustimetable.BusTimeTableVacationChungjuShuttleYongamMorningFragment;
import in.koreatech.koin.ui.bus.bustimetable.seasonbustimetable.BusTimeTableVacationSeoulShuttleFragment;
import in.koreatech.koin.ui.bus.bustimetable.seasonbustimetable.BusTimeTableVacationShuttleDojungFragment;
import in.koreatech.koin.ui.bus.bustimetable.seasonbustimetable.BusTimeTableVacationShuttleFragment;
import in.koreatech.koin.ui.bus.presenter.BusTimeTableContract;
import in.koreatech.koin.ui.bus.presenter.BusTimeTablePresenter;


public class BusTimeTableFragment extends BusBaseFragment implements BusTimeTableContract.View {
    private final String TAG = "BusTimeTableFragment";


    private Unbinder unbinder;
    private FragmentManager fragmentManger;
    private BusTimeTablePresenter busTimeTablePresenter;
    private boolean isVacation;

    @BindView(R.id.bus_timetable_fragment_spinner)
    Spinner busTimetableShuttleBusSpinner;
    @BindView(R.id.bus_timetable_bustype_shuttle)
    AppCompatButton busTimetableTypeShuttle;
    @BindView(R.id.bus_timetable_bustype_daesung)
    AppCompatButton busTimetableTypeDaesung;
    @BindView(R.id.bus_timetable_bustype_city)
    AppCompatButton busTimetableTypeCity;
    @BindView(R.id.bus_timetable_fragment_cheonan_start_endspinner)
    Spinner busTimetableCheonanStartEndSpinner;
    @BindView(R.id.bus_timetable_fragment_chungju_spinner)
    Spinner busTimetableChungjuSpinner;
    @BindView(R.id.bus_timetable_fragment_daesung_spinner)
    Spinner busTimetableDaesungSpinner;

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
        this.unbinder = ButterKnife.bind(this, mView);
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
        this.unbinder.unbind();
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
        FragmentTransaction fragmentTransaction = this.fragmentManger.beginTransaction();
        fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableCheonanShuttleStartEndTrainStationFragment());
        busTimetableCheonanStartEndSpinner.setVisibility(View.VISIBLE);
        busTimetableDaesungSpinner.setVisibility(View.GONE);
        busTimetableChungjuSpinner.setVisibility(View.GONE);
        busTimetableShuttleBusSpinner.setVisibility(View.VISIBLE);
        busTimetableCheonanStartEndSpinner.setSelection(0);
        busTimetableShuttleBusSpinner.setSelection(0);

        fragmentTransaction.commit();
    }

    @OnClick(R.id.bus_timetable_bustype_daesung)
    public void onClickBustypeDaesung() {
        FragmentTransaction fragmentTransaction = this.fragmentManger.beginTransaction();
        fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableUnitoYawooriDaesungFragment());
        busTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
        busTimetableDaesungSpinner.setVisibility(View.VISIBLE);
        busTimetableChungjuSpinner.setVisibility(View.GONE);
        busTimetableShuttleBusSpinner.setVisibility(View.GONE);
        busTimetableDaesungSpinner.setSelection(0);

        fragmentTransaction.commit();
    }

    @OnClick(R.id.bus_timetable_bustype_city)
    public void onClickBustypeCity() {
        FragmentTransaction fragmentTransaction = this.fragmentManger.beginTransaction();
        fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableCityBusFragment());
        busTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
        busTimetableDaesungSpinner.setVisibility(View.GONE);
        busTimetableChungjuSpinner.setVisibility(View.GONE);
        busTimetableShuttleBusSpinner.setVisibility(View.GONE);

        fragmentTransaction.commit();

    }


    @OnItemSelected(R.id.bus_timetable_fragment_spinner)
    public void onBusArrivalSpinnerSelect(Spinner spinner, int position) {
        FragmentTransaction fragmentTransaction = this.fragmentManger.beginTransaction();
        if (!isVacation) {                                                              //학기중
            switch (position) {
                case 0:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableCheonanShuttleStartEndTrainStationFragment());
                    busTimetableCheonanStartEndSpinner.setVisibility(View.VISIBLE);
                    busTimetableChungjuSpinner.setVisibility(View.GONE);
                    busTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 1:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableCheonanShuttleFragment());
                    busTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    busTimetableChungjuSpinner.setVisibility(View.GONE);
                    busTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 2:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableChungjuShuttleStartEndGymFragment());
                    busTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    busTimetableChungjuSpinner.setVisibility(View.VISIBLE);
                    busTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 3:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableChungjuShuttleFragment());
                    busTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    busTimetableChungjuSpinner.setVisibility(View.GONE);
                    busTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 4:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableSeoulShuttleFragment());
                    busTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    busTimetableChungjuSpinner.setVisibility(View.GONE);
                    break;
                case 5:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableDaejeonShuttleFragment());
                    busTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    busTimetableChungjuSpinner.setVisibility(View.GONE);
                    busTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
            }
            fragmentTransaction.commit();
        } else {                                                                        //방학중
            switch (position) {
                case 0:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableSeasonShuttleTerminalFragment());
                    busTimetableCheonanStartEndSpinner.setSelection(0);
                    busTimetableChungjuSpinner.setSelection(0);
                    busTimetableDaesungSpinner.setSelection(0);
                    busTimetableCheonanStartEndSpinner.setVisibility(View.VISIBLE);
                    busTimetableChungjuSpinner.setVisibility(View.GONE);
                    busTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 1:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableVacationShuttleFragment());
                    busTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    busTimetableChungjuSpinner.setVisibility(View.GONE);
                    busTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 2:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableVacationShuttleDojungFragment());
                    busTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    busTimetableChungjuSpinner.setVisibility(View.GONE);
                    busTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 3:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableSeasonChungjuShuttleYongamMorningFragment());
                    busTimetableCheonanStartEndSpinner.setSelection(0);
                    busTimetableChungjuSpinner.setSelection(0);
                    busTimetableDaesungSpinner.setSelection(0);
                    busTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    busTimetableChungjuSpinner.setVisibility(View.VISIBLE);
                    busTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 4:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableVacationChungjuShuttleYongamMorningFragment());
                    busTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    busTimetableChungjuSpinner.setVisibility(View.GONE);
                    busTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 5:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableVacationChungjuShuttleYongamEveningFragment());
                    busTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    busTimetableChungjuSpinner.setVisibility(View.GONE);
                    busTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 6:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableVacationSeoulShuttleFragment());
                    busTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    busTimetableChungjuSpinner.setVisibility(View.GONE);
                    busTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;
                case 7:
                    fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableVacaionCheonanShuttleFragment());
                    busTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
                    busTimetableChungjuSpinner.setVisibility(View.GONE);
                    busTimetableDaesungSpinner.setVisibility(View.GONE);
                    break;


            }
            fragmentTransaction.commit();
        }


    }


    @OnItemSelected(R.id.bus_timetable_fragment_cheonan_start_endspinner)
    public void onBusArrivalCheonanSpinnerSelect(Spinner spinner, int position) {
        FragmentTransaction fragmentTransaction = this.fragmentManger.beginTransaction();

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
        FragmentTransaction fragmentTransaction = this.fragmentManger.beginTransaction();
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
        FragmentTransaction fragmentTransaction = this.fragmentManger.beginTransaction();
        switch (position) {
            case 0:
                fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableUnitoYawooriDaesungFragment());
                break;
            case 1:
                fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableYawooritoUniDaesungFragment());
                break;

        }

        busTimetableCheonanStartEndSpinner.setVisibility(View.GONE);
        busTimetableChungjuSpinner.setVisibility(View.GONE);
        busTimetableDaesungSpinner.setVisibility(View.VISIBLE);
        fragmentTransaction.commit();
    }


    public void init() {
        this.fragmentManger = getChildFragmentManager();
        busTimetableChungjuSpinner.setVisibility(View.GONE);
        busTimetableShuttleBusSpinner.setVisibility(View.VISIBLE);
        busTimetableCheonanStartEndSpinner.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = this.fragmentManger.beginTransaction();
        setPresenter(new BusTimeTablePresenter(this, new TermRestInteractor()));
        busTimeTablePresenter.getTerm();
        busTimetableChungjuSpinner.setVisibility(View.GONE);
        busTimetableShuttleBusSpinner.setVisibility(View.VISIBLE);
        busTimetableCheonanStartEndSpinner.setVisibility(View.VISIBLE);
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
            changeSpinnerItem(R.array.bus_stop_place, busTimetableShuttleBusSpinner);
            changeSpinnerItem(R.array.bus_stop_cheonan_start_end, busTimetableCheonanStartEndSpinner);
            changeSpinnerItem(R.array.bus_stop_chungju, busTimetableChungjuSpinner);
        } else {
            isVacation = true;
            changeSpinnerItem(R.array.bus_stop_place_season, busTimetableShuttleBusSpinner);
            changeSpinnerItem(R.array.bus_stop_cheonan_start_end_season, busTimetableCheonanStartEndSpinner);
            changeSpinnerItem(R.array.bus_stop_chungju_season, busTimetableChungjuSpinner);
        }


    }


    @Override
    public void setPresenter(BusTimeTablePresenter presenter) {
        this.busTimeTablePresenter = presenter;
    }
}