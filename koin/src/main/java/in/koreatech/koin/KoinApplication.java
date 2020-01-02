package in.koreatech.koin;

import android.app.Application;
import android.content.Context;

import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.util.font_change.Typekit;
import in.koreatech.koin.data.sharedpreference.RecentSearchSharedPreference;

/**
 * Created by hyerim on 2018. 5. 28....
 */
public class KoinApplication extends Application {
    private Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        Typekit.getInstance()
                .addBold(Typekit.createFromAsset(this, "fonts/notosanscjkkr_bold.otf"))
                .addNormal(Typekit.createFromAsset(this, "fonts/notosans_regular.ttf"))
                .addCustom1(Typekit.createFromAsset(this, "fonts/notosans_semibold.ttf"))
                .addCustom2(Typekit.createFromAsset(this, "fonts/notosanscjkkr_light.otf"))
                .addCustom3(Typekit.createFromAsset(this, "fonts/notosans_medium.ttf"))
                .addCustom4(Typekit.createFromAsset(this, "fonts/notosans_light.ttf"))
                .addCustom5(Typekit.createFromAsset(this, "fonts/notosanscjkkr_medium.otf"))
                .addCustom6(Typekit.createFromAsset(this, "fonts/notosanscjkkr_regular.otf"));
        init();
    }

    private void init() {
        UserInfoSharedPreferencesHelper.getInstance().init(applicationContext);
        ToastUtil.getInstance().init(applicationContext);
        RecentSearchSharedPreference.getInstance().init(applicationContext);

    }


}
