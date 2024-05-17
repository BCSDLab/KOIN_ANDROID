package `in`.koreatech.business.feature.signup.businessauth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorDescription
import `in`.koreatech.business.ui.theme.ColorHelper
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.Gray1
import `in`.koreatech.business.ui.theme.Gray2
import `in`.koreatech.koin.domain.model.store.Store
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SearchStoreScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchStoreViewModel = hiltViewModel(),
    businessAuthViewModel: BusinessAuthViewModel = hiltViewModel(),
    onBackButtonClicked: () -> Unit = {}
) {
    val state = viewModel.collectAsState().value
    Column(
        modifier = modifier,

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            IconButton(
                onClick = { viewModel.onNavigateToBackScreen() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = stringResource(id = R.string.back_icon),
                )
            }

            Text(
                text = stringResource(id = R.string.search_store),
                fontSize = 18.sp,
                fontWeight = Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }


        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier,
                    color = ColorPrimary,
                    fontWeight = Bold,
                    text = stringResource(id = R.string.business_auth),
                )
                Text(
                    text = stringResource(id = R.string.three_third),
                    color = ColorPrimary,
                    fontWeight = Bold,
                )
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                drawLine(
                    color = ColorPrimary,
                    start = Offset(-40f, 0f),
                    end = Offset((size.width + 40), size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(6.dp),
                value = state.search, onValueChange = { viewModel.onSearchChanged(it)
                                                      viewModel.onSearchStore()},
                textStyle = TextStyle(fontSize = 14.sp),

                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = ColorTextField,
                    unfocusedIndicatorColor = ColorTextField,
                    focusedLabelColor = ColorTextField,
                ),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_shop),
                        color = ColorDescription,
                        fontSize = 13.sp
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            modifier = Modifier.size(17.dp),
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = stringResource(id = R.string.search_icon)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(5.dp))
            StoreList(state.stores, viewModel, businessAuthViewModel)

        }


        viewModel.collectSideEffect {
            when (it) {
                is SearchStoreSideEffect.SearchStore -> {
                    viewModel.searchStore()
                }

                SearchStoreSideEffect.NavigateToBackScreen -> {
                    onBackButtonClicked()
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StoreList(
    item: List<Store>,
    viewModel: SearchStoreViewModel = hiltViewModel(),
    businessAuthViewModel: BusinessAuthViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    val selectedStoreState = businessAuthViewModel.collectAsState().value
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .height(200.dp)

    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp)
                .fillMaxSize()
        ) {
            items(item.size) { index ->
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .height(55.dp)
                        .clickable {
                            scope.launch {
                                sheetState.show()
                            }
                            viewModel.onItemIndexChange(index)
                        }
                        .border(
                            BorderStroke(
                                width = if (state.itemIndex == index) 1.5.dp else 1.dp,
                                color = if (state.itemIndex == index) ColorPrimary else ColorHelper
                            ),
                            shape = RoundedCornerShape(6.dp),
                        ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            modifier = Modifier,
                            text = item[index].name,
                            fontSize = 15.sp,
                            color = Color.Black,

                            )
                        Spacer(modifier = Modifier.width(74.dp))
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(color = ColorPrimary)) {
                                    append(text = stringResource(id = R.string.delivery))
                                }
                                append(" ")
                                withStyle(style = SpanStyle(color = ColorPrimary)) {
                                    append(text = stringResource(id = R.string.card_payment))
                                }
                                append(" ")
                                withStyle(style = SpanStyle(color = ColorPrimary)) {
                                    append(text = stringResource(id = R.string.account_transfer))
                                }
                            },
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
        Spacer(modifier =Modifier.height(70.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
            shape = RoundedCornerShape(4.dp),
            enabled = state.itemIndex > -1,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorPrimary,
                contentColor = Color.White,
                disabledBackgroundColor = Gray2,
                disabledContentColor = Gray1,
            ),
            onClick = {
                viewModel.onNavigateToBackScreen()
                selectedStoreState.shopId?.let { businessAuthViewModel.onShopIdChanged(state.itemIndex) }
                businessAuthViewModel.onShopNameChanged(state.stores[state.itemIndex].name)
            }) {
            Text(
                text = stringResource(id = R.string.next),
                fontSize = 13.sp,
                fontWeight = Bold,
            )
        }
    }
}
