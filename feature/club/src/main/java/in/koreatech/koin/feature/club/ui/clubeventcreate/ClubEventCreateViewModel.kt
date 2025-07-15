package `in`.koreatech.koin.feature.club.ui.clubeventcreate

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.club.CreateClubEventUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetClubPreSignedUrlUseCase
import `in`.koreatech.koin.feature.club.navigation.CLUB_ID
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ClubEventCreateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getClubPreSignedUrlUseCase: GetClubPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase,
    private val createClubEventUseCase: CreateClubEventUseCase
) : ViewModel(), ContainerHost<ClubEventCreateState, ClubEventCreateSideEffect> {

    override val container = container<ClubEventCreateState, ClubEventCreateSideEffect>(ClubEventCreateState(), savedStateHandle) {
        val clubId = savedStateHandle.get<Int>(CLUB_ID)
        checkNotNull(clubId)
        intent { reduce { state.copy(clubId = clubId) } }
    }

    fun deleteImageUrl(index: Int) = intent {
        reduce { state.copy(eventImageUrls = state.eventImageUrls.toPersistentList().removeAt(index)) }
    }

    fun postNavigateUp() = intent {
        postSideEffect(ClubEventCreateSideEffect.NavigateUp)
    }

    fun createClubEvent() = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        createClubEventUseCase(
            clubId = state.clubId,
            name = state.eventName,
            imageUrls = state.eventImageUrls,
            startDate = state.eventStartDateTime.format(dateFormatter),
            endDate = state.eventEndDateTime.format(dateFormatter),
            introduce = state.eventIntroduce,
            content = state.eventContent
        ).onSuccess {
            reduce { state.copy(isLoading = false) }
            postSideEffect(ClubEventCreateSideEffect.EventCreateSuccess)
        }.onFailure { e ->
            reduce { state.copy(isLoading = false) }
            postSideEffect(ClubEventCreateSideEffect.EventCreateFailure)
        }
    }

    fun updateEventName(value: String) = blockingIntent {
        reduce { state.copy(eventName = value) }
    }

    fun updateEventIntroduce(value: String) = blockingIntent {
        reduce { state.copy(eventIntroduce = value) }
    }

    fun updateEventContent(value: String) = blockingIntent {
        reduce { state.copy(eventContent = value) }
    }

    fun updateCreateRequestDialog(bool: Boolean) = blockingIntent {
        if (bool) {
            postRequiredError()
        } else {
            reduce { state.copy(showCreateRequestDialog = false) }
        }
    }

    private fun postRequiredError() = intent {
        when {
            state.eventName.isBlank() -> postSideEffect(ClubEventCreateSideEffect.EventNameError)
            state.eventIntroduce.isBlank() -> postSideEffect(ClubEventCreateSideEffect.EventIntroError)
            else -> reduce { state.copy(showCreateRequestDialog = true) }
        }
    }

    fun updateCreateCancelDialog(bool: Boolean) = blockingIntent {
        reduce { state.copy(showCreateCancelDialog = bool) }
    }

    fun updateDatePickerDialog(bool: Boolean) = blockingIntent {
        reduce { state.copy(showDatePickerDialog = bool) }
    }

    fun updateTimePickerDialog(bool: Boolean) = blockingIntent {
        reduce { state.copy(showTimePickerDialog = bool) }
    }

    fun setEventStartDate(date: LocalDate) = intent {
        val newDate = state.eventStartDateTime.withYear(date.year).withMonth(date.monthValue).withDayOfMonth(date.dayOfMonth)
        if (state.eventEndDateTime <= newDate) {
            reduce { state.copy(eventEndDateTime = newDate.plusDays(1)) }
        }
        reduce { state.copy(eventStartDateTime = newDate) }
    }

    fun setEventEndDate(date: LocalDate) = intent {
        val newDate = state.eventEndDateTime.withYear(date.year).withMonth(date.monthValue).withDayOfMonth(date.dayOfMonth)
        if (state.eventStartDateTime >= newDate) {
            reduce { state.copy(eventStartDateTime = newDate.minusDays(1)) }
        }
        reduce { state.copy(eventEndDateTime = newDate) }
    }

    fun setEventStartTime(time: LocalTime) = intent {
        val newDate = state.eventStartDateTime.withHour(time.hour).withMinute(time.minute)
        if (state.eventEndDateTime <= newDate) {
            reduce { state.copy(eventEndDateTime = newDate.plusHours(1)) }
        }
        reduce { state.copy(eventStartDateTime = newDate) }
    }

    fun setEventEndTime(time: LocalTime) = intent {
        val newDate = state.eventEndDateTime.withHour(time.hour).withMinute(time.minute)
        if (state.eventStartDateTime >= newDate) {
            reduce { state.copy(eventStartDateTime = newDate.minusHours(1)) }
        }
        reduce { state.copy(eventEndDateTime = newDate) }
    }

    fun getPreSignedUrl(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: Uri
    ) = blockingIntent {
        reduce { state.copy(isLoading = true) }
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
            reduce { state.copy(isLoading = false) }
            postSideEffect(ClubEventCreateSideEffect.ClubImageUploadFailure)
        }
    }

    private fun uploadImage(
        preSignedUrl: String,
        fileUrl: String,
        mediaType: String,
        mediaSize: Long,
        imageUri: Uri
    ) = blockingIntent {
        uploadFilesUseCase(
            preSignedUrl,
            mediaType,
            mediaSize,
            imageUri.toString()
        ).onSuccess {
            reduce {
                state.copy(
                    eventImageUrls = state.eventImageUrls.toPersistentList().add(fileUrl),
                    isLoading = false
                )
            }
        }.onFailure {
            reduce { state.copy(isLoading = false) }
            postSideEffect(ClubEventCreateSideEffect.ClubImageUploadFailure)
        }
    }
}
