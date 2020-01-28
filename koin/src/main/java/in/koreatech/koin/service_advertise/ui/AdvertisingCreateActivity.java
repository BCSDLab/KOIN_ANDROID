package in.koreatech.koin.service_advertise.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

import butterknife.BindBitmap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.ui.MainActivity;

/**
 * Created by hansol on 2020.1.8...
 */
public class AdvertisingCreateActivity extends AppCompatActivity {
    Calendar SelectDate;
    DatePickerDialog.OnDateSetListener dataPicker;
    DatePickerDialog.OnDateSetListener dataPicker2;
    int questionMarkClickCount = 0;

    @BindView(R.id.advertising_create_question_mark_imageview)
    ImageView questionMark;
    @BindView(R.id.advertising_create_calender_startdate_textview)
    TextView startDateTextview;
    @BindView(R.id.advertising_create_calender_enddate_textview)
    TextView endDateTextview;
    @BindView(R.id.advertising_create_question_info_frame_layout)
    FrameLayout questionInfoFrameLayout;

    @OnClick(R.id.advertising_create_question_mark_imageview)
    public void questionMarkOnClicked() {
        questionMarkClickCount++;
        if (questionMarkClickCount % 2 == 0) {
            questionMark.setImageResource(R.drawable.ic_question_mark2);
            questionInfoFrameLayout.setVisibility(View.VISIBLE);
        } else {
            questionMark.setImageResource(R.drawable.ic_question_mark);
            questionInfoFrameLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertising_create_activity);

        init();
    }

    void init() {
        ButterKnife.bind(this);
        calenderCheck();
    }

    void calenderCheck() {
        SelectDate = Calendar.getInstance();
        dataPicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SelectDate.set(Calendar.YEAR, year);
                SelectDate.set(Calendar.MONTH, month);
                SelectDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDateTextview.setText(year + "." + (month + 1) + "." + dayOfMonth);

            }
        };

        dataPicker2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SelectDate.set(Calendar.YEAR, year);
                SelectDate.set(Calendar.MONTH, month);
                SelectDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDateTextview.setText(year + "." + (month + 1) + "." + dayOfMonth);

            }
        };
    }

    @OnClick({R.id.advertising_calender_reck_layout})
    void calender1OnClicked() {
        new DatePickerDialog(AdvertisingCreateActivity.this, dataPicker,
                SelectDate.get(Calendar.YEAR),
                SelectDate.get(Calendar.MONTH),
                SelectDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick({R.id.advertising_calender2_reck_layout})
    void calender2OnClicked() {
        new DatePickerDialog(AdvertisingCreateActivity.this, dataPicker2,
                SelectDate.get(Calendar.YEAR),
                SelectDate.get(Calendar.MONTH),
                SelectDate.get(Calendar.DAY_OF_MONTH)).show();
    }
}
