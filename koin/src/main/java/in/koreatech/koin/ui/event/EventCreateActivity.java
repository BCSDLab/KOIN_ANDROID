package in.koreatech.koin.ui.event;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Event;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.interactor.EventRestInteractor;
import in.koreatech.koin.ui.board.KoinEditorActivity;
import in.koreatech.koin.ui.event.presenter.EventCreateContract;
import in.koreatech.koin.ui.event.presenter.EventCreatePresenter;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.util.TimeUtil;

// TODO: 도움말을 AlertDialog로 출력

public class EventCreateActivity extends KoinEditorActivity implements EventCreateContract.View, TextWatcher, AdapterView.OnItemSelectedListener {
    private final static String TAG = "EventCreateActivity";
    private Calendar SelectStartDate;
    private Calendar SelectEndDate;
    private DatePickerDialog.OnDateSetListener dataPicker;
    private DatePickerDialog.OnDateSetListener dataPicker2;
    private boolean isClickedQuestion = false;
    private EventCreatePresenter eventCreatePresenter;
    private InputMethodManager inputMethodManager;

    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase koinBaseAppbar;
//    @BindView(R.id.event_create_shops_spinner)
    Spinner shopListSpinner;
    @BindView(R.id.event_create_question_mark_textview)
    TextView questionMark;
    @BindView(R.id.event_detail_title_edittext)
    EditText createTitleEditText;
    @BindView(R.id.event_detail_event_title_edittext)
    EditText eventTitleEditText;
    @BindView(R.id.event_create_calender_startdate_textview)
    TextView startDateTextview;
    @BindView(R.id.event_create_calender_enddate_textview)
    TextView endDateTextview;
//    @BindView(R.id.event_create_question_info_frame_layout)
//    FrameLayout questionInfoFrameLayout;

