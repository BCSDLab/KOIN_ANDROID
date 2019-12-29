package in.koreatech.koin.presenters;

import in.koreatech.koin.core.contracts.LoginContract;
import in.koreatech.koin.core.helpers.TokenSessionInteractor;
import in.koreatech.koin.core.helpers.TokenSessionLocalInteractor;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Auth;
import in.koreatech.koin.core.networks.interactors.UserInteractor;
import in.koreatech.koin.core.networks.interactors.UserRestInteractor;
import in.koreatech.koin.core.util.HashGeneratorUtil;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hyerim on 2018. 5. 31....
 */
public class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View mLoginView;

    private final UserInteractor mUserInteractor;
    private final TokenSessionInteractor mTokenSessionInteractor;

    private String mUserPw;

    public LoginPresenter(LoginContract.View loginView) {
        mUserInteractor = new UserRestInteractor();
        mTokenSessionInteractor = new TokenSessionLocalInteractor();
        mLoginView = checkNotNull(loginView, "signUpView cannnot be null");
        mLoginView.setPresenter(this);
    }

    /**
     * 로그인 api 호출 메소드
     *
     * @param id             학교 포탈 id
     * @param password       koin 비밀번호
     * @param isPasswordHash 비밀번호 hash 여부
     */
    @Override
    public void login(String id, String password, Boolean isPasswordHash) {
        mLoginView.showProgress();
        mUserPw = HashGeneratorUtil.generateSHA256(password);
        mUserInteractor.readToken(id, password, isPasswordHash, loginApiCallback);
    }

    /**
     * login api 결과 callback
     * 성공할 경우 saveSession 호출
     * 실패할 경우 안내 메시지
     */
    private final ApiCallback loginApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Auth auth = (Auth) (object);
            saveSession(auth);
            mLoginView.hideProgress();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mLoginView.hideProgress();
            mLoginView.showMessage("로그인에 실패하였습니다");
        }
    };

    /**
     * token, user정보 저장하는 메소드 호출
     *
     * @param auth token, user로 이루어진 파라미터
     */
    private void saveSession(Auth auth) {
        mTokenSessionInteractor.saveSession(auth, mUserPw, sessionApiCallback);
    }

    /**
     * token, user정보 저장하는 메소드 결과 callback
     * 성공할 경우 main으로 이동
     */
    private final ApiCallback sessionApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            mLoginView.gotoMain();
            mLoginView.hideProgress();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mLoginView.hideProgress();
        }
    };
}
