package in.koreatech.koin.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;


import com.crashlytics.android.Crashlytics;

import in.koreatech.koin.BuildConfig;
import in.koreatech.koin.R;
import in.koreatech.koin.core.bases.BaseActivity;
import in.koreatech.koin.core.contracts.SplashContract;
import in.koreatech.koin.core.helpers.DefaultSharedPreferencesHelper;
import in.koreatech.koin.core.helpers.VersionDialogClickListner;
import in.koreatech.koin.core.networks.RetrofitManager;
import in.koreatech.koin.core.networks.entity.Version;
import in.koreatech.koin.core.util.FirebasePerformanceUtil;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.presenters.SplashPresenter;
import io.fabric.sdk.android.Fabric;

public class StartActivity extends BaseActivity implements SplashContract.View,VersionDialogClickListner {
    private final String TAG = StartActivity.class.getSimpleName();

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
        DefaultSharedPreferencesHelper.getInstance().init(getApplicationContext());
        new SplashPresenter(this);
        mCurrentVersionName = getVersionName();
        Fabric.with(this,new Crashlytics());
        //TODO : 버전 확인, 업데이트
        //TODO : 자동로그인 방식 수정 필요 (토큰 업데이트)
        if(BuildConfig.IS_DEBUG)
            Crashlytics.log(Log.DEBUG,"BuildType","debug모드");
        else
            Crashlytics.log(Log.DEBUG,"BuildType","release모드");
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
        ToastUtil.makeShortToast(mContext, message);
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