    private boolean isEdit;
    private int articleId;
    private int shopId;
    private HashMap<String, Integer> shopIdNameHashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.event_create_activity);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setPresenter(new EventCreatePresenter(this, new EventRestInteractor()));
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        shopIdNameHashMap = new HashMap<>();
        shopListSpinner = findViewById(R.id.event_create_shops_spinner);

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
        return R.id.event_create_content;
    }

    @Override
    protected boolean isEditable() {
        return true;
    }

    @Override
    protected void successImageProcessing(File imageFile, String uuid) {
        eventCreatePresenter.uploadImage(imageFile, uuid);
    }

    void init() {
        koinBaseAppbar.setTitleText("홍보게시판");
        if (isEdit) {
            loadEventDetail();
        } else {
            startDateTextview.setText(TimeUtil.getDeviceCreatedDateOnlyString());
            endDateTextview.setText(TimeUtil.getDeviceCreatedDateOnlyString());
            eventCreatePresenter.getMyShopList();
        }
        calenderCheck();
        koinBaseAppbar.setLeftButtonText("취소");
        koinBaseAppbar.setRightButtonText("등록");

        // 상점 Spinner Adapter
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.event_create_spinner_custom, new ArrayList<>(shopIdNameHashMap.keySet()));
        spinnerAdapter.setDropDownViewResource(R.layout.event_create_spinner_item );
        shopListSpinner.setAdapter(spinnerAdapter);
        shopListSpinner.setOnItemSelectedListener(this);

        // 자동으로 제목에 포커스를 주면서 키보드 올리기
        createTitleEditText.setFocusableInTouchMode(true);
        createTitleEditText.requestFocus();
        inputMethodManager.showSoftInput(createTitleEditText, 0);
    }

    private void loadEventDetail() {
        articleId = getIntent().getIntExtra("ID", 0);
        createTitleEditText.setText(getIntent().getStringExtra("TITLE"));
        eventTitleEditText.setText(getIntent().getStringExtra("EVENT_TITLE"));
        startDateTextview.setText(getIntent().getStringExtra("START_DATE"));
        endDateTextview.setText(getIntent().getStringExtra("END_DATE"));
        renderEditor(renderHtmltoString(getIntent().getStringExtra("CONTENT")));
        shopId = getIntent().getIntExtra("SHOP_ID", 0);
        eventCreatePresenter.getShopName(shopId);
    }

    void calenderCheck() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SelectStartDate = Calendar.getInstance();
        SelectEndDate = Calendar.getInstance();

        if (isEdit) {
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

            String startDate = "" + year
                    + (((month + 1) < 10) ? ("-0" + (month + 1)) : ("-" + (month + 1)))
                    + ((dayOfMonth < 10) ? ("-0" + dayOfMonth) : ("-" + dayOfMonth));

            startDateTextview.setText(startDate);
        };

        // 종료 일자 캘린더 클릭 이벤트
        dataPicker2 = (view, year, month, dayOfMonth) -> {
            SelectEndDate.set(Calendar.YEAR, year);
            SelectEndDate.set(Calendar.MONTH, month);
            SelectEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String endDate = "" + year
                    + (((month + 1) < 10) ? ("-0" + (month + 1)) : ("-" + (month + 1)))
                    + ((dayOfMonth < 10) ? ("-0" + dayOfMonth) : ("-" + dayOfMonth));

            endDateTextview.setText(endDate);
        };
    }

    // 시작 일자를 선택하기 위한 달력 표시
    @OnClick({R.id.event_calender_reck_layout})
    void calender1OnClicked() {
        new DatePickerDialog(EventCreateActivity.this, dataPicker,
                SelectStartDate.get(Calendar.YEAR),
                SelectStartDate.get(Calendar.MONTH),
                SelectStartDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    // 종료 일자를 선택하기 위한 달력 표시
    @OnClick({R.id.event_calender2_reck_layout})
    void calender2OnClicked() {
        new DatePickerDialog(EventCreateActivity.this, dataPicker2,
                SelectEndDate.get(Calendar.YEAR),
                SelectEndDate.get(Calendar.MONTH),
                SelectEndDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    // 도움말 버튼 클릭 이벤트
    @OnClick(R.id.event_create_question_mark_textview)
    public void questionMarkOnClicked() {
        if (!isClickedQuestion) {
            questionMark.setBackground(getResources().getDrawable(R.drawable.bg_ring_squash));
            questionMark.setTextColor(getResources().getColor(R.color.colorAccent));
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//            questionInfoFrameLayout.setVisibility(View.VISIBLE);
//            questionInfoFrameLayout.bringToFront();
            isClickedQuestion = true;
        } else {
            questionMark.setBackground(getResources().getDrawable(R.drawable.bg_circle_squash));
            questionMark.setTextColor(getResources().getColor(R.color.white));
            inputMethodManager.showSoftInput(createTitleEditText, 0);
//            questionInfoFrameLayout.setVisibility(View.INVISIBLE);
            isClickedQuestion = false;
        }
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickKoinBaseAppbar(View v) {
        int viewId = v.getId();
        if (viewId == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (viewId == AppBarBase.getRightButtonId())
            onClickEditButton();
    }

    @Override
    public void showLoading() {
        showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeLong(message);
    }

    @Override
    public void onBackPressed() {
        View view = this.getCurrentFocus();

        if (view != null) {
            Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(view.getWindowToken(), 0);
            SnackbarUtil.makeLongSnackbarActionYes(view, getString(R.string.back_button_pressed), this::finish);
        } else {
            finish();
        }
    }

    @Override
    public void onClickEditButton() {
        if (!isImageAllUploaded()) {
            ToastUtil.getInstance().makeShort("이미지 업로드 중입니다.");
            return;
        }
        if (FormValidatorUtil.validateStringIsEmpty(createTitleEditText.getText().toString())) {
            ToastUtil.getInstance().makeShort("제목을 입력하세요");
            return;
        }
        if (FormValidatorUtil.validateStringIsEmpty(eventTitleEditText.getText().toString())) {
            ToastUtil.getInstance().makeShort("홍보 문구를 입력하세요");
            return;
        }
        if (FormValidatorUtil.validateStringIsEmpty(getContent())) {
            ToastUtil.getInstance().makeShort("내용을 입력하세요");
            return;
        }

        String title = createTitleEditText.getText().toString().trim();
        String eventTitle = this.eventTitleEditText.getText().toString().trim();
        String content = getContentAsHTML();
        String startDate = startDateTextview.getText().toString();
        String endDate = endDateTextview.getText().toString();
        String thumbnail = getThumbnail();

        if (isEdit) {
            eventCreatePresenter.updateEvent(articleId, new Event(title, eventTitle, content, shopId, startDate, endDate, thumbnail));
        } else {
            eventCreatePresenter.createEvent(new Event(title, eventTitle, content, shopId, startDate, endDate, thumbnail));
        }
    }

    @Override
    public void onEventDataReceived(Event event) {
        goToEventActivity(event);
    }

    // 홍보글을 처음 작성할 때 점주가 보유한 상점 목록을 이름과 shopId를 맵핑
    @Override
    public void onMyShopListReceived(ArrayList<Store> shopArrayList) {
        if (!shopArrayList.isEmpty()) {
            for (Store store : shopArrayList) {
                shopIdNameHashMap.put(store.getName(), store.getUid());
            }
        }

        // Test
        shopIdNameHashMap.put("가장 맛있는 족발", 2);
        shopIdNameHashMap.put("노걸대 감자탕&가마삼겹 목천점", 17);
    }

    // 홍보글을 수정할 때 shopId로 가게 이름을 확인
    @Override
    public void onShopNameReceived(String shopName) {
        shopIdNameHashMap.put(shopName, shopId);
    }

    @Override
    public void goToEventActivity(Event event) {
        if (!isEdit) {
            Intent intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra("ID", event.getId());
            intent.putExtra("GRANT_EDIT", true);
            startActivity(intent);
        }

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
    public void setPresenter(EventCreatePresenter presenter) {
        this.eventCreatePresenter = presenter;
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//        String shopName = (String) shopListSpinner.getItemAtPosition(position);
//        if(shopIdNameHashMap != null && shopIdNameHashMap.containsKey(shopName)) {
//            shopId = shopIdNameHashMap.get(shopName);
//        }
        String selectedItem = shopListSpinner.getItemAtPosition(position).toString();
        Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}