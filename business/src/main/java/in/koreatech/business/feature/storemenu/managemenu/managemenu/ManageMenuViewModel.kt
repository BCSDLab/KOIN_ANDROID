package `in`.koreatech.business.feature.storemenu.managemenu.managemenu

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.business.GetOwnerShopMenusUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ManageMenuViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val  getOwnerShopMenusUseCase: GetOwnerShopMenusUseCase
): ViewModel(), ContainerHost<ManageMenuState, ManageMenuSideEffect> {
    override val container = container<ManageMenuState, ManageMenuSideEffect>(ManageMenuState())

    private val menuId: Int = checkNotNull(savedStateHandle["menuId"])

    init{
        getSettingMenuState(menuId)
    }

    private fun getSettingMenuState(storeId: Int){
        intent{
                reduce {
                    state.copy(
                        storeId = storeId
                    )
                }
        }
        getShopMenus()
    }

    private fun getShopMenus() = intent {
        viewModelScope.launch {
            getOwnerShopMenusUseCase(state.storeId).also {
                reduce {
                    state.copy(storeMenuList = it.menuCategories?.toImmutableList())
                }
            }
        }
    }

    fun onRegisterMenuClicked() = intent {
        postSideEffect(ManageMenuSideEffect.NavigateToRegisterMenuScreen(state.storeId))
    }

    fun onModifyMenuClicked(menuId: Int){
        intent {
            postSideEffect(ManageMenuSideEffect.NavigateToModifyMenuScreen(menuId))
        }
    }
}