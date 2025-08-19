package `in`.koreatech.koin.feature.dining.ui.diningnotice

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.coopshop.CoopShop
import `in`.koreatech.koin.domain.model.coopshop.CoopShopType
import `in`.koreatech.koin.domain.usecase.coopshop.GetCoopShopUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class DiningNoticeViewModel @Inject constructor(
    private val getCoopShopUseCase: GetCoopShopUseCase
) : BaseViewModel() {
    private val _diningNotice = MutableStateFlow(
        CoopShop(
            id = -1,
            name = "",
            semester = "",
            opens = listOf(),
            phone = "",
            location = "",
            remarks = "",
            updatedAt = ""
        )
    )
    val diningNotice: StateFlow<CoopShop> get() = _diningNotice
    private val _toastErrorMessage = MutableStateFlow("")
    val toastErrorMessage: StateFlow<String> get() = _toastErrorMessage

    init {
        getDiningNotice(CoopShopType.Dining)
    }

    fun getDiningNotice(type: CoopShopType) {
        viewModelScope.launchWithLoading {
            getCoopShopUseCase(type)
                .onSuccess {
                    _diningNotice.value = it
                }
                .onFailure {
                    _toastErrorMessage.value = it.message
                }
        }
    }
}
