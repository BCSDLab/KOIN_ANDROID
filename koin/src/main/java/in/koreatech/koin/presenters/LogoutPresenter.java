package in.koreatech.koin.presenters;

import in.koreatech.koin.core.contracts.LogoutContract;
import in.koreatech.koin.core.helpers.TokenSessionInteractor;
import in.koreatech.koin.core.helpers.TokenSessionLocalInteractor;
import in.koreatech.koin.core.networks.ApiCallback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hyerim on 2018. 5. 31....
 */
public class LogoutPresenter implements LogoutContract.Presenter {

    private final LogoutContract.View mLogoutView;
    private final TokenSessionInteractor mTokenSessionInteractor;

    public LogoutPresenter(LogoutContract.View logoutView) {
        mTokenSessionInteractor = new TokenSessionLocalInteractor();
        mLogoutView = checkNotNull(logoutView);
        mLogoutView.setPresenter(this);
    }

    /**
     * 로그아웃시 정보 삭제하는 메소드 호출하는 메소드
     */
    public void closeSession(){
        mTokenSessionInteractor.closeSession(sessionApiCallback);
    }

    /**
     * 로그아웃 결과 callback
     * 성공할 경우 login 화면으로 이동
     * 실패할 경우 안내메시지 출력
     */
    private final ApiCallback sessionApiCallback= new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            mLogoutView.hideLoading();
            mLogoutView.gotoLogIn();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mLogoutView.hideLoading();
        }
    };
}
