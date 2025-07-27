package `in`.koreatech.koin.feature.store.view.cart.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.store.KoinStoreException
import `in`.koreatech.koin.domain.model.store.AddCartItemOption
import `in`.koreatech.koin.domain.model.store.CartAdd
import `in`.koreatech.koin.domain.usecase.store.AddCartItemUseCase
import `in`.koreatech.koin.domain.usecase.store.GetOrderableShopMenuUseCase
import `in`.koreatech.koin.feature.store.enums.CartError
import `in`.koreatech.koin.feature.store.model.toLocalShopMenuOptionGroup
import `in`.koreatech.koin.feature.store.model.toLocalShopPrice
import `in`.koreatech.koin.feature.store.navigation.ORDERABLE_SHOP_ID
import `in`.koreatech.koin.feature.store.navigation.ORDERABLE_SHOP_MENU_ID
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class CartAddViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getOrderableShopMenuUseCase: GetOrderableShopMenuUseCase,
    private val addCartItemUseCase: AddCartItemUseCase
) : ViewModel(), ContainerHost<CartAddState, CartAddSideEffect> {
    override val container = container<CartAddState, CartAddSideEffect>(CartAddState()) {
        val orderableShopId = savedStateHandle.get<Int>(ORDERABLE_SHOP_ID)
        val orderableShopMenuId = savedStateHandle.get<Int>(ORDERABLE_SHOP_MENU_ID)

        checkNotNull(orderableShopId)
        checkNotNull(orderableShopMenuId)

        intent {
            reduce {
                state.copy(
                    orderableShopId = orderableShopId,
                    orderableShopMenuId = orderableShopMenuId
                )
            }
        }

        getMenus()
    }

    private fun getMenus() = intent {
        reduce {
            state.copy(isLoading = true)
        }
        getOrderableShopMenuUseCase(
            shopId = state.orderableShopId,
            menuId = state.orderableShopMenuId
        ).onSuccess {
            reduce {
                state.copy(
                    isLoading = false,
                    menuName = it.name,
                    menuDescription = it.description,
                    menuImageUrls = it.images,
                    prices = it.prices.map { it.toLocalShopPrice() },
                    options = it.optionGroups.map { it.toLocalShopMenuOptionGroup() },
                    orderableShopMenuPriceId = if (it.prices.size == 1) {
                        it.prices.first().id
                    } else {
                        -1
                    }
                )
            }
        }.onFailure {
            reduce {
                state.copy(isLoading = false)
            }
        }
    }

    fun addCartItem() = intent {
        reduce {
            state.copy(isLoading = true)
        }
        addCartItemUseCase(
            CartAdd(
                orderableShopId = state.orderableShopId,
                orderableShopMenuId = state.orderableShopMenuId,
                orderableShopMenuPriceId = state.prices.getOrNull(0)?.id ?: -1,
                orderableShopMenuOptionIds = state.options.flatMap {
                    it.options.filter { option -> option.optionSelected }.map { option ->
                        AddCartItemOption(
                            optionGroupId = it.id,
                            optionId = option.id
                        )
                    }
                }
            )
        ).onSuccess {
            reduce {
                state.copy(
                    isLoading = false
                )
            }
            postSideEffect(CartAddSideEffect.CartItemAdded)
        }.onFailure { exception ->
            intent {
                reduce {
                    state.copy(isLoading = false)
                }
                when (exception) {
                    is KoinStoreException.DifferentShopItemInCartException -> {
                        reduce {
                            state.copy(error = CartError.DIFFERENT_SHOP_ITEM_IN_CART, showErrorDialog = true)
                        }
                    }
                    is KoinStoreException.MenuSoldOutException -> {
                        reduce {
                            state.copy(error = CartError.MENU_SOLD_OUT, showErrorDialog = true)
                        }
                    }
                    is KoinStoreException.RequiredOptionGroupMissingException -> {
                        reduce {
                            state.copy(error = CartError.REQUIRED_OPTION_GROUP_MISSING, showErrorDialog = true)
                        }
                    }
                    is KoinStoreException.MaxSelectionExceededException -> {
                        reduce {
                            state.copy(error = CartError.MAX_SELECTION_EXCEEDED, showErrorDialog = true)
                        }
                    }
                    is KoinStoreException.InvalidMenuInShopException -> {
                        reduce {
                            state.copy(error = CartError.INVALID_MENU_IN_SHOP, showErrorDialog = true)
                        }
                    }
                    is KoinStoreException.ShopClosedException -> {
                        reduce {
                            state.copy(error = CartError.SHOP_CLOSED, showErrorDialog = true)
                        }
                    }
                }
            }
        }
    }

    fun updateMenuPriceId(index: Int) = intent {
        reduce {
            state.copy(orderableShopMenuPriceId = index)
        }
    }

    fun updateSelectedOptionGroup(
        optionGroupId: Int,
        selectedOptionId: Int
    ) = intent {
        reduce {
            state.copy(
                options = state.options.map { optionGroup ->
                    if (optionGroup.id == optionGroupId) {
                        optionGroup.copy(
                            options = optionGroup.options.map { option ->
                                if (option.id == selectedOptionId) {
                                    option.copy(optionSelected = !option.optionSelected)
                                } else {
                                    option
                                }
                            }
                        )
                    } else {
                        optionGroup
                    }
                }
            )
        }
    }

    fun dismissErrorDialog() = intent {
        reduce {
            state.copy(
                showErrorDialog = false,
                error = CartError.NONE
            )
        }
    }
}
