package `in`.koreatech.koin.feature.club.ui.clubrecruitcreate

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.club.CreateClubRecruitmentUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetClubPreSignedUrlUseCase
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
    private val getClubPreSignedUrlUseCase: GetClubPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase,
    private val createClubRecruitmentUseCase: CreateClubRecruitmentUseCase
) : ViewModel(), ContainerHost<ClubRecruitCreateState, ClubRecruitCreateSideEffect> {

    override val container = container<ClubRecruitCreateState, ClubRecruitCreateSideEffect>(ClubRecruitCreateState(), savedStateHandle) {
        val clubId = savedStateHandle.get<Int>(CLUB_ID)
        checkNotNull(clubId)
        intent { reduce { state.copy(clubId = clubId) } }
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

    fun getPreSignedUrl(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: Uri
    ) = intent {
        if (state.isLoading) return@intent
        reduce {
            state.copy(isLoading = true)
        }
        getClubPreSignedUrlUseCase(
            fileSize,
            fileType,
            fileName
        ).onSuccess {
            uploadImage(
                preSignedUrl = it.preSignedUrl,
                fileUrl = it.fileUrl,
                mediaType = fileType,
                mediaSize = fileSize,
                imageUri = imageUri
            )
        }.onFailure {
            intent {
                reduce {
                    state.copy(isLoading = false)
                }
                postSideEffect(ClubRecruitCreateSideEffect.ClubImageUploadFailure)
            }
        }
    }

    private fun uploadImage(
        preSignedUrl: String,
        fileUrl: String,
        mediaType: String,
        mediaSize: Long,
        imageUri: Uri
    ) = intent {
        uploadFilesUseCase(
            preSignedUrl,
            mediaType,
            mediaSize,
            imageUri.toString()
        ).onSuccess {
            reduce {
                state.copy(
                    recruitImageUrl = fileUrl,
                    isLoading = false
                )
            }
        }.onFailure {
            reduce {
                state.copy(isLoading = false)
            }
            postSideEffect(ClubRecruitCreateSideEffect.ClubImageUploadFailure)
        }
    }
}
