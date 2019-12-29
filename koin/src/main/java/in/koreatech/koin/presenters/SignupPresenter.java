package in.koreatech.koin.presenters;

import androidx.annotation.NonNull;

import in.koreatech.koin.core.contracts.SignupContract;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.interactors.UserInteractor;
import in.koreatech.koin.core.networks.interactors.UserRestInteractor;
import in.koreatech.koin.core.networks.responses.DefaultResponse;
import retrofit2.HttpException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hyerim on 2018. 6. 1....
 */
public class SignupPresenter implements SignupContract.Presenter {

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
                mSignUpView.showMessage("이미 가입한 계정이거나\n이메일 전송을 요청하였습니다.");
            else
                mSignUpView.showMessage("네트워크 환경을 확인해주세요");
        }
    };

    /**
     * 회원가입 api를 호출하기 위한 메소드
     *
     * @param id       학교 포털 IDㅌ
     * @param password koin 계정 비밀번호
     */
    @Override
    public void signUp(String id, String password) {
        mSignUpView.showProgress();
        mUserInteractor.createToken(id, password, signUpApiCallback);
    }

}
