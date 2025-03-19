package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

/**
 * 시간표 프레임과 담긴 강의들을 복구하는 유즈케이스
 * 학기가 함께 삭제된 경우, 학기도 같이 추가한다
 */
class RollbackFrameUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    suspend operator fun invoke(frameId: Int): Result<TimetableLectures> {
        return timetableRepository.postRollbackFrame(frameId)
    }
}
