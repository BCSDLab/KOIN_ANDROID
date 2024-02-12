package `in`.koreatech.koin.ui.business.registerstore.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.business.mystore.RegisterStore
import `in`.koreatech.koin.domain.usecase.business.mystore.MyStoreRegisterUseCase
import javax.inject.Inject

@HiltViewModel
class RegisterOwnerStoreViewModel @Inject constructor(
    private val myStoreRegisterUseCase: MyStoreRegisterUseCase
): BaseViewModel(){

    fun registerOwnerStore(registerStore: RegisterStore) {
        if(isLoading.value == false) {
            viewModelScope.launchWithLoading {
                myStoreRegisterUseCase(
                    registerStore.name, registerStore.category.toString(), registerStore.address, registerStore.imageUri.toString(),
                    registerStore.phoneNumber.toString(), registerStore.deliveryPrice.toString(), registerStore.description.toString(), registerStore.dayOff,
                    registerStore.openTime.toString(), registerStore.closeTime.toString(),
                    registerStore.isDeliveryOk, registerStore.isCardOk, registerStore.isBankOk
                )
            }
        }
    }
}