package `in`.koreatech.koin.ui.dining.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.coopshop.CoopShop
import `in`.koreatech.koin.domain.model.coopshop.CoopShopType
import `in`.koreatech.koin.domain.usecase.coopshop.GetCoopShopAllUseCase
import `in`.koreatech.koin.domain.usecase.coopshop.GetCoopShopUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import javax.inject.Inject

@HiltViewModel
class DiningNoticeViewModel @Inject constructor(
    private val getCoopShopUseCase: GetCoopShopUseCase
): BaseViewModel() {

    private val _diningNotice = MutableLiveData<CoopShop>()
    val diningNotice: LiveData<CoopShop> get() = _diningNotice
    private val _toastErrorMessage = SingleLiveEvent<String>()
    val toastErrorMessage: LiveData<String> get() = _toastErrorMessage

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