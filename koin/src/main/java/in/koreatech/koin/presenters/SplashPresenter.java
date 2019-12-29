package in.koreatech.koin.presenters;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.StringTokenizer;

import in.koreatech.koin.core.contracts.SplashContract;
import in.koreatech.koin.core.helpers.DefaultSharedPreferencesHelper;
import in.koreatech.koin.core.helpers.TokenSessionInteractor;
import in.koreatech.koin.core.helpers.TokenSessionLocalInteractor;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Auth;
import in.koreatech.koin.core.networks.entity.Version;
import in.koreatech.koin.core.networks.interactors.AppVersionRestInteractor;
import in.koreatech.koin.core.networks.interactors.UserInteractor;
import in.koreatech.koin.core.networks.interactors.UserRestInteractor;
import in.koreatech.koin.core.util.TimeUtil;

import static com.google.common.base.Preconditions.checkNotNull;
import static in.koreatech.koin.core.util.FormValidatorUtil.validateStringIsEmpty;

/**
 * Created by hyerim on 2018. 6. 1....
 */
public class SplashPresenter implements SplashContract.Presenter {
    private static final String TAG = SplashPresenter.class.getName();
    public static final String ANDROID_CODE = "android";
    private final SplashContract.View mSplashView;
    private final UserInteractor mAuthInteractor;
    private final TokenSessionInteractor mTokenSessionInteractor;
    private final AppVersionRestInteractor mAppVersionRestInteractor;
    private String mCurrentVersionName;
    private String mStoreVersionName;


    public SplashPresenter(@NonNull SplashContract.View splashView) {
        mAuthInteractor = new UserRestInteractor();
        mTokenSessionInteractor = new TokenSessionLocalInteractor();
        mAppVersionRestInteractor = new AppVersionRestInteractor();
        mSplashView = checkNotNull(splashView, "splashView cannnot be null");
        mSplashView.setPresenter(this);
    }

    /**
     * 화면을 호출하는 메소드
     * 저장된 토큰이 없을 경우 로그인 화면으로 전환
     * 저장된 토큰이 없을 경우 토큰 검증 메소드로 이동
     */
    @Override
    public void callActivity() {
        //저장된 토큰이 없을 경우
        if (validateStringIsEmpty(DefaultSharedPreferencesHelper.getInstance().loadToken())) {
            mSplashView.gotoLogin();
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
        mCurrentVersionName = currentVersion;
        mAppVersionRestInteractor.readAppVersion(ANDROID_CODE, versionApiCallback);
    }

    /**
     * 유저의 토큰이 유효한지 확인하는 메소드
     * 접속한지 한달이 넘은 경우 로그인화면으로 전환
     * 접속한지 한달이 되지 않은 경우 토큰을 업데이트하는 메소드로 이동
     */
    @Override
    public void checkToken() {
        long lastLoginDate = (DefaultSharedPreferencesHelper.getInstance().loadLastLoginDate()) / 10000;
        long currentDate = TimeUtil.getDeviceCreatedDateOnlyLong() / 10000;

        if (Math.abs(currentDate - lastLoginDate) >= 100) { //접속한 지 한달 넘었을 경우
            mSplashView.showMessage("사용자 인증이 만료되었습니다.");
            DefaultSharedPreferencesHelper.getInstance().clear();
            mSplashView.gotoLogin();
        } else {
            updateToken();
        }
    }

    /**
     * 토큰을 업데이트 하는 메소드
     */
    public void updateToken() {
        mAuthInteractor.readToken(DefaultSharedPreferencesHelper.getInstance().loadUser().userId,
                DefaultSharedPreferencesHelper.getInstance().loadUserPw(),
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
            mSplashView.gotoLogin();
        }
    };

    /**
     * 현재 토큰이 올바른지 검사하는 메소드
     *
     * @param auth token, user로 구성된 파라미터
     */
    private void validateCurrentSession(Auth auth) {
        mTokenSessionInteractor.validateCurrentSession(auth, sessionApiCallback);
    }

    /**
     * 토큰이 유효한 경우 메인 화면으로 이동
     * 토큰이 유효하지 않은 경우 로그인화면으로 이동
     */
    private final ApiCallback sessionApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            mSplashView.gotoMain();
        }

        @Override
        public void onFailure(Throwable throwable) {
            mSplashView.gotoLogin();
        }
    };

    /**
     * 버전 검사 api callback
     */
    private ApiCallback versionApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Version version = (Version) object;
            mStoreVersionName = version.getVersion();
            int currentMajor, currentMinor, currentPoint;
            int storeMajor, storeMinor, storePoint;
            try {
                StringTokenizer currentVersionTokenizer = new StringTokenizer(mCurrentVersionName, ".");
                StringTokenizer storeVersionTokenizer = new StringTokenizer(mStoreVersionName, ".");
                currentMajor = Integer.parseInt(currentVersionTokenizer.nextToken());
                currentMinor = Integer.parseInt(currentVersionTokenizer.nextToken());
                currentPoint = Integer.parseInt(currentVersionTokenizer.nextToken());
                storeMajor = Integer.parseInt(storeVersionTokenizer.nextToken());
                storeMinor = Integer.parseInt(storeVersionTokenizer.nextToken());
                storePoint = Integer.parseInt(storeVersionTokenizer.nextToken());
                if (storeMajor > currentMajor) {
                    mSplashView.gotoAppMarket(Version.PRIORITY_HIGH, mStoreVersionName);
                } else if (storeMinor > currentMinor) {
                    mSplashView.gotoAppMarket(Version.PRIORITY_MIDDLE, mStoreVersionName);
                } else mSplashView.gotoAppMarket(Version.PRIORITY_LOW, mStoreVersionName);
            } catch (Exception e) {
                Log.d(TAG, e.getMessage() + "current version : " + mCurrentVersionName + "store version : " + mStoreVersionName);
                mSplashView.gotoAppMarket(Version.PRIORITY_LOW, mStoreVersionName);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            mSplashView.gotoAppMarket(Version.PRIORITY_LOW, mStoreVersionName);
        }
    };

}

