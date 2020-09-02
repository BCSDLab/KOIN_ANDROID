package in.koreatech.koin.ui.bus;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.widget.RemoteViews;

import java.text.ParseException;

import in.koreatech.koin.R;
import in.koreatech.koin.constant.BusType;
import in.koreatech.koin.data.network.entity.RegularSemesterBus;
import in.koreatech.koin.data.network.entity.SeasonalSemesterBus;
import in.koreatech.koin.data.network.entity.Term;
import in.koreatech.koin.data.network.entity.VacationBus;
import in.koreatech.koin.data.network.interactor.TermInteractor;
import in.koreatech.koin.data.network.interactor.TermRestInteractor;
import in.koreatech.koin.data.sharedpreference.BusWidgetSharedPreferencesHelper;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Bus;
import in.koreatech.koin.data.network.interactor.CityBusInteractor;
import in.koreatech.koin.data.network.interactor.CityBusRestInteractor;
import in.koreatech.koin.data.network.response.BusResponse;
import in.koreatech.koin.constant.BusDestinationEnum;

/**
 * 버스 위젯
 */
public class BusWidget extends AppWidgetProvider {
    public static final String TAG = "BusWidget";
    private CityBusInteractor busInteractor;
    private TermInteractor termInteractor;
    private Pair<String, String> currentBusPair;
    private Context context;
    private static final String SYNC_CLICKED = "buttonTimeTextRefreshClicked";
    private static final String CITYBUS_CLICKED = "buttonKoreatechClicked";
    private static final String EXPRESSEBUS_CLIKCED = "buttonExpressedClicked";
    private static final String SHUTTLE_BUS_CLICKED = "buttonShuttleClicked";
    private static final String REVERSE_BUTTON_CLICKED = "reverseButtonClicked";
    private static final String LEFT_BUTTON_CLICKED = "leftButtonClicked";
    private static final String RIGHT_BUTTON_CLICKED = "rightButtonClicked";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bus_widget);
        views.setOnClickPendingIntent(R.id.time_text_refresh_button, getPendingSelfIntent(context, appWidgetId, SYNC_CLICKED));
        views.setOnClickPendingIntent(R.id.city_bus_button, getPendingSelfIntent(context, appWidgetId, CITYBUS_CLICKED));
        views.setOnClickPendingIntent(R.id.koreatech_bus_button, getPendingSelfIntent(context, appWidgetId, SHUTTLE_BUS_CLICKED));
        views.setOnClickPendingIntent(R.id.express_bus_button, getPendingSelfIntent(context, appWidgetId, EXPRESSEBUS_CLIKCED));
        views.setOnClickPendingIntent(R.id.reverse_button, getPendingSelfIntent(context, appWidgetId, REVERSE_BUTTON_CLICKED));
        views.setOnClickPendingIntent(R.id.left_arrow_imagebutton, getPendingSelfIntent(context, appWidgetId, LEFT_BUTTON_CLICKED));
        views.setOnClickPendingIntent(R.id.right_arrow_imagebutton, getPendingSelfIntent(context, appWidgetId, RIGHT_BUTTON_CLICKED));
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context = context;
        for (int appWidgetId : appWidgetIds) {
            init(context);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    public void init(Context context) {
        if (context == null) return;
        this.context = context;
        initBusWidgetSharedPreferenece(context);
        int busType = BusWidgetSharedPreferencesHelper.getInstance().loadLastButtonSelectedPosition();
        int destinationType = BusWidgetSharedPreferencesHelper.getInstance().loadBusDestinationType();
        boolean isReversed = BusWidgetSharedPreferencesHelper.getInstance().loadBusSelectionReversed();
        updateBusTime(busType, destinationType, isReversed);
    }

    /**
     * 버튼 클릭 및 이벤트를 받아와서 처리
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        initBusWidgetSharedPreferenece(context);
        int destinationType = BusWidgetSharedPreferencesHelper.getInstance().loadBusDestinationType();
        if (intent.getAction() == null) return;
        Log.e(TAG, "onReceive: " + intent.getAction());
        switch (intent.getAction()) {
            case CITYBUS_CLICKED:
                BusWidgetSharedPreferencesHelper.getInstance().saveLastButtonSelectedPosition(2);
                break;
            case EXPRESSEBUS_CLIKCED:
                BusWidgetSharedPreferencesHelper.getInstance().saveLastButtonSelectedPosition(1);
                break;
            case SHUTTLE_BUS_CLICKED:
                BusWidgetSharedPreferencesHelper.getInstance().saveLastButtonSelectedPosition(0);
                break;
            case REVERSE_BUTTON_CLICKED:
                boolean isReversed = BusWidgetSharedPreferencesHelper.getInstance().loadBusSelectionReversed();
                BusWidgetSharedPreferencesHelper.getInstance().saveBusSelectionReversed(!isReversed);
                break;
            case LEFT_BUTTON_CLICKED:
                if (destinationType - 1 < 0) destinationType = 3;
                BusWidgetSharedPreferencesHelper.getInstance().saveBusDestinationType(--destinationType);
                break;
            case RIGHT_BUTTON_CLICKED:
                BusWidgetSharedPreferencesHelper.getInstance().saveBusDestinationType((++destinationType) % 3);
                break;
            default:
                break;

        }
        super.onReceive(context, intent);
        init(context);
    }

    @Override
    public void onEnabled(Context context) {
        this.context = context;
        init(context);
    }

    @Override
    public void onDisabled(Context context) {
    }

    /**
     * {@link BusWidgetSharedPreferencesHelper 초기화
     *
     * @param context
     */
    public void initBusWidgetSharedPreferenece(Context context) {
        BusWidgetSharedPreferencesHelper.getInstance().init(context.getApplicationContext());
        busInteractor = new CityBusRestInteractor();
        termInteractor = new TermRestInteractor();
    }

    /**
     * 버스 시간 업데이트
     *
     * @param busType         버스 종류  0 => 셔틀 버스 1 => 대성 고속 2 => 시내버스
     * @param destinationType 도착 타입 {@link BusDestinationEnum} 참고
     * @param isReversed      화살표가 반대인지 확인
     */
    public void updateBusTime(int busType, int destinationType, boolean isReversed) {
        Pair<String, String> busPair;
        int arrivalTime = 0;
        busPair = BusDestinationEnum.getValueOf(destinationType, isReversed);
        updateWidgetDepartureAndDestination(busPair.first, busPair.second);
        updateButtonColor(busType);
        this.currentBusPair =  busPair;

        try {
            switch (busType) {
                case 0:
                    termInteractor.readTerm(termApiCallback);
                    break;
                case 1:
                    arrivalTime = (int) Bus.getRemainExpressTimeToLong(busPair.first, busPair.second, true);
                    updateWidgetTimeWIthTime(arrivalTime);
                    break;
                case 2:
                    busInteractor.readCityBusList(updateCityBusApiCallback, busPair.first, busPair.second);
                    break;
            }
        } catch (ParseException e) {
            Log.e(TAG, "updateBusTime: ", e);
        }
    }

    /**
     * 시내 버스 도착 시간 값 api callback
     */
    private final ApiCallback updateCityBusApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            BusResponse busResponse = (BusResponse) object;
            int arrivalTime = busResponse.remainTime / 1000;
            Log.d(TAG, "onSuccess: " + arrivalTime);
            if (arrivalTime == 0) arrivalTime = -1;
            updateWidgetTimeWIthTime(arrivalTime);
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.d(TAG, throwable.getMessage());
            updateWidgetTimeWIthTime(-1);
        }
    };

    /**
     * 학기 정보 api callback
     */
    private final ApiCallback termApiCallback = new ApiCallback() {             //방학인지 학기중인지 정보를 받아오는 api callback
        @Override
        public void onSuccess(Object object) {
            Term termInfo = (Term) object;
            Bus currentSemesterBus;
            int arrivalTime;
            switch (termInfo.getTerm() % 10) {
                case 0:
                    currentSemesterBus = new RegularSemesterBus();
                case 1:
                    currentSemesterBus = new SeasonalSemesterBus();
                default:
                    currentSemesterBus = new VacationBus();
            }
            try {
                arrivalTime = (int) currentSemesterBus.getRemainShuttleTimeToLong(currentBusPair.first, currentBusPair.second, true);
                updateWidgetTimeWIthTime(arrivalTime);
            }
            catch (Exception e){
                updateWidgetTimeWIthTime(-1);
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.d(TAG, throwable.getMessage());

        }
    };
    /**
     * 버스 위젯 시간 update
     *
     * @param seconds 초
     */
    public void updateWidgetTimeWIthTime(int seconds) {
        Log.d(TAG, "updateBusTime: " + seconds);
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.bus_widget);
        view.setTextViewText(R.id.bus_time_textview, millsToIntervalTimeToString(seconds));
        ComponentName theWidget = new ComponentName(context, BusWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(theWidget, view);
    }

    /**
     * 출발지와 도착지 업데이트
     *
     * @param departure   출발지
     * @param destination 도착지
     */
    public void updateWidgetDepartureAndDestination(String departure, String destination) {
        Log.d(TAG, "departure" + departure + " destination: " + destination);
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.bus_widget);
        view.setTextViewText(R.id.bus_start_name, translatePlaceEnglishToKorean(departure));
        view.setTextViewText(R.id.bus_end_name, translatePlaceEnglishToKorean(destination));
        ComponentName theWidget = new ComponentName(context, BusWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(theWidget, view);
    }

    /**
     * 버튼 색 변경
     *
     * @param type 버튼 타입
     */
    public void updateButtonColor(int type) {
        Log.d(TAG, "busType : " + type);
        int busChangeButtonId = R.id.koreatech_bus_button;
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.bus_widget);
        initButtonView(view);
        switch (type) {
            case 0:
                busChangeButtonId = R.id.koreatech_bus_button;
                break;
            case 1:
                busChangeButtonId = R.id.express_bus_button;
                break;
            case 2:
                busChangeButtonId = R.id.city_bus_button;
                break;

        }
        view.setInt(busChangeButtonId, "setBackgroundResource", R.drawable.button_rect_line_squash_radius11dp);
        view.setInt(busChangeButtonId, "setTextColor", Color.argb(100, 247, 148, 30));
        ComponentName theWidget = new ComponentName(context, BusWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(theWidget, view);
    }

    /**
     * 버튼 초기화
     *
     * @param view
     */
    public void initButtonView(RemoteViews view) {
        view.setInt(R.id.koreatech_bus_button, "setBackgroundColor", Color.WHITE);
        view.setInt(R.id.express_bus_button, "setBackgroundColor", Color.WHITE);
        view.setInt(R.id.city_bus_button, "setBackgroundColor", Color.WHITE);
        view.setInt(R.id.koreatech_bus_button, "setTextColor", Color.BLACK);
        view.setInt(R.id.express_bus_button, "setTextColor", Color.BLACK);
        view.setInt(R.id.city_bus_button, "setTextColor", Color.BLACK);
    }

    /**
     * 남은 시간 String으로 변경
     *
     * @param mills 밀리초
     * @return 남은 시간 String 으로 반환
     */
    public String millsToIntervalTimeToString(int mills) {
        if (mills <= -1) return "운행정보없음";

        String strTime;
        int hour = mills / 3600;
        int min = (mills / 60) % 60;
        if (hour > 0)
            strTime = String.format("%d시간 %d분 남음", hour, min);
        else {
            if (min > 1)
                strTime = String.format("%d분 남음", min);
            else {
                strTime = String.format("약 %d분 남음", min);
            }
        }
        return strTime;
    }

    /**
     * 도착지 및 출발지를 한국어로 변환
     *
     * @param place 위치
     * @return 한국어로 번역된 도착지 와 출발지
     */
    public String translatePlaceEnglishToKorean(String place) {
        String korean = "";
        switch (place) {
            case "koreatech":
                korean = "한기대";
                break;
            case "terminal":
                korean = "터미널";
                break;
            case "station":
                korean = "천안역";
                break;
        }
        return korean;
    }

    /**
     * 버튼 클릭 pedingInetent를 반환 해준다.
     *
     * @param context
     * @param appWidgetId
     * @param action      버튼 클릭 action
     * @return
     */
    private static PendingIntent getPendingSelfIntent(Context context, int appWidgetId, String action) {
        Intent intent = new Intent(context, BusWidget.class).setAction(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
        return pendingIntent;
    }
}

