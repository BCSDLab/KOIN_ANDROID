package in.koreatech.koin.ui;

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
import in.koreatech.koin.core.asynctasks.GenerateProgressTask;
import in.koreatech.koin.core.bases.BaseActivity;
import in.koreatech.koin.core.contracts.LoginContract;
import in.koreatech.koin.presenters.LoginPresenter;
import in.koreatech.koin.core.util.ToastUtil;

import static in.koreatech.koin.core.util.FormValidatorUtil.validateStringIsEmpty;


/**
 * Created by hyerim on 2018. 4. 30....
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {
    private final String TAG = LoginActivity.class.getSimpleName();
    private Context mContext;
    private LoginContract.Presenter mLoginPresenter;
    private boolean mIsMainActivity;
    private GenerateProgressTask generateProgressTask;

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
            ToastUtil.makeShortToast(mContext, "아이디와 비밀번호를 입력해주세요");
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
        if (generateProgressTask == null) {
            generateProgressTask = new GenerateProgressTask(this, "진행 중");
            generateProgressTask.execute();
        }
    }

    @Override
    public void hideProgress() {
        if (generateProgressTask != null) {
            generateProgressTask.cancel(true);
            generateProgressTask = null;
        }
    }

    /**
     * Toast 메시지 띄우는 메소드
     *
     * @param message toast 메시지 내용
     */
    @Override
    public void showMessage(String message) {
        ToastUtil.makeShortToast(mContext, message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

