package in.koreatech.koin.core.util;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;


public class LeakcanaryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}