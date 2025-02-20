package `in`.koreatech.business.feature.storemenu.managemenu.managemenu

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.owner.MenuCategory
import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.domain.usecase.business.GetOwnerShopMenusUseCase
import `in`.koreatech.koin.domain.usecase.business.menu.DeleteMenuUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
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
    private val  getOwnerShopMenusUseCase: GetOwnerShopMenusUseCase,
    private val deleteMenuUseCase: DeleteMenuUseCase
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

    private fun setCheckBox() = intent {
        reduce{
            val newList: MutableList<MenuCategory> = mutableListOf()
            newList.add(MenuCategory("추천 메뉴", state.currentCheckboxState == CheckBoxState.RECOMMENDMENU))
            newList.add(MenuCategory("메인 메뉴", state.currentCheckboxState == CheckBoxState.MAINMENU))
            newList.add(MenuCategory("세트 메뉴", state.currentCheckboxState == CheckBoxState.SETMENU))
            newList.add(MenuCategory("사이드 메뉴",  state.currentCheckboxState ==CheckBoxState.SIDEMENU))

            state.copy(
                storeMenuCategoryList = newList
            )
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

    fun onChangeIsShowDialogState() = intent {
        reduce {
            state.copy(
                isShowDeleteDialog = !state.isShowDeleteDialog
            )
        }
    }
    fun onDeleteMenuClicked(menuItem: ShopMenus){
        intent {
            onChangeIsShowDialogState()
            reduce {
                state.copy(
                    menuId = menuItem.id,
                    menuName = menuItem.name
                )
            }
        }
    }


    fun onCheckBoxClicked(index: Int) = intent {
        reduce {
            state.copy(
                currentCheckboxState = when(index){
                    0 -> CheckBoxState.RECOMMENDMENU
                    1 -> CheckBoxState.MAINMENU
                    2 -> CheckBoxState.SETMENU
                    3 -> CheckBoxState.SIDEMENU
                    else -> CheckBoxState.RECOMMENDMENU
                }
            )
        }
        setCheckBox()
    }

    fun onPositiveButtonClicked() = intent{
        viewModelScope.launch(Dispatchers.IO) {
            deleteMenuUseCase(state.menuId)
                .onSuccess {
                    Log.e("로그", "성공")
                    onChangeIsShowDialogState()
                    getShopMenus()
                    postSideEffect(ManageMenuSideEffect.SuccessDeleteMenu)
                }
                .onFailure {
                    Log.e("로그", "실패")
                    postSideEffect(ManageMenuSideEffect.FailedDeleteMenu)
                }
        }
    }
}