package in.koreatech.koin.service_timetable.widget.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableLayout;

import in.koreatech.koin.core.util.timetable.TimetableView;


public class ScreenshotUtil {
    private static ScreenshotUtil mInstance;

    private ScreenshotUtil() {
    }

    public static ScreenshotUtil getInstance() {
        if (mInstance == null) {
            synchronized (ScreenshotUtil.class) {
                if (mInstance == null) {
                    mInstance = new ScreenshotUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * Measures and takes a screenshot of the provided {@link View}.
     *
     * @param view The view of which the screenshot is taken
     * @return A {@link Bitmap} for the taken screenshot.
     */
    public Bitmap takeScreenshotForView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        return bitmap;
    }


    public Bitmap takeTimeTableScreenShot(ScrollView scrollView) {

        Bitmap bitmap = Bitmap.createBitmap(scrollView.getWidth(), TimetableView.getCellHeight() * 9, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        scrollView.setDrawingCacheEnabled(true);
        scrollView.buildDrawingCache(true);
        return bitmap;
    }

    public Bitmap takeTimeTableScreenShot(ScrollView scrollView, TableLayout tableLayout) {
        Bitmap gatherBitmap;
        Bitmap header = takeScreenshotForView(tableLayout);
        Bitmap table = takeTimeTableScreenShot(scrollView);
        int width = header.getWidth();
        int height = header.getHeight() + table.getHeight();
        gatherBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(gatherBitmap);
        comboImage.drawBitmap(header, 0f, 0f, null);
        comboImage.drawBitmap(table, 0f, header.getHeight(), null);

        return gatherBitmap;
    }

    public Bitmap takeScreenshotForScreen(Activity activity) {
        return takeScreenshotForView(activity.getWindow().getDecorView().getRootView());
    }
}