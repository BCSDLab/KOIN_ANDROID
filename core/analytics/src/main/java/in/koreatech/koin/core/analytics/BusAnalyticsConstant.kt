package `in`.koreatech.koin.core.analytics

object BusAnalyticsConstant {
    object Category {
        const val CLICK = "click"
        const val SELECT = "select"
    }

    object Label {
        const val NOTICE_OPEN = "bus__bus_timetable__bus_notice__open__click"

        const val NOTICE_CLOSE = "bus__bus_timetable__bus_notice__close__click"

        const val FEEDBACK_ENTRY = "bus__bus_timetable__bus_feedback__entry__click"

        const val BUS_TYPE_TAB = "bus__bus_timetable__filter__bus_type__click"

        const val SHUTTLE_ROUTE_FILTER = "bus__bus_timetable__filter__shuttle_route__select"

        const val SHUTTLE_ROUTE_ENTRY = "bus__bus_timetable__shuttle_route__entry__click"

        const val TO_SCHOOL = "bus__bus_timetable_detail__filter__to_school__click"

        const val TO_HOME = "bus__bus_timetable_detail__filter__to_home__click"

        const val DAESEONG_DIRECTION = "bus__bus_timetable__filter__daeseong_direction__select"

        const val CITY_ROUTE = "bus__bus_timetable__filter__city_route__select"

        const val CITY_DIRECTION = "bus__bus_timetable__filter__city_direction__select"

        const val SEARCH_DEPARTURE_OPEN = "bus__bus_search__search__departure_open__click"

        const val SEARCH_DEPARTURE_CONFIRM = "bus__bus_search__search__departure_confirm__select"

        const val SEARCH_ARRIVAL_OPEN = "bus__bus_search__search__arrival_open__click"

        const val SEARCH_ARRIVAL_CONFIRM = "bus__bus_search__search__arrival_confirm__select"

        const val SEARCH_SWAP = "bus__bus_search__search__swap__click"

        const val SEARCH_SUBMIT = "bus__bus_search__search__click"

        const val SEARCH_RESULT_BACK = "bus__bus_search_result__navigation__back__click"

        const val SEARCH_RESULT_CLOSE = "bus__bus_search_result__navigation__bus_search_return__click"

        const val DEPARTURE_TIME_OPEN = "bus__bus_search_result__search__departure_time_open__click"

        const val DEPARTURE_TIME_NOW = "bus__bus_search_result__search__departure_time_now__click"

        const val DEPARTURE_TIME_CONFIRM = "bus__bus_search_result__search__departure_time_confirm__click"

        const val SEARCH_RESULT_BUS_TYPE = "bus__bus_search_result__filter__bus_type__select"
    }

    object Value {
        const val BUS_SEARCH = "버스 교통편"
        const val DEPARTURE_OPEN = "출발지 선택"
        const val ARRIVAL_OPEN = "목적지 선택"
        const val SWAP = "출발지 도착지 바꾸기"
        const val SEARCH = "조회하기"
        const val BACK = "뒤로가기"
        const val CLOSE = "닫기"
        const val DEPARTURE_TIME_OPEN = "출발 시각 설정"
        const val DEPARTURE_NOW = "지금 출발"
        const val TIME_CHANGED = "시간 변경"
        const val TIME_UNCHANGED = "시간 유지"
        const val SEPARATOR = "_"
    }
}
