package `in`.koreatech.koin.feature.club.ui.clubrecruitmodify

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.club.KoinClubException
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.club.GetClubRecruitmentUseCase
import `in`.koreatech.koin.domain.usecase.club.ModifyClubRecruitmentUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetClubPreSignedUrlUseCase
import `in`.koreatech.koin.feature.club.model.RecruitmentStatus
import `in`.koreatech.koin.feature.club.model.toParcelizeClubRecruitment
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
class ClubRecruitModifyViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getClubPreSignedUrlUseCase: GetClubPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase,
    private val getClubRecruitmentUseCase: GetClubRecruitmentUseCase,
    private val modifyClubRecruitmentUseCase: ModifyClubRecruitmentUseCase
) : ViewModel(), ContainerHost<ClubRecruitModifyState, ClubRecruitModifySideEffect> {

    override val container = container<ClubRecruitModifyState, ClubRecruitModifySideEffect>(ClubRecruitModifyState(), savedStateHandle) {
        val clubId = savedStateHandle.get<Int>(CLUB_ID)
        checkNotNull(clubId)
        intent { reduce { state.copy(clubId = clubId) } }
        loadClubRecruitment()
    }

    object BeforeRecruitState {
        private lateinit var content: String
        private lateinit var imageUrl: String
        private lateinit var startDate: LocalDate
        private lateinit var endDate: LocalDate
        private var always: Boolean = false

        operator fun invoke(
            content: String,
            imageUrl: String,
            startDate: LocalDate,
            endDate: LocalDate,
            always: Boolean
        ) {
            this.content = content
            this.imageUrl = imageUrl
            this.startDate = startDate
            this.endDate = endDate
            this.always = always
        }
        fun isDifference(
            content: String,
            imageUrl: String,
            startDate: LocalDate,
            endDate: LocalDate,
            always: Boolean
        ) = (
            this.content != content ||
                this.imageUrl != imageUrl ||
                this.startDate != startDate ||
                this.endDate != endDate ||
                this.always != always
            )
    }

    private fun loadClubRecruitment() = intent {
        reduce { state.copy(isLoading = true, isRecruitLoading = true) }
        getClubRecruitmentUseCase(state.clubId).onSuccess {
            val clubRecruitment = it.toParcelizeClubRecruitment()
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            clubRecruitment?.let {
                reduce {
                    state.copy(
                        isLoading = false,
                        isRecruitLoading = false,
                        recruitImageUrl = it.imageUrl ?: "",
                        recruitStartDate = if (it.startDate.isNotEmpty()) LocalDate.parse(it.startDate, dateFormatter) else state.recruitStartDate,
                        recruitEndDate = if (it.startDate.isNotEmpty()) LocalDate.parse(it.endDate, dateFormatter) else state.recruitEndDate,
                        recruitAlways = it.status == RecruitmentStatus.ALWAYS,
                        recruitContent = it.content
                    )
                }
                BeforeRecruitState(
                    state.recruitContent,
                    state.recruitImageUrl,
                    state.recruitStartDate,
                    state.recruitEndDate,
                    state.recruitAlways
                )
            }
        }.onFailure { e ->
            reduce { state.copy(isLoading = false, isRecruitLoading = false) }
            when (e) {
                is KoinClubException.WrongInputDataException,
                is KoinClubException.ClubRecruitNotFoundException -> {
                    postSideEffect(ClubRecruitModifySideEffect.LoadClubRecruitmentError)
                }
                else -> throw e
            }
        }
    }

    fun deleteImageUrl() = intent {
        reduce { state.copy(recruitImageUrl = "") }
    }

    fun updateModifyRequestDialog(bool: Boolean) = intent {
        reduce { state.copy(showModifyRequestDialog = bool) }
    }

    fun postNavigateUp() = intent {
        postSideEffect(ClubRecruitModifySideEffect.NavigateUp)
    }

    fun updateModifyCancelDialog(bool: Boolean) = intent {
        if (bool) {
            if (
                BeforeRecruitState.isDifference(
                    state.recruitContent,
                    state.recruitImageUrl,
                    state.recruitStartDate,
                    state.recruitEndDate,
                    state.recruitAlways
                )
            ) {
                reduce { state.copy(showModifyCancelDialog = true) }
            } else {
                postNavigateUp()
            }
        } else {
            reduce { state.copy(showModifyCancelDialog = false) }
        }
    }

    fun updateDatePickerDialog(bool: Boolean) = intent {
        reduce { state.copy(showDatePickerDialog = bool) }
    }

    fun updateRecruitContent(value: String) = intent {
        reduce { state.copy(recruitContent = value) }
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

    fun modifyClubRecruitment() = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        modifyClubRecruitmentUseCase(
            clubId = state.clubId,
            startDate = if (!state.recruitAlways) state.recruitStartDate.format(dateFormatter) else null,
            endDate = if (!state.recruitAlways) state.recruitEndDate.format(dateFormatter) else null,
            isAlwaysRecruiting = state.recruitAlways,
            imageUrl = state.recruitImageUrl,
            content = state.recruitContent
        ).onSuccess {
            reduce { state.copy(isLoading = false) }
            postSideEffect(ClubRecruitModifySideEffect.RecruitModifySuccess)
        }.onFailure {
            reduce { state.copy(isLoading = false) }
            postSideEffect(ClubRecruitModifySideEffect.RecruitModifyFailure)
        }
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
                postSideEffect(ClubRecruitModifySideEffect.ClubImageUploadFailure)
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
            postSideEffect(ClubRecruitModifySideEffect.ClubImageUploadFailure)
        }
    }
}
