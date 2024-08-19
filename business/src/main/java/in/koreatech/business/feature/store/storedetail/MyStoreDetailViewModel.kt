package `in`.koreatech.business.feature.store.storedetail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.business.DeleteOwnerEventsUseCase
import `in`.koreatech.koin.domain.usecase.business.GetOwnerShopEventsUseCase
import `in`.koreatech.koin.domain.usecase.business.GetOwnerShopInfoUseCase
import `in`.koreatech.koin.domain.usecase.business.GetOwnerShopListUseCase
import `in`.koreatech.koin.domain.usecase.business.GetOwnerShopMenusUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyStoreDetailViewModel @Inject constructor(
    private val getOwnerShopInfoUseCase: GetOwnerShopInfoUseCase,
    private val getOwnerShopListUseCase: GetOwnerShopListUseCase,
    private val getOwnerShopEventsUseCase: GetOwnerShopEventsUseCase,
    private val getOwnerShopMenusUseCase: GetOwnerShopMenusUseCase,
    private val deleteOwnerShopEventsUseCase: DeleteOwnerEventsUseCase,
) : ContainerHost<MyStoreDetailState, MyStoreDetailSideEffect>, ViewModel() {
    override val container =
        container<MyStoreDetailState, MyStoreDetailSideEffect>(MyStoreDetailState())

    init {
        initOwnerShopList()
    }

    fun onChangeAllEventSelected() = intent {
        reduce {
            state.copy(
                isAllEventSelected = if (state.storeEvent?.size == 0) false
                else !state.isAllEventSelected,
            )
        }
        reduce {
            state.copy(
                isSelectedEvent = if (state.isAllEventSelected) {
                    mutableListOf<Int>().apply { state.storeEvent?.forEach { add(it.eventId) } }
                } else {
                    mutableListOf<Int>().apply { state.storeEvent?.forEach { remove(it.eventId) } }
                }
            )
        }
    }

    fun onChangeEventSelected(eventId: Int) = intent {
        reduce {
            if (!state.isSelectedEvent.contains(eventId)) {
                state.copy(
                    isSelectedEvent = state.isSelectedEvent.toMutableList().apply { add(eventId) },
                )
            } else {
                state.copy(
                    isSelectedEvent = state.isSelectedEvent.toMutableList()
                        .apply { remove(eventId) })
            }
        }
        reduce {
            state.copy(
                isAllEventSelected = state.isSelectedEvent.size == state.storeEvent?.size
            )
        }
    }

    fun onChangeEditMode() = intent {
        reduce {
            state.copy(isEditMode = !state.isEditMode, isSelectedEvent = mutableListOf(), isAllEventSelected = false)
        }
    }

    fun initEventItem() = intent {
        reduce {
            state.copy(isEventExpanded = List(state.isEventExpanded.size) { _ -> false })
        }
    }

    fun onCardAvailableChanged() = intent {
        reduce {
            state.copy(
                storeInfo = state.storeInfo?.copy(
                    isCardOk = !(state.storeInfo?.isCardOk ?: false)
                )
            )

        }
    }

    fun onDeliveryAvailableChanged() = intent {
        reduce {
            state.copy(
                storeInfo = state.storeInfo?.copy(
                    isDeliveryOk = !(state.storeInfo?.isDeliveryOk ?: false)
                )
            )
        }
    }

    fun onTransferAvailableChanged() = intent {
        reduce {
            state.copy(
                storeInfo = state.storeInfo?.copy(
                    isBankOk = !(state.storeInfo?.isBankOk ?: false)
                )
            )
        }
    }

    fun onStoreNameChanged(storeName: String) = intent {
        reduce {
            state.copy(storeInfo = state.storeInfo?.copy(name = storeName))
        }
    }

    fun onPhoneNumberChanged(phone: String) = intent {
        reduce {
            state.copy(storeInfo = state.storeInfo?.copy(phone = phone))
        }
    }

    fun onAddressChanged(address: String) = intent {
        reduce {
            state.copy(storeInfo = state.storeInfo?.copy(address = address))
        }
    }

    fun onDeliveryPriceChanged(price: Int) = intent {
        reduce {
            state.copy(storeInfo = state.storeInfo?.copy(deliveryPrice = price))
        }
    }

    fun onDescriptionChanged(description: String) = intent {
        reduce {
            state.copy(storeInfo = state.storeInfo?.copy(description = description))
        }
    }


    fun getOwnerShopInfo(shopId: Int) = intent {
        viewModelScope.launch {
            getOwnerShopInfoUseCase(shopId).onSuccess {
                reduce {
                    state.copy(storeInfo = it)
                }
                getShopEvents()
                getShopMenus()

            }.onFailure {
                postSideEffect(MyStoreDetailSideEffect.ShowErrorMessage(it.message))
            }
        }
    }

    fun initOwnerShopList() = intent {
        viewModelScope.launch {
            getOwnerShopListUseCase().onSuccess {
                reduce {
                    state.copy(
                        storeList = it,
                    )
                }
                reduce {
                    state.copy(
                        storeId = if(state.storeList.isNotEmpty()) state.storeList.first().uid else -1
                    )
                }
                getOwnerShopInfo(state.storeId)

            }.onFailure {
                reduce {
                    state.copy(
                        storeList = emptyList(),
                        storeId = if (state.storeList.isNotEmpty()) state.storeList.first().uid else -1
                    )
                }
                postSideEffect(MyStoreDetailSideEffect.ShowErrorMessage(it.message))
            }
        }
    }

    fun getShopMenus() = intent {
        viewModelScope.launch {
            getOwnerShopMenusUseCase(state.storeId).also {
                reduce {
                    state.copy(storeMenu = it.menuCategories?.toImmutableList())
                }
            }
        }
    }

    fun getShopEvents() = intent {
        viewModelScope.launch {
            getOwnerShopEventsUseCase(state.storeId).also {
                reduce {
                    state.copy(
                        storeEvent = it.events.toImmutableList(),
                        isEventExpanded = List(it.events.size) { _ -> false })
                }
            }
        }
    }

    fun changeDialogVisibility() = intent {
        reduce {
            state.copy(
                dialogVisibility = if (state.isSelectedEvent.size > 0) {
                    !state.dialogVisibility
                } else false
            )
        }
    }
    fun navigateToModifyScreen() = intent {
        postSideEffect(MyStoreDetailSideEffect.NavigateToModifyScreen)
    }

    fun modifyEventError() = intent {
        postSideEffect(MyStoreDetailSideEffect.ShowErrorModifyEventToast)
    }

    fun deleteEventAll() = intent{
        state.isSelectedEvent.forEach {
            deleteEventItem(state.storeId, it)
        }
    }

    fun deleteEventItem(shopId: Int, eventId: Int) = intent {
        viewModelScope.launch {
            deleteOwnerShopEventsUseCase(shopId, eventId).also {
                reduce {
                    state.copy(
                        storeEvent = state.storeEvent?.filter { it.eventId != eventId }
                            ?.toImmutableList(),
                        isSelectedEvent = state.isSelectedEvent.toMutableList()
                            .apply { remove(eventId) },
                        dialogVisibility = false
                    )
                }
                reduce {
                    state.copy(
                        isAllEventSelected = if (state.storeEvent?.size == 0) false else state.isAllEventSelected
                    )
                }
            }
        }
    }

    fun toggleEventItem(index: Int) = intent {
        reduce {
            state.copy(
                isEventExpanded = state.isEventExpanded.mapIndexed { i, isExpanded ->
                    if (i == index) !isExpanded else isExpanded
                }
            )
        }
    }
}