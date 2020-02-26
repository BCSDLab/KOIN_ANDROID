package in.koreatech.koin.service_advertise.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorControl;
import com.github.irshulx.models.EditorTextStyle;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindBitmap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.KoinEditorActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.asynctasks.GenerateProgressTask;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.interactors.AdDetailRestInterator;
import in.koreatech.koin.core.networks.interactors.CommunityRestInteractor;
import in.koreatech.koin.core.util.FormValidatorUtil;
import in.koreatech.koin.core.util.ImageUtil;
import in.koreatech.koin.core.util.SnackbarUtil;
import in.koreatech.koin.core.util.TimeUtil;
import in.koreatech.koin.core.util.TimerUtil;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_advertise.contracts.AdvertisingContract;
import in.koreatech.koin.service_advertise.contracts.AdvertisingCreatingContract;
import in.koreatech.koin.service_advertise.presenters.AdvertisingCreatingPresenter;
import in.koreatech.koin.service_advertise.presenters.AdvertisingPresenter;
import in.koreatech.koin.service_board.contracts.ArticleEditContract;
import in.koreatech.koin.service_board.presenters.ArticleEditPresenter;
import in.koreatech.koin.service_board.ui.KoinRichEditor;
import in.koreatech.koin.ui.MainActivity;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import top.defaults.colorpicker.ColorPickerPopup;

/**
 * Created by hansol on 2020.1.8...
 * Edited by seongyun on 2020. 02. 17...
 */
public class AdvertisingCreateActivity extends KoinEditorActivity implements AdvertisingCreatingContract.View, TextWatcher {
    private Context context;
    private final static String TAG = "AdCreateActivity";
    private Calendar SelectStartDate;
    private Calendar SelectEndDate;
    private DatePickerDialog.OnDateSetListener dataPicker;
    private DatePickerDialog.OnDateSetListener dataPicker2;
    private boolean isClickedQuestion = false;
    private GenerateProgressTask generateProgressTask;
    private AdvertisingCreatingPresenter advertisingCreatingPresenter;
    private InputMethodManager inputMethodManager;

    @BindView(R.id.koin_base_app_bar_dark)
    KoinBaseAppbarDark koinBaseAppbar;
    @BindView(R.id.advertising_create_question_mark_imageview)
    ImageView questionMark;
    @BindView(R.id.advertising_detail_title_edittext)
    EditText createTitleEditText;
    @BindView(R.id.advertising_detail_event_title_edittext)
    EditText eventTitleEditText;
    @BindView(R.id.advertising_create_calender_startdate_textview)
    TextView startDateTextview;
    @BindView(R.id.advertising_create_calender_enddate_textview)
    TextView endDateTextview;
    @BindView(R.id.advertising_create_question_info_frame_layout)
    FrameLayout questionInfoFrameLayout;

    @BindView(R.id.advertising_create_content)
    KoinRichEditor advertisingRichEditor;

