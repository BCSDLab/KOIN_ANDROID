package in.koreatech.koin.core.bases;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by hyerim on 2018. 4. 1..
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}

