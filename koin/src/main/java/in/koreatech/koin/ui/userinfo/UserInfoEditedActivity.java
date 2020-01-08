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
import in.koreatech.koin.core.appbar.AppbarBase;
import in.koreatech.koin.ui.userinfo.presenter.UserInfoEditContract;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.util.FilterUtil;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.util.TimeUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.userinfo.presenter.UserInfoEditPresenter;

/**
 * Created by hyerim on 2018. 7. 2....
 * Edited by yunjae on 2018. 8. 23.... check nickname
 */
public class UserInfoEditedActivity extends KoinNavigationDrawerActivity implements UserInfoEditContract.View {
    private final String TAG = "UserInfoEditedActivity";
    private Context context;

    private User mUser;
    private UserInfoEditContract.Presenter mUserInfoEditPresenter;


    private String changedNickname;
    private boolean isNicknameChecked;

    private ArrayList<String> ARRAY_MAJOR;

    /* View Component */

    //default data
    @BindView(R.id.userinfoedited_textview_id)
    public TextView mUserinfoeditedTextviewId;
    @BindView(R.id.userinfoedited_edittext_name)
    public EditText mUserinfoeditedEdittextName;
    @BindView(R.id.userinfoedited_edittext_nick_name)
    public EditText mUserinfoeditedEdittextNickname;
    @BindView(R.id.userinfoedited_textview_anonymous_nick_name)
    public TextView mUserinfoeditedTextviewAnonymousNickName;
    @BindView(R.id.userinfoedited_button_nickname_check)
    public LinearLayout mUserinfoeditedButtonNicknameCheck;
    @BindView(R.id.userinfoedited_edittext_phone_num_1)
    public EditText mUserinfoeditedEdittextPhone1;
    @BindView(R.id.userinfoedited_edittext_phone_num_2)
    public EditText mUserinfoeditedEdittextPhone2;
    @BindView(R.id.userinfoedited_edittext_phone_num_3)
    public EditText mUserinfoeditedEdittextPhone3;
    @BindView(R.id.userinfoedited_radiobutton_gender_woman)
    public RadioButton mUserinfoeditedRadiobuttonGenderWoman;
    @BindView(R.id.userinfoedited_radiobutton_gender_man)
    public RadioButton mUserinfoeditedRadiobuttonGenderMan;

