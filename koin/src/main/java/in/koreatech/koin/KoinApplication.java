package in.koreatech.koin;
import android.app.Application;
import android.content.Context;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.util.ExceptionHandlerUtil;
import in.koreatech.koin.util.font_change.Typekit;
import in.koreatech.koin.data.sharedpreference.RecentSearchSharedPreference;
public class KoinApplication extends Application {
    private Context applicationContext;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        Typekit.getInstance()
                .addBold(Typekit.createFromAsset(this, getString(R.string.font_kr_bold)))
                .addNormal(Typekit.createFromAsset(this, getString(R.string.font_regular)))
                .addCustom1(Typekit.createFromAsset(this, getString(R.string.font_semibold)))
                .addCustom2(Typekit.createFromAsset(this, getString(R.string.font_kr_light)))
                .addCustom3(Typekit.createFromAsset(this, getString(R.string.font_medium)))
                .addCustom4(Typekit.createFromAsset(this, getString(R.string.font_light)))
                .addCustom5(Typekit.createFromAsset(this, getString(R.string.font_kr_medium)))
                .addCustom6(Typekit.createFromAsset(this, getString(R.string.font_kr_regular)));
        init();
    }
    private void init() {
        UserInfoSharedPreferencesHelper.getInstance().init(applicationContext);
        ToastUtil.getInstance().init(applicationContext);
        RecentSearchSharedPreference.getInstance().init(applicationContext);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandlerUtil(applicationContext));
    }
}