package `in`.koreatech.koin.feature.store.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.menu.AddMenuType
import `in`.koreatech.koin.domain.model.store.AddCartItemOption
import `in`.koreatech.koin.domain.model.store.CartAdd
import `in`.koreatech.koin.domain.model.store.CartItem
import `in`.koreatech.koin.domain.usecase.store.AddCartItemUseCase
import `in`.koreatech.koin.domain.usecase.store.GetCartItemEditUseCase
import `in`.koreatech.koin.domain.usecase.store.GetOrderableShopMenuUseCase
import `in`.koreatech.koin.domain.usecase.store.UpdateCartItemUseCase
import `in`.koreatech.koin.feature.store.view.menu.AddMenuState
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class AddMenuViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCartItemEditUseCase: GetCartItemEditUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val addCartItemUseCase: AddCartItemUseCase,
    private val getOrderableShopMenuUseCase: GetOrderableShopMenuUseCase
) : ViewModel(), ContainerHost<AddMenuState, Unit> {
    override val container =
        container<AddMenuState, Unit>(AddMenuState()) {
            val orderableStoreId = savedStateHandle.get<Int>(ORDERABLE_STORE_ID)
            val orderableShopMenuId = savedStateHandle.get<Int>(CART_MENU_ID)
            val addMenuMode = AddMenuType.valueOf(savedStateHandle.get<String>(KEY_ADD_MENU_MODE) ?: AddMenuType.ADD.name)

            checkNotNull(orderableShopMenuId)
            checkNotNull(orderableStoreId)

            reduce {
                state.copy(
                    orderableShopId = orderableStoreId,
                    orderableShopMenuId = orderableShopMenuId
                )
            }

            when (addMenuMode) {
                AddMenuType.EDIT -> getCartItemEdit(orderableShopMenuId)
                AddMenuType.ADD -> getOrderableShopMenu(orderableStoreId, orderableShopMenuId)
                else -> {}
            }
        }

    private fun getOrderableShopMenu(shopId: Int, menuId: Int) = intent { // 장바구니에 추가할 메뉴 조회 api
        getOrderableShopMenuUseCase(shopId, menuId).onSuccess { shopMenu ->
            reduce {
                state.copy(
                    cartItemEdit = shopMenu,
                    totalPrice = shopMenu.prices.sumOf { it.price } +
                        shopMenu.optionGroups.sumOf { optionGroup ->
                            optionGroup.options.filter { it.isSelected }.sumOf { it.price }
                        }
                )
            }
        }.onFailure {
            reduce {
                state.copy(
                    cartItemEdit = null
                )
            }
        }
    }

    private fun getCartItemEdit(cartMenuItemId: Int) = intent { // 장바구니 옵션 변경용 메뉴 조회 api
        getCartItemEditUseCase(cartMenuItemId).onSuccess { cartItemEdit ->
            reduce {
                state.copy(
                    cartItemEdit = cartItemEdit,
                    totalPrice = cartItemEdit.prices.sumOf { it.price } +
                        cartItemEdit.optionGroups.sumOf { optionGroup ->
                            optionGroup.options.filter { it.isSelected }.sumOf { it.price }
                        }
                )
            }
        }.onFailure {
            reduce {
                state.copy(
                    cartItemEdit = null
                )
            }
        }
    }

    fun onAddMenuClick() = intent {
        addCartItem()
    }

    private fun updateCartItem() = intent { // 장바구니 옵션 변경 api
        updateCartItemUseCase(
            cartMenuItemId = state.cartItemEdit?.id ?: 0,
            cartItem = CartItem(
                orderableShopMenuPriceId = state.orderableShopMenuPriceId,
                options = state.cartItemEdit?.optionGroups?.flatMap { optionGroup ->
                    optionGroup.options.filter { it.isSelected }.map { option ->
                        AddCartItemOption(
                            optionGroupId = optionGroup.id,
                            optionId = option.id
                        )
                    }
                } ?: emptyList()
            )

        ).onFailure {
            reduce {
                state.copy(
                    cartItem = null
                )
            }
        }
    }

    private fun addCartItem() = intent { // 장바구니 옵션 변경 api
        addCartItemUseCase(
            cartAdd = CartAdd(
                orderableShopId = state.orderableShopId,
                orderableShopMenuId = state.cartItemEdit?.id ?: 0,
                orderableShopMenuPriceId = state.orderableShopMenuPriceId,
                orderableShopMenuOptionIds = state.cartItemEdit?.optionGroups?.flatMap { optionGroup ->
                    optionGroup.options.filter { it.isSelected }.map { option ->
                        AddCartItemOption(
                            optionGroupId = optionGroup.id,
                            optionId = option.id
                        )
                    }
                },
                quantity = state.quantity
            )
        ).onFailure {
            Timber.d("AddMenuViewModel addCartItem error: $it")
            reduce {
                state.copy(
                    cartAdd = null
                )
            }
        }
    }

    fun changeCartPriceEdit(priceId: Int) = blockingIntent {
        reduce {
            val updatedCartItemEdit = state.cartItemEdit?.copy(
                prices = state.cartItemEdit?.prices?.map { price ->
                    if (price.id == priceId) {
                        price.copy(isSelected = !price.isSelected)
                    } else {
                        price.copy(isSelected = false)
                    }
                } ?: emptyList()
            )

            val newTotalPrice = (updatedCartItemEdit?.prices?.find { it.isSelected }?.price ?: 0) +
                (
                    updatedCartItemEdit?.optionGroups?.sumOf { optionGroup ->
                        optionGroup.options.filter { it.isSelected }.sumOf { it.price }
                    } ?: 0
                    )

            state.copy(
                orderableShopMenuPriceId = priceId,
                totalPrice = newTotalPrice
            )
        }
    }

    fun changeCartOptionEdit(optionGroupId: Int, optionId: Int) = blockingIntent {
        reduce {
            val updatedCartItemEdit = state.cartItemEdit?.copy(
                optionGroups = state.cartItemEdit?.optionGroups?.map { optionGroup ->
                    if (optionGroup.id == optionGroupId) {
                        optionGroup.copy(
                            options = optionGroup.options.map { option ->
                                if (option.id == optionId) {
                                    option.copy(isSelected = !option.isSelected)
                                } else {
                                    option
                                }
                            }
                        )
                    } else {
                        optionGroup
                    }
                } ?: emptyList(),
                prices = state.cartItemEdit?.prices ?: emptyList()
            )

            val newTotalPrice = (updatedCartItemEdit?.prices?.filter { it.isSelected }?.sumOf { it.price } ?: 0) +
                (
                    updatedCartItemEdit?.optionGroups?.sumOf { optionGroup ->
                        optionGroup.options.filter { it.isSelected }.sumOf { it.price }
                    } ?: 0
                    )

            state.copy(
                cartItemEdit = updatedCartItemEdit,
                totalPrice = newTotalPrice
            )
        }
    }

    companion object {
        const val CART_MENU_ID = "cartMenuId"
        const val ORDERABLE_STORE_ID = "orderableStoreId"
        const val KEY_ADD_MENU_MODE = "addMenuMode"
    }
}
