package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.repository.TimetableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSemesterTimetableFramesUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    suspend operator fun invoke(isAnonymous: Boolean, semesters: List<String>): Flow<Map<String, List<TimetableFrame>>> = flow {
        val map = mutableMapOf<String, List<TimetableFrame>>()

        if(isAnonymous) {
            // TODO::hyeok 로컬 저장소에서 가져오도록 분기?
        } else {
            semesters.forEach { semester ->
                timetableRepository.getTimetableFrames(semester).collect {
                    map.put(semester, it.sortedBy { !it.isMain })
                }
            }
        }

        emit(map)
    }
}