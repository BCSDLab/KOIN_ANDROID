package `in`.koreatech.koin.core.analytics

object TimetableAnalyticsConstant {
    object Category {
        const val CLICK = "click"
        const val SELECT = "select"
    }

    object Label {
        const val BACK_CLICK = "timetable__timetable__navigation__back__click"

        const val CATEGORY_ENTRY = "timetable__main_category__category_navigation__timetable_entry__click"

        const val TIMETABLE_LIST_ENTRY = "timetable__timetable__timetable_list__entry__click"

        const val TIMETABLE_LIST_SELECT = "timetable__timetable_list__timetable_list__select"

        const val IMAGE_SAVE = "timetable__timetable__timetable_export__image_save__click"

        const val EDIT_ENTRY = "timetable__timetable__timetable_edit__entry__click"

        const val SEMESTER_MANAGE_APPLY = "timetable__timetable__semester_manage__edit_apply__click"
    }

    object Value {
        const val BACK = "뒤로가기"
        const val CATEGORY_ENTRY = "시간표"
        const val TIMETABLE_LIST_ENTRY = "학기 시간표 선택"
        const val IMAGE_SAVE = "이미지 저장"
        const val EDIT_ENTRY = "시간표 수정"
        const val ADD = "추가"
        const val DELETE = "삭제"
        const val SEPARATOR = "_"
    }
}
