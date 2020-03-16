package in.koreatech.koin.ui.userinfo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.ui.userinfo.presenter.UserInfoEditContract;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.util.FilterUtil;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.util.TimeUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.userinfo.presenter.UserInfoEditPresenter;

public class UserInfoEditedActivity extends KoinNavigationDrawerActivity implements UserInfoEditContract.View {
    private final String TAG = "UserInfoEditedActivity";
    private Context context;

    private User user;
    private UserInfoEditPresenter userInfoEditPresenter;


    private String changedNickname;
    private boolean isNicknameChecked;

    private ArrayList<String> ARRAY_MAJOR;

    /* View Component */

    //default data
    @BindView(R.id.userinfoedited_textview_id)
    public TextView userinfoeditedTextviewId;
    @BindView(R.id.userinfoedited_edittext_name)
    public EditText userinfoeditedEdittextName;
    @BindView(R.id.userinfoedited_edittext_nick_name)
    public EditText userinfoeditedEdittextNickname;
    @BindView(R.id.userinfoedited_textview_anonymous_nick_name)
    public TextView userinfoeditedTextviewAnonymousNickName;
    @BindView(R.id.userinfoedited_button_nickname_check)
    public LinearLayout userinfoeditedButtonNicknameCheck;
    @BindView(R.id.userinfoedited_edittext_phone_num_1)
    public EditText userinfoeditedEdittextPhone1;
    @BindView(R.id.userinfoedited_edittext_phone_num_2)
    public EditText userinfoeditedEdittextPhone2;
    @BindView(R.id.userinfoedited_edittext_phone_num_3)
    public EditText userinfoeditedEdittextPhone3;
    @BindView(R.id.userinfoedited_radiobutton_gender_woman)
    public RadioButton userinfoeditedRadiobuttonGenderWoman;
    @BindView(R.id.userinfoedited_radiobutton_gender_man)
    public RadioButton userinfoeditedRadiobuttonGenderMan;

