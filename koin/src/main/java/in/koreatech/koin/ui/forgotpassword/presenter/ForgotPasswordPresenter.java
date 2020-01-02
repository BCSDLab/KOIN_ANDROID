package in.koreatech.koin.ui.forgotpassword.presenter;

import androidx.annotation.NonNull;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.interactor.UserInteractor;
import in.koreatech.koin.data.network.interactor.UserRestInteractor;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hyerim on 2018. 7. 8....
 */
public class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter {

    private final ForgotPasswordContract.View mForgotPasswordView;

    private final UserInteractor mUserInteractor;

    public ForgotPasswordPresenter(@NonNull ForgotPasswordContract.View forgotPasswordView) {
        mUserInteractor = new UserRestInteractor();
        mForgotPasswordView = checkNotNull(forgotPasswordView, "forgatPasswordView cannnot be null");
        mForgotPasswordView.setPresenter(this);
    }

    /**
     * 비밀번호 재설정 api 호출 메소드
     * @param id 포탈 계정 id
     */
    @Override
    public void findPassword(String id) {
        mForgotPasswordView.showProgress();
        mUserInteractor.createFindPassword(id, findPasswordApiCallback);
    }

    /**
     * 비밀번호 재설정 api 결과 callback
     * 성공할 경우 학교 이메일 사이트로 이동
     * 실패할 경우 안내 메시지 보여주기
     */
    private final ApiCallback findPasswordApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            mForgotPasswordView.goToEmail();
            mForgotPasswordView.hideProgress();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mForgotPasswordView.showMessage(throwable.getMessage());
            mForgotPasswordView.hideProgress();
        }
    };

}
