package in.koreatech.koin.ui.login.presenter;

import in.koreatech.koin.data.network.interactor.TokenSessionInteractor;
import in.koreatech.koin.data.network.interactor.TokenSessionLocalInteractor;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Auth;
import in.koreatech.koin.data.network.interactor.UserInteractor;
import in.koreatech.koin.data.network.interactor.UserRestInteractor;
import in.koreatech.koin.util.HashGeneratorUtil;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginPresenter{
    private final LoginContract.View loginView;

    private final UserInteractor userInteractor;
    private final TokenSessionInteractor tokenSessionInteractor;

    private String userPw;

    public LoginPresenter(LoginContract.View loginView) {
        this.userInteractor = new UserRestInteractor();
        this.tokenSessionInteractor = new TokenSessionLocalInteractor();
        this.loginView = checkNotNull(loginView, "signUpView cannnot be null");
        this.loginView.setPresenter(this);
    }

    /**
     * 로그인 api 호출 메소드
     *
     * @param id             학교 포탈 id
     * @param password       koin 비밀번호
     * @param isPasswordHash 비밀번호 hash 여부
     */
    public void login(String id, String password, Boolean isPasswordHash) {
        this.loginView.showProgress();
        this.userPw = HashGeneratorUtil.generateSHA256(password);
        this.userInteractor.readToken(id, password, isPasswordHash, loginApiCallback);
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
            loginView.hideProgress();
        }

        @Override
        public void onFailure(Throwable throwable) {
            loginView.hideProgress();
            loginView.showMessage("로그인에 실패하였습니다");
        }
    };

    /**
     * token, user정보 저장하는 메소드 호출
     *
     * @param auth token, user로 이루어진 파라미터
     */
    private void saveSession(Auth auth) {
        this.tokenSessionInteractor.saveSession(auth, this.userPw, sessionApiCallback);
    }

    /**
     * token, user정보 저장하는 메소드 결과 callback
     * 성공할 경우 main으로 이동
     */
    private final ApiCallback sessionApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            loginView.gotoMain();
            loginView.hideProgress();
        }

        @Override
        public void onFailure(Throwable throwable) {
            loginView.hideProgress();
        }
    };
}
