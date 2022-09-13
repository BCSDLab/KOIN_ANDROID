package `in`.koreatech.koin.ui.dining.appwidget

import `in`.koreatech.koin.R
import `in`.koreatech.koin.constant.DINING
import `in`.koreatech.koin.data.api.DiningApi
import `in`.koreatech.koin.data.constant.URLConstant.BASE_URL_PRODUCTION
import `in`.koreatech.koin.data.mapper.toDining
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

class DiningAppWidget : AppWidgetProvider() {
    private var currentDiningPlace: DiningPlace = DiningPlace.Korean
    private var retrofit: Retrofit? = null
    private var job: Job? = null

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val remoteViews = RemoteViews(context.packageName, R.layout.dining_widget)
        currentDiningPlace = DiningPlace.Korean
        makePlacesBackground(context, R.id.dining_widget_hansik_textview, remoteViews)
        setDiningWidget(context, remoteViews)
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val action = intent.action
        val remoteViews = RemoteViews(context.packageName, R.layout.dining_widget)
        val componentName = ComponentName(context, DiningAppWidget::class.java)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        if (action == null) return
        when (action) {
            DINING.WIDGET_ACTION_KOREAN_CLICKED -> {
                makePlacesBackground(context, R.id.dining_widget_hansik_textview, remoteViews)
                currentDiningPlace = DiningPlace.Korean
            }
            DINING.WIDGET_ACTION_ONEDISH_CLICKED -> {
                makePlacesBackground(context, R.id.dining_widget_ilpumsik_textview, remoteViews)
                currentDiningPlace = DiningPlace.Onedish
            }
            DINING.WIDGET_ACTION_WESTERN_CLICKED -> {
                makePlacesBackground(context, R.id.dining_widget_yangsik_textview, remoteViews)
                currentDiningPlace = DiningPlace.Western
            }
            DINING.WIDGET_ACTION_NUNGSU_CLICKED -> {
                makePlacesBackground(context, R.id.dining_widget_nungsu_textview, remoteViews)
                currentDiningPlace = DiningPlace.Nungsu
            }
            DINING.WIDGET_ACTION_REFRESH_CLICKED -> onUpdate(
                context,
                appWidgetManager,
                appWidgetManager.getAppWidgetIds(componentName)
            )
            else -> currentDiningPlace = DiningPlace.Korean
        }
        setDiningWidget(context, remoteViews)
        appWidgetManager.updateAppWidget(componentName, remoteViews)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        if (job != null && job!!.isActive) job!!.cancel()
    }

    private fun setDiningWidget(context: Context, remoteViews: RemoteViews) {
        setDiningType(remoteViews) // 아침, 점심, 석식 저장 및 출력
        val targetDay =
            if (DiningUtil.isNextDay()) TimeUtil.getNextDayDate(TimeUtil.getCurrentTime())
            else TimeUtil.getCurrentTime()
        setDate(remoteViews, targetDay)
        job = CoroutineScope(Dispatchers.IO).launch {
            getDiningList(targetDay)
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        setDiningList(it, context)
                    }
                }
                .onFailure {
                }
        }
        setButtonEventClickListener(remoteViews, context) // 클릭 리스너 생성
    }

    private fun setDiningType(remoteViews: RemoteViews) {
        remoteViews.setTextViewText(
            R.id.dining_widget_name_textview,
            DiningUtil.getCurrentType().typeKorean
        )
    }

    private fun setDiningList(diningArrayList: List<Dining>, context: Context) {
        val componentName = ComponentName(context, DiningAppWidget::class.java)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val remoteViews = RemoteViews(context.packageName, R.layout.dining_widget)
        val currentType = DiningUtil.getCurrentType()
        var selectedDining: Dining?
        if (currentDiningPlace is DiningPlace.Western) {  // 양식 식단은 없으나 특식 식단이 존재할 경우 양식 식단에 특식 식단을 배치함
            selectedDining = searchDining(diningArrayList, currentDiningPlace, currentType)
            if (selectedDining == null) {
                selectedDining =
                    searchDining(diningArrayList, DiningPlace.Special, currentType) // 특식으로 탐색
            }
        } else {
            selectedDining = searchDining(diningArrayList, currentDiningPlace, currentType)
        }
        if (selectedDining == null) { // 조건을 만족하는 식단이 없을 경우
            setNoMenu(remoteViews)
        } else {
            setDiningMenu(remoteViews, selectedDining.menu, context)
        }
        appWidgetManager.updateAppWidget(componentName, remoteViews)
    }

    private fun searchDining(
        diningList: List<Dining>,
        place: DiningPlace,
        type: DiningType
    ): Dining? {
        diningList.forEach {
            if (it.type == type.typeEnglish && it.place == place.placeKorean) {
                return it
            }
        }
        return null
    }

    private fun setNoMenu(remoteViews: RemoteViews) {
        remoteViews.setViewVisibility(R.id.dining_widget_menulist_linearlayout, View.GONE)
        remoteViews.setViewVisibility(R.id.dining_widget_no_menulist_linearlayout, View.VISIBLE)
    }

    private fun setDiningMenu(
        remoteViews: RemoteViews,
        diningMenuList: List<String>,
        context: Context
    ) {
        remoteViews.setViewVisibility(R.id.dining_widget_menulist_linearlayout, View.VISIBLE)
        remoteViews.setViewVisibility(R.id.dining_widget_no_menulist_linearlayout, View.GONE)
        val menuTextViewId = "dining_widget_menu_textview"
        clearDiningMenu(remoteViews, menuTextViewId, context)
        for (i in 0 until if (diningMenuList.size >= DINING.WIDGET_MAX_MENU_NUMBERS) DINING.WIDGET_MAX_MENU_NUMBERS else diningMenuList.size) {
            val resId: Int = context.resources
                .getIdentifier(menuTextViewId + i, "id", context.packageName)
            if (diningMenuList[i] == "") continue
            remoteViews.setTextViewText(resId, diningMenuList[i])
        }
    }

    private fun clearDiningMenu(
        remoteViews: RemoteViews,
        menuTextViewId: String,
        context: Context
    ) {
        for (i in 0 until DINING.WIDGET_MAX_MENU_NUMBERS) {
            val resId: Int = context.resources
                .getIdentifier(menuTextViewId + i, "id", context.packageName)
            remoteViews.setTextViewText(resId, "")
        }
    }

    private fun setButtonEventClickListener(remoteViews: RemoteViews, context: Context?) {
        setButtonEventName(
            remoteViews,
            context,
            DINING.WIDGET_ACTION_KOREAN_CLICKED,
            R.id.dining_widget_hansik_textview
        )
        setButtonEventName(
            remoteViews,
            context,
            DINING.WIDGET_ACTION_ONEDISH_CLICKED,
            R.id.dining_widget_ilpumsik_textview
        )
        setButtonEventName(
            remoteViews,
            context,
            DINING.WIDGET_ACTION_WESTERN_CLICKED,
            R.id.dining_widget_yangsik_textview
        )
        setButtonEventName(
            remoteViews,
            context,
            DINING.WIDGET_ACTION_NUNGSU_CLICKED,
            R.id.dining_widget_nungsu_textview
        )
        setButtonEventName(
            remoteViews,
            context,
            DINING.WIDGET_ACTION_REFRESH_CLICKED,
            R.id.dining_widget_refresh_imageview
        )
    }

    private fun setButtonEventName(
        remoteViews: RemoteViews,
        context: Context?,
        name: String?,
        viewNum: Int
    ) {
        val intent = Intent(context, DiningAppWidget::class.java).setAction(name)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        remoteViews.setOnClickPendingIntent(viewNum, pendingIntent)
    }

    private fun setDate(remoteViews: RemoteViews, targetDay: Date) {
        remoteViews.setTextViewText(
            R.id.dining_widget_date_textview,
            TimeUtil.dateFormatToMMDDEE(targetDay)
        )
    }

    private fun makePlacesBackground(context: Context, id: Int, remoteViews: RemoteViews) {
        //모든 텍스트의 색상을 very_dark_gray로 변경
        remoteViews.setTextColor(
            R.id.dining_widget_hansik_textview,
            context.getColor(R.color.very_dark_gray)
        )
        remoteViews.setTextColor(
            R.id.dining_widget_ilpumsik_textview,
            context.getColor(R.color.very_dark_gray)
        )
        remoteViews.setTextColor(
            R.id.dining_widget_yangsik_textview,
            context.getColor(R.color.very_dark_gray)
        )
        remoteViews.setTextColor(
            R.id.dining_widget_nungsu_textview,
            context.getColor(R.color.very_dark_gray)
        )
        //모든 백그라운드를 white색으로 변경
        remoteViews.setInt(
            R.id.dining_widget_hansik_textview,
            "setBackgroundResource",
            R.color.white
        )
        remoteViews.setInt(
            R.id.dining_widget_ilpumsik_textview,
            "setBackgroundResource",
            R.color.white
        )
        remoteViews.setInt(
            R.id.dining_widget_yangsik_textview,
            "setBackgroundResource",
            R.color.white
        )
        remoteViews.setInt(
            R.id.dining_widget_nungsu_textview,
            "setBackgroundResource",
            R.color.white
        )
        //선택한 버튼의 텍스트 색상과 백그라운드를 강조하는 것으로 변경
        remoteViews.setTextColor(id, context.getColor(R.color.vivid_orange))
        remoteViews.setInt(id, "setBackgroundResource", R.drawable.bg_rect_vividorange_radius_10dp)
    }

    private fun getRetrofitInstance(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder().baseUrl(BASE_URL_PRODUCTION).addConverterFactory(
                GsonConverterFactory.create()
            ).build()
        }
        return retrofit!!
    }

    private suspend fun getDiningList(date: Date): Result<List<Dining>> {
        return kotlin.runCatching {
            getRetrofitInstance().create(DiningApi::class.java)
                .getDining(TimeUtil.dateFormatToYYMMDD(date)).map {
                    it.toDining()
                }
        }
    }
}