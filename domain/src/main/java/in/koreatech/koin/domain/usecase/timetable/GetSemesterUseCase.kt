package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.repository.TimetableRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Deprecated("GetUserSemestersUseCase 로 변경")
class GetSemesterUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    operator fun invoke(isAnonymous: Boolean): Flow<List<String>> =
        if (isAnonymous) timetableRepository.getSemesters() else timetableRepository.getSemesterCheck()
}