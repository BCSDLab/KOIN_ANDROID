package `in`.koreatech.koin.feature.category

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.domain.usecase.token.IsTokenSavedInDeviceUseCase
import `in`.koreatech.koin.feature.category.component.CategoryMenuId
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val isTokenSavedInDeviceUseCase: IsTokenSavedInDeviceUseCase
) : ViewModel(), ContainerHost<CategoryState, CategorySideEffect> {
    override val container = container<CategoryState, CategorySideEffect>(CategoryState()) {
        checkLoginStatus()
    }

    private fun checkLoginStatus() = intent {
        val isTokenSaved = isTokenSavedInDeviceUseCase()
        reduce { state.copy(isAnonymous = !isTokenSaved) }
    }

    fun onMenuClick(id: CategoryMenuId) = intent {
        logCategoryMenuClick(id)
        postSideEffect(CategorySideEffect.NavigateToMenu(id))
    }

    private fun logCategoryMenuClick(id: CategoryMenuId) {
        val (label, value) = when (id) {
            CategoryMenuId.TIMETABLE -> AnalyticsConstant.Label.Category.CATEGORY_TIMETABLE to "시간표"
            CategoryMenuId.LOST_AND_FOUND -> AnalyticsConstant.Label.Category.CATEGORY_LOST_PROPERTY to "분실물"
            CategoryMenuId.OPERATING_INFO -> AnalyticsConstant.Label.Category.CATEGORY_CAMPUS to "교내 시설물 정보"
            CategoryMenuId.DEPARTMENT_INFO -> AnalyticsConstant.Label.Category.CATEGORY_CAMPUS to "학교 부서정보"
            CategoryMenuId.DINING -> AnalyticsConstant.Label.Category.CATEGORY_CAMPUS to "식단"
            CategoryMenuId.STORE -> AnalyticsConstant.Label.Category.CATEGORY_CAMPUS to "주변상점"
            CategoryMenuId.BUS_TIMETABLE -> AnalyticsConstant.Label.Category.CATEGORY_TRANSPORTATION to "버스 시간표"
            CategoryMenuId.TRANSPORT_SEARCH -> AnalyticsConstant.Label.Category.CATEGORY_TRANSPORTATION to "교통편 조회하기"
            CategoryMenuId.CALLVAN -> AnalyticsConstant.Label.Category.CATEGORY_TRANSPORTATION to "콜벤팟 모집"
            CategoryMenuId.CHAT -> AnalyticsConstant.Label.Category.CATEGORY_ETC to "채팅"
            CategoryMenuId.HOUSING -> AnalyticsConstant.Label.Category.CATEGORY_ETC to "복덕방"
            CategoryMenuId.KOIN_BUSINESS -> AnalyticsConstant.Label.Category.CATEGORY_ETC to "코인 for Business"
        }
        EventLogger.logCampusClickEvent(label, value)
    }
}
