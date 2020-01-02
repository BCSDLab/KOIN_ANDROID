package in.koreatech.koin.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.res.ResourcesCompat;
import android.view.View;


import static com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE;
import static com.google.android.material.snackbar.Snackbar.LENGTH_LONG;
import static com.google.android.material.snackbar.Snackbar.LENGTH_SHORT;

import in.koreatech.koin.core.R;
import in.koreatech.koin.core.activity.WebViewActivity;

/**
 * Created by hyerim on 2018. 4. 9....
 */
public class SnackbarUtil {
    static Snackbar mSnackbar = null;

    public static void makeShortSnackbar(View view, String message) {
        Snackbar.make(view, message, LENGTH_SHORT).show();
    }

    public static void makeLongSnackbar(View view, String message) {
        Snackbar.make(view, message, 5000).show();
    }

    public static void makeLongSnackbarActionYes(View view, String message) {
        mSnackbar = Snackbar.make(view, message, LENGTH_LONG);

        mSnackbar.setAction("YES", v -> mSnackbar.dismiss());

        View snackbarView = mSnackbar.getView();
        mSnackbar.setActionTextColor(ResourcesCompat.getColor(view.getResources(), R.color.white, null));
        snackbarView.setBackgroundColor(ResourcesCompat.getColor(view.getResources(), R.color.colorAccent, null));

        mSnackbar.show();
    }

    public static void makeLongSnackbarActionYes(View view, String message, final Runnable runnable) {
        mSnackbar = Snackbar.make(view, message, LENGTH_LONG);

        mSnackbar.setAction("YES", v -> {
            runnable.run();
            mSnackbar.dismiss();
        });

        View snackbarView = mSnackbar.getView();
        mSnackbar.setActionTextColor(ResourcesCompat.getColor(view.getResources(), R.color.white, null));
        snackbarView.setBackgroundColor(ResourcesCompat.getColor(view.getResources(), R.color.colorAccent, null));

        mSnackbar.show();
    }

    public static void makeSnackbarActionWebView(final Activity activity, final int resourceId, String message, final String title, final String url, int duration) {
        final View view = activity.findViewById(resourceId);
        mSnackbar = Snackbar.make(view, message, duration);

        mSnackbar.setAction("YES", v -> {
            mSnackbar.dismiss();

            Intent intent = new Intent(view.getContext(), WebViewActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("url", url);
            view.getContext().startActivity(intent);
            activity.finish();
        });

        View snackbarView = mSnackbar.getView();
        mSnackbar.setActionTextColor(ResourcesCompat.getColor(view.getResources(), R.color.white, null));
        snackbarView.setBackgroundColor(ResourcesCompat.getColor(view.getResources(), R.color.colorAccent, null));

        mSnackbar.show();
    }

    public static void makeIndefiniteSnackbarActionYes(View view, String message, final Runnable runnnable) {
        final Snackbar snackbar = Snackbar.make(view, message, LENGTH_INDEFINITE);

        snackbar.setAction("YES", v -> {
            new Handler() {
            }.postDelayed(runnnable, 2000);
            snackbar.dismiss();
        });

        View snackbarView = snackbar.getView();
        snackbar.setActionTextColor(ResourcesCompat.getColor(view.getResources(), R.color.white, null));
        snackbarView.setBackgroundColor(ResourcesCompat.getColor(view.getResources(), R.color.colorAccent, null));

        snackbar.show();
    }


}
