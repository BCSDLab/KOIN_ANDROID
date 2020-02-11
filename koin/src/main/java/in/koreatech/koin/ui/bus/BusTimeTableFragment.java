package in.koreatech.koin.ui.bus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class BusTimeTableFragment extends BusBaseFragment {
    private final String TAG = "BusTimeTableFragment";


    private Unbinder unbinder;
    private FragmentManager fragmentManger;

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
    public void onClickBustypeShuttle(){
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
    public void onClickBustypeDaesung(){
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
    public void onClickBustypeCity(){
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

    }

    @OnItemSelected(R.id.bus_timetable_fragment_cheonan_start_endspinner)
    public void onBusArrivalCheonanSpinnerSelect(Spinner spinner, int position) {
        FragmentTransaction fragmentTransaction = this.fragmentManger.beginTransaction();
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

    }

    @OnItemSelected(R.id.bus_timetable_fragment_chungju_spinner)
    public void onBusArrivalChungjuSpinnerSelect(Spinner spinner, int position) {
        FragmentTransaction fragmentTransaction = this.fragmentManger.beginTransaction();
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
        fragmentTransaction.replace(R.id.bus_timetable_fragment, new BusTimeTableCheonanShuttleStartEndTrainStationFragment());
        fragmentTransaction.commit();
    }


}