package `in`.koreatech.koin.feature.club.ui.clubcreate

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.club.model.ClubCategories
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ClubCreateViewModel @Inject constructor(
) : ViewModel(), ContainerHost<ClubCreateState, ClubCreateSideEffect> {
    override val container = container<ClubCreateState, ClubCreateSideEffect>(ClubCreateState())

    fun updateClubName(name: String) = blockingIntent {
        reduce {
            state.copy(
                clubName = name,
            )
        }
    }

    fun updateClubDescription(description: String) = blockingIntent {
        reduce {
            state.copy(
                clubDescription = description,
            )
        }
    }

    fun updateClubCategory(category: ClubCategories) = blockingIntent {
        reduce {
            state.copy(
                clubCategory = category,
            )
        }
    }

    fun updateIsClubCategoryDropdownExpanded(isExpanded: Boolean) = blockingIntent {
        reduce {
            state.copy(
                isClubCategoryDropdownExpanded = isExpanded,
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

    fun requestCreateClub() = intent {
        reduce {
            state.copy(
                shouldCheckRequiredField = true
            )
        }
        postSideEffect(ClubCreateSideEffect.ClubCreateSuccess)
    }
}