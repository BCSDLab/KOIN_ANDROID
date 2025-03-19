package `in`.koreatech.business.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.owner.OwnerHasStoreUseCase
import `in`.koreatech.koin.domain.usecase.owner.OwnerTokenIsValidUseCase
import `in`.koreatech.koin.domain.usecase.version.OwnerGetVersionInformationUseCase
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class BusinessMainActivityViewModel @Inject constructor(
    private val ownerTokenIsValidUseCase: OwnerTokenIsValidUseCase,
    private val ownerHasStoreUseCase: OwnerHasStoreUseCase,
    private val ownerGetVersionInformationUseCase: OwnerGetVersionInformationUseCase
) : ViewModel(), ContainerHost<BusinessMainActivityState, BusinessMainSideEffect> {
    override val container = container<BusinessMainActivityState, BusinessMainSideEffect>(BusinessMainActivityState())

    init {
        ownerTokenIsValid()
        checkUpdate()
    }

    private fun checkUpdate() {
        intent {
            viewModelScope.launch {
                ownerGetVersionInformationUseCase()
                    .onSuccess {
                        reduce {
                            state.copy(
                                version = it
                            )
                        }
                    }.onFailure {
                        postSideEffect(BusinessMainSideEffect.NetWorkError)
                    }
            }
        }
    }

    private fun ownerTokenIsValid() {
        intent {
            reduce {
                state.copy(
                    destination =
                    when {
                        !ownerTokenIsValidUseCase() -> SIGNINSCREEN
                        ownerHasStoreUseCase() -> REGISTERSTORESCREEN
                        else -> MYSTORESCREEN
                    }
                )
            }
        }
    }
}

const val SIGNINSCREEN = "sign_in_screen"
const val MYSTORESCREEN = "my_store_screen"
const val REGISTERSTORESCREEN = "register_store_screen"
