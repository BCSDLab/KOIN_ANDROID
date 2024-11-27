package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.repository.TimetableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSemesterTimetableFramesUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    suspend operator fun invoke(isAnonymous: Boolean, semesters: List<String>): Flow<Map<String, List<TimetableFrame>>> = flow {

        if (isAnonymous) {
            emitAll(timetableRepository.getSemesters().map {
                it.map {
                    it to listOf(
                        TimetableFrame(
                            id = 0,
                            timetableName = "시간표1",
                            true
                        )
                    )
                }.toMap()
            })
        } else {
            val map = mutableMapOf<String, List<TimetableFrame>>()

            semesters.forEach { semester ->
                timetableRepository.getTimetableFrames(semester).collect {
                    map.put(semester, it.sortedBy { !it.isMain })
                }
            }
            emit(map)
        }
    }
}