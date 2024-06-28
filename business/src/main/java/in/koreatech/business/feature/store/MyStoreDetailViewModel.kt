package `in`.koreatech.business.feature.store


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.business.GetOwnerShopInfoUseCase
import `in`.koreatech.koin.domain.usecase.business.GetOwnerShopListUseCase
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyStoreDetailViewModel @Inject constructor(
    private val getOwnerShopInfoUseCase: GetOwnerShopInfoUseCase,
    private val getOwnerShopListUseCase: GetOwnerShopListUseCase
) : ContainerHost<MyStoreDetailState, MyStoreDetailSideEffect>, ViewModel() {
    override val container =
        container<MyStoreDetailState, MyStoreDetailSideEffect>(MyStoreDetailState())

    fun initEventItem() = intent {
        reduce {
            state.copy(isEventExpanded = state.isEventExpanded.mapIndexed { _, _ -> false })
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

    fun getOwnerShopList() = intent {
        viewModelScope.launch {
            getOwnerShopListUseCase().let { storeList ->
                reduce {
                    state.copy(
                        storeList = storeList, storeId = storeList.first().uid
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