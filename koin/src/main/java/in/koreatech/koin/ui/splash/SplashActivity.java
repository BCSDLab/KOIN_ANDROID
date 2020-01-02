package in.koreatech.koin.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;


import com.crashlytics.android.Crashlytics;

import in.koreatech.koin.BuildConfig;
import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.ui.splash.presenter.SplashContract;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.helper.VersionDialogClickListener;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.entity.Version;
import in.koreatech.koin.util.FirebasePerformanceUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.splash.presenter.SplashPresenter;
import in.koreatech.koin.ui.login.LoginActivity;
import in.koreatech.koin.ui.main.MainActivity;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends ActivityBase implements SplashContract.View, VersionDialogClickListener {
    private final String TAG = SplashActivity.class.getSimpleName();

    private Context mContext;
    private SplashContract.Presenter mSplashPresenter;
    private FirebasePerformanceUtil mFirebasePerformanceUtil;
    private PackageInfo mPakageInfo;
    private String mCurrentVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        this.mContext = this;

        init();
        mSplashPresenter.checkUpdate(mCurrentVersionName);

    }

    public void init() {
        mFirebasePerformanceUtil = new FirebasePerformanceUtil("koin_start");
        mFirebasePerformanceUtil.start();
        RetrofitManager.getInstance().init();
        UserInfoSharedPreferencesHelper.getInstance().init(getApplicationContext());
        new SplashPresenter(this);
        mCurrentVersionName = getVersionName();
        //TODO : 버전 확인, 업데이트
        //TODO : 자동로그인 방식 수정 필요 (토큰 업데이트)
        if(!BuildConfig.IS_DEBUG)
        Fabric.with(this,new Crashlytics());
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        this.mSplashPresenter = presenter;

    }

    @Override
    public void gotoLogin() {
        new Handler() {
        }.postDelayed(mLoginActivityRunnable, 2000);
    }

    @Override
    public void gotoMain() {
        new Handler() {
        }.postDelayed(mMainActivityRunnable, 2000);
    }

    @Override
    public void gotoAppMarket(int type,String storeVersion) {
        if(type!=Version.PRIORITY_LOW)
                createVersionUpdateDialog(type,storeVersion);
        else
            mSplashPresenter.callActivity();
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShortToast(message);
    }

    private final Runnable mLoginActivityRunnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(mContext, LoginActivity.class));
            overridePendingTransition(R.anim.fade, R.anim.hold);
            finish();
            mFirebasePerformanceUtil.stop();
        }
    };

    private final Runnable mMainActivityRunnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(mContext, MainActivity.class));
            overridePendingTransition(R.anim.fade, R.anim.hold);
            finish();
            mFirebasePerformanceUtil.stop();
        }
    };

    private String getVersionName()
    {
        try {
            mPakageInfo = getPackageManager().getPackageInfo(
                    this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return mPakageInfo.versionName;
    }

    public void createVersionUpdateDialog(int type,String storeVersion)
    {
        VersionUpdateDialog dialog = new VersionUpdateDialog(this,type,mCurrentVersionName,storeVersion,this);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Window window = dialog.getWindow();
        int x = (int)(size.x*0.8f);
        int y = (int)(size.y*0.4f);
        window.setLayout(x,y);
    }

    @Override
    public void onLaterClick() {
        mSplashPresenter.callActivity();
    }

    @Override
    public void onUpdateClick() {
    finish();
    }


    private Runnable mFinishAppRunnable = this::finish;
}