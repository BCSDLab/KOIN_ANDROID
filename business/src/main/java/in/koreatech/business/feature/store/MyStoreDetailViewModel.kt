package `in`.koreatech.business.feature.store


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.business.GetOwnerShopEventsUseCase
import `in`.koreatech.koin.domain.usecase.business.GetOwnerShopInfoUseCase
import `in`.koreatech.koin.domain.usecase.business.GetOwnerShopListUseCase
import `in`.koreatech.koin.domain.usecase.business.GetOwnerShopMenusUseCase
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyStoreDetailViewModel @Inject constructor(
    private val getOwnerShopInfoUseCase: GetOwnerShopInfoUseCase,
    private val getOwnerShopListUseCase: GetOwnerShopListUseCase,
    private val getOwnerShopEventsUseCase: GetOwnerShopEventsUseCase,
    private val getOwnerShopMenusUseCase: GetOwnerShopMenusUseCase,
) : ContainerHost<MyStoreDetailState, MyStoreDetailSideEffect>, ViewModel() {
    override val container =
        container<MyStoreDetailState, MyStoreDetailSideEffect>(MyStoreDetailState())

    init {
        initOwnerShopList()
    }

    fun initEventItem() = intent {
        reduce {
            state.copy(isEventExpanded = List(state.isEventExpanded.size) { _ -> false })
        }
    }

    fun getOwnerShopInfo(shopId: Int) = intent {
        viewModelScope.launch {
            getOwnerShopInfoUseCase(shopId).let { storeInfo ->
                reduce {
                    state.copy(
                        storeInfo = storeInfo
                    )
                }
            }
        }
    }

    fun initOwnerShopList() = intent {
        viewModelScope.launch {
            getOwnerShopListUseCase().let { storeList ->
                if (storeList.isNotEmpty()) {
                    reduce {
                        state.copy(
                            storeList = storeList,
                        )
                    }
                } else {
                    reduce {
                        state.copy(
                            storeList = emptyList(),
                        )
                    }
                }
            }
            intent {
                reduce { state.copy(storeId = state.storeList.first().uid) }
                getOwnerShopInfo(state.storeId)
                getShopEvents()
                getShopMenus()
            }
        }
    }

    fun getShopMenus() = intent {
        viewModelScope.launch {
            getOwnerShopMenusUseCase(state.storeId).also {
                reduce {
                    state.copy(storeMenu = it.menuCategories)
                }
            }
        }
    }

    fun getShopEvents() = intent{
        viewModelScope.launch {
            getOwnerShopEventsUseCase(state.storeId).also {
                reduce {
                    state.copy(storeEvent = it, isEventExpanded = List(it.events.size) { _ -> false })
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