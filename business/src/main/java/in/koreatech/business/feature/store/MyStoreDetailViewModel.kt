package `in`.koreatech.business.feature.store


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
            getOwnerShopInfoUseCase(shopId).onSuccess {
                reduce {
                    state.copy(storeInfo = it)
                }
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
                        storeId = if(state.storeList.isNotEmpty()) state.storeList.first().uid else -1
                    )
                }
                getOwnerShopInfo(state.storeId)
                getShopEvents()
                getShopMenus()

            }.onFailure {
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