    private boolean isEdit;
    private String title;
    private String eventTitle;
    private String startDate;
    private String endDate;
    private String content;
    private int shopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.advertising_create_activity);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        context = this;
        setPresenter(new AdvertisingCreatingPresenter(this, new AdDetailRestInterator()));
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        isEdit = getIntent().getBooleanExtra("IS_EDIT", false);

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @Override
    protected int getRichEditorId() {
        return R.id.advertising_create_content;
    }

    @Override
    protected boolean isEditable() {
        return true;
    }

    @Override
    protected void successImageProcessing(File imageFile, String uuid) {
        advertisingCreatingPresenter.uploadImage(imageFile, uuid);
    }

    void init() {
        if(isEdit) {
            createTitleEditText.setText(getIntent().getStringExtra("TITLE"));
            eventTitleEditText.setText(getIntent().getStringExtra("EVENT_TITLE"));
            startDateTextview.setText(getIntent().getStringExtra("START_DATE"));
            endDateTextview.setText(getIntent().getStringExtra("END_DATE"));
            renderEditor(getIntent().getStringExtra("CONTENT"));
        } else {
            startDateTextview.setText(TimeUtil.getDeviceCreatedDateOnlyString());
            endDateTextview.setText(TimeUtil.getDeviceCreatedDateOnlyString());
        }
        calenderCheck();
        koinBaseAppbar.setLeftButtonText("취소");
        koinBaseAppbar.setRightButtonText("등록");

        // 자동으로 제목에 포커스를 주면서 키보드 올리기
        createTitleEditText.setFocusableInTouchMode(true);
        createTitleEditText.requestFocus();
        inputMethodManager.showSoftInput(createTitleEditText, 0);
    }

    void calenderCheck() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SelectStartDate = Calendar.getInstance();
        SelectEndDate = Calendar.getInstance();

        if(isEdit) {
            try {
                SelectStartDate.setTime(sdf.parse(startDateTextview.getText().toString()));
                SelectEndDate.setTime(sdf.parse(endDateTextview.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        // 시작 일자 캘린더 클릭 이벤트
        dataPicker = (view, year, month, dayOfMonth) -> {
            SelectStartDate.set(Calendar.YEAR, year);
            SelectStartDate.set(Calendar.MONTH, month);
            SelectStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if(month < 10)
                startDateTextview.setText(year + "-0" + (month + 1) + "-" + dayOfMonth);
            else
                startDateTextview.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
        };

        // 종료 일자 캘린더 클릭 이벤트
        dataPicker2 = (view, year, month, dayOfMonth) -> {
            SelectEndDate.set(Calendar.YEAR, year);
            SelectEndDate.set(Calendar.MONTH, month);
            SelectEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if(month < 10)
                endDateTextview.setText(year + "-0" + (month + 1) + "-" + dayOfMonth);
            else
                endDateTextview.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
        };
    }

    // 시작 일자를 선택하기 위한 달력 표시
    @OnClick({R.id.advertising_calender_reck_layout})
    void calender1OnClicked() {
        new DatePickerDialog(AdvertisingCreateActivity.this, dataPicker,
                SelectStartDate.get(Calendar.YEAR),
                SelectStartDate.get(Calendar.MONTH),
                SelectStartDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    // 종료 일자를 선택하기 위한 달력 표시
    @OnClick({R.id.advertising_calender2_reck_layout})
    void calender2OnClicked() {
        new DatePickerDialog(AdvertisingCreateActivity.this, dataPicker2,
                SelectEndDate.get(Calendar.YEAR),
                SelectEndDate.get(Calendar.MONTH),
                SelectEndDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    // 도움말 버튼 클릭 이벤트
    @OnClick(R.id.advertising_create_question_mark_imageview)
    public void questionMarkOnClicked() {
        if (!isClickedQuestion) {
            questionMark.setImageResource(R.drawable.ic_question_mark2);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            questionInfoFrameLayout.setVisibility(View.VISIBLE);
            questionInfoFrameLayout.bringToFront();
            isClickedQuestion = true;
        } else {
            questionMark.setImageResource(R.drawable.ic_question_mark);
            inputMethodManager.showSoftInput(createTitleEditText, 0);
            questionInfoFrameLayout.setVisibility(View.INVISIBLE);
            isClickedQuestion = false;
        }
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickKoinBaseAppbar(View v) {
        int viewId = v.getId();

        if (viewId == KoinBaseAppbarDark.getLeftButtonId())
            onBackPressed();
        else if (viewId == KoinBaseAppbarDark.getRightButtonId())
            onClickEditButton();
    }

    @Override
    public void showLoading() {
        if (generateProgressTask == null) {
            generateProgressTask = new GenerateProgressTask(this, "로딩 중");
            generateProgressTask.execute();
        }
    }

    @Override
    public void hideLoading() {
        if (generateProgressTask != null) {
            generateProgressTask.cancel(true);
            generateProgressTask = null;
        }
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.makeLongToast(context, message);
    }



    @Override
    public void onBackPressed(){
        View view = this.getCurrentFocus();

        if(view != null) {
            Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(view.getWindowToken(), 0);
            SnackbarUtil.makeLongSnackbarActionYes(view, getString(R.string.back_button_pressed), this::finish);
        }
        else {
            finish();
        }
    }

    @Override
    public void onClickEditButton() {
        if(!isImageAllUploaded()) {
            ToastUtil.makeShortToast(context, "이미지 업로드 중입니다.");
            return;
        }
        if (FormValidatorUtil.validateStringIsEmpty(createTitleEditText.getText().toString())) {
            ToastUtil.makeShortToast(context, "제목을 입력하세요");
            return;
        }
        if (FormValidatorUtil.validateStringIsEmpty(eventTitleEditText.getText().toString())) {
            ToastUtil.makeShortToast(context, "홍보 문구를 입력하세요");
            return;
        }
        if (FormValidatorUtil.validateStringIsEmpty(getContent())) {
            ToastUtil.makeShortToast(context, "내용을 입력하세요");
            return;
        }

        title = createTitleEditText.getText().toString().trim();
        eventTitle = this.eventTitleEditText.getText().toString().trim();
        content = getContentAsHTML();

        // TODO : shopId를 어떻게 받아와야하는가
        shopId = 12;

        startDate = startDateTextview.getText().toString();
        endDate = endDateTextview.getText().toString();

        String thumbnail = getThumbnail();
        if(thumbnail == null) {
            advertisingCreatingPresenter.createAdDetail(new AdDetail(title, eventTitle, content, shopId, startDate, endDate));
        }
        else {
            advertisingCreatingPresenter.createAdDetail(new AdDetail(title, eventTitle, content, shopId, startDate, endDate, thumbnail));
        }
    }

    @Override
    public void onAdDetailDataReceived(AdDetail adDetail) {
        goToAdvertisingActivity(adDetail);
    }

    @Override
    public void goToAdvertisingActivity(AdDetail adDetail) {
        finish();
    }

    @Override
    public void showUploadImage(String url, String uploadImageId) {
        onImageUploadComplete(url, uploadImageId);
    }

    @Override
    public void showFailUploadImage(String uploadImageId) {
        onImageUploadFailed(uploadImageId);
    }

    @Override
    public void setPresenter(AdvertisingCreatingPresenter presenter) {
        this.advertisingCreatingPresenter = presenter;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
