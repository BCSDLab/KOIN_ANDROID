package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameQuery
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject


class UpdateTimetableFrameUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    /**
     * 시간표 프레임 정보 업데이트,
     * @param id 대상 시간표 프레임
     * @param name 변경할 프레임 이름
     * @param isMain 변경할 프레임 기본 시간표 여부
     */
    suspend operator fun invoke(id: Int, name: String, isMain: Boolean): Result<TimetableFrame> {
        return timetableRepository.putTimetableFrame(
            id,
            TimetableFrameQuery(
                timetableName = name,
                isMain = isMain
            )
        )
    }
}