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
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.interactors.AdDetailRestInterator;
import in.koreatech.koin.core.networks.interactors.CommunityRestInteractor;
import in.koreatech.koin.core.util.FormValidatorUtil;
import in.koreatech.koin.core.util.ImageUtil;
import in.koreatech.koin.core.util.SnackbarUtil;
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
 */
public class AdvertisingCreateActivity extends KoinEditorActivity implements AdvertisingCreatingContract.View, TextWatcher {
    private final static String TAG = "AdCreateActivity";
    private Calendar SelectDate;
    private DatePickerDialog.OnDateSetListener dataPicker;
    private DatePickerDialog.OnDateSetListener dataPicker2;
    private boolean isClickedQuestion = false;

    @BindView(R.id.koin_base_app_bar_dark)
    KoinBaseAppbarDark koinBaseAppbar;
    @BindView(R.id.advertising_create_question_mark_imageview)
    ImageView questionMark;
    @BindView(R.id.advertising_detail_title_edittext)
    EditText createTitle;
    @BindView(R.id.advertising_detail_event_title_edittext)
    EditText eventTitle;
    @BindView(R.id.advertising_create_calender_startdate_textview)
    TextView startDateTextview;
    @BindView(R.id.advertising_create_calender_enddate_textview)
    TextView endDateTextview;
    @BindView(R.id.advertising_create_question_info_frame_layout)
    FrameLayout questionInfoFrameLayout;

    @BindView(R.id.advertising_create_content)
    KoinRichEditor advertisingRichEditor;
    private Context context;

    AdvertisingCreatingPresenter advertisingCreatingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.advertising_create_activity);
        super.onCreate(savedInstanceState);

        context = this;
        setPresenter(new AdvertisingCreatingPresenter(this, new AdDetailRestInterator()));

        init();
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
        ButterKnife.bind(this);
        calenderCheck();
        koinBaseAppbar.setLeftButtonText("취소");
        koinBaseAppbar.setRightButtonText("등록");
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

    @OnClick(R.id.advertising_create_question_mark_imageview)
    public void questionMarkOnClicked() {
        if (!isClickedQuestion) {
            questionMark.setImageResource(R.drawable.ic_question_mark2);
            questionInfoFrameLayout.setVisibility(View.VISIBLE);
            isClickedQuestion = true;
        } else {
            questionMark.setImageResource(R.drawable.ic_question_mark);
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

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        ToastUtil.makeLongToast(context, message);
    }



    @Override
    public void onBackPressed(){
        View view = this.getCurrentFocus();

        if(view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        if (FormValidatorUtil.validateStringIsEmpty(createTitle.getText().toString())) {
            ToastUtil.makeShortToast(context, "제목을 입력하세요");
            return;
        }
        if (FormValidatorUtil.validateStringIsEmpty(eventTitle.getText().toString())) {
            ToastUtil.makeShortToast(context, "홍보 문구를 입력하세요");
            return;
        }
        if (FormValidatorUtil.validateStringIsEmpty(getContent())) {
            ToastUtil.makeShortToast(context, "내용을 입력하세요");
            return;
        }

        String title = createTitle.getText().toString().trim();
        String eventTitle = this.eventTitle.getText().toString().trim();
        String content = getContentAsHTML();
        int shopId = 12;
        String startDate = startDateTextview.getText().toString();
        String endDate = endDateTextview.getText().toString();
//        if(getThumbnail() != null) {
//            String thumbnail = getThumbnail();
//            Log.d(TAG, thumbnail);
//        }

        Log.d(TAG, title);
        Log.d(TAG, eventTitle);
        Log.d(TAG, content);
        Log.d(TAG, startDate);
        Log.d(TAG, endDate);

        String thumbnail = getThumbnail();
        if(thumbnail == null)
            advertisingCreatingPresenter.createAdDetail(new AdDetail(title, eventTitle, content, shopId, startDate, endDate));
        else
            advertisingCreatingPresenter.createAdDetail(new AdDetail(title, eventTitle, content, shopId, startDate, endDate, thumbnail));
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
