package `in`.koreatech.koin.feature.article.ui.lostandfound.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class LostAndFoundViewModel @Inject constructor(
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<LostAndFoundState, Unit> {
    override val container = container<LostAndFoundState, Unit>(LostAndFoundState())

    init {
        getUserType()
    }

    fun getUserType() =
        intent {
            getUserStatusUseCase().collectLatest { user ->
                when (user) {
                    is User.Student -> reduce {
                        state.copy(
                            isAnonymous = false,
                            userType = user.userType
                        )
                    }

                    is User.General -> reduce {
                        state.copy(
                            isAnonymous = false,
                            userType = user.userType
                        )
                    }

                    User.Anonymous -> reduce {
                        state.copy(
                            isAnonymous = true
                        )
                    }
                }
            }
        }

    fun setShowLoginRequestDialog(showDialog: Boolean) =
        intent {
            reduce {
                state.copy(
                    showLoginRequestDialog = showDialog
                )
            }
        }

    fun setFabDialogExpanded(isExpanded: Boolean) =
        intent {
            reduce {
                state.copy(
                    isFabDialogExpanded = isExpanded
                )
            }
        }

    fun setDropdownExpanded(isExpanded: Boolean) =
        intent {
            reduce {
                state.copy(
                    isDropdownExpanded = isExpanded
                )
            }
        }
}

