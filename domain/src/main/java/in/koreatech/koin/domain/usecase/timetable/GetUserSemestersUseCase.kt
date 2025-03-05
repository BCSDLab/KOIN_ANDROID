package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.repository.TimetableRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 유저가 추가한 학기 리스트
 * @param isAnonymouse 익명 여부
 */
class GetUserSemestersUseCase
    @Inject
    constructor(
        private val timetableRepository: TimetableRepository,
    ) {
        operator fun invoke(isAnonymous: Boolean): Flow<List<String>> {
            return if (isAnonymous) timetableRepository.getSemesters() else timetableRepository.getSemesterCheck()
        }
    }
