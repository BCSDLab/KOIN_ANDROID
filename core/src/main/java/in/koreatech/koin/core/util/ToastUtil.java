package in.koreatech.koin.core.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author hyerim
 * @author nayunjae
 * @since 2018.4.9
 */
public class ToastUtil {
    private static Context applicationContext;

    private ToastUtil() {
    }

    private static class Holder {
        private static final ToastUtil toastUtil = new ToastUtil();
    }

    public static ToastUtil getInstance() {
        return Holder.toastUtil;
    }

    public void init(Context context) {
        applicationContext = context.getApplicationContext();
    }

    public void makeShortToast(String message) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show();
    }

    public void makeLongToast(String message) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show();
    }

    public void makeShortToast(int message) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show();
    }

    public void makeLongToast(int message) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show();
    }
}
