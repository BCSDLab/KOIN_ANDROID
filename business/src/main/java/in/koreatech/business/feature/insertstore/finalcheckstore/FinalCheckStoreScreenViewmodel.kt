package `in`.koreatech.business.feature.insertstore.finalcheckstore

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.InsertDetailInfoScreenState
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.OperatingTimeState
import `in`.koreatech.koin.domain.model.owner.insertstore.OperatingTime
import `in`.koreatech.koin.domain.model.store.StoreCategories
import `in`.koreatech.koin.domain.usecase.business.store.RegisterStoreUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreCategoriesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class FinalCheckStoreScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val registerStoreUseCase : RegisterStoreUseCase,
    private val getStoreCategoriesUseCase: GetStoreCategoriesUseCase
): ViewModel(), ContainerHost<FinalCheckStoreScreenState, FinalCheckStoreScreenSideEffect> {
    override val container: Container<FinalCheckStoreScreenState, FinalCheckStoreScreenSideEffect> =
        container(FinalCheckStoreScreenState(), savedStateHandle = savedStateHandle) {

            val storeInfoJson: InsertDetailInfoScreenState? =
                savedStateHandle.get<InsertDetailInfoScreenState>("storeInfo")
            if (storeInfoJson != null) getStoreInfo(storeInfoJson)
        }

    private val registerStoreFlow = MutableSharedFlow<FinalCheckStoreScreenState>()

    init {
        viewModelScope.launch {
            registerStoreFlow
                .debounce(500L)
                .collect {
                    performRegisterStore()
                }
        }
    }

    private fun getStoreInfo(storeInfo: InsertDetailInfoScreenState) {
        intent {
            reduce {
                state.copy(
                    storeCategory = storeInfo.storeCategory,
                    storeName = storeInfo.storeName,
                    storeAddress = storeInfo.storeAddress,
                    storeImage = storeInfo.storeImage,
                    storePhoneNumber = storeInfo.storePhoneNumber,
                    storeDeliveryFee = storeInfo.storeDeliveryFee,
                    storeOtherInfo = storeInfo.storeOtherInfo,
                    isDeliveryOk = storeInfo.isDeliveryOk,
                    isBankOk = storeInfo.isBankOk,
                    isCardOk = storeInfo.isCardOk,
                    operatingTimeList = storeInfo.operatingTimeList
                )
            }
            getCategory()
        }
    }

    private suspend fun performRegisterStore(){
        intent {
            registerStoreUseCase(
                name = state.storeName,
                category = state.storeCategory,
                address = state.storeAddress,
                imageUri = state.storeImage,
                phoneNumber = state.storePhoneNumber,
                deliveryPrice = state.storeDeliveryFee,
                description = state.storeOtherInfo,
                operatingTime = state.operatingTimeList.toOperatingTimeList(),
                isDeliveryOk = state.isDeliveryOk,
                isCardOk = state.isCardOk,
                isBankOk = state.isBankOk
            ).onSuccess {
                intent{
                    postSideEffect(FinalCheckStoreScreenSideEffect.GoToFinishScreen)
                }
            }.onFailure {
                intent{
                    postSideEffect(FinalCheckStoreScreenSideEffect.FailRegisterStore)
                }
            }
        }
    }

    private fun getCategory() {
        intent {
            viewModelScope.launch {
                val categories = getStoreCategoriesUseCase()
                reduce {
                    state.copy(
                        storeCategoryString = categories[state.storeCategory - 1].name
                    )
                }
            }
        }
    }

    fun registerStore() {
        intent {
            viewModelScope.launch {
                registerStoreFlow.emit(state)
            }
        }
    }

    private fun List<OperatingTimeState>.toOperatingTimeList(): List<OperatingTime> {
        return this.map { operatingTimeState ->
            OperatingTime(
                closeTime = operatingTimeState.closeTime,
                closed = operatingTimeState.closed,
                dayOfWeek = operatingTimeState.dayOfWeekEnglish,
                openTime = operatingTimeState.openTime
            )
        }
    }
}