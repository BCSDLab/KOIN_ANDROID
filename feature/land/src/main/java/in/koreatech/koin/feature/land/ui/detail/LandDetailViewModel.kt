package `in`.koreatech.koin.feature.land.ui.detail

import android.telephony.PhoneNumberUtils
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.land.GetLandDetailUseCase
import `in`.koreatech.koin.feature.land.model.toUiModel
import `in`.koreatech.koin.feature.land.navigation.LandNavType
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
internal class LandDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getLandDetailUseCase: GetLandDetailUseCase
) : ViewModel(), ContainerHost<LandDetailState, LandDetailSideEffect> {
    private val route: LandNavType.LandDetail = savedStateHandle.toRoute()

    override val container = container<LandDetailState, LandDetailSideEffect>(
        LandDetailState(
            isMonthlyAvailable = route.isMonthlyAvailable,
            isCharterAvailable = route.isCharterAvailable
        )
    ) {
        getLandDetailUseCase(route.landId)
            .onSuccess { detail ->
                reduce {
                    state.copy(
                        detail = detail.toUiModel(),
                        isLoading = false,
                        hasLoadError = false,
                        canDialPhone = canDialLandPhone(detail.phone)
                    )
                }
            }
            .onFailure {
                reduce { state.copy(isLoading = false, hasLoadError = true) }
            }
    }

    fun onBackClick() = intent { postSideEffect(LandDetailSideEffect.NavigateBack) }

    fun onPhoneClick() = intent {
        if (!state.canDialPhone) return@intent
        val phone = state.detail?.phone ?: return@intent
        postSideEffect(LandDetailSideEffect.CallPhone(phone.filterNot(Char::isWhitespace)))
    }

    private fun canDialLandPhone(phone: String): Boolean {
        val number = phone.filterNot(Char::isWhitespace)
        return number.any(Char::isDigit) && PhoneNumberUtils.isGlobalPhoneNumber(number)
    }
}
