package `in`.koreatech.koin.core.analytics

object DepartmentAnalyticsConstant {
    const val EVENT_NAME = "DA2"

    object Label {
        const val MAIN_DEPARTMENT_CATEGORY = "department__department_main__department_category__click"
        const val MAIN_CONTACT_PHONE_COPY = "department__department_main__contact__phone_copy__click"
        const val MAIN_SEARCH = "department__department_main__search__click"
        const val LIST_CONTACT_PHONE_COPY = "department__department_list__contact__phone_copy__click"
        const val LIST_SEARCH = "department__department_list__search__click"
    }

    object Value {
        const val PHONE_COPY = "전화번호 복사"
    }
}
