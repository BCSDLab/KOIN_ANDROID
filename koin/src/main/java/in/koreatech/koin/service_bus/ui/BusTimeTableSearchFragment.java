package in.koreatech.koin.service_bus.ui;

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
import in.koreatech.koin.service_bus.contracts.BusTimeTableSearchContract;
import in.koreatech.koin.service_bus.presenters.BusTimeTableSearchPresenter;


/**
 * @author yunjae na
 * @since 2018.12.02
 */
public class BusTimeTableSearchFragment extends BusBaseFragment implements BusTimeTableSearchContract.View {
    private final String TAG = BusTimeTableSearchFragment.class.getSimpleName();

    private CustomProgressDialog customProgressDialog;
    private Unbinder mUnbinder;
    private int mDepartureState;
    private int mArrivalState;
    private int mHour;
    private int mMin;
    private String mTotalDate;
    private String mTotalTime;
    private String mShuttleBusDepartInfo;
    private String mDaesungBusDepartInfo;
    private BusTimeTableSearchPresenter mPresenter;
    private String mResultDate;
    private Calendar mCalendar;

    @BindView(R.id.bus_search_bus_departure_spinner)
    Spinner mBusTimetableSearchDepartureSpinner;
    @BindView(R.id.bus_search_bus_arrival_spinner)
    Spinner mBusTimetalbeSearchArrivalSpinner;
    @BindView(R.id.bus_timetable_search_date_textview)
    TextView mBusTimetableSearchDateTextView;
    @BindView(R.id.bus_search_timePicker)
    TimePicker mBusTimetableSearchTimePicker;
    @BindView(R.id.bus_timetable_search_fragment_information_textview)
    TextView mBusTimetableSearchInformationTextview;
    @BindView(R.id.bus_timetable_search_fragment_search_button)
    Button mBusTimetableSearchButton;


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


        if (position != mArrivalState)
            mDepartureState = position;
        else {
            mArrivalState = mDepartureState;
            mDepartureState = position;
        }

        setSpinner();
    }

    @OnItemSelected(R.id.bus_search_bus_arrival_spinner)
    public void onItemSelectedBusSearchArrivalSpinner(Spinner spinner, int position) {
        if (position != mDepartureState)
            mArrivalState = position;
        else {
            mDepartureState = mArrivalState;
            mArrivalState = position;
        }

        setSpinner();
    }

    public void setSpinner() {
        mBusTimetableSearchDepartureSpinner.setSelection(mDepartureState);
        mBusTimetalbeSearchArrivalSpinner.setSelection(mArrivalState);
    }

    public void init() {
        mDepartureState = 0;
        mArrivalState = 1;

        mCalendar = Calendar.getInstance();
        mCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        updateLabel();
        mBusTimetableSearchDateTextView.setText("오늘 - " + mBusTimetableSearchDateTextView.getText());

        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMin = mCalendar.get(Calendar.MINUTE);

        setPresenter(new BusTimeTableSearchPresenter(this));
        showUserSelectDateTime();
        setDayPickerTime(mHour, mMin);

        mBusTimetableSearchTimePicker.setOnTimeChangedListener((timePicker, hour, minute) -> {
            BusTimeTableSearchFragment.this.mHour = hour;
            BusTimeTableSearchFragment.this.mMin = minute;
            showUserSelectDateTime();
        });

    }

    private void setDayPickerTime(int hour, int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBusTimetableSearchTimePicker.setHour(hour);
            mBusTimetableSearchTimePicker.setMinute(minute);
        } else {
            mBusTimetableSearchTimePicker.setCurrentHour(hour);
            mBusTimetableSearchTimePicker.setCurrentMinute(minute);
        }
    }

    public void showUserSelectDateTime() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder timeStringBulider = new StringBuilder();
        stringBuilder.append(mResultDate);
        stringBuilder.append("   ");
        if (mHour == 12) {
            stringBuilder.append("오후 ");
            stringBuilder.append(mHour);
        } else if (mHour > 12) {
            stringBuilder.append("오후 ");
            stringBuilder.append(mHour - 12);
        } else {
            stringBuilder.append("오전 ");
            stringBuilder.append(mHour);
        }

        stringBuilder.append("시 ");
        stringBuilder.append(mMin);
        stringBuilder.append("분");
        mBusTimetableSearchInformationTextview.setText(stringBuilder.toString());

        mTotalDate = mResultDate.replaceAll(" / ", "-");

        if (mHour < 10) {
            timeStringBulider.append("0");
        }
        timeStringBulider.append(mHour);
        timeStringBulider.append(":");

        if (mMin < 10) {
            timeStringBulider.append("0");
        }
        timeStringBulider.append(mMin);
        mTotalTime = timeStringBulider.toString();
    }

    DatePickerDialog.OnDateSetListener mDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String selectDate = "M월 dd일 (E)";    // 출력형식  3월 24일(일)
        String resultDate = "yyyy / M / dd";    // 출력형식  2019/3/24

        SimpleDateFormat selectDateFormat = new SimpleDateFormat(selectDate, Locale.KOREA);
        mBusTimetableSearchDateTextView.setText(selectDateFormat.format(mCalendar.getTime()));

        SimpleDateFormat resultDateFormat = new SimpleDateFormat(resultDate, Locale.KOREA);
        mResultDate = resultDateFormat.format(mCalendar.getTime());
        showUserSelectDateTime();
    }

    @OnClick(R.id.bus_timetable_search_date_imageButton)
    public void onTimetableSearchDatepickerClick() {
        new DatePickerDialog(getContext(), R.style.KAPDatePicker, mDatePicker, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    @OnClick(R.id.bus_timetable_search_fragment_search_button)
    public void onTimetableSearchButtonClick() {
        mPresenter.getDaesungBus(mDepartureState, mArrivalState, mTotalDate, mTotalTime);
        mPresenter.getShuttleBus(mDepartureState, mArrivalState, mTotalDate, mTotalTime);
    }

    @Override
    public void updateShuttleBusTime(String time) {
        if (time == null || time.isEmpty()) time = getResources().getString(R.string.bus_end_information);
        mShuttleBusDepartInfo = time;
    }

    @Override
    public void updateDaesungBusTime(String time) {
        if (time == null || time.isEmpty()) time = getResources().getString(R.string.bus_end_information);
        mDaesungBusDepartInfo = time;
    }

    @Override
    public void updateFailDaesungBusDepartInfo() {
        mDaesungBusDepartInfo = getResources().getString(R.string.bus_end_information);
    }

    @Override
    public void updateFailShuttleBusDepartInfo() {
        mShuttleBusDepartInfo = getResources().getString(R.string.bus_end_information);
    }

    @Override
    public void showBusTimeInfo() {
        BusTimeTableSearchResultDialog dialog = new BusTimeTableSearchResultDialog(getActivity(), mShuttleBusDepartInfo, mDaesungBusDepartInfo);
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
        this.mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(getContext(), "로딩중");
            customProgressDialog.execute();
        }

    }

    @Override
    public void hideLoading() {
        if (customProgressDialog != null) {
            customProgressDialog.cancel(true);
            customProgressDialog = null;
        }
    }
}