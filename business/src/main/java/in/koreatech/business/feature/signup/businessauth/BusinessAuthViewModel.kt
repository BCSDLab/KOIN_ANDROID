package `in`.koreatech.business.feature.signup.businessauth

import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class BusinessAuthViewModel @Inject constructor(

) : ContainerHost<BusinessAuthState, BusinessAuthSideEffect>, BaseViewModel() {
    override val container = container<BusinessAuthState, BusinessAuthSideEffect>(BusinessAuthState())
    fun onShowDialog() = intent {
        postSideEffect(BusinessAuthSideEffect.ShowDialog)
    }
}