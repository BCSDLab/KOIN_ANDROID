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

public class LoginActivity extends ActivityBase implements LoginContract.View {
    private final String TAG = "LoginActivity";
    private Context context;
    private LoginPresenter loginPresenter;
    private boolean isMainActivity;

    /* View Components */
    @BindView(R.id.login_edittext_id)
    EditText editTextID;
    @BindView(R.id.login_edittext_pw)
    EditText editTextPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.isMainActivity = getIntent().getBooleanExtra("FIRST_LOGIN", true);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        context = this;
        new LoginPresenter(this);
    }

    @Override
    public void setPresenter(LoginPresenter presenter) {
        this.loginPresenter = presenter;
    }

    @OnEditorAction({R.id.login_edittext_id, R.id.login_edittext_pw})
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.login_edittext_id:
                this.editTextPW.requestFocus();
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

        if (validateStringIsEmpty(this.editTextID.getText().toString()) || validateStringIsEmpty(this.editTextPW.getText().toString())) {
            ToastUtil.getInstance().makeShort("아이디와 비밀번호를 입력해주세요");
        } else {
            this.loginPresenter.login(this.editTextID.getText().toString(), this.editTextPW.getText().toString(), false);
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
        if (this.isMainActivity) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            this.isMainActivity = false;
        }
        finish();
    }

    @Override
    public void gotoMain() {
        if (this.isMainActivity) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            this.isMainActivity = false;
        }
        finish();
    }

    @Override
    public void showProgress() {
        showProgressDialog("진행 중");
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    /**
     * Toast 메시지 띄우는 메소드
     *
     * @param message toast 메시지 내용
     */
    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShort(message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

