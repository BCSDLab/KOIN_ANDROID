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
import android.util.Log;
import android.view.View;
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

import butterknife.BindBitmap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.KoinEditorActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.interactors.AdDetailRestInterator;
import in.koreatech.koin.core.networks.interactors.CommunityRestInteractor;
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
public class AdvertisingCreateActivity extends KoinEditorActivity implements AdvertisingCreatingContract.View {
    private final static String TAG = "AdCreateActivity";
    Calendar SelectDate;
    DatePickerDialog.OnDateSetListener dataPicker;
    DatePickerDialog.OnDateSetListener dataPicker2;
    int questionMarkClickCount = 0;

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
    public void onClickEditButton() {
        String title = createTitle.getText().toString().trim();
        String eventTitle = this.eventTitle.getText().toString().trim();
        String content = getContentAsHTML();
        int shopId;
        String startDate = startDateTextview.getText().toString();
        String endDate = endDateTextview.getText().toString();
        String thumbnail;

        Log.d(TAG, title);
        Log.d(TAG, eventTitle);
        Log.d(TAG, content);
        Log.d(TAG, startDate);
        Log.d(TAG, endDate);
//        advertisingCreatingPresenter.createAdDetail(new AdDetail());
    }

    @Override
    public void onAdDetailDataReceived(AdDetail adDetail) {

    }

    @Override
    public void goToAdvertisingActivity(AdDetail adDetail) {

    }

    @Override
    public void showUploadImage(String url, String uploadImageId) {
        onImageUploadComplete(url, uploadImageId);
    }

    @Override
    public void showFailUploadImage(String uploadImageId) {

    }

    @Override
    public void setPresenter(AdvertisingCreatingPresenter presenter) {
        this.advertisingCreatingPresenter = presenter;
    }
}
