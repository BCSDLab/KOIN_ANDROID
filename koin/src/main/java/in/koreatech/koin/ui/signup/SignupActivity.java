package in.koreatech.koin.ui.signup;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.interactor.UserRestInteractor;
import in.koreatech.koin.ui.signup.presenter.SignupContract;
import in.koreatech.koin.ui.signup.presenter.SignupPresenter;
import in.koreatech.koin.util.FirebasePerformanceUtil;
import in.koreatech.koin.util.SnackbarUtil;

public class SignupActivity extends ActivityBase implements SignupContract.View {
    final static String TAG = "SignupActivity";

    private Context context;
    private SignupPresenter signupPresenter;
    private boolean isverified;
    private FirebasePerformanceUtil firebasePerformanceUtil;
    private long signUpButtonClickCount; // Firebase Performance 측정 회원가입 버튼 클릭 빈도수 측정

    /* View Components */
    @BindView(R.id.signup_edittext_id)
    EditText mEditTextID;
    @BindView(R.id.signup_edittext_pw)
    EditText mEditTextPW;
    @BindView(R.id.signup_edittext_pw_confirm)
    EditText mEditTextPWConfirm;
    @BindView(R.id.signup_textview_personal_info_terms)
    TextView mTextViewPersonalInfoTerms;
    @BindView(R.id.signup_textview_signup_terms)
    TextView mTextViewSignUpTerms;
    @BindView(R.id.signup_check_box_personal_info_terms)
    CheckBox mCheckBoxPersonalInfoTerms;
    @BindView(R.id.signup_check_box_signup_terms)
    CheckBox mCheckBoxSignupTerms;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        this.context = this;

        init();

    }

    @Override
    protected void onStart() {
        super.onStart();
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
        super.onBackPressed();
    }

    private void init() {
        isverified = false;
        this.firebasePerformanceUtil = new FirebasePerformanceUtil("signup_activity");
        mTextViewPersonalInfoTerms.setPaintFlags(mTextViewPersonalInfoTerms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mTextViewSignUpTerms.setPaintFlags(mTextViewPersonalInfoTerms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.firebasePerformanceUtil.start();
        this.signUpButtonClickCount = 0;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //create presenter
        new SignupPresenter(this, new UserRestInteractor());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setPresenter(SignupPresenter presenter) {
        this.signupPresenter = presenter;
    }

    @OnTextChanged({R.id.signup_edittext_pw, R.id.signup_edittext_pw_confirm})
    public void setTextChangedPwEditText() {

    }


    /**
     * 비밀번호와 비밀 번호의 확인이 일치하는지 판별하는 메서드
     *
     * @return 일치하면 true, 다르면 false
     */
    public boolean isPasswordSame() {
        return (mEditTextPW.getText().toString().equals(mEditTextPWConfirm.getText().toString()));
    }

    /**
     * 가입 버튼 onClick 메소드
     * 필수 정보 입력 여부 & 이용약관 동의 여부 확인
     */
    @OnClick(R.id.signup_send_verification_button)
    public void onClickSendVerificationButton() {
        if (isverified) {
            ToastUtil.getInstance().makeShort(R.string.signup_wait_for_sending_email);
            return;
        }
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        if (!isPasswordSame()) {
            ToastUtil.getInstance().makeShort(R.string.signup_password_not_same);
            return;
        }

        if (!mCheckBoxPersonalInfoTerms.isChecked() || !mCheckBoxSignupTerms.isChecked()) {
            ToastUtil.getInstance().makeShort(R.string.signup_agree_term_warning);
            return;
        }

        this.firebasePerformanceUtil.incrementMetric("signup_button_click", ++this.signUpButtonClickCount);
        this.signupPresenter.signUp(mEditTextID.getText().toString().trim(), mEditTextPW.getText().toString().trim());
    }

    @Override
    public void buttonClickBlock(boolean isBlock) {
        isverified = isBlock;
    }

    /**
     * toast 메시지를 띄우는 메소드
     *
     * @param message toast 메시지 내용
     */
    @Override
    public void showMessage(String message) {
        Log.d(TAG, message);
        ToastUtil.getInstance().makeShort(message);
    }

    @Override
    public void showMessage(int message) {
        ToastUtil.getInstance().makeShort(message);
    }

    /**
     * 학교 이메일 화면으로 이동할지 확인하는 snackbar 표시 메소드
     */
    @Override
    public void gotoEmail() {
        SnackbarUtil.makeSnackbarActionWebView(this, R.id.signup_box, "10분안에 학교 메일로 인증을 완료해 주세요. 이동하실래요?",
                "KOREATECH E-mail 인증", this.context.getResources().getString(R.string.koreatech_url), 5000);
    }

    /**
     * 이용약관 상세 dialog 띄우는 onClick 메소드
     */
    @OnClick(R.id.signup_textview_personal_info_terms)
    public void onClickPersonalInfoTermsTextView() {
        makeDialogTerms("개인정보 이용약관", "Terms_personal_information.txt");
    }

    @OnClick(R.id.signup_textview_signup_terms)
    public void onClickSignupTermsTextView() {
        makeDialogTerms("코인 이용약관", "Terms_koin_sign_up.txt");
    }


    /**
     * 이용약관 dialog를 띄우는 메소드
     *
     * @param title    이용약관 타이틀
     * @param textFile 이용약관 파일
     */
    @Override
    public void makeDialogTerms(String title, String textFile) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_terms, null, false);

        BufferedReader reader = null;

        TextView textViewTitle = dialogView.findViewById(R.id.dialog_terms_title);
        textViewTitle.setText(title);

        TextView textViewContent = dialogView.findViewById(R.id.dialog_terms_content);
        textViewContent.setMovementMethod(ScrollingMovementMethod.getInstance());

        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(textFile), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            StringBuilder content = new StringBuilder();
            while ((mLine = reader.readLine()) != null) {
                content.append(mLine).append('\n');
            }
            textViewContent.setText(content.toString());
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.KAPDialog);
        builder.setView(dialogView);
        builder.setPositiveButton("확인",
                (dialog, which) -> {

                });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        this.firebasePerformanceUtil.stop();
        super.onDestroy();
    }

    @OnClick(R.id.signup_back_button)
    public void onClickedBackButton() {
        onBackPressed();
    }

    @Override
    public void showProgress() {
        showProgressDialog(R.string.email_sending);
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();//프로그레스바 해제
    }
}
