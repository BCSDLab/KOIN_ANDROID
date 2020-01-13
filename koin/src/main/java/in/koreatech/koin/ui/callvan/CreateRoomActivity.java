package in.koreatech.koin.ui.callvan;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.core.appbar.AppbarBase;
import in.koreatech.koin.data.network.entity.CallvanRoom;
import in.koreatech.koin.data.network.interactor.CallvanRestInteractor;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.util.TimeUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.callvan.presenter.CreateRoomContract;
import in.koreatech.koin.ui.callvan.presenter.CreateRoomPresenter;

/**
 * Created by hyerim on 2018. 6. 18....
 */
public class CreateRoomActivity extends ActivityBase implements CreateRoomContract.View {
    private final static String TAG = "CreateRoomActivity";

    private Context context;
    private int maxPeople;
    private CreateRoomPresenter mCreateRoomPresenter;

    /* View Component */
    @BindView(R.id.koin_base_appbar)
    AppbarBase koinBaseAppbar;

    @BindView(R.id.create_room_start_place)
    TextView createRoomStartPlaceTextView;

    @BindView(R.id.create_room_end_place)
    TextView createRoomEndPlaceTextView;

    @BindView(R.id.create_room_start_day)
    TextView createRoomStartDayTextView;

    @BindView(R.id.create_room_button)
    AppCompatButton createRoomButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callvan_activity_create_room);
        ButterKnife.bind(this);
        this.context = this;
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        SnackbarUtil.makeLongSnackbarActionYes(createRoomButton, getString(R.string.back_button_pressed), new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @OnClick(R.id.koin_base_appbar)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == AppbarBase.getLeftButtonId()) {
            onBackPressed();

        }
    }

    private void init() {
        maxPeople = 6;

//        mCreateRoomStartYearTextView.setText(TimeUtil.getCurrentYear());
//
//        if (TimeUtil.getCurrentMonthOnlyLong() < 10) {
//            mCreateRoomStartMonthTextView.setText("0" + TimeUtil.getCurrentMonth());
//        } else {
//            mCreateRoomStartMonthTextView.setText(TimeUtil.getCurrentMonth());
//        }

//        if (TimeUtil.getCurrentDayOnlyLong() < 10) {
//            createRoomStartDayTextView.setText("0" + TimeUtil.getCurrentDay());
//        } else {
//            createRoomStartDayTextView.setText(TimeUtil.getCurrentDay());
//        }
//
//        mCreateRoomStartHourTextView.setText("00");
//        mCreateRoomStartMinuteTextView.setText("00");

        setPresenter(new CreateRoomPresenter(this, new CallvanRestInteractor()));
    }

    @Override
    public void setPresenter(CreateRoomPresenter presenter) {
        mCreateRoomPresenter = presenter;
    }

    @Override
    @OnClick(R.id.create_room_start_place_layout)
    public void onClickCreateRoomStartPlaceLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("출발지 선택")
                .setItems(R.array.favorite_place, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] list = CreateRoomActivity.this.getResources().getStringArray(
                                R.array.favorite_place);

                        createRoomStartPlaceTextView.setText(list[which]);
                    }
                });
        builder.show();
    }

    @Override
    @OnClick(R.id.create_room_end_place_layout)
    public void onClickCreateRoomEndPlaceLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("목적지 선택")
                .setItems(R.array.favorite_place, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] list = CreateRoomActivity.this.getResources().getStringArray(
                                R.array.favorite_place);

                        createRoomEndPlaceTextView.setText(list[which]);
                    }
                });
        builder.show();
    }

    @Override
    @OnClick(R.id.create_room_button)
    public void onClickCreateRoomButton() {
        String startPlaceTextView = createRoomStartPlaceTextView.getText().toString();
//        String startPlaceEditText = mCreateRoomStartPlaceEditText.getText().toString();
        String startPlaceResult = startPlaceTextView;
        if (FormValidatorUtil.validateStartPlaceIsEmpty(startPlaceTextView)) {
//            if (FormValidatorUtil.validateStringIsEmpty(startPlaceEditText)) {
//                ToastUtil.makeShort(this, "출발지를 선택하거나 입력하세요");
//                return;
//            }
        }

//        if (!FormValidatorUtil.validateStringIsEmpty(startPlaceEditText)) {
//            startPlaceResult = startPlaceEditText;
//        }

        String endPlaceTextView = this.createRoomEndPlaceTextView.getText().toString();
//        String endPlaceEditText = mCreateRoomEndPlaceEditText.getText().toString();
        String endPlaceResult = endPlaceTextView;
        if (FormValidatorUtil.validateEndPlaceIsEmpty(endPlaceTextView)) {
//            if (FormValidatorUtil.validateStringIsEmpty(endPlaceEditText)) {
//                ToastUtil.makeShort(this, "목적지를 선택하거나 입력하세요");
//                return;
//            }
        }
//
//        if (!FormValidatorUtil.validateStringIsEmpty(endPlaceEditText)) {
//            endPlaceResult = endPlaceEditText;
//        }

        if (startPlaceResult.compareTo(endPlaceResult) == 0) {
            ToastUtil.getInstance().makeShort("출발지와 목적지가 같습니다");
            return;
        }

//        String selectYear = mCreateRoomStartYearTextView.getText().toString();
//        String selectMonth = mCreateRoomStartMonthTextView.getText().toString();
        String selectDay = createRoomStartDayTextView.getText().toString();
//        String selectHour = mCreateRoomStartHourTextView.getText().toString();
//        String selectMinute = mCreateRoomStartMinuteTextView.getText().toString();

        StringBuilder sb = new StringBuilder();
//        sb.append(selectYear).append(selectMonth).append(selectDay).append(selectHour).append(selectMinute);

        String selectTime = sb.toString();
        if (Long.parseLong(selectTime) < TimeUtil.getDeviceCreatedDateOnlyLong()) {
            ToastUtil.getInstance().makeShort("현재 시간보다 나중으로 선택하세요");
            return;
        } else if (Long.parseLong(selectTime) > TimeUtil.getDeviceCreatedDateOnlyLongAddTimeLimit(30 * 24 * 60)) {
            ToastUtil.getInstance().makeShort("한달 이내로 시간을 선택해주세요");
            return;
        }

        sb = new StringBuilder();
//        sb.append(selectYear).append("-").append(selectMonth).append("-").append(selectDay).append(" ")
//                .append(selectHour).append(":").append(selectMinute);

        createRoomToServer(startPlaceResult.trim(), endPlaceResult.trim(), sb.toString(), maxPeople);

    }

    @Override
    public void createRoomToServer(final String startPlaceResult, final String endPlaceResult, final String startTime, final int maxPeople) {
        CallvanRoom newRoom = new CallvanRoom(startPlaceResult, endPlaceResult, startTime, maxPeople);
        mCreateRoomPresenter.createCallvanRoom(newRoom);
    }

    @Override
    public void onCreateRoomDataReceived(boolean isSuccess) {
        finish();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShort(message);
    }
}