    //univ. data
    @BindView(R.id.userinfoedited_edittext_student_id)
    public EditText mUserinfoeditedEdittextStudentId;
    @BindView(R.id.userinfoedited_edittext_major)
    public EditText mUserinfoeditedTextviewMajor;

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
        SnackbarUtil.makeLongSnackbarActionYes(mUserinfoeditedTextviewId, getString(R.string.back_button_pressed), new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    private void init() {

        new UserInfoEditPresenter(this);


        mUserinfoeditedEdittextName.setFilters(new InputFilter[]{new FilterUtil(FilterUtil.FILTER_E_H), new InputFilter.LengthFilter(20)});
        mUserinfoeditedEdittextNickname.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});

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
                mUserinfoeditedEdittextNickname.requestFocus();
                break;
            case R.id.userinfoedited_edittext_nick_name:
                mUserinfoeditedEdittextPhone1.requestFocus();
                break;
            case R.id.userinfoedited_edittext_phone_num_1:
                mUserinfoeditedEdittextPhone2.requestFocus();
                break;
            case R.id.userinfoedited_edittext_phone_num_2:
                mUserinfoeditedEdittextPhone3.requestFocus();
                break;
            case R.id.userinfoedited_edittext_phone_num_3:
                mUserinfoeditedEdittextStudentId.requestFocus();
                break;
            case R.id.userinfoedited_edittext_student_id:
                view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                checkStudentID(mUserinfoeditedEdittextStudentId.getText().toString().trim());
                break;
            default:
                break;
        }
        return false;
    }


    @OnClick(R.id.userinfoedited_button_nickname_check)
    public void onClickNicknameCheckButton() {
        String beforeNickName = UserInfoSharedPreferencesHelper.getInstance().loadUser().userNickName;
        String currentNickName = mUserinfoeditedEdittextNickname.getText().toString().trim().replace(" ","");

        if(beforeNickName==null&&currentNickName.isEmpty())
        {
            ToastUtil.getInstance().makeShort("닉네임을 입력해주세요");
            return;
        }
        if(beforeNickName==null||!beforeNickName.equals(currentNickName)) {
            mUserInfoEditPresenter.getUserCheckNickName(currentNickName);
            mUserinfoeditedEdittextNickname.setText(currentNickName);
        }
        else {
            ToastUtil.getInstance().makeShort("기존 닉네임과 동일 합니다.");
        }
    }



    @OnClick(R.id.koin_base_app_bar)
    public void onClickBaseAppbar(View v)
    {
        int id = v.getId();
        if(id == AppbarBase.getLeftButtonId())
            onBackPressed();
        else if(id == AppbarBase.getRightButtonId())
            checkInputUserStudentId();
    }

    private boolean checkInputUserStudentId() {
        if (!FormValidatorUtil.validateStringIsEmpty(mUserinfoeditedEdittextStudentId.getText().toString().trim()) &&
                !checkStudentID(mUserinfoeditedEdittextStudentId.getText().toString().trim())) {
            ToastUtil.getInstance().makeShort("올바른 형태의 학번을 입력해주세요");

            return true;
        }

        //학번이 입력이 안되어있는 경우
        if(mUserinfoeditedEdittextStudentId.getText().toString().trim().isEmpty())
            checkNickName();

        //학번을 처음 입력할 경우
        else if (FormValidatorUtil.validateStringIsEmpty(UserInfoSharedPreferencesHelper.getInstance().loadUser().studentId)) {
            SnackbarUtil.makeLongSnackbarActionYes(mUserinfoeditedEdittextStudentId, "학번은 변경이 불가능합니다. 저장하시겠어요?", new Runnable() {
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
        if(!mUserinfoeditedEdittextNickname.getText().toString().trim().isEmpty())
            curNickname = mUserinfoeditedEdittextNickname.getText().toString().trim();
        if(curNickname.isEmpty() && ( mUser.userNickName == null))
            checkInputUserData();
        else if((!curNickname.isEmpty()) &&  (mUser.userNickName == null) &&  !isNicknameChecked)
            ToastUtil.getInstance().makeShort( "닉네임 중복 검사를 해주세요");
        else if((!curNickname.isEmpty())&& (mUser.userNickName == null) && isNicknameChecked)
            checkInputUserData();
        else if (mUser.userNickName.equals(curNickname)) {
            //기존 닉네임 여부 검사 / 입력 안 한 경우
            checkInputUserData();
        } else if (changedNickname.equals(curNickname) && isNicknameChecked) {
            //닉네임 검사 버튼 누른 경우
            checkInputUserData();
        } else {
            ToastUtil.getInstance().makeShort( "닉네임 중복 검사를 해주세요");
        }
    }

    private void checkInputUserData() {
        if (FormValidatorUtil.validateStringIsEmpty(UserInfoSharedPreferencesHelper.getInstance().loadUser().userName)) {
            SnackbarUtil.makeLongSnackbarActionYes(mUserinfoeditedTextviewId, "이름은 변경이 불가능합니다. 저장하시겠어요?", new Runnable() {
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
        mUser = UserInfoSharedPreferencesHelper.getInstance().loadUser();

        mUserinfoeditedTextviewId.setText(isNull(mUser.userId) + "@koreatech.ac.kr");
        mUserinfoeditedTextviewAnonymousNickName.setText(isNull(mUser.anonymousNickName));

        if (!FormValidatorUtil.validateStringIsEmpty(mUser.userName)) {
            mUserinfoeditedEdittextName.setText(mUser.userName);
            mUserinfoeditedEdittextName.setEnabled(false);
            mUserinfoeditedEdittextName.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        } else {
            mUserinfoeditedEdittextName.setText(isNull(mUser.userName));
        }

        mUserinfoeditedEdittextNickname.setText(isNull(mUser.userNickName));
        changedNickname = isNull(mUser.userNickName);

        if (FormValidatorUtil.validateStringIsEmpty(mUser.phoneNumber) || mUser.phoneNumber.equals("")) {
            mUserinfoeditedEdittextPhone1.setText("");
            mUserinfoeditedEdittextPhone2.setText("");
            mUserinfoeditedEdittextPhone3.setText("");
        } else {
            String[] phone = mUser.phoneNumber.split("-");

            EditText[] phoneNumberArray = {mUserinfoeditedEdittextPhone1, mUserinfoeditedEdittextPhone2, mUserinfoeditedEdittextPhone3};
            for (int i = 0; i < phone.length; i++) {
                phoneNumberArray[i].setText(phone[i]);
            }
        }

        if (mUser.gender == 0) {
            mUserinfoeditedRadiobuttonGenderMan.setChecked(true);
            mUserinfoeditedRadiobuttonGenderWoman.setChecked(false);
        } else {
            mUserinfoeditedRadiobuttonGenderMan.setChecked(false);
            mUserinfoeditedRadiobuttonGenderWoman.setChecked(true);
        }

        if (!FormValidatorUtil.validateStringIsEmpty(mUser.studentId)) {
            mUserinfoeditedEdittextStudentId.setEnabled(false);
            mUserinfoeditedEdittextStudentId.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        }

        mUserinfoeditedEdittextStudentId.setText(isNull(mUser.studentId));


        mUserinfoeditedTextviewMajor.setText("");
        mUserinfoeditedTextviewMajor.setHint("학번 입력시 자동 입력");

        if (!FormValidatorUtil.validateStringIsEmpty(mUser.major)) {
            for (int i = 0; i < ARRAY_MAJOR.size(); i++) {
                if (mUser.major.equals(ARRAY_MAJOR.get(i))) {
                    mUserinfoeditedTextviewMajor.setText(ARRAY_MAJOR.get(i));
                }
            }
        }
    }

    private String isNull(String str) {
        return FormValidatorUtil.validateStringIsEmpty(str) ? "" : str;
    }

    public void saveUserInfo() {
        User changeUserData = new User();

        changeUserData.userName = mUserinfoeditedEdittextName.getText().toString().replaceAll(" ", "");
        String nickname = mUserinfoeditedEdittextNickname.getText().toString().replaceAll(" ", "");

        if((mUser.userNickName!=null) && !mUser.userNickName.equals(nickname))
            changeUserData.userNickName = nickname;
        else if((mUser.userNickName==null) && !nickname.isEmpty())
            changeUserData.userNickName = nickname;
        else
            changeUserData.userNickName = null;

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(mUserinfoeditedEdittextPhone1.getText().toString()).append("-");
        stringBuffer.append(mUserinfoeditedEdittextPhone2.getText().toString()).append("-");
        stringBuffer.append(mUserinfoeditedEdittextPhone3.getText().toString());
        changeUserData.phoneNumber = (stringBuffer.length() > 4 ? stringBuffer.toString() : "");

        if (mUserinfoeditedRadiobuttonGenderMan.isChecked()) {
            changeUserData.gender = 0;
        } else {
            changeUserData.gender = 1;
        }


        if(!mUserinfoeditedEdittextStudentId.getText().toString().trim().isEmpty())
        changeUserData.studentId = mUserinfoeditedEdittextStudentId.getText().toString().trim();

        changeUserData.major = mUserinfoeditedTextviewMajor.getText().toString();

        if(changeUserData.phoneNumber.isEmpty())
            changeUserData.phoneNumber = null;
        if(changeUserData.major.isEmpty())
            changeUserData.major = null;
        if(changeUserData.userName.isEmpty())
            changeUserData.userName = null;

        mUserInfoEditPresenter.updateUserInfo(changeUserData);
    }

    @Override
    public void showCheckNickNameSuccess() {
        // 닉네임 사용가능시 사용가능 Toast message onNicknameCheckSuccesst 실행
        ToastUtil.getInstance().makeShort("사용가능한 닉네임입니다.");
        onNicknameCheckSuccess(mUserinfoeditedEdittextNickname.getText().toString());
        mUserinfoeditedEdittextNickname.getBackground().setColorFilter(null);
    }

    @Override
    public void showCheckNickNameFail() {
        // 닉네임 사용불가시 사용불가능 Toast message 후 밑줄 빨간색으로 변경
        ToastUtil.getInstance().makeShort("기존에 중복된 닉네임입니다.");
        mUserinfoeditedEdittextNickname.getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
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
    public void setPresenter(UserInfoEditContract.Presenter presenter) {
        this.mUserInfoEditPresenter = presenter;

    }

    private boolean checkStudentID(String studentId) {
        if (studentId.trim().length() < 10) {
            mUserinfoeditedTextviewMajor.setText("");
            mUserinfoeditedTextviewMajor.setHint("학번 입력시 자동 입력");
            return false;
        }

        int year = Integer.parseInt(studentId.trim().substring(0, 4));
        if (1992 > year || year > Integer.parseInt(TimeUtil.getCurrentYear())) {
            return false;
        }

        int departmentNum = Integer.parseInt(studentId.trim().substring(4, 7));
        switch (departmentNum) {
            case 120: //기계공학부
                mUserinfoeditedTextviewMajor.setText("기계공학부");
                break;
            case 135: //컴퓨터공학부(인터넷미디어공학부)
            case 136: //컴퓨터공학부
                mUserinfoeditedTextviewMajor.setText("컴퓨터공학부");
                break;
            case 140: //메카트로닉스공학부
                mUserinfoeditedTextviewMajor.setText("메카트로닉스공학부");
                break;
            case 161: //전기전자통신공학부
                mUserinfoeditedTextviewMajor.setText("전기전자통신공학부");
                break;
            case 151: //디자인건축공학부
                mUserinfoeditedTextviewMajor.setText("디자인건축공학부");
                break;
            case 174: //에너지신소재화학공학부
                mUserinfoeditedTextviewMajor.setText("에너지신소재화학공학부");
                break;
            case 180: //산업경영학부
                mUserinfoeditedTextviewMajor.setText("산업경영학부");
                break;
            default:
                mUserinfoeditedTextviewMajor.setText("");
                mUserinfoeditedTextviewMajor.setHint("학번 입력시 자동 입력");
                return false;

        }

        return true;
    }

    public void onNicknameCheckSuccess(String nickname) {
        isNicknameChecked = true;
        changedNickname = nickname;
    }
}