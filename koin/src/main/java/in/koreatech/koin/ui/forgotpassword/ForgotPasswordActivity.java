package in.koreatech.koin.ui.forgotpassword;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.ui.forgotpassword.presenter.ForgotPasswordContract;
import in.koreatech.koin.ui.forgotpassword.presenter.ForgotPasswordPresenter;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.core.toast.ToastUtil;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hyerim on 2018. 6. 18....
 */
public class ForgotPasswordActivity extends ActivityBase implements ForgotPasswordContract.View {
    private final static String TAG = "ForgotPasswordActivity";

    private Context context;
    private ForgotPasswordContract.Presenter mForgotPasswordPresenter;
    private CustomProgressDialog customProgressDialog;

    @BindView(R.id.forgot_password_id_edittext)
    EditText mIdEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        context = this;

        //create presenter
        new ForgotPasswordPresenter(this);

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
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void setPresenter(@NonNull ForgotPasswordContract.Presenter presenter) {
        mForgotPasswordPresenter = checkNotNull(presenter);
    }

    @OnEditorAction(R.id.forgot_password_id_edittext)
    public boolean onEditorAction(EditText v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.forgot_password_id_edittext:
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

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShort(message);
    }

    @OnClick(R.id.reset_password_button)
    public void onClickResetPasswordButton() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        mForgotPasswordPresenter.findPassword(mIdEditText.getText().toString());
    }

    @OnClick(R.id.forgot_password_to_login_button)
    public void onClickToLoginButton() {
        finish();
    }

    @Override
    public void goToEmail() {
        SnackbarUtil.makeSnackbarActionWebView(this, R.id.forgot_password_id_edittext, "학교 메일로 비밀번호 초기화를 완료해 주세요. 이동하실래요?",
                "KOREATECH E-mail 인증", context.getResources().getString(R.string.koreatech_url), 5000);
    }


}
