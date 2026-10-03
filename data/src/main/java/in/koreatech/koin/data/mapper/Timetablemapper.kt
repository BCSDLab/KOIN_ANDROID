@file:Suppress("Detekt.TooManyFunctions")

package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.request.timetable.v3.TimetableCustomLectureRequest.TimetableCustomLectureBody
import `in`.koreatech.koin.data.request.timetable.v3.TimetableCustomLectureRequest.TimetableCustomLectureInfo
import `in`.koreatech.koin.data.request.timetable.v3.TimetableRegularLectureRequest.ClassPlace
import `in`.koreatech.koin.data.request.timetable.v3.TimetableRegularLectureRequest.TimetableRegularLectureBody
import `in`.koreatech.koin.data.response.timetable.v3.LectureResponseV3
import `in`.koreatech.koin.data.response.timetable.v3.TimetableFrameResponseV3
import `in`.koreatech.koin.data.response.timetable.v3.TimetableFramesResponseV3
import `in`.koreatech.koin.data.response.timetable.v3.TimetableLectureInfoResponse
import `in`.koreatech.koin.data.response.timetable.v3.TimetableLectureResponseV3
import `in`.koreatech.koin.data.response.timetable.v3.TimetableLecturesResponseV3
import `in`.koreatech.koin.domain.model.timetable.request.TimetableLectureQuery
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectureClassInfo
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import timber.log.Timber

internal data class YearTerm(val year: Int, val term: String)

internal fun String.toYearTerm(): YearTerm =
    YearTerm(
        year = substring(0, 4).toInt(),
        term =
        when (substring(4)) {
            "1" -> TERM_SPRING
            "-여름" -> TERM_SUMMER
            "2" -> TERM_FALL
            "-겨울" -> TERM_WINTER
            else -> throw IllegalArgumentException("\"$this\"")
        }
    )

internal fun toLegacySemester(year: Int, term: String): String =
    when (term) {
        TERM_SPRING -> "${year}1"
        TERM_FALL -> "${year}2"
        TERM_SUMMER -> "$year-여름"
        TERM_WINTER -> "$year-겨울"
        else -> {
            Timber.e("알 수 없는 학기 응답 : $term")
            "${year}1"
        }
    }

private const val TERM_SPRING = "1학기"
private const val TERM_SUMMER = "여름학기"
private const val TERM_FALL = "2학기"
private const val TERM_WINTER = "겨울학기"

internal data class TimeRange(val startTime: Int, val endTime: Int)

internal fun TimeRange.toClassTimes(): List<Int> = (startTime..endTime).toList()

internal fun List<Int>.toTimeRanges(): List<TimeRange> {
    val times = distinct().sorted()
    if (times.isEmpty()) return emptyList()

    val ranges = mutableListOf<TimeRange>()
    var start = times.first()
    var prev = start
    times.drop(1).forEach { time ->
        if (time != prev + 1 || time / 100 != prev / 100) {
            ranges.add(TimeRange(start, prev))
            start = time
        }
        prev = time
    }
    ranges.add(TimeRange(start, prev))
    return ranges
}

internal fun TimetableFrameResponseV3.toTimetableFrame(): TimetableFrame =
    TimetableFrame(
        id = id,
        timetableName = name,
        isMain = isMain
    )

internal fun List<TimetableFramesResponseV3>.toSemesterFrames(): Map<String, List<TimetableFrame>> =
    flatMap { yearFrames ->
        yearFrames.semesterFrames.map { semester ->
            toLegacySemester(yearFrames.year, semester.term) to semester.frames.map { it.toTimetableFrame() }
        }
    }.toMap()

internal fun TimetableLectureInfoResponse.toTimetableLectureClassInfo(): TimetableLectureClassInfo =
    TimetableLectureClassInfo(
        classTime = TimeRange(startTime, endTime).toClassTimes(),
        classPlace = place.orEmpty()
    )

internal fun TimetableLectureResponseV3.toTimetableLecture(): TimetableLecture =
    TimetableLecture(
        id = id,
        lectureId = lectureId ?: 0,
        regularNumber = regularNumber.orEmpty(),
        code = code.orEmpty(),
        designScore = designScore.orEmpty(),
        classInfos = lectureInfos.map { it.toTimetableLectureClassInfo() },
        memo = memo.orEmpty(),
        grades = grades.orEmpty(),
        classTitle = classTitle.orEmpty(),
        lectureClass = lectureClass.orEmpty(),
        target = target.orEmpty(),
        professor = professor.orEmpty(),
        department = department.orEmpty()
    )

internal fun TimetableLecturesResponseV3.toTimetableLectures(): TimetableLectures =
    TimetableLectures(
        timetableFrameId = timetableFrameId,
        timetable = timetable.orEmpty().map { it.toTimetableLecture() },
        grades = grades ?: 0,
        totalGrades = totalGrades ?: 0
    )

internal fun LectureResponseV3.toLecture(): Lecture =
    Lecture(
        id = id,
        code = code,
        name = name,
        grades = grades,
        lectureClass = lectureClass,
        regularNumber = regularNumber.orEmpty(),
        department = department,
        target = target,
        professor = professor.orEmpty(),
        isEnglish = isEnglish,
        designScore = designScore,
        isElearning = isElearning,
        classTime = lectureInfos.flatMap { TimeRange(it.startTime, it.endTime).toClassTimes() }
    )

internal fun TimetableLectureQuery.toRegularLectureBody(): TimetableRegularLectureBody =
    TimetableRegularLectureBody(
        id = id,
        lectureId = lectureId,
        classTitle = classTitle,
        courseType = null,
        generalEducationArea = null,
        classPlaces = classInfos.map { ClassPlace(it.classPlace) }
    )

internal fun List<TimetableLectureClassInfo>.toCustomLectureInfos(): List<TimetableCustomLectureInfo> =
    flatMap { classInfo ->
        classInfo.classTime.toTimeRanges().map { range ->
            TimetableCustomLectureInfo(
                startTime = range.startTime,
                endTime = range.endTime,
                place = classInfo.classPlace
            )
        }
    }

internal fun TimetableLecture.toCustomLectureCreateBody(): TimetableCustomLectureBody =
    TimetableCustomLectureBody(
        id = null,
        classTitle = classTitle,
        lectureInfos = classInfos.toCustomLectureInfos(),
        professor = professor,
        grades = "0",
        memo = ""
    )

internal fun TimetableLectureQuery.toCustomLectureUpdateBody(): TimetableCustomLectureBody =
    TimetableCustomLectureBody(
        id = id,
        classTitle = classTitle,
        lectureInfos = classInfos.toCustomLectureInfos(),
        professor = professor,
        grades = null,
        memo = null
    )
