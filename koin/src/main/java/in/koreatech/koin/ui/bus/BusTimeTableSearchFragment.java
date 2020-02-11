package in.koreatech.koin.ui.bus;

import android.app.DatePickerDialog;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import in.koreatech.koin.R;
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.ui.bus.presenter.BusTimeTableSearchContract;
import in.koreatech.koin.ui.bus.presenter.BusTimeTableSearchPresenter;

public class BusTimeTableSearchFragment extends BusBaseFragment implements BusTimeTableSearchContract.View {
    private final String TAG = "BusTimeTableSearchFragment";

    private Unbinder unbinder;
    private int departureState;
    private int arrivalState;
    private int hour;
    private int min;
    private String totalDate;
    private String totalTime;
    private String shuttleBusDepartInfo;
    private String daesungBusDepartInfo;
    private BusTimeTableSearchPresenter presenter;
    private String resultDate;
    private Calendar calendar;

    @BindView(R.id.bus_search_bus_departure_spinner)
    Spinner busTimetableSearchDepartureSpinner;
    @BindView(R.id.bus_search_bus_arrival_spinner)
    Spinner busTimetalbeSearchArrivalSpinner;
    @BindView(R.id.bus_timetable_search_date_textview)
    TextView busTimetableSearchDateTextView;
    @BindView(R.id.bus_search_timePicker)
    TimePicker busTimetableSearchTimePicker;
    @BindView(R.id.bus_timetable_search_fragment_information_textview)
    TextView busTimetableSearchInformationTextview;
    @BindView(R.id.bus_timetable_search_fragment_search_button)
    Button busTimetableSearchButton;


    /* View Component */
    private View mView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.bus_timetable_search_fragment, container, false);
        ButterKnife.bind(this, mView);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @OnItemSelected(R.id.bus_search_bus_departure_spinner)
    public void onItemSelectedBusSearchDepartureSpinner(Spinner spinner, int position) {


        if (position != arrivalState)
            departureState = position;
        else {
            arrivalState = departureState;
            departureState = position;
        }

        setSpinner();
    }

    @OnItemSelected(R.id.bus_search_bus_arrival_spinner)
    public void onItemSelectedBusSearchArrivalSpinner(Spinner spinner, int position) {
        if (position != departureState)
            arrivalState = position;
        else {
            departureState = arrivalState;
            arrivalState = position;
        }

        setSpinner();
    }

    public void setSpinner() {
        busTimetableSearchDepartureSpinner.setSelection(departureState);
        busTimetalbeSearchArrivalSpinner.setSelection(arrivalState);
    }

    public void init() {
        departureState = 0;
        arrivalState = 1;

        this.calendar = Calendar.getInstance();
        this.calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        updateLabel();
        busTimetableSearchDateTextView.setText("오늘 - " + busTimetableSearchDateTextView.getText());

        this.hour = this.calendar.get(Calendar.HOUR_OF_DAY);
        this.min = this.calendar.get(Calendar.MINUTE);

        setPresenter(new BusTimeTableSearchPresenter(this));
        showUserSelectDateTime();
        setDayPickerTime(this.hour, this.min);

        busTimetableSearchTimePicker.setOnTimeChangedListener((timePicker, hour, minute) -> {
            this.hour = hour;
            this.min = minute;
            showUserSelectDateTime();
        });

    }

    private void setDayPickerTime(int hour, int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            busTimetableSearchTimePicker.setHour(hour);
            busTimetableSearchTimePicker.setMinute(minute);
        } else {
            busTimetableSearchTimePicker.setCurrentHour(hour);
            busTimetableSearchTimePicker.setCurrentMinute(minute);
        }
    }

    public void showUserSelectDateTime() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder timeStringBulider = new StringBuilder();
        stringBuilder.append(this.resultDate);
        stringBuilder.append("   ");
        if (this.hour == 12) {
            stringBuilder.append("오후 ");
            stringBuilder.append(this.hour);
        } else if (this.hour > 12) {
            stringBuilder.append("오후 ");
            stringBuilder.append(this.hour - 12);
        } else {
            stringBuilder.append("오전 ");
            stringBuilder.append(this.hour);
        }

        stringBuilder.append("시 ");
        stringBuilder.append(this.min);
        stringBuilder.append("분");
        busTimetableSearchInformationTextview.setText(stringBuilder.toString());

        this.totalDate = this.resultDate.replaceAll(" / ", "-");

        if (this.hour < 10) {
            timeStringBulider.append("0");
        }
        timeStringBulider.append(this.hour);
        timeStringBulider.append(":");

        if (this.min < 10) {
            timeStringBulider.append("0");
        }
        timeStringBulider.append(this.min);
        this.totalTime = timeStringBulider.toString();
    }

    DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String selectDate = "M월 dd일 (E)";    // 출력형식  3월 24일(일)
        String resultDate = "yyyy / M / dd";    // 출력형식  2019/3/24

        SimpleDateFormat selectDateFormat = new SimpleDateFormat(selectDate, Locale.KOREA);
        busTimetableSearchDateTextView.setText(selectDateFormat.format(this.calendar.getTime()));

        SimpleDateFormat resultDateFormat = new SimpleDateFormat(resultDate, Locale.KOREA);
        this.resultDate = resultDateFormat.format(this.calendar.getTime());
        showUserSelectDateTime();
    }

    @OnClick(R.id.bus_timetable_search_date_imageButton)
    public void onTimetableSearchDatepickerClick() {
        new DatePickerDialog(getContext(), R.style.KAPDatePicker, datePicker, this.calendar.get(Calendar.YEAR), this.calendar.get(Calendar.MONTH), this.calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    @OnClick(R.id.bus_timetable_search_fragment_search_button)
    public void onTimetableSearchButtonClick() {
        this.presenter.getDaesungBus(departureState, arrivalState, this.totalDate, this.totalTime);
        this.presenter.getShuttleBus(departureState, arrivalState, this.totalDate, this.totalTime);
    }

    @Override
    public void updateShuttleBusTime(String time) {
        if (time == null || time.isEmpty())
            time = getResources().getString(R.string.bus_end_information);
        this.shuttleBusDepartInfo = time;
    }

    @Override
    public void updateDaesungBusTime(String time) {
        if (time == null || time.isEmpty())
            time = getResources().getString(R.string.bus_end_information);
        daesungBusDepartInfo = time;
    }

    @Override
    public void updateFailDaesungBusDepartInfo() {
        daesungBusDepartInfo = getResources().getString(R.string.bus_end_information);
    }

    @Override
    public void updateFailShuttleBusDepartInfo() {
        this.shuttleBusDepartInfo = getResources().getString(R.string.bus_end_information);
    }

    @Override
    public void showBusTimeInfo() {
        BusTimeTableSearchResultDialog dialog = new BusTimeTableSearchResultDialog(getActivity(), this.shuttleBusDepartInfo, daesungBusDepartInfo);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Window window = dialog.getWindow();
        int x = (int) (size.x * 0.783f);
        int y = (int) (size.y * 0.325f);
        window.setLayout(x, y);
    }

    @Override
    public void setPresenter(BusTimeTableSearchPresenter presenter) {
        this.presenter = presenter;
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