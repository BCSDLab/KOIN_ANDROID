package in.koreatech.koin.core.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

/**
 * Created by hyerim on 2018. 4. 9....
 */
public class ToastUtil {
    public static void makeShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void makeLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void makeShortToast(Context context, @StringRes int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void makeLongToast(Context context, @StringRes int message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
