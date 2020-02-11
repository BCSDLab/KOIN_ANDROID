package in.koreatech.koin.ui.timetable;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.RemoteViews;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import in.koreatech.koin.R;
import in.koreatech.koin.data.sharedpreference.TimeTableSharedPreferencesHelper;
import in.koreatech.koin.ui.splash.SplashActivity;

/**
 * 외부저장소(/storage/emulated/0)에 저장된 test.png파일을 홈화면에 표현하는 위젯
 * test.png파일은 ScreenshotActivity기능을 통해 스크린샷 찍으면 만들 수 있다.
 */
public class TimetableWidget extends AppWidgetProvider {

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        TimeTableSharedPreferencesHelper.getInstance().init(context.getApplicationContext());
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timetable_widget);
        Bitmap bitmap = null;
        String path = TimeTableSharedPreferencesHelper.getInstance().loadLastPathTimetableImage();
        if (path != null) {
            try {
                try {
                    File file = new File(path, "timetable.png");
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                views.setTextViewText(R.id.timetable_widget_need_to_add_textview, "시간표를 저장해주세요");
            }
        }

        if (bitmap != null) {
            views.setImageViewBitmap(R.id.timetable_timetableview, bitmap);
            views.setViewVisibility(R.id.timetable_widget_need_to_add_textview, View.GONE);
        } else
            views.setViewVisibility(R.id.timetable_widget_need_to_add_textview, View.VISIBLE);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(context, SplashActivity.class));
        PendingIntent pi = PendingIntent.getActivity(context, 1, intent, 0);
        views.setOnClickPendingIntent(R.id.timetable_timetableview, pi);
        views.setOnClickPendingIntent(R.id.timetable_widget_need_to_add_textview, pi);


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName testWidget = new ComponentName(context.getPackageName(), TimetableWidget.class.getName());
        int[] widgetIds = appWidgetManager.getAppWidgetIds(testWidget);

        if (widgetIds != null && widgetIds.length > 0) {
            this.onUpdate(context, AppWidgetManager.getInstance(context), widgetIds);
        }

    }

}

