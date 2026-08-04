package `in`.koreatech.koin.feature.department.type

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.department.R
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

@Keep
@Serializable
enum class DepartmentCategory(
    @param:StringRes val titleRes: Int,
    @param:DrawableRes val iconRes: Int,
    val loggingValue: String
) {
    ACADEMIC(
        R.string.department_category_academic,
        R.drawable.ic_department_academic,
        "학사 / 수업"
    ),
    STUDENT_SUPPORT(
        R.string.department_category_student_support,
        R.drawable.ic_department_student_support,
        "학생지원 / 행정"
    ),
    EMPLOYMENT(
        R.string.department_category_employment,
        R.drawable.ic_department_employment,
        "취업 / 현장실습"
    ),
    INTERNATIONAL(
        R.string.department_category_international,
        R.drawable.ic_department_international,
        "국제 / 교환학생"
    ),
    FACILITY(
        R.string.department_category_facility,
        R.drawable.ic_department_facility,
        "시설 / 생활"
    ),
    OTHER(
        R.string.department_category_other,
        R.drawable.ic_department_other,
        "기타 기관"
    );

    companion object {
        val ALL = entries.toImmutableList()
    }
}
