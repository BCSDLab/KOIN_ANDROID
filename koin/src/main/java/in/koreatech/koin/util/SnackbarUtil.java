package in.koreatech.koin.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import com.google.android.material.snackbar.Snackbar;

import androidx.core.content.res.ResourcesCompat;

import android.provider.Settings;
import android.view.View;


import static com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE;
import static com.google.android.material.snackbar.Snackbar.LENGTH_LONG;
import static com.google.android.material.snackbar.Snackbar.LENGTH_SHORT;

import in.koreatech.koin.core.R;
import in.koreatech.koin.core.activity.WebViewActivity;

public class SnackbarUtil {
    static Snackbar snackbar = null;

    public static void makeShortSnackbar(View view, String message) {
        Snackbar.make(view, message, LENGTH_SHORT).show();
    }

    public static void makeLongSnackbar(View view, String message) {
        Snackbar.make(view, message, 5000).show();
    }

    public static void makePermissionSnackBar(View view, String message) {
        snackbar = Snackbar.make(view, message, LENGTH_LONG);

        snackbar.setAction("설정", v -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", view.getContext().getPackageName(), null);
            intent.setData(uri);
            view.getContext().startActivity(intent);
        });

        View snackbarView = snackbar.getView();
        snackbar.setActionTextColor(ResourcesCompat.getColor(view.getResources(), R.color.white, null));
        snackbarView.setBackgroundColor(ResourcesCompat.getColor(view.getResources(), R.color.colorAccent, null));

        snackbar.show();
    }

    public static void makeLongSnackbarActionYes(View view, String message) {
        snackbar = Snackbar.make(view, message, LENGTH_LONG);

        snackbar.setAction("YES", v -> snackbar.dismiss());

        View snackbarView = snackbar.getView();
        snackbar.setActionTextColor(ResourcesCompat.getColor(view.getResources(), R.color.white, null));
        snackbarView.setBackgroundColor(ResourcesCompat.getColor(view.getResources(), R.color.colorAccent, null));

        snackbar.show();
    }

    public static void makeLongSnackbarActionYes(View view, String message, final Runnable runnable) {
        snackbar = Snackbar.make(view, message, LENGTH_LONG);

        snackbar.setAction("YES", v -> {
            runnable.run();
            snackbar.dismiss();
        });

        View snackbarView = snackbar.getView();
        snackbar.setActionTextColor(ResourcesCompat.getColor(view.getResources(), R.color.white, null));
        snackbarView.setBackgroundColor(ResourcesCompat.getColor(view.getResources(), R.color.colorAccent, null));

        snackbar.show();
    }

    public static void makeSnackbarActionWebView(final Activity activity, final int resourceId, String message, final String title, final String url, int duration) {
        final View view = activity.findViewById(resourceId);
        snackbar = Snackbar.make(view, message, duration);

        snackbar.setAction("YES", v -> {
            snackbar.dismiss();

            Intent intent = new Intent(view.getContext(), WebViewActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("url", url);
            view.getContext().startActivity(intent);
            activity.finish();
        });

        View snackbarView = snackbar.getView();
        snackbar.setActionTextColor(ResourcesCompat.getColor(view.getResources(), R.color.white, null));
        snackbarView.setBackgroundColor(ResourcesCompat.getColor(view.getResources(), R.color.colorAccent, null));

        snackbar.show();
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
