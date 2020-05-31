package in.koreatech.koin.ui.lostfound;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.data.network.interactor.LostAndFoundRestInteractor;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.network.entity.LostItem;
import in.koreatech.koin.util.LoadImageFromUrl;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.lostfound.presenter.LostFoundEditContract;
import in.koreatech.koin.ui.lostfound.presenter.LostFoundEditPresenter;

/**
 * 분실물 게시판 글 수정 및 작성
 */
public class LostFoundEditActivity extends KoinNavigationDrawerActivity implements CompoundButton.OnCheckedChangeListener, Html.ImageGetter, LostFoundEditContract.View {
    public static final String TAG = "LostFoundEditActivity";
    public static final int CREATE_MODE = 0;
    public static final int EDIT_MODE = 1;
    private final Calendar calendar = Calendar.getInstance();
    private int mode;
    private LostItem lostItem;
    private int lostDateYear;
    private int lostDateMonth;
    private int lostDateDay;
    private LostFoundEditPresenter lostAndFoundPresenter;

    @BindView(R.id.lostfound_create_nestedscrollview)
    NestedScrollView lostfoundCreateNestedScrollView;
    @BindView(R.id.lostfound_create_lostdate_textview)
    TextView lostfoundCreateLostdateTextview;
    @BindView(R.id.lostfound_create_date_textview)
    TextView lostFoundCreateDateTextView;
    @BindView(R.id.lostfound_create_placename_textview)
    TextView lostFoundCreatePlaceNameTextView;
    @BindView(R.id.lostfound_create_get_radiobutton)
    RadioButton lostFoundCreateGetRadioButton;
    @BindView(R.id.lostfound_create_found_radiobutton)
    RadioButton lostFoundCreateFoundRadioButton;
    @BindView(R.id.lostfound_create_phone_public_radiobutton)
    RadioButton lostFoundCreatePhonePublicRadioButton;
    @BindView(R.id.lostfound_create_phone_private_radiobutton)
    RadioButton lostFoundCreatePhonePraivateRadioButton;
    @BindView(R.id.lostfound_create_phone_num_ediitext)
    EditText lostFoundCreatePhoneNumEditText;
    @BindView(R.id.lostfound_create_title_edittext)
    EditText lostFoundCreateTitleEditText;
    @BindView(R.id.lostfound_create_placename_edittext)
    EditText lostFoundCreatePlaceNameEditText;
    @BindView(R.id.lostfound_create_content_ediitext)
    EditText lostFoundCreateContentEditText;
    private ArrayList<String> imageURL;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lostfound_create);
        ButterKnife.bind(this);
        mode = getIntent().getIntExtra("MODE", CREATE_MODE);
        init();
    }

    public void init() {
        new LostFoundEditPresenter(this, new LostAndFoundRestInteractor());
        lostItem = new LostItem();
        lostItem.setType(0);
        lostItem.setPhoneOpen(false);
        lostItem.setContent("");
        lostItem.setPhone("");
        setPhoneNumber(lostItem.isPhoneOpen());
        imageURL = new ArrayList<>();
        lostDateYear = calendar.get(Calendar.YEAR);
        lostDateMonth = calendar.get(Calendar.MONTH) + 1;
        lostDateDay = calendar.get(Calendar.DAY_OF_MONTH);
        updateDateTextview(lostDateYear, lostDateMonth, lostDateDay);
        if (mode == EDIT_MODE) {
            lostItem = (LostItem) getIntent().getSerializableExtra("LOST_ITEM");
            setLostItem(lostItem);
        }
        lostFoundCreateGetRadioButton.setOnCheckedChangeListener(this);
        lostFoundCreateFoundRadioButton.setOnCheckedChangeListener(this);
        lostFoundCreatePhonePublicRadioButton.setOnCheckedChangeListener(this);
        lostFoundCreatePhonePraivateRadioButton.setOnCheckedChangeListener(this);
        setKoreanTextViewByType(lostItem.getType());
        contentEditTextChangedListener();
    }

    /**
     * 수정할때 LostItem 을 통해 내용을 설정한다.
     *
     * @param lostItem
     */
    public void setLostItem(LostItem lostItem) {
        if (lostItem == null) return;
        if (lostItem.getDate() != null) {
            try {
                StringTokenizer stringTokenizer = new StringTokenizer(lostItem.getDate(), "-");
                lostDateYear = Integer.parseInt(stringTokenizer.nextToken());
                lostDateMonth = Integer.parseInt(stringTokenizer.nextToken());
                lostDateDay = Integer.parseInt(stringTokenizer.nextToken());
                updateDateTextview(lostDateYear, lostDateMonth, lostDateDay);
            } catch (NumberFormatException e) {
                lostDateYear = calendar.get(Calendar.YEAR);
                lostDateMonth = calendar.get(Calendar.MONTH) + 1;
                lostDateDay = calendar.get(Calendar.DAY_OF_MONTH);
                updateDateTextview(lostDateYear, lostDateMonth, lostDateDay);
                Log.e(TAG, "setLostItem: " + e.getMessage());
            }
            if (lostItem.isPhoneOpen()) {
                lostFoundCreatePhonePublicRadioButton.setChecked(true);
                lostFoundCreatePhonePraivateRadioButton.setChecked(false);
                lostFoundCreatePhoneNumEditText.setText(lostItem.getPhone());
            } else {
                lostFoundCreatePhonePublicRadioButton.setChecked(false);
                lostFoundCreatePhonePraivateRadioButton.setChecked(true);
            }
            if (lostItem.getTitle() != null)
                lostFoundCreateTitleEditText.setText(lostItem.getTitle());
            if (lostItem.getLocation() != null)
                lostFoundCreatePlaceNameEditText.setText(lostItem.getLocation());
        }
        if (lostItem.getLocation() != null)
            lostFoundCreatePlaceNameEditText.setText(lostItem.getLocation());
        if (lostItem.getContent() != null) {
            Spanned spanned = Html.fromHtml(lostItem.getContent(), this, null);
            lostFoundCreateContentEditText.setText(spanned);
        }
        setPhoneNumber(lostItem.isPhoneOpen());
        setKoreanTextViewByType(lostItem.getType());
    }

    /**
     * 뒤로가기 버튼을 눌렀을때 취소를 누른건과 같이 동작한다.
     */
    @Override
    public void onBackPressed() {
        onClickedCancelButton();
    }

    @OnClick(R.id.lostfound_create_lostdate_textview)
    public void onClickedLostDateTextview(View view) {
        setDateTextviewFromDatePicker();
    }

    /**
     * 날짜 TextView를 업데이트 한다.
     *
     * @param year  년도
     * @param month 월
     * @param day   일
     */
    public void updateDateTextview(int year, int month, int day) {
        String date = String.format("%04d-%02d-%02d", year, month, day);
        lostfoundCreateLostdateTextview.setText(date);
    }

    public void setKoreanTextViewByType(int type) {
        if (type == 0) {
            lostFoundCreateDateTextView.setText("습득일");
            lostFoundCreatePlaceNameTextView.setText("습득장소");
            lostFoundCreatePlaceNameEditText.setHint("습득장소를 입력해주세요");
            lostFoundCreateFoundRadioButton.setChecked(false);
            lostFoundCreateGetRadioButton.setChecked(true);
        } else {
            lostFoundCreateDateTextView.setText("분실일");
            lostFoundCreatePlaceNameTextView.setText("분실장소");
            lostFoundCreatePlaceNameEditText.setHint("분실장소를 입력해주세요");
            lostFoundCreateFoundRadioButton.setChecked(true);
            lostFoundCreateGetRadioButton.setChecked(false);
        }
        lostItem.setType(type);
    }

    public void setPhoneNumber(boolean isPublic) {
        lostItem.setPhoneOpen(isPublic);
        if (isPublic) {
            lostFoundCreatePhoneNumEditText.setFocusableInTouchMode(true);
            if (lostItem.getPhone() != null) {
                lostFoundCreatePhoneNumEditText.setText(lostItem.getPhone());
            } else if (UserInfoSharedPreferencesHelper.getInstance().loadUser() != null && UserInfoSharedPreferencesHelper.getInstance().loadUser().getPhoneNumber() != null) {
                lostFoundCreatePhoneNumEditText.setText(UserInfoSharedPreferencesHelper.getInstance().loadUser().getPhoneNumber());
            }
        } else {
            lostFoundCreatePhoneNumEditText.setFocusable(false);
            lostItem.setPhone(lostFoundCreatePhoneNumEditText.getText().toString().trim());
            lostFoundCreatePhoneNumEditText.getText().clear();
        }
    }


    public void setDateTextviewFromDatePicker() {

        DatePickerDialog datePicker = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            lostDateYear = year;
            lostDateMonth = monthOfYear + 1;
            lostDateDay = dayOfMonth;
            updateDateTextview(lostDateYear, lostDateMonth, lostDateDay);
        }, lostDateYear, lostDateMonth - 1, lostDateDay);
        datePicker.show();
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId()) {
            onClickedCancelButton();
        } else if (id == AppBarBase.getRightButtonId()) {
            submitContent();
        }
    }

    public void submitContent() {
        LostItem submitLostItem = new LostItem();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(lostFoundCreateContentEditText.getText().toString().trim());
        String content;
        String title = lostFoundCreateTitleEditText.getText().toString().trim();
        String lostPlace = lostFoundCreatePlaceNameEditText.getText().toString().trim();
        String lostDate = lostfoundCreateLostdateTextview.getText().toString();
        String phoneNumber = lostFoundCreatePhoneNumEditText.getText().toString().trim();
        boolean isPhoneValid = lostItem.isPhoneOpen();
        int type = lostItem.getType();
        content = Html.toHtml(spannableStringBuilder).trim();
        content = addImageSpan(content, imageURL);
        submitLostItem.setType(type);
        submitLostItem.setContent(content);
        submitLostItem.setTitle(title);
        submitLostItem.setLocation(lostPlace);
        submitLostItem.setDate(lostDate);
        submitLostItem.setPhoneOpen(isPhoneValid);
        if (isPhoneValid)
            submitLostItem.setPhone(phoneNumber);

        if (mode == CREATE_MODE) {
            lostAndFoundPresenter.createLostItem(submitLostItem);
        } else if (mode == EDIT_MODE) {
            lostAndFoundPresenter.updateLostItem(lostItem.getId(), submitLostItem);
        }

    }

    public void onClickedCancelButton() {
        SnackbarUtil.makeLongSnackbarActionYes(lostfoundCreateNestedScrollView, "취소하시겠습니까?", this::finish);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked)
            switch (compoundButton.getId()) {
                case R.id.lostfound_create_get_radiobutton:
                    setKoreanTextViewByType(0);
                    break;
                case R.id.lostfound_create_found_radiobutton:
                    setKoreanTextViewByType(1);
                    break;
                case R.id.lostfound_create_phone_public_radiobutton:
                    setPhoneNumber(true);
                    break;
                case R.id.lostfound_create_phone_private_radiobutton:
                    setPhoneNumber(false);
                    break;
            }
    }

    public String addImageSpan(String content, ArrayList<String> imageUrl) {
        if (content.contains("&#65532;")) {
            for (int i = 0; i < imageUrl.size(); i++) {
                String imageUrlSpan = "<img src=\"" + imageUrl.get(i) + "\">";
                content = content.replaceFirst("&#65532;", imageUrlSpan);
            }
            return content;
        }
        return content;
    }

    @Override
    public Drawable getDrawable(String source) {
        imageURL.add(source);
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.drawable.image_no_image);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
        new LoadImageFromUrl(lostFoundCreateContentEditText, this).execute(source, d);
        return d;
    }

    public void contentEditTextChangedListener() {
        lostFoundCreateContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(lostItem.getContent())) {
                    lostItem.setContent(s.toString());
                    int number = getCharNumber(lostItem.getContent(), (char) 65532);
                    if (number < imageURL.size())
                        deleteImageUrlAtArrayList(lostItem.getContent(), start);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });
    }


    public int getCharNumber(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c)
                count++;
        }
        return count;
    }

    public int getCharNumber(String str, int start, char c) {
        int count = 0;
        for (int i = 0; i < start; i++) {
            if (str.charAt(i) == c)
                count++;
        }
        return count;
    }

    public void deleteImageUrlAtArrayList(String content, int start) {
        int number;
        if (!content.isEmpty() && !imageURL.isEmpty()) {
            number = getCharNumber(content, start, (char) 65532);
            imageURL.remove(number);
        }
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
    public void showSuccessUpdate(LostItem lostItem) {
        finish();
        ToastUtil.getInstance().makeShort("수정되었습니다.");
    }

    @Override
    public void showSuccessCreate(LostItem lostItem) {
        Intent intent = new Intent(this, LostFoundDetailActivity.class);
        intent.putExtra("ID", lostItem.getId());
        startActivity(intent);
        finish();
        ToastUtil.getInstance().makeShort("생성되었습니다.");
    }

    @Override
    public void showMessage(int message) {
        ToastUtil.getInstance().makeShort(message);
    }

    @Override
    public void setPresenter(LostFoundEditPresenter presenter) {
        this.lostAndFoundPresenter = presenter;
    }
}
