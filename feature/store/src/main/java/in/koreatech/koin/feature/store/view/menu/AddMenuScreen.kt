package `in`.koreatech.koin.feature.store.view.menu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.feature.store.util.CustomClosingToolbarScreenDefaults
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.model.store.CartItemEdit.CartItemEditOptionGroup
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.MenuOptionCard
import `in`.koreatech.koin.feature.store.scroll.storeCollapsingToolbarConnection
import `in`.koreatech.koin.feature.store.state.currentToolbarHeightDp
import `in`.koreatech.koin.feature.store.state.progress
import `in`.koreatech.koin.feature.store.state.rememberCollapsingToolbarState
import `in`.koreatech.koin.feature.store.viewmodel.StoreDetailViewModel
import org.orbitmvi.orbit.compose.collectAsState
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddMenuScreen(
    viewModel: StoreDetailViewModel = hiltViewModel(),
    navigateToCart: () -> Unit = {},
    navigateToBack: () -> Unit = {},
    navigateToDetailInfo: () -> Unit = {},
    navigateToReview: () -> Unit = {}
) {
    val uiState by viewModel.collectAsState()

    val rememberState = rememberCollapsingToolbarState()
    val progress = rememberState.progress()
    val overlayAlpha = (progress).coerceIn(0f, 1f)
    val nestedScrollConnection = storeCollapsingToolbarConnection(
        listState = rememberState.listState,
        toolbarOffsetPx = rememberState.toolbarOffsetPx,
        toolbarHeightPx = rememberState.toolbarHeightPx,
        minHeightPx = rememberState.minHeightPx
    )
    val currentToolbarHeightDp = rememberState.currentToolbarHeightDp()
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()

            .background(color = colorResource(id = R.color.store_detail_background))
            .nestedScroll(nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
                .padding(
                    top = currentToolbarHeightDp + CustomClosingToolbarScreenDefaults.windowInsets
                        .asPaddingValues()
                        .calculateTopPadding()
                ),
            state = rememberState.listState
        ) {
            item {
                Text(
                    text = "메뉴 이름",
                    style = RebrandKoinTheme.typography.bold18,
                    color = RebrandKoinTheme.colors.neutral800
                )
                Text(
                    modifier = Modifier.padding(vertical = 3.dp),
                    text = "25000원",
                    style = RebrandKoinTheme.typography.bold18,
                    color = RebrandKoinTheme.colors.primary500
                )
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = "메뉴세부사항등등등",
                    style = RebrandKoinTheme.typography.medium12,
                    color = RebrandKoinTheme.colors.neutral500
                )
            }
            item{
                MenuOptionCard(
                    shopMenuOption = CartItemEditOptionGroup(
                        id = 0,
                        name = "옵션 없음",
                        description = "옵션이 없습니다.",
                        isRequired = false,
                        minSelect = 0,
                        maxSelect = 0,
                        options = listOf(
                            CartItemEditOptionGroup.CartItemEditOption(
                                id = 0,
                                name = "옵션 없음",
                                price = 0,
                                isSelected = false
                            ),
                            CartItemEditOptionGroup.CartItemEditOption(
                                id = 1,
                                name = "옵션 없음",
                                price = 1000,
                                isSelected = true
                            ),
                            CartItemEditOptionGroup.CartItemEditOption(
                                id = 1,
                                name = "옵션 없음",
                                price = 1000,
                                isSelected = true
                            ),

                        ),
                    ),

                )
            }
            item{
                MenuOptionCard(
                    shopMenuOption = CartItemEditOptionGroup(
                        id = 0,
                        name = "옵션 없음",
                        description = "옵션이 없습니다.",
                        isRequired = false,
                        minSelect = 0,
                        maxSelect = 0,
                        options = listOf(
                            CartItemEditOptionGroup.CartItemEditOption(
                                id = 0,
                                name = "옵션 없음",
                                price = 0,
                                isSelected = false
                            ),
                            CartItemEditOptionGroup.CartItemEditOption(
                                id = 1,
                                name = "옵션 없음",
                                price = 1000,
                                isSelected = true
                            ),
                            CartItemEditOptionGroup.CartItemEditOption(
                                id = 1,
                                name = "옵션 없음",
                                price = 1000,
                                isSelected = true
                            ),

                            ),
                    ),

                    )
            }

        }
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .zIndex(2f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navigateToBack()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = null,
                    tint = lerp(
                        KoinTheme.colors.neutral800,
                        KoinTheme.colors.neutral0,
                        1f - overlayAlpha
                    )
                )
            }
            Text(
                text = uiState.store.name,
                fontWeight = Bold,
                color = KoinTheme.colors.neutral800.copy(alpha = overlayAlpha)
            )
            Box(contentAlignment = Alignment.TopEnd) {
                IconButton(onClick = {
                    navigateToCart()
                }) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_shopping_cart),
                        contentDescription = null,
                        tint = lerp(
                            KoinTheme.colors.neutral800,
                            KoinTheme.colors.neutral0,
                            1f - overlayAlpha
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .offset(x = (-5).dp, y = 5.dp)
                        .size(16.dp)
                        .background(Color.Magenta, CircleShape)
                ) {
                    Text(
                        text = "3",
                        fontSize = 10.sp,
                        lineHeight = 11.sp,
                        color = KoinTheme.colors.neutral0,
                        modifier = Modifier.padding(start = 4.dp, bottom = 0.5.dp)
                    )
                }
            }
        }
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(rememberState.toolbarMaxHeight)
                .offset { IntOffset(0, rememberState.toolbarOffsetPx.floatValue.roundToInt()) }
                .zIndex(1f),
            painter = painterResource(id = R.drawable.ic_delivery),
            contentDescription = null,
            alpha = 1 - overlayAlpha,
            alignment = Alignment.Center,
        )
    }
}

