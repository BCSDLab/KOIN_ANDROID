package `in`.koreatech.koin.feature.timetable.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.usecase.timetable.GetSemestersUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetUserSemestersUseCase
import javax.inject.Inject

@HiltViewModel
class SemesterViewModel @Inject constructor(
    private val getUserSemestersUseCase: GetUserSemestersUseCase,
    private val getSemestersUseCase: GetSemestersUseCase,
) : BaseViewModel() {

}