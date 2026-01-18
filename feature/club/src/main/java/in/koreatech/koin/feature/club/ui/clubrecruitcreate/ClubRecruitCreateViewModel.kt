package `in`.koreatech.koin.feature.club.ui.clubrecruitcreate

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.upload.PreSignedUrlDomain
import `in`.koreatech.koin.domain.usecase.club.CreateClubRecruitmentUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.UploadPreSignedUrlV2UseCase
import `in`.koreatech.koin.feature.club.navigation.CLUB_ID
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ClubRecruitCreateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val uploadPreSignedUrlV2UseCase: UploadPreSignedUrlV2UseCase,
    private val createClubRecruitmentUseCase: CreateClubRecruitmentUseCase
) : ViewModel(), ContainerHost<ClubRecruitCreateState, ClubRecruitCreateSideEffect> {

    override val container = container<ClubRecruitCreateState, ClubRecruitCreateSideEffect>(ClubRecruitCreateState(), savedStateHandle) {
        val clubId = savedStateHandle.get<Int>(CLUB_ID)
        checkNotNull(clubId)
        intent { reduce { state.copy(clubId = clubId) } }
    }

    fun deleteImageUrl() = intent {
        reduce { state.copy(recruitImageUrl = "") }
    }

    fun showCreateRequestDialog() = intent {
        reduce { state.copy(showCreateRequestDialog = true) }
    }

    fun dismissCreateRequestDialog() = intent {
        reduce { state.copy(showCreateRequestDialog = false) }
    }

    fun createClubRecruitment(
        content: String
    ) = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        createClubRecruitmentUseCase(
            clubId = state.clubId,
            startDate = if (!state.recruitAlways) state.recruitStartDate.format(dateFormatter) else null,
            endDate = if (!state.recruitAlways) state.recruitEndDate.format(dateFormatter) else null,
            isAlwaysRecruiting = state.recruitAlways,
            imageUrl = state.recruitImageUrl,
            content = content
        ).onSuccess {
            reduce { state.copy(isLoading = false) }
            postSideEffect(ClubRecruitCreateSideEffect.RecruitCreateSuccess)
        }.onFailure {
            reduce { state.copy(isLoading = false) }
            postSideEffect(ClubRecruitCreateSideEffect.RecruitCreateFailure)
        }
    }

    fun postNavigateUp() = intent {
        postSideEffect(ClubRecruitCreateSideEffect.NavigateUp)
    }

    fun showCreateCancelDialog() = intent {
        reduce { state.copy(showCreateCancelDialog = true) }
    }

    fun dismissCreateCancelDialog() = intent {
        reduce { state.copy(showCreateCancelDialog = false) }
    }

    fun showDatePickerDialog() = intent {
        reduce { state.copy(showDatePickerDialog = true) }
    }

    fun dismissDatePickerDialog() = intent {
        reduce { state.copy(showDatePickerDialog = false) }
    }

    fun setRecruitStartDate(date: LocalDate) = intent {
        if (state.recruitEndDate <= date) {
            reduce { state.copy(recruitEndDate = date.plusDays(1)) }
        }
        reduce { state.copy(recruitStartDate = date) }
    }

    fun setRecruitEndDate(date: LocalDate) = intent {
        if (state.recruitStartDate >= date) {
            reduce { state.copy(recruitStartDate = date.minusDays(1)) }
        }
        reduce { state.copy(recruitEndDate = date) }
    }

    fun changeRecruitAlways() = intent {
        reduce { state.copy(recruitAlways = !state.recruitAlways) }
    }

    fun uploadImage(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: Uri
    ) = intent {
        if (state.isLoading) return@intent
        reduce {
            state.copy(isLoading = true)
        }
        uploadPreSignedUrlV2UseCase(
            domain = PreSignedUrlDomain.CLUB,
            contentLength = fileSize,
            contentType = fileType,
            fileName = fileName,
            imageUri = imageUri.toString()
        ).onSuccess {
            reduce {
                state.copy(
                    recruitImageUrl = it,
                    isLoading = false
                )
            }
        }.onFailure {
            intent {
                reduce {
                    state.copy(isLoading = false)
                }
                postSideEffect(ClubRecruitCreateSideEffect.ClubImageUploadFailure)
            }
        }
    }
}