    //univ. data
    @BindView(R.id.userinfoedited_edittext_student_id)
    public EditText userinfoeditedEdittextStudentId;
    @BindView(R.id.userinfoedited_edittext_major)
    public EditText userinfoeditedTextviewMajor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edited);
        ButterKnife.bind(this);
        this.context = this;

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();

        readUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    public void onBackPressed() {
        SnackbarUtil.makeLongSnackbarActionYes(this.userinfoeditedTextviewId, getString(R.string.back_button_pressed), new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    private void init() {

        new UserInfoEditPresenter(this);


        this.userinfoeditedEdittextName.setFilters(new InputFilter[]{new FilterUtil(FilterUtil.FILTER_E_H), new InputFilter.LengthFilter(20)});
        this.userinfoeditedEdittextNickname.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});

        changedNickname = "";
        isNicknameChecked = false;

        ARRAY_MAJOR = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.major)));
    }

    @OnEditorAction({R.id.userinfoedited_edittext_name, R.id.userinfoedited_edittext_nick_name,
            R.id.userinfoedited_edittext_phone_num_1, R.id.userinfoedited_edittext_phone_num_2,
            R.id.userinfoedited_edittext_phone_num_3, R.id.userinfoedited_edittext_student_id})
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        View view;
        switch (v.getId()) {
            case R.id.userinfoedited_edittext_name:
                this.userinfoeditedEdittextNickname.requestFocus();
                break;
            case R.id.userinfoedited_edittext_nick_name:
                this.userinfoeditedEdittextPhone1.requestFocus();
                break;
            case R.id.userinfoedited_edittext_phone_num_1:
                this.userinfoeditedEdittextPhone2.requestFocus();
                break;
            case R.id.userinfoedited_edittext_phone_num_2:
                this.userinfoeditedEdittextPhone3.requestFocus();
                break;
            case R.id.userinfoedited_edittext_phone_num_3:
                this.userinfoeditedEdittextStudentId.requestFocus();
                break;
            case R.id.userinfoedited_edittext_student_id:
                view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                checkStudentID(this.userinfoeditedEdittextStudentId.getText().toString().trim());
                break;
            default:
                break;
        }
        return false;
    }


    @OnClick(R.id.userinfoedited_button_nickname_check)
    public void onClickNicknameCheckButton() {
        String beforeNickName = UserInfoSharedPreferencesHelper.getInstance().loadUser().getUserNickName();
        String currentNickName = this.userinfoeditedEdittextNickname.getText().toString().trim().replace(" ", "");

        if (beforeNickName == null && currentNickName.isEmpty()) {
            ToastUtil.getInstance().makeShort("닉네임을 입력해주세요");
            return;
        }
        if (beforeNickName == null || !beforeNickName.equals(currentNickName)) {
            this.userInfoEditPresenter.getUserCheckNickName(currentNickName);
            this.userinfoeditedEdittextNickname.setText(currentNickName);
        } else {
            ToastUtil.getInstance().makeShort("기존 닉네임과 동일 합니다.");
        }
    }


    @OnClick(R.id.koin_base_app_bar)
    public void onClickBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId())
            checkInputUserStudentId();
    }

    private boolean checkInputUserStudentId() {
        if (!FormValidatorUtil.validateStringIsEmpty(this.userinfoeditedEdittextStudentId.getText().toString().trim()) &&
                !checkStudentID(this.userinfoeditedEdittextStudentId.getText().toString().trim())) {
            ToastUtil.getInstance().makeShort("올바른 형태의 학번을 입력해주세요");

            return true;
        }

        //학번이 입력이 안되어있는 경우
        if (this.userinfoeditedEdittextStudentId.getText().toString().trim().isEmpty())
            checkNickName();

            //학번을 처음 입력할 경우
        else if (FormValidatorUtil.validateStringIsEmpty(UserInfoSharedPreferencesHelper.getInstance().loadUser().getStudentId())) {
            SnackbarUtil.makeLongSnackbarActionYes(this.userinfoeditedEdittextStudentId, "학번은 변경이 불가능합니다. 저장하시겠어요?", new Runnable() {
                @Override
                public void run() {
                    checkNickName();
                }
            });
        } else { //학번이 입력되어 있을 경우
            checkNickName();
        }

        return true;
    }

    private void checkNickName() {
        String curNickname = "";
        //닉네임 검사
        if (!this.userinfoeditedEdittextNickname.getText().toString().trim().isEmpty())
            curNickname = this.userinfoeditedEdittextNickname.getText().toString().trim();
        if (curNickname.isEmpty() && (this.user.getUserNickName() == null))
            checkInputUserData();
        else if ((!curNickname.isEmpty()) && (this.user.getUserNickName() == null) && !isNicknameChecked)
            ToastUtil.getInstance().makeShort("닉네임 중복 검사를 해주세요");
        else if ((!curNickname.isEmpty()) && (this.user.getUserNickName() == null) && isNicknameChecked)
            checkInputUserData();
        else if (this.user.getUserNickName().equals(curNickname)) {
            //기존 닉네임 여부 검사 / 입력 안 한 경우
            checkInputUserData();
        } else if (changedNickname.equals(curNickname) && isNicknameChecked) {
            //닉네임 검사 버튼 누른 경우
            checkInputUserData();
        } else {
            ToastUtil.getInstance().makeShort("닉네임 중복 검사를 해주세요");
        }
    }

    private void checkInputUserData() {
        if (FormValidatorUtil.validateStringIsEmpty(UserInfoSharedPreferencesHelper.getInstance().loadUser().getUserName())) {
            SnackbarUtil.makeLongSnackbarActionYes(this.userinfoeditedTextviewId, "이름은 변경이 불가능합니다. 저장하시겠어요?", new Runnable() {
                @Override
                public void run() {
                    saveUserInfo();
                }
            });
        } else { //이름이 입력되어 있을 경우
            saveUserInfo();
        }

    }

    private void readUser() {
        this.user = UserInfoSharedPreferencesHelper.getInstance().loadUser();

        this.userinfoeditedTextviewId.setText(isNull(this.user.getUserId()) + "@koreatech.ac.kr");
        this.userinfoeditedTextviewAnonymousNickName.setText(isNull(this.user.getAnonymousNickName()));

        if (!FormValidatorUtil.validateStringIsEmpty(this.user.getUserName())) {
            this.userinfoeditedEdittextName.setText(this.user.getUserName());
            this.userinfoeditedEdittextName.setEnabled(false);
            this.userinfoeditedEdittextName.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        } else {
            this.userinfoeditedEdittextName.setText(isNull(this.user.getUserName()));
        }

        this.userinfoeditedEdittextNickname.setText(isNull(this.user.getUserNickName()));
        changedNickname = isNull(this.user.getUserNickName());

        if (FormValidatorUtil.validateStringIsEmpty(this.user.getPhoneNumber()) || this.user.getPhoneNumber().equals("")) {
            this.userinfoeditedEdittextPhone1.setText("");
            this.userinfoeditedEdittextPhone2.setText("");
            this.userinfoeditedEdittextPhone3.setText("");
        } else {
            String[] phone = this.user.getPhoneNumber().split("-");

            EditText[] phoneNumberArray = {this.userinfoeditedEdittextPhone1, this.userinfoeditedEdittextPhone2, this.userinfoeditedEdittextPhone3};
            for (int i = 0; i < phone.length; i++) {
                phoneNumberArray[i].setText(phone[i]);
            }
        }

        if (this.user.getGender() == 0) {
            this.userinfoeditedRadiobuttonGenderMan.setChecked(true);
            this.userinfoeditedRadiobuttonGenderWoman.setChecked(false);
        } else {
            this.userinfoeditedRadiobuttonGenderMan.setChecked(false);
            this.userinfoeditedRadiobuttonGenderWoman.setChecked(true);
        }

        if (!FormValidatorUtil.validateStringIsEmpty(this.user.getStudentId())) {
            this.userinfoeditedEdittextStudentId.setEnabled(false);
            this.userinfoeditedEdittextStudentId.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        }

        this.userinfoeditedEdittextStudentId.setText(isNull(this.user.getStudentId()));


        this.userinfoeditedTextviewMajor.setText("");
        this.userinfoeditedTextviewMajor.setHint("학번 입력시 자동 입력");

        if (!FormValidatorUtil.validateStringIsEmpty(this.user.getMajor())) {
            for (int i = 0; i < ARRAY_MAJOR.size(); i++) {
                if (this.user.getMajor().equals(ARRAY_MAJOR.get(i))) {
                    this.userinfoeditedTextviewMajor.setText(ARRAY_MAJOR.get(i));
                }
            }
        }
    }

    private String isNull(String str) {
        return FormValidatorUtil.validateStringIsEmpty(str) ? "" : str;
    }

    public void saveUserInfo() {
        User changeUserData = new User();

        changeUserData.setUserName(this.userinfoeditedEdittextName.getText().toString().replaceAll(" ", ""));
        String nickname = this.userinfoeditedEdittextNickname.getText().toString().replaceAll(" ", "");

        if ((this.user.getUserNickName() != null) && !this.user.getUserNickName().equals(nickname))
            changeUserData.setUserNickName(nickname);
        else if ((this.user.getUserNickName() == null) && !nickname.isEmpty())
            changeUserData.setUserNickName(nickname);
        else
            changeUserData.setUserNickName(null);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.userinfoeditedEdittextPhone1.getText().toString()).append("-");
        stringBuffer.append(this.userinfoeditedEdittextPhone2.getText().toString()).append("-");
        stringBuffer.append(this.userinfoeditedEdittextPhone3.getText().toString());
        changeUserData.setPhoneNumber(stringBuffer.length() > 4 ? stringBuffer.toString() : "");

        if (this.userinfoeditedRadiobuttonGenderMan.isChecked()) {
            changeUserData.setGender(0);
        } else {
            changeUserData.setGender(1);
        }


        if (!this.userinfoeditedEdittextStudentId.getText().toString().trim().isEmpty())
            changeUserData.setStudentId(this.userinfoeditedEdittextStudentId.getText().toString().trim());

        changeUserData.setMajor(this.userinfoeditedTextviewMajor.getText().toString());

        if (changeUserData.getPhoneNumber().isEmpty())
            changeUserData.setPhoneNumber(null);
        if (changeUserData.getMajor().isEmpty())
            changeUserData.setMajor(null);
        if (changeUserData.getUserName().isEmpty())
            changeUserData.setUserName(null);

        this.userInfoEditPresenter.updateUserInfo(changeUserData);
    }

    @Override
    public void showCheckNickNameSuccess() {
        // 닉네임 사용가능시 사용가능 Toast message onNicknameCheckSuccesst 실행
        ToastUtil.getInstance().makeShort("사용가능한 닉네임입니다.");
        onNicknameCheckSuccess(this.userinfoeditedEdittextNickname.getText().toString());
        this.userinfoeditedEdittextNickname.getBackground().setColorFilter(null);
    }

    @Override
    public void showCheckNickNameFail() {
        // 닉네임 사용불가시 사용불가능 Toast message 후 밑줄 빨간색으로 변경
        ToastUtil.getInstance().makeShort("기존에 중복된 닉네임입니다.");
        this.userinfoeditedEdittextNickname.getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void showConfirm() {
        ToastUtil.getInstance().makeShort("정보가 수정되었습니다.");
        finish();
    }

    @Override
    public void showConfirmFail() {
        ToastUtil.getInstance().makeShort("정보 수정에 실패하였습니다.");
    }

    @Override
    public void setPresenter(UserInfoEditPresenter presenter) {
        this.userInfoEditPresenter = presenter;

    }

    private boolean checkStudentID(String studentId) {
        if (studentId.trim().length() < 10) {
            this.userinfoeditedTextviewMajor.setText("");
            this.userinfoeditedTextviewMajor.setHint("학번 입력시 자동 입력");
            return false;
        }

        int year = Integer.parseInt(studentId.trim().substring(0, 4));
        if (1992 > year || year > Integer.parseInt(TimeUtil.getCurrentYear())) {
            return false;
        }

        int departmentNum = Integer.parseInt(studentId.trim().substring(4, 7));
        switch (departmentNum) {
            case 120: //기계공학부
                this.userinfoeditedTextviewMajor.setText("기계공학부");
                break;
            case 135: //컴퓨터공학부(인터넷미디어공학부)
            case 136: //컴퓨터공학부
                this.userinfoeditedTextviewMajor.setText("컴퓨터공학부");
                break;
            case 140: //메카트로닉스공학부
                this.userinfoeditedTextviewMajor.setText("메카트로닉스공학부");
                break;
            case 161: //전기전자통신공학부
                this.userinfoeditedTextviewMajor.setText("전기전자통신공학부");
                break;
            case 151: //디자인건축공학부
                this.userinfoeditedTextviewMajor.setText("디자인건축공학부");
                break;
            case 174: //에너지신소재화학공학부
                this.userinfoeditedTextviewMajor.setText("에너지신소재화학공학부");
                break;
            case 180: //산업경영학부
                this.userinfoeditedTextviewMajor.setText("산업경영학부");
                break;
            default:
                this.userinfoeditedTextviewMajor.setText("");
                this.userinfoeditedTextviewMajor.setHint("학번 입력시 자동 입력");
                return false;

        }

        return true;
    }

    public void onNicknameCheckSuccess(String nickname) {
        isNicknameChecked = true;
        changedNickname = nickname;
    }
}