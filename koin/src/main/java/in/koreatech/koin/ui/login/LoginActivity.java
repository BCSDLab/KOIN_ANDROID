package in.koreatech.koin.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.ui.main.MainActivity;
import in.koreatech.koin.ui.signup.SignupActivity;
import in.koreatech.koin.ui.login.presenter.LoginContract;
import in.koreatech.koin.ui.login.presenter.LoginPresenter;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.forgotpassword.ForgotPasswordActivity;

import static in.koreatech.koin.util.FormValidatorUtil.validateStringIsEmpty;


/**
 * Created by hyerim on 2018. 4. 30....
 */
public class LoginActivity extends ActivityBase implements LoginContract.View {
    private final String TAG = "LoginActivity";
    private Context mContext;
    private LoginContract.Presenter mLoginPresenter;
    private boolean mIsMainActivity;
    private CustomProgressDialog customProgressDialog;

    /* View Components */
    @BindView(R.id.login_edittext_id)
    EditText mEditTextID;
    @BindView(R.id.login_edittext_pw)
    EditText mEditTextPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mIsMainActivity = getIntent().getBooleanExtra("FIRST_LOGIN", true);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mContext = this;
        new LoginPresenter(this);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mLoginPresenter = presenter;
    }

    @OnEditorAction({R.id.login_edittext_id, R.id.login_edittext_pw})
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.login_edittext_id:
                mEditTextPW.requestFocus();
                break;
            case R.id.login_edittext_pw:
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 로그인버튼 onclick 메소드
     * 키보드 숨김
     * 아이디/비밀번호 입력 여부 확인
     * LoginPresenter의 login 메소드 호출(password hash false)
     */
    @OnClick(R.id.login_button)
    public void onClickLoginButton() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (validateStringIsEmpty(mEditTextID.getText().toString()) || validateStringIsEmpty(mEditTextPW.getText().toString())) {
            ToastUtil.getInstance().makeShortToast("아이디와 비밀번호를 입력해주세요");
        } else {
            mLoginPresenter.login(mEditTextID.getText().toString(), mEditTextPW.getText().toString(), false);
        }
    }

    @OnClick(R.id.login_button_signup)
    public void onClickSignUpButton() {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    @OnClick(R.id.forgot_password_linearLayout)
    public void onClickForgotPassword() {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }

    @OnClick(R.id.anonymous_login_linearLayout)
    public void onClickAnonymousLogin() {
        if (mIsMainActivity) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            mIsMainActivity = false;
        }
        finish();
    }

    @Override
    public void gotoMain() {
        if (mIsMainActivity) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            mIsMainActivity = false;
        }
        finish();
    }

    @Override
    public void showProgress() {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(this, "진행 중");
            customProgressDialog.execute();
        }
    }

    @Override
    public void hideProgress() {
        if (customProgressDialog != null) {
            customProgressDialog.cancel(true);
            customProgressDialog = null;
        }
    }

    /**
     * Toast 메시지 띄우는 메소드
     *
     * @param message toast 메시지 내용
     */
    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShortToast(message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

