package in.koreatech.koin.ui.signup.presenter;

import androidx.annotation.NonNull;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.interactor.UserInteractor;
import in.koreatech.koin.data.network.interactor.UserRestInteractor;
import retrofit2.HttpException;

import static com.google.common.base.Preconditions.checkNotNull;

public class SignupPresenter {

    private final SignupContract.View mSignUpView;

    private final UserInteractor mUserInteractor;

    public SignupPresenter(@NonNull SignupContract.View signupView) {
        mUserInteractor = new UserRestInteractor();
        mSignUpView = checkNotNull(signupView, "signUpView cannnot be null");
        mSignUpView.setPresenter(this);
    }

    /**
     * 회원가입 api 요청 결과 callback
     * 성공할 경우 학교 포털 이메일사이트로 이동
     * 실패할 경우 안내 메시지 출력
     */
    private final ApiCallback signUpApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            mSignUpView.gotoEmail();
            mSignUpView.hideProgress();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mSignUpView.hideProgress();
            if (throwable instanceof HttpException && ((HttpException) throwable).code() == 409)
                mSignUpView.showMessage(R.string.email_already_send_or_email_requested);
            else
                mSignUpView.showMessage(R.string.error_network);
        }
    };

    /**
     * 회원가입 api를 호출하기 위한 메소드
     *
     * @param id       학교 포털 IDㅌ
     * @param password koin 계정 비밀번호
     */
    public void signUp(String id, String password) {
        mSignUpView.showProgress();
        mUserInteractor.createToken(id, password, signUpApiCallback);
    }

}
