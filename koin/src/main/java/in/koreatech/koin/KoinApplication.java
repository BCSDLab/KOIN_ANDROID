package in.koreatech.koin;

import com.squareup.leakcanary.LeakCanary;

import in.koreatech.koin.core.bases.BaseApplication;
import in.koreatech.koin.core.helpers.DefaultSharedPreferencesHelper;
import in.koreatech.koin.core.util.font_change.Typekit;

/**
 * Created by hyerim on 2018. 5. 28....
 */
public class KoinApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        DefaultSharedPreferencesHelper.getInstance().init(getApplicationContext());
        if (LeakCanary.isInAnalyzerProcess(this)) {
        }
        LeakCanary.install(this);

        Typekit.getInstance()
                .addBold(Typekit.createFromAsset(this, "fonts/notosanscjkkr_bold.otf"))
                .addNormal(Typekit.createFromAsset(this, "fonts/notosans_regular.ttf"))
                .addCustom1(Typekit.createFromAsset(this, "fonts/notosans_semibold.ttf"))
                .addCustom2(Typekit.createFromAsset(this, "fonts/notosanscjkkr_light.otf"))
                .addCustom3(Typekit.createFromAsset(this, "fonts/notosans_medium.ttf"))
                .addCustom4(Typekit.createFromAsset(this, "fonts/notosans_light.ttf"))
                .addCustom5(Typekit.createFromAsset(this, "fonts/notosanscjkkr_medium.otf"))
                .addCustom6(Typekit.createFromAsset(this, "fonts/notosanscjkkr_regular.otf"));
    }
}
