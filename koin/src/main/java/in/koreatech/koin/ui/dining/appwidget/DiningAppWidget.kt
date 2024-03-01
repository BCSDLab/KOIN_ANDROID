package `in`.koreatech.koin.ui.dining.appwidget

import `in`.koreatech.koin.R
import `in`.koreatech.koin.constant.DINING
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.usecase.dining.DiningUseCase
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
import android.widget.Toast
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class DiningAppWidget : AppWidgetProvider() {
    @Inject
    lateinit var diningUseCase: DiningUseCase
    private var currentDiningPlace: String? = null
    private var job: Job? = null

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val remoteViews = RemoteViews(context.packageName, R.layout.dining_widget)
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
        when (action) {
            null -> return
            DINING.WIDGET_ACTION_REFRESH_CLICKED -> {
                setDiningWidget(context, remoteViews)
                appWidgetManager.updateAppWidget(componentName, remoteViews)
            }

            else -> {
                if (action.contains(DINING.WIDGET_ACTION_CLICKED)) {
                    currentDiningPlace =
                        action.substring(DINING.WIDGET_ACTION_CLICKED.length + 1 until action.length)
                    setDiningWidget(context, remoteViews)
                    appWidgetManager.updateAppWidget(componentName, remoteViews)
                }
            }
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        if (job != null && job!!.isActive) job!!.cancel()
    }

    private fun setDiningWidget(context: Context, remoteViews: RemoteViews) {
        setDiningType(remoteViews) // 아침, 점심, 석식 저장 및 출력
        val targetDay = DiningUtil.getCurrentDate()
        setDate(remoteViews, targetDay)
        setButtonEventName(
            remoteViews,
            context,
            DINING.WIDGET_ACTION_REFRESH_CLICKED,
            R.id.dining_widget_refresh_imageview
        )
        job = CoroutineScope(Dispatchers.IO).launch {
            diningUseCase(targetDay)
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        setDiningList(it, context)
                    }
                }
                .onFailure {
                    Toast.makeText(
                        context,
                        R.string.error_network,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun setDiningType(remoteViews: RemoteViews) {
        remoteViews.setTextViewText(
            R.id.dining_widget_name_textview,
            DiningUtil.getCurrentType().typeKorean
        )
    }

    private fun setDiningList(diningList: List<Dining>, context: Context) {
        val componentName = ComponentName(context, DiningAppWidget::class.java)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val remoteViews = RemoteViews(context.packageName, R.layout.dining_widget)
        val currentType = DiningUtil.getCurrentType()
        remoteViews.removeAllViews(R.id.dining_widget_place_layout)
        with(DiningUtil.typeFiltering(diningList, currentType)) {
            var isCurrentPlaceInDining = false
            forEachIndexed { index, dining ->
                if (index < DINING.WIDGET_PLACE_NUMBERS && dining.place == currentDiningPlace) isCurrentPlaceInDining =
                    true
            }
            if (!isCurrentPlaceInDining) currentDiningPlace = null
            forEachIndexed { index, dining ->
                if (index < DINING.WIDGET_PLACE_NUMBERS) {
                    val placeRemoteViews =
                        RemoteViews(context.packageName, R.layout.dining_widget_place)
                    remoteViews.addView(
                        R.id.dining_widget_place_layout,
                        placeRemoteViews
                    )
                    if (currentDiningPlace == null) {
                        if (index == 0) {
                            currentDiningPlace = dining.place
                            makePlaceViewSelected(
                                context,
                                R.id.dining_widget_place_name_textview,
                                placeRemoteViews
                            )
                        }
                    } else {
                        if (currentDiningPlace == dining.place) {
                            makePlaceViewSelected(
                                context,
                                R.id.dining_widget_place_name_textview,
                                placeRemoteViews
                            )
                        }
                    }
                    placeRemoteViews.setTextViewText(
                        R.id.dining_widget_place_name_textview,
                        dining.place
                    )
                    setButtonEventName(
                        placeRemoteViews,
                        context,
                        "${DINING.WIDGET_ACTION_CLICKED} ${dining.place}",
                        R.id.dining_widget_place_name_textview
                    )
                }
            }
        }
        val selectedDining =
            currentDiningPlace?.let { DiningUtil.findDining(diningList, currentType, it) }
        if (selectedDining == null) { // 조건을 만족하는 식단이 없을 경우
            setNoMenu(remoteViews)
        } else {
            setDiningMenu(remoteViews, selectedDining.menu, context)
        }
        appWidgetManager.updateAppWidget(componentName, remoteViews)
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
        clearDiningMenu(remoteViews, context)
        for (i in 0 until if (diningMenuList.size >= DINING.WIDGET_MAX_MENU_NUMBERS) DINING.WIDGET_MAX_MENU_NUMBERS else diningMenuList.size) {
            val resId: Int = context.resources
                .getIdentifier(DINING.WIDGET_MENU_TEXT_VIEW_ID + i, "id", context.packageName)
            if (diningMenuList[i] == "") continue
            remoteViews.setTextViewText(resId, diningMenuList[i])
        }
    }

    private fun clearDiningMenu(
        remoteViews: RemoteViews,
        context: Context
    ) {
        for (i in 0 until DINING.WIDGET_MAX_MENU_NUMBERS) {
            val resId: Int = context.resources
                .getIdentifier(DINING.WIDGET_MENU_TEXT_VIEW_ID + i, "id", context.packageName)
            remoteViews.setTextViewText(resId, "")
        }
    }

    private fun setButtonEventName(
        remoteViews: RemoteViews,
        context: Context,
        name: String,
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

    private fun makePlaceViewSelected(context: Context, id: Int, remoteViews: RemoteViews) {
        remoteViews.setTextColor(id, ContextCompat.getColor(context, R.color.vivid_orange))
        remoteViews.setInt(id, "setBackgroundResource", R.drawable.bg_rect_vividorange_radius_10dp)
    }

    private fun makePlaceViewNonSelected(context: Context, id: Int, remoteViews: RemoteViews) {
        remoteViews.setTextColor(id, ContextCompat.getColor(context, R.color.very_dark_gray))
        remoteViews.setInt(id, "setBackgroundResource", R.color.white)
    }
}