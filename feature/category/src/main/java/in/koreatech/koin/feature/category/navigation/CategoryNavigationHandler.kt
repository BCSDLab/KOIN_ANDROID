package `in`.koreatech.koin.feature.category.navigation

import android.content.Context
import android.content.Intent
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.feature.category.component.CategoryMenuId

internal object CategoryNavigationHandler {
    fun getIntent(
        id: CategoryMenuId,
        navigator: Navigator,
        context: Context,
        isAnonymous: Boolean
    ): Intent? = when (id) {
        CategoryMenuId.TIMETABLE -> navigator.navigateToTimetable(context, isAnonymous)
        CategoryMenuId.LOST_AND_FOUND -> navigator.navigateToLostAndFound(context)
        CategoryMenuId.OPERATING_INFO -> navigator.navigateToOperatingInfo(context)
        // CategoryMenuId.DEPARTMENT_INFO -> null
        CategoryMenuId.DINING -> navigator.navigateToDining(context)
        CategoryMenuId.STORE -> navigator.navigateToStore(context)
        CategoryMenuId.BUS_TIMETABLE -> navigator.navigateToBusTimeTable(context)
        CategoryMenuId.TRANSPORT_SEARCH -> navigator.navigateToBusSearch(context)
        CategoryMenuId.CALLVAN -> navigator.navigateToCallvan(context)
        CategoryMenuId.HOUSING -> navigator.navigateToLand(context)
        CategoryMenuId.KOIN_BUSINESS -> navigator.navigateToBusiness(context)
    }
}
