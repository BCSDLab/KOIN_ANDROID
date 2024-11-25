package `in`.koreatech.koin.feature.timetable.state

import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.view.TimetableBottomSheetContentMode

data class TimetableState(
    val range: Int = 9, // 강의 시간 범위 (9시~18시 : 9(최소), 9시~24시 : 15(최대))
    val frameId: Int = 0, // 시간표 프레임 ID
    val currentSemester: String = "", // 현재 선택된 학기
    val timetableName: String = "", // 현재 선택된 학기의 시간표 이름
    val semesters: List<String> = emptyList(), // 전체 학기
    val detailLecture: TimetableLecture? = null, // 선택된 강의 클릭 후 나오는 강의 상세 데이터
    val deleteLecture: TimetableLecture? = null,
    val duplicationLecture: Lecture? = null, // 기존 강의 중복
    val duplicationTimetableEvent: TimetableEvent? = null, // 커스텀 강의 중복
    val timetableEvents: List<TimetableEvent> = emptyList(), // 선택된 강의들 (색 칠해있는 강의들)
    val clickedTimetableEvents: List<TimetableEvent> = emptyList(), // 강의 클릭 시 블럭 선으로된 사각형으로 활성화
    val etcClickedTimetableEvents: List<TimetableEvent> = emptyList(), // 강의 클릭 시(클릭한 강의 이외에 같은 강의) 블럭 점선으로된 사각형으로 활성화
    val selectedLecture: Lecture? = null, // 기존 강의 클릭 시, 선택 강의 활성화
    val customTimeData: CustomExtraContentState = CustomExtraContentState(), // 커스텀 강의 첫 번째 시간 데이터
    val timetableLectures: TimetableLectures = TimetableLectures(0, emptyList(), 0, 0), // 서버로 부터 받아오면서 갱신되는 나의 강의들
    val loading: Boolean = false, // 로딩
    val isAnonymous: Boolean = true, // 로그인 / 비 로그인 판단 플래그 값
    val bottomSheetMode: TimetableBottomSheetContentMode = TimetableBottomSheetContentMode.BASIC, // 바텀시트 모드 (BASIC : 기존 강의, CUSTOM : 커스텀 강의)
    val bottomSheetUI:BottomSheetUI = BottomSheetUI.DEFAULT, // 바텀 시트 ui 모드 (DEFAULT : 강의 추가, DETAIL : 강의 세부 사항, NOTHING: 내려가있는 상태)
    val bottomSheetCollapse: Boolean = false,
)

enum class BottomSheetUI {
    DEFAULT, DETAIL
}