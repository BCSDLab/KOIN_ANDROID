package in.koreatech.koin.ui.splash.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.StringTokenizer;

import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.network.interactor.TokenSessionInteractor;
import in.koreatech.koin.data.network.interactor.TokenSessionLocalInteractor;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Auth;
import in.koreatech.koin.data.network.entity.Version;
import in.koreatech.koin.data.network.interactor.AppVersionRestInteractor;
import in.koreatech.koin.data.network.interactor.UserInteractor;
import in.koreatech.koin.data.network.interactor.UserRestInteractor;
import in.koreatech.koin.util.TimeUtil;

import static com.google.common.base.Preconditions.checkNotNull;
import static in.koreatech.koin.util.FormValidatorUtil.validateStringIsEmpty;

public class SplashPresenter implements SplashContract.Presenter {
    private static final String TAG = "SplashPresenter";
    public static final String ANDROID_CODE = "android";
    private final SplashContract.View splashView;
    private final UserInteractor authInteractor;
    private final TokenSessionInteractor tokenSessionInteractor;
    private final AppVersionRestInteractor appVersionRestInteractor;
    private String currentVersionName;
    private String storeVersionName;


    public SplashPresenter(@NonNull SplashContract.View splashView) {
        authInteractor = new UserRestInteractor();
        this.tokenSessionInteractor = new TokenSessionLocalInteractor();
        appVersionRestInteractor = new AppVersionRestInteractor();
        this.splashView = checkNotNull(splashView, "splashView cannnot be null");
        this.splashView.setPresenter(this);
    }

    /**
     * 화면을 호출하는 메소드
     * 저장된 토큰이 없을 경우 로그인 화면으로 전환
     * 저장된 토큰이 없을 경우 토큰 검증 메소드로 이동
     */
    @Override
    public void callActivity() {
        //저장된 토큰이 없을 경우
        if (validateStringIsEmpty(UserInfoSharedPreferencesHelper.getInstance().loadToken())) {
            this.splashView.gotoLogin();
        }
        //저장된 토큰이 있을 경우
        else {
            checkToken();
        }
    }

    /**
     * 앱 버전을 확인하는 메소드
     * 업데이트가 필요할 경우 플레이스토어로 이동
     */
    @Override
    public void checkUpdate(String currentVersion) {
        currentVersionName = currentVersion;
        appVersionRestInteractor.readAppVersion(ANDROID_CODE, versionApiCallback);
    }

    /**
     * 유저의 토큰이 유효한지 확인하는 메소드
     * 접속한지 한달이 넘은 경우 로그인화면으로 전환
     * 접속한지 한달이 되지 않은 경우 토큰을 업데이트하는 메소드로 이동
     */
    @Override
    public void checkToken() {
        long lastLoginDate = (UserInfoSharedPreferencesHelper.getInstance().loadLastLoginDate()) / 10000;
        long currentDate = TimeUtil.getDeviceCreatedDateOnlyLong() / 10000;

        if (Math.abs(currentDate - lastLoginDate) >= 100) { //접속한 지 한달 넘었을 경우
            this.splashView.showMessage("사용자 인증이 만료되었습니다.");
            UserInfoSharedPreferencesHelper.getInstance().clear();
            this.splashView.gotoLogin();
        } else {
            updateToken();
        }
    }

    /**
     * 토큰을 업데이트 하는 메소드
     */
    public void updateToken() {
        authInteractor.readToken(UserInfoSharedPreferencesHelper.getInstance().loadUser().userId,
                UserInfoSharedPreferencesHelper.getInstance().loadUserPw(),
                true, authApiCallback);
    }

    /**
     * read token api 결과를 처리하는 api callback
     * 성공했을 경우 올바른지 확인하는 메소드로 이동
     */
    private final ApiCallback authApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Auth auth = (Auth) (object);
            validateCurrentSession(auth);
        }

        @Override
        public void onFailure(Throwable throwable) {
            splashView.gotoLogin();
        }
    };

    /**
     * 현재 토큰이 올바른지 검사하는 메소드
     *
     * @param auth token, user로 구성된 파라미터
     */
    private void validateCurrentSession(Auth auth) {
        this.tokenSessionInteractor.validateCurrentSession(auth, sessionApiCallback);
    }

    /**
     * 토큰이 유효한 경우 메인 화면으로 이동
     * 토큰이 유효하지 않은 경우 로그인화면으로 이동
     */
    private final ApiCallback sessionApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            splashView.gotoMain();
        }

        @Override
        public void onFailure(Throwable throwable) {
            splashView.gotoLogin();
        }
    };

    /**
     * 버전 검사 api callback
     */
    private ApiCallback versionApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Version version = (Version) object;
            storeVersionName = version.getVersion();
            int currentMajor, currentMinor, currentPoint;
            int storeMajor, storeMinor, storePoint;
            try {
                StringTokenizer currentVersionTokenizer = new StringTokenizer(currentVersionName, ".");
                StringTokenizer storeVersionTokenizer = new StringTokenizer(storeVersionName, ".");
                currentMajor = Integer.parseInt(currentVersionTokenizer.nextToken());
                currentMinor = Integer.parseInt(currentVersionTokenizer.nextToken());
                currentPoint = Integer.parseInt(currentVersionTokenizer.nextToken());
                storeMajor = Integer.parseInt(storeVersionTokenizer.nextToken());
                storeMinor = Integer.parseInt(storeVersionTokenizer.nextToken());
                storePoint = Integer.parseInt(storeVersionTokenizer.nextToken());
                if (storeMajor > currentMajor) {
                    splashView.gotoAppMarket(Version.PRIORITY_HIGH, storeVersionName);
                } else if (storeMinor > currentMinor) {
                    splashView.gotoAppMarket(Version.PRIORITY_MIDDLE, storeVersionName);
                } else splashView.gotoAppMarket(Version.PRIORITY_LOW, storeVersionName);
            } catch (Exception e) {
                Log.d(TAG, e.getMessage() + "current version : " + currentVersionName + "store version : " + storeVersionName);
                splashView.gotoAppMarket(Version.PRIORITY_LOW, storeVersionName);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            splashView.gotoAppMarket(Version.PRIORITY_LOW, storeVersionName);
        }
    };

}

