package `in`.koreatech.koin.feature.timetable.state

data class TimetableDialogState(
    val isLoginVisible: Boolean = false, // 로그인 유도 팝업
    val isStartTimePickerVisible: Boolean = false, // 시작 시간 설정 팝업
    val isEndTimePickerVisible: Boolean = false, // 종료 시간 설정 팝업
    val isSelectDepartmentVisible: Boolean = false, // 필터링 학과 선택 팝업
    val isLectureDuplicationVisible: Boolean = false, // 기존 강의 중복 팝업
    val isCustomLectureDuplicationVisible: Boolean = false, // 커스텀 강의 추가 시, 기존 강의 중복 팜업
    val isDeleteLectureVisible : Boolean = false
)
