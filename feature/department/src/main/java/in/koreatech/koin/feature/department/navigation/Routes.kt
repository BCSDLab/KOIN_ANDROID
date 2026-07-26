package `in`.koreatech.koin.feature.department.navigation

import `in`.koreatech.koin.feature.department.type.DepartmentCategory
import kotlinx.serialization.Serializable

internal object Routes {

    @Serializable data object DepartmentList

    @Serializable data class DepartmentDetail(val category: DepartmentCategory)
}
