package in.koreatech.koin.service_dining.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.util.ArrayList;

import in.koreatech.koin.R;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Dining;
import in.koreatech.koin.core.networks.interactors.DiningInteractor;
import in.koreatech.koin.core.networks.interactors.DiningRestInteractor;
import in.koreatech.koin.core.util.TimeUtil;

/**
 * Created by hansol, seongyun on 2019. 9. 22....
 */
public class DiningWidget extends AppWidgetProvider {
    private static final String TAG = DiningWidget.class.getName();
    private static final String[] TYPE = {"BREAKFAST", "LUNCH", "DINNER"};    // 식단 유형
    private static final String[] PLACE = {"한식", "일품식", "양식", "특식", "능수관"};   // 식단 제공 장소
    private static final String[] ENDTIME = {"09:00", "13:30", "18:30", "23:59"}; // 식단 출력 기준 시간
    private static final int MENUES = 8;    // 위젯에 출력가능한 메뉴의 최대 개수

    private static final String KOREAN = "KOREAN Clicked";
    private static final String ONEDISH = "ONEDISH Clicked";
    private static final String WESTERN = "WESTERN Clicked";
    private static final String NUNGSU = "NUNGSU Clicked";
    private static final String REFRESH = "REFRESH Clicked";

    private Context context;
    private String currentPlace;
    private String currentType;

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.d(TAG, "updateAppWidget");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.dining_widget);


        makePlacesBackground(context, R.id.dining_widget_hansik_textview, remoteViews);
        currentPlace = PLACE[0];    // 기본적으로 한식을 출력

        setDiningWidget(remoteViews);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");
        this.context = context;
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        this.context = context;
        super.onReceive(context, intent);

        String action = intent.getAction();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.dining_widget);
        ComponentName componentName = new ComponentName(context, DiningWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        if (action == null) return;
        switch (action) {
            case KOREAN:
                makePlacesBackground(context, R.id.dining_widget_hansik_textview, remoteViews);
                currentPlace = PLACE[0];
                break;
            case ONEDISH:
                makePlacesBackground(context, R.id.dining_widget_ilpumsik_textview, remoteViews);
                currentPlace = PLACE[1];
                break;
            case WESTERN:
                makePlacesBackground(context, R.id.dining_widget_yangsik_textview, remoteViews);
                currentPlace = PLACE[2];
                break;
            case NUNGSU:
                makePlacesBackground(context, R.id.dining_widget_nungsu_textview, remoteViews);
                currentPlace = PLACE[4];
                break;
            case REFRESH:
                this.onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(componentName));
                break;
            default:
                currentPlace = PLACE[0];
        }
        Log.d(TAG, action);

        setDiningWidget(remoteViews);
        appWidgetManager.updateAppWidget(componentName, remoteViews);
    }

    /**
     * 식단 위젯에 필요한 정보를 얻어내고 그에 맞는 데이터를 서버로부터 불러오는 메소드
     *
     * @param remoteViews
     */
    private void setDiningWidget(RemoteViews remoteViews) {
        int today = setDiningType(remoteViews); // 아침, 점심, 석식 저장 및 출력
        String targetDay = TimeUtil.getChangeDateFormatYYMMDD(today);

        Log.d(TAG, "Target Day : " + targetDay);
        setDate(remoteViews, targetDay);  // 목표 날짜 출력
        DiningInteractor diningInteractor = new DiningRestInteractor();
        diningInteractor.readDiningList(targetDay, updateDiningApiCallback);

        setButtonEventClickListener(remoteViews, context);  // 클릭 리스너 생성
    }

    /**
     * 현재 시각과 기준 시각을 비교하여 아침, 점심, 저녁을 출력하는 메소드
     *
     * @param remoteViews
     * @return
     */
    private int setDiningType(RemoteViews remoteViews) {
        if (TimeUtil.timeCompareWithCurrent(false, ENDTIME[0]) >= 0) {   // 조식메뉴 출력
            remoteViews.setTextViewText(R.id.dining_widget_name_textview, "아침");
            currentType = TYPE[0];
        } else if (TimeUtil.timeCompareWithCurrent(false, ENDTIME[1]) >= 0) {   // 중식메뉴 출력
            remoteViews.setTextViewText(R.id.dining_widget_name_textview, "점심");
            currentType = TYPE[1];
        } else if (TimeUtil.timeCompareWithCurrent(false, ENDTIME[2]) >= 0) {   // 석식메뉴 출력
            remoteViews.setTextViewText(R.id.dining_widget_name_textview, "저녁");
            currentType = TYPE[2];
        } else if (TimeUtil.timeCompareWithCurrent(false, ENDTIME[3]) >= 0) {    // 다음날 조식메뉴 출력
            remoteViews.setTextViewText(R.id.dining_widget_name_textview, "아침");
            currentType = TYPE[0];
            return 1;
        }
        return 0;
    }

    public final ApiCallback updateDiningApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Log.d(TAG, "OnSuccess");
            ArrayList<Dining> diningArrayList = (ArrayList<Dining>) object;
            setDiningList(diningArrayList);
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.d(TAG, throwable.getMessage());
        }
    };

    /**
     * 서버에서 받아온 식단 데이터를 현재 식사시간의 각 식단제공장소에 따라 분류
     *
     * @param diningArrayList
     */
    private void setDiningList(ArrayList<Dining> diningArrayList) {
        ComponentName componentName = new ComponentName(context, DiningWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.dining_widget);
        Dining selectedDining;

        if (currentPlace.equals(PLACE[2])) {  // 양식 식단은 없으나 특식 식단이 존재할 경우 양식 식단에 특식 식단을 배치함
            selectedDining = searchDining(diningArrayList, currentPlace);
            if (selectedDining == null) {
                selectedDining = searchDining(diningArrayList, PLACE[3]);    // 특식으로 탐색
            }
        } else {
            selectedDining = searchDining(diningArrayList, currentPlace);
        }

        if (selectedDining == null) { // 조건을 만족하는 식단이 없을 경우
            setNoMenu(remoteViews);
        } else {
            setDiningMenu(remoteViews, selectedDining.menu);
        }
        appWidgetManager.updateAppWidget(componentName, remoteViews);
    }

    /**
     * 식사제공장소에 맞는 식단이 있으면 식단을 출력하고 없으면 NULL 을 반환하는 메소드
     * @param diningArrayList 서버로부터 받아온 식단데이터
     * @param place 원하는 장소
     * @return
     */
    public Dining searchDining(ArrayList<Dining> diningArrayList, String place) {
        for (Dining dining : diningArrayList) {
            if (dining.type.equals(currentType) && dining.place.equals(place)) {
                return dining;
            }
        }

        return null;
    }

    /**
     * 식단 정보가 없을 때 "학교에서 식단이 업로드 되지 않았습니다." 문구를 출력하는 메소드
     *
     * @param remoteViews
     */
    private void setNoMenu(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.dining_widget_menulist_linearlayout, View.GONE);
        remoteViews.setViewVisibility(R.id.dining_widget_no_menulist_linearlayout, View.VISIBLE);
    }

    /**
     * 식사시간에 맞는 메뉴를 출력하는 메소드
     *
     * @param remoteViews
     * @param diningMenuList 식단 메뉴 리스트
     */
    private void setDiningMenu(RemoteViews remoteViews, ArrayList<String> diningMenuList) {
        remoteViews.setViewVisibility(R.id.dining_widget_menulist_linearlayout, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.dining_widget_no_menulist_linearlayout, View.GONE);

        String menuTextViewId = "dining_widget_menu_textview";

        clearDiningMenu(remoteViews, menuTextViewId);

        for (int i = 0; i < ((diningMenuList.size() >= MENUES) ? MENUES : diningMenuList.size()); i++) {
            int resId = context.getResources().getIdentifier(menuTextViewId + i, "id", context.getPackageName());
            if (diningMenuList.get(i).equals("")) continue;
            remoteViews.setTextViewText(resId, diningMenuList.get(i));
        }
    }

    /**
     * 식단 메뉴를 출력하는 텍스트뷰들을 "" 로 리셋
     *
     * @param remoteViews
     * @param menuTextViewId 텍스트뷰 Id 의 공통적인 부분
     */
    private void clearDiningMenu(RemoteViews remoteViews, String menuTextViewId) {
        for (int i = 0; i < MENUES; i++) {
            int resId = context.getResources().getIdentifier(menuTextViewId + i, "id", context.getPackageName());
            remoteViews.setTextViewText(resId, "");
        }
    }

    /**
     * 이벤트 클릭 리스너가 필요한 버튼 모음
     *
     * @param context
     */
    public void setButtonEventClickListener(RemoteViews remoteViews, Context context) {
        setButtonEvnetName(remoteViews, context, KOREAN, R.id.dining_widget_hansik_textview);
        setButtonEvnetName(remoteViews, context, ONEDISH, R.id.dining_widget_ilpumsik_textview);
        setButtonEvnetName(remoteViews, context, WESTERN, R.id.dining_widget_yangsik_textview);
        setButtonEvnetName(remoteViews, context, NUNGSU, R.id.dining_widget_nungsu_textview);
        setButtonEvnetName(remoteViews, context, REFRESH, R.id.dining_widget_refresh_imageview);
    }

    /**
     * Action값을 설정한 PendingIntent를 remoteView에 이벤트 등록으로 설정하는 메소드
     *
     * @param context widget의 시스템 정보
     * @param name    Action Name
     * @return
     */
    public void setButtonEvnetName(RemoteViews remoteViews, Context context, String name, int viewNum) {
        Intent intent = new Intent(context, DiningWidget.class).setAction(name);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(viewNum, pendingIntent);
    }

    /**
     * 날짜를 표시해주는 메소드
     *
     * @param remoteViews
     */
    public void setDate(RemoteViews remoteViews, String targetDay) {
        Log.d(TAG, "setDate : " + targetDay);

        String month = targetDay.substring(2, 4);
        String day = targetDay.substring(4, 6);
        String dayWeekStr = null;

        try {
            dayWeekStr = TimeUtil.getDateDay(targetDay, "yyMMdd");
        } catch (Exception e) {
            e.printStackTrace();
        }

        remoteViews.setTextViewText(R.id.dining_widget_date_textview, month + "/" + day + " (" + dayWeekStr + ")");
    }

    /**
     * 선택한 항목의 텍스트컬러와 백그라운드 이미지를 강조하는 메소드
     *
     * @param context
     * @param id          선택된 버튼의 id 값
     * @param remoteViews
     */
    public void makePlacesBackground(Context context, int id, RemoteViews remoteViews) {
        //모든 텍스트의 색상을 very_dark_gray로 변경
        remoteViews.setTextColor(R.id.dining_widget_hansik_textview, context.getResources().getColor(R.color.very_dark_gray));
        remoteViews.setTextColor(R.id.dining_widget_ilpumsik_textview, context.getResources().getColor(R.color.very_dark_gray));
        remoteViews.setTextColor(R.id.dining_widget_yangsik_textview, context.getResources().getColor(R.color.very_dark_gray));
        remoteViews.setTextColor(R.id.dining_widget_nungsu_textview, context.getResources().getColor(R.color.very_dark_gray));
        //모든 백그라운드를 white색으로 변경
        remoteViews.setInt(R.id.dining_widget_hansik_textview, "setBackgroundResource", R.color.white);
        remoteViews.setInt(R.id.dining_widget_ilpumsik_textview, "setBackgroundResource", R.color.white);
        remoteViews.setInt(R.id.dining_widget_yangsik_textview, "setBackgroundResource", R.color.white);
        remoteViews.setInt(R.id.dining_widget_nungsu_textview, "setBackgroundResource", R.color.white);
        //선택한 버튼의 텍스트 색상과 백그라운드를 강조하는 것으로 변경
        remoteViews.setTextColor(id, context.getResources().getColor(R.color.vivid_orange));
        remoteViews.setInt(id, "setBackgroundResource", R.drawable.bg_rect_vividorange_radius_10dp);
    }
}

