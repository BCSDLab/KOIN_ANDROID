package in.koreatech.koin.ui.forgotpassword.presenter;

import androidx.annotation.NonNull;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.interactor.UserInteractor;
import in.koreatech.koin.data.network.interactor.UserRestInteractor;
import in.koreatech.koin.util.FormValidatorUtil;

import static com.google.common.base.Preconditions.checkNotNull;


public class ForgotPasswordPresenter{

    private final ForgotPasswordContract.View forgotPasswordView;

    private final UserInteractor mUserInteractor;

    public ForgotPasswordPresenter(@NonNull ForgotPasswordContract.View forgotPasswordView) {
        mUserInteractor = new UserRestInteractor();
        this.forgotPasswordView = checkNotNull(forgotPasswordView, "forgatPasswordView cannnot be null");
        this.forgotPasswordView.setPresenter(this);
    }

    /**
     * 비밀번호 재설정 api 호출 메소드
     *
     * @param id 포탈 계정 id
     */
    public void findPassword(String id) {
        if(FormValidatorUtil.validateStringIsEmpty(id)){
            forgotPasswordView.showMessage(R.string.email_empty_string_warning);
            return;
        }
        this.forgotPasswordView.showProgress();
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
            forgotPasswordView.goToEmail();
            forgotPasswordView.hideProgress();
        }

        @Override
        public void onFailure(Throwable throwable) {
            forgotPasswordView.showMessage(throwable.getMessage());
            forgotPasswordView.hideProgress();
        }
    };

}
