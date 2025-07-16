package `in`.koreatech.koin.feature.club.ui.clubeventmodify

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.club.GetClubEventUseCase
import `in`.koreatech.koin.domain.usecase.club.ModifyClubEventUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetClubPreSignedUrlUseCase
import `in`.koreatech.koin.feature.club.model.toLocalDateTime
import `in`.koreatech.koin.feature.club.navigation.CLUB_ID
import `in`.koreatech.koin.feature.club.navigation.EVENT_ID
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@HiltViewModel
class ClubEventModifyViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getClubPreSignedUrlUseCase: GetClubPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase,
    private val getClubEventUseCase: GetClubEventUseCase,
    private val modifyClubEventUseCase: ModifyClubEventUseCase
) : ViewModel(), ContainerHost<ClubEventModifyState, ClubEventModifySideEffect> {

    override val container = container<ClubEventModifyState, ClubEventModifySideEffect>(ClubEventModifyState(), savedStateHandle) {
        val clubId = savedStateHandle.get<Int>(CLUB_ID)
        val eventId = savedStateHandle.get<Int>(EVENT_ID)
        checkNotNull(clubId)
        checkNotNull(eventId)
        intent { reduce { state.copy(clubId = clubId, eventId = eventId) } }
        loadClubEvent()
    }

    object BeforeEventState {
        private lateinit var name: String
        private lateinit var introduce: String
        private lateinit var content: String
        private lateinit var imageUrls: List<String>
        private lateinit var startDateTime: LocalDateTime
        private lateinit var endDateTime: LocalDateTime

        operator fun invoke(
            name: String,
            introduce: String,
            content: String,
            imageUrls: List<String>,
            startDateTime: LocalDateTime,
            endDateTime: LocalDateTime
        ) {
            this.name = name
            this.introduce = introduce
            this.content = content
            this.imageUrls = imageUrls
            this.startDateTime = startDateTime
            this.endDateTime = endDateTime
        }
        fun isDifference(
            name: String,
            introduce: String,
            content: String,
            imageUrls: List<String>,
            startDateTime: LocalDateTime,
            endDateTime: LocalDateTime
        ) = (
                this.name != name ||
                this.introduce != introduce ||
                this.content != content ||
                this.imageUrls != imageUrls ||
                this.startDateTime != startDateTime ||
                this.endDateTime != endDateTime
        )
    }

    private val maxImageItems = 7

    private fun loadClubEvent() = intent {
        reduce { state.copy(isLoading = true, isEventLoading = true) }
        getClubEventUseCase(state.clubId, state.eventId).onSuccess {
            reduce {
                state.copy(
                    eventName = it.name,
                    eventIntroduce = it.introduce,
                    eventContent = it.content ?: "",
                    eventImageUrls = it.imageUrls,
                    eventStartDateTime = it.startDate.toLocalDateTime(),
                    eventEndDateTime = it.endDate.toLocalDateTime(),
                    isLoading = false,
                    isEventLoading = false
                )
            }
            BeforeEventState(
                state.eventName,
                state.eventIntroduce,
                state.eventContent,
                state.eventImageUrls,
                state.eventStartDateTime,
                state.eventEndDateTime
            )
        }.onFailure {
            reduce { state.copy(isLoading = false, isEventLoading = true) }
            postSideEffect(ClubEventModifySideEffect.LoadClubEventError)
        }
    }

    fun modifyClubEvent() = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        modifyClubEventUseCase(
            clubId = state.clubId,
            eventId = state.eventId,
            name = state.eventName,
            imageUrls = state.eventImageUrls,
            startDate = state.eventStartDateTime.format(dateFormatter),
            endDate = state.eventEndDateTime.format(dateFormatter),
            introduce = state.eventIntroduce,
            content = state.eventContent
        ).onSuccess {
            reduce { state.copy(isLoading = false) }
            postSideEffect(ClubEventModifySideEffect.EventCreateSuccess)
        }.onFailure { e ->
            reduce { state.copy(isLoading = false) }
            postSideEffect(ClubEventModifySideEffect.EventCreateFailure)
        }
    }

    fun deleteImageUrl(index: Int) = intent {
        reduce { state.copy(eventImageUrls = state.eventImageUrls.toPersistentList().removeAt(index)) }
    }

    fun postNavigateUp() = intent {
        postSideEffect(ClubEventModifySideEffect.NavigateUp)
    }

    fun postMaxImageLimitError() = intent {
        postSideEffect(ClubEventModifySideEffect.MaxImageLimit)
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

    fun updateModifyRequestDialog(bool: Boolean) = blockingIntent {
        if (bool) {
            checkRequiredField()
        } else {
            reduce { state.copy(showModifyRequestDialog = false) }
        }
    }

    private fun checkRequiredField() = intent {
        if (state.eventName.isBlank()) {
            reduce { state.copy(eventNameRequired = true) }
        } else {
            reduce { state.copy(eventNameRequired = false) }
        }
        if (state.eventIntroduce.isBlank()) {
            reduce { state.copy(eventIntroduceRequired = true) }
        } else {
            reduce { state.copy(eventIntroduceRequired = false) }
        }
        if(!state.eventNameRequired && !state.eventIntroduceRequired) {
            reduce { state.copy(showModifyRequestDialog = true) }
        }
    }

    fun updateModifyCancelDialog(bool: Boolean) = blockingIntent {
        if(bool) {
            if (BeforeEventState.isDifference(
                    state.eventName,
                    state.eventIntroduce,
                    state.eventContent,
                    state.eventImageUrls,
                    state.eventStartDateTime,
                    state.eventEndDateTime
            )) {
                reduce { state.copy(showModifyCancelDialog = true) }
            } else {
                postNavigateUp()
            }
        } else {
            reduce { state.copy(showModifyCancelDialog = false) }
        }
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
            postSideEffect(ClubEventModifySideEffect.ClubImageUploadFailure)
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
                    eventImageUrls = if ( state.eventImageUrls.size < maxImageItems) {
                        state.eventImageUrls.toPersistentList().add(fileUrl)
                    } else {
                        state.eventImageUrls
                    },
                    isLoading = false
                )
            }
        }.onFailure {
            reduce { state.copy(isLoading = false) }
            postSideEffect(ClubEventModifySideEffect.ClubImageUploadFailure)
        }
    }
}
