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
import in.koreatech.koin.R;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.interactors.CommunityRestInteractor;
import in.koreatech.koin.core.util.ImageUtil;
import in.koreatech.koin.core.util.SnackbarUtil;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_advertise.contracts.AdvertisingContract;
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
public class AdvertisingCreateActivity extends AppCompatActivity implements ArticleEditContract.View {
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

    @BindView(R.id.advertising_create_content)
    KoinRichEditor advertisingRichEditor;
    private Context context;
    private HashMap<String, Boolean> uploadImageHashMap;             // 갤러리에서 불러온 이미지의 HashMap
    private HashMap<String, Boolean> isUploadImageCompeleteHashMap;  // 압축 과정을 완료한 이미지의 HashMap
    private ArticleEditPresenter articleEditPresenter;

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

        context = this;
        advertisingRichEditor.setEditorImageLayout(R.layout.rich_editor_image_layout);
        setPresenter(new ArticleEditPresenter(this, new CommunityRestInteractor()));

        init();
    }

    void init() {
        ButterKnife.bind(this);
        uploadImageHashMap = new HashMap<>();
        isUploadImageCompeleteHashMap = new HashMap<>();
        calenderCheck();
        
        initEditor();
    }

    private void initEditor() {
        findViewById(R.id.action_h1).setOnClickListener(v -> advertisingRichEditor.updateTextStyle(EditorTextStyle.H1));
        findViewById(R.id.action_h2).setOnClickListener(v -> advertisingRichEditor.updateTextStyle(EditorTextStyle.H2));
        findViewById(R.id.action_h3).setOnClickListener(v -> advertisingRichEditor.updateTextStyle(EditorTextStyle.H3));
        findViewById(R.id.action_bold).setOnClickListener(v -> advertisingRichEditor.updateTextStyle(EditorTextStyle.BOLD));
        findViewById(R.id.action_Italic).setOnClickListener(v -> advertisingRichEditor.updateTextStyle(EditorTextStyle.ITALIC));
        findViewById(R.id.action_indent).setOnClickListener(v -> advertisingRichEditor.updateTextStyle(EditorTextStyle.INDENT));
        findViewById(R.id.action_outdent).setOnClickListener(v -> advertisingRichEditor.updateTextStyle(EditorTextStyle.OUTDENT));
        findViewById(R.id.action_bulleted).setOnClickListener(v -> advertisingRichEditor.insertList(false));
        findViewById(R.id.action_unordered_numbered).setOnClickListener(v -> advertisingRichEditor.insertList(true));
        findViewById(R.id.action_hr).setOnClickListener(v -> advertisingRichEditor.insertDivider());
        findViewById(R.id.action_insert_image).setOnClickListener(v -> {
            advertisingRichEditor.openImagePicker();

            // btn_remove 버튼을 클릭하여 갤러리에서 가져온 이미지를 삭제
            advertisingRichEditor.setOnCancelListener(view -> {
                String imageTag = ((EditorControl) ((RelativeLayout) view.getParent()).getTag()).path;  // btn_remove 버튼을 포함하고 있는 이미지의 tag를 저장
                uploadImageHashMap.put(imageTag, false);
                advertisingRichEditor.onImageUploadFailed(imageTag);
                isUploadImageCompeleteHashMap.remove(imageTag);
                advertisingRichEditor.getParentView().removeView(((RelativeLayout) view.getParent()));
            });
        });
        findViewById(R.id.action_insert_link).setOnClickListener(v -> {
            createURLCreateDialog();
        });
        findViewById(R.id.action_erase).setOnClickListener(v -> SnackbarUtil.makeLongSnackbarActionYes(advertisingRichEditor, getString(R.string.erase_button_pressed), this::eraseEditorText));
        findViewById(R.id.action_blockquote).setOnClickListener(v -> advertisingRichEditor.updateTextStyle(EditorTextStyle.BLOCKQUOTE));
        findViewById(R.id.action_color).setOnClickListener(view -> createColorPicker());

        advertisingRichEditor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                // Toast.makeText(EditorTestActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            // 갤러리에서 이미지를 선택하고 호출되는 insertImage() 메소드 안에서 인자값을 넘겨받는 메소드
            @Override
            public void onUpload(Bitmap bitmap, String uuid) {
                uploadImageHashMap.put(uuid, true);
                isUploadImageCompeleteHashMap.put(uuid, false);
                Observable.defer(() -> {
                    File imageFile = ImageUtil.changeBMPtoFILE(bitmap, uuid, 1, context);
                    return Observable.just(imageFile);
                })
                        .subscribeOn(Schedulers.io())
                        .subscribe(imageFile -> {
                            if (uploadImageHashMap.containsKey(uuid) && uploadImageHashMap.get(uuid)) {
                                articleEditPresenter.uploadImage(imageFile, uuid);
                                isUploadImageCompeleteHashMap.put(uuid, true);
                            }
                        }, throwable -> {
                            new Handler().post(() -> {
                                advertisingRichEditor.onImageUploadFailed(uuid);
                                ToastUtil.makeLongToast(context, R.string.fail_upload);
                                isUploadImageCompeleteHashMap.put(uuid, true);
                            });

                        });
            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                return null;
            }
        });
        advertisingRichEditor.render();
    }

    private void createURLCreateDialog() {
        final AlertDialog.Builder inputAlert = new AlertDialog.Builder(this);
        inputAlert.setTitle("링크 추가");
        final EditText userInput = new EditText(this);
        userInput.setHint("주소를 입력해주세요");
        userInput.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
        inputAlert.setView(userInput);
        inputAlert.setPositiveButton("삽입", (dialog, which) -> {
            String userInputValue = userInput.getText().toString();
            advertisingRichEditor.insertLink(checkUrl(userInputValue));
        });
        inputAlert.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = inputAlert.create();
        alertDialog.show();
    }

    public String checkUrl(String url) {
        String patter = "^(http|https|ftp)://.*$";
        if (url.matches(patter)) {
            return url;
        } else {
            return "http://" + url;
        }
    }

    private void eraseEditorText() {
        advertisingRichEditor.clearAllContents();
    }

    public void createColorPicker() {
        new ColorPickerPopup.Builder(context)
                .initialColor(Color.RED) // 색초기화
                .enableAlpha(true)
                .okTitle("선택")
                .cancelTitle("취소")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(findViewById(android.R.id.content), new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        advertisingRichEditor.updateTextColor(colorHex(color));
                    }

                    @Override
                    public void onColor(int color, boolean fromUser) {

                    }
                });
    }

    private String colorHex(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "#%02X%02X%02X", r, g, b);
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
    public void setPresenter(ArticleEditPresenter presenter) {
        articleEditPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onClickEditButton() {

    }

    @Override
    public void onArticleDataReceived(Article article) {

    }

    @Override
    public void goToArticleActivity(Article article) {

    }

    @Override
    public void showUploadImage(String url, String uploadImageId) {

    }

    @Override
    public void showFailUploadImage(String uploadImageId) {

    }
}
