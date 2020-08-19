package in.koreatech.koin.core.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class StatusBarUtil {
    public static void makeStatusBarFillTransparent(@NonNull Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static int getStatusBarHeight(@NonNull Resources resources) {
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private static void applyTopPaddingStatusBarHeight(@NonNull View view, int height) {
        view.setPadding(0, height, 0, 0);
    }

    public static void applyTopPaddingStatusBarHeight(@NonNull View view, @NonNull Resources resources) {
        applyTopPaddingStatusBarHeight(view, getStatusBarHeight(resources));
    }
}
