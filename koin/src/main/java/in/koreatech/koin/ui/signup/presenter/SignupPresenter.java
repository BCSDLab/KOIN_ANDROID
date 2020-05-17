package in.koreatech.koin.ui.signup.presenter;

import androidx.annotation.NonNull;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.interactor.UserInteractor;
import in.koreatech.koin.data.network.interactor.UserRestInteractor;
import in.koreatech.koin.util.FilterUtil;
import in.koreatech.koin.util.FormValidatorUtil;
import retrofit2.HttpException;

import static com.google.common.base.Preconditions.checkNotNull;

public class SignupPresenter {

    private final SignupContract.View signUpView;

    private final UserInteractor userInteractor;

    public SignupPresenter(@NonNull SignupContract.View signUpView, UserInteractor userInteractor) {
        this.userInteractor = userInteractor;
        this.signUpView = checkNotNull(signUpView, "signUpView cannnot be null");
        this.signUpView.setPresenter(this);
    }

    /**
     * 회원가입 api 요청 결과 callback
     * 성공할 경우 학교 포털 이메일사이트로 이동
     * 실패할 경우 안내 메시지 출력
     */
    private final ApiCallback signUpApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            signUpView.gotoEmail();
            signUpView.hideProgress();
            signUpView.buttonClickBlock(false);
        }

        @Override
        public void onFailure(Throwable throwable) {
            signUpView.hideProgress();
            if (throwable instanceof HttpException && ((HttpException) throwable).code() == 409)
                signUpView.showMessage(R.string.email_already_send_or_email_requested);
            else
                signUpView.showMessage(R.string.error_network);
            signUpView.buttonClickBlock(false);
        }
    };

    /**
     * 회원가입 api를 호출하기 위한 메소드
     *
     * @param id       학교 포털 IDㅌ
     * @param password koin 계정 비밀번호
     */
    public void signUp(String id, String password) {
        if(FormValidatorUtil.validateStringIsEmpty(id)){
            signUpView.showMessage(R.string.signup_id_input_warning);
            return;
        }
        else if(FormValidatorUtil.validateStringIsEmpty(password)){
            signUpView.showMessage(R.string.signup_password_input_warning);
            return;
        }
        else if (!FilterUtil.isEmailValidate(id)) {
            signUpView.showMessage(R.string.signup_email_format_warning);
            return;
        }
        else if(!FilterUtil.isPasswordValidate(password)){
            signUpView.showMessage(R.string.signup_password_format_warning);
            return;
        }

        signUpView.buttonClickBlock(true);
        signUpView.showProgress();
        userInteractor.createToken(id, password, signUpApiCallback);
    }

}
