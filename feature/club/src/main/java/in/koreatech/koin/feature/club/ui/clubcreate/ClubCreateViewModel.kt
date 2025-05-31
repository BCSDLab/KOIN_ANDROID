package `in`.koreatech.koin.feature.club.ui.clubcreate

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetClubPreSignedUrlUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.club.model.ClubCategories
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ClubCreateViewModel @Inject constructor(
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val getClubPreSignedUrlUseCase: GetClubPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase
) : ViewModel(), ContainerHost<ClubCreateState, ClubCreateSideEffect> {
    override val container = container<ClubCreateState, ClubCreateSideEffect>(ClubCreateState())

    init {
        getUserInfo()
    }

    private fun getUserInfo() = viewModelScope.launch {
        getUserStatusUseCase().collect {
            when (it) {
                is User.Anonymous -> {
                    throw IllegalStateException()
                }

                is User.Student -> {
                    intent {
                        reduce {
                            // TODO: User ID will be changed after the user team's sprint.
                            state.copy(
                                userId = it.email?.replace("@koreatech.ac.kr", "") ?: "",
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateClubName(name: String) = blockingIntent {
        reduce {
            state.copy(
                clubName = name
            )
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

    fun updateShowCreateDialog(shouldShow: Boolean) = blockingIntent {
        reduce {
            state.copy(
                shouldCheckRequiredField = true
            )
        }

        if (state.clubNameRequired || state.clubCategoryRequired || state.locationRequired) return@blockingIntent

        reduce {
            state.copy(
                shouldShowCreateDialog = shouldShow
            )
        }
    }

    fun updateShowPermissionDialog(shouldShow: Boolean) = blockingIntent {
        reduce {
            state.copy(
                shouldShowPermissionDialog = shouldShow
            )
        }
    }

    fun updateUserRole(role: String) = blockingIntent {
        reduce {
            state.copy(
                userRole = role
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
                        isLoading = false
                    )
                }
            }
        }.onFailure {
            intent {
                reduce {
                    state.copy(isLoading = true)
                }
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

            }
        }
    }

    fun requestCreateClub() = intent {
        reduce {
            state.copy(
                shouldCheckRequiredField = true
            )
        }
        postSideEffect(ClubCreateSideEffect.ClubCreateSuccess)
    }
}
