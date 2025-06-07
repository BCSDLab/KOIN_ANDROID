package `in`.koreatech.koin.feature.club.ui.clubmodify

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.club.GetClubDetailsUseCase
import `in`.koreatech.koin.domain.usecase.club.ModifyClubUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetClubPreSignedUrlUseCase
import `in`.koreatech.koin.feature.club.model.ClubCategories
import `in`.koreatech.koin.feature.club.model.toClubCategory
import `in`.koreatech.koin.feature.club.navigation.CLUB_ID
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class ClubModifyViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getClubDetailsUseCase: GetClubDetailsUseCase,
    private val modifyClubUseCase: ModifyClubUseCase,
    private val getClubPreSignedUrlUseCase: GetClubPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase
) : ViewModel(), ContainerHost<ClubModifyState, ClubModifySideEffect> {
    override val container = container<ClubModifyState, ClubModifySideEffect>(ClubModifyState(), savedStateHandle) {
        val clubId = savedStateHandle.get<Int>(CLUB_ID)
        checkNotNull(clubId)
        intent {
            reduce {
                state.copy(
                    clubId = clubId
                )
            }
        }
        loadClubDetails(clubId)
    }

    private fun loadClubDetails(clubId: Int) = viewModelScope.launch {
        intent {
            reduce {
                state.copy(isLoading = true)
            }
            getClubDetailsUseCase(clubId).onSuccess {
                Timber.d("Club details loaded: $it")
                reduce {
                    state.copy(
                        isLoading = false,
                        clubName = it.name,
                        clubDescription = it.description,
                        clubCategory = it.category.toClubCategory(),
                        location = it.location,
                        instagramUrl = it.instagram ?: "",
                        googleFormUrl = it.googleForm ?: "",
                        openChatUrl = it.openChat ?: "",
                        phoneNumber = it.phoneNumber ?: "",
                        isLikeHidden = it.isLikeHidden,
                        likes = it.likes,
                        clubImageUrl = it.imageUrl
                    )
                }
            }
        }
    }

    fun updateClubDescription(description: String) = blockingIntent {
        reduce {
            state.copy(
                clubDescription = description
            )
        }
    }

    fun updateClubCategory(category: ClubCategories) = blockingIntent {
        reduce {
            state.copy(
                clubCategory = category
            )
        }
    }

    fun updateIsClubCategoryDropdownExpanded(isExpanded: Boolean) = blockingIntent {
        reduce {
            state.copy(
                isClubCategoryDropdownExpanded = isExpanded
            )
        }
    }

    fun updateLocation(location: String) = blockingIntent {
        reduce {
            state.copy(
                location = location
            )
        }
    }

    fun updateInstagramUrl(url: String) = blockingIntent {
        reduce {
            state.copy(
                instagramUrl = url
            )
        }
    }

    fun updateGoogleFormUrl(url: String) = blockingIntent {
        reduce {
            state.copy(
                googleFormUrl = url
            )
        }
    }

    fun updateOpenChatUrl(url: String) = blockingIntent {
        reduce {
            state.copy(
                openChatUrl = url
            )
        }
    }

    fun updatePhoneNumber(phoneNumber: String) = blockingIntent {
        reduce {
            state.copy(
                phoneNumber = phoneNumber
            )
        }
    }

    fun updateShowModifyDialog(shouldShow: Boolean) = blockingIntent {
        reduce {
            state.copy(
                shouldCheckRequiredField = true
            )
        }

        if (state.clubNameRequired || state.clubCategoryRequired || state.locationRequired) return@blockingIntent

        reduce {
            state.copy(
                shouldShowModifyDialog = shouldShow
            )
        }
    }

    fun updateIsLikeHidden(isLikeHidden: Boolean) = blockingIntent {
        reduce {
            state.copy(
                isLikeHidden = isLikeHidden
            )
        }
    }

    private fun uploadImage(
        preSignedUrl: String,
        fileUrl: String,
        mediaType: String,
        mediaSize: Long,
        imageUri: Uri
    ) = viewModelScope.launch {
        uploadFilesUseCase(
            preSignedUrl,
            mediaType,
            mediaSize,
            imageUri.toString()
        ).onSuccess {
            intent {
                reduce {
                    state.copy(
                        clubImageUrl = fileUrl,
                        isClubImageChanged = true,
                        isLoading = false
                    )
                }
            }
        }.onFailure {
            intent {
                reduce {
                    state.copy(isLoading = false)
                }
                postSideEffect(ClubModifySideEffect.ClubImageUploadFailure)
            }
        }
    }

    fun getPreSignedUrl(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: Uri
    ) {
        intent {
            reduce {
                state.copy(isLoading = true)
            }
        }
        viewModelScope.launch {
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
                    postSideEffect(ClubModifySideEffect.ClubImageUploadFailure)
                }
            }
        }
    }

    fun requestModifyClub() = viewModelScope.launch {
        intent {
            reduce {
                state.copy(
                    shouldCheckRequiredField = true
                )
            }

            if (state.clubNameRequired || state.clubCategoryRequired || state.locationRequired) return@intent

            reduce {
                state.copy(
                    isLoading = true
                )
            }

            modifyClubUseCase(
                clubId = state.clubId,
                name = state.clubName,
                imageUrl = state.clubImageUrl,
                clubCategoryId = state.clubCategory!!.id,
                location = state.location,
                description = state.clubDescription,
                instagram = state.instagramUrl,
                googleForm = state.googleFormUrl,
                openChat = state.openChatUrl,
                phoneNumber = state.phoneNumber,
                isLikeHidden = state.isLikeHidden
            ).onSuccess {
                reduce {
                    state.copy(
                        isLoading = false
                    )
                }
                postSideEffect(ClubModifySideEffect.ClubModifySuccess)
            }.onFailure {
                reduce {
                    state.copy(
                        isLoading = false
                    )
                }
            }
        }
    }
}
