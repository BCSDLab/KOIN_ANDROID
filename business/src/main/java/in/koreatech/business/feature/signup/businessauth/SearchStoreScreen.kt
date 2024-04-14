package `in`.koreatech.business.feature.signup.businessauth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorActiveButton
import `in`.koreatech.business.ui.theme.ColorDescription
import `in`.koreatech.business.ui.theme.ColorHelper
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSearch
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.KOIN_ANDROIDTheme
import kotlinx.coroutines.launch

@Composable
fun SearchStoreScreen(modifier: Modifier = Modifier, onBackClicked: () -> Unit = {}) {
    var search by remember { mutableStateOf("") }
    val storeItems = ImmutableList.of("")
    Column(
        modifier = modifier,
    ) {
        IconButton(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { onBackClicked() }
        ) {
            Icon(
                modifier = Modifier.padding(start = 10.dp),
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.back_icon),
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
        ) {

            Text(
                text = stringResource(id = R.string.master_sign_up),
                fontSize = 24.sp,
                fontWeight = Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.search_store),
                fontSize = 18.sp,
                fontWeight = Bold,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(42.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                value = search, onValueChange = { search = it },
                textStyle = TextStyle(fontSize = 14.sp),

                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = ColorSearch,
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
            Spacer(modifier = Modifier.height(16.dp))
            StoreList(storeItems, onSelected = {})
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StoreList(item: ImmutableList<String>, onSelected: () -> Unit = {}) {
    var selectedItemIndex by remember { mutableStateOf(-1) }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()

    Scaffold() { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(item.size) { index ->
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .height(59.dp)
                        .clickable {
                            selectedItemIndex = index
                            scope.launch {
                                sheetState.show()
                            }
                        }
                        .border(
                            BorderStroke(
                                width = if (selectedItemIndex == index) 1.5.dp else 1.dp,
                                color = if (selectedItemIndex == index) ColorPrimary else ColorHelper
                            )
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
                            text = stringResource(id = R.string.store_name),
                            fontSize = 15.sp,
                            color = Color.Black,
                            fontWeight = Bold
                        )
                        Spacer(modifier = Modifier.width(74.dp))
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(color = ColorSecondary)) {
                                    append(text = stringResource(id = R.string.delivery))
                                }
                                append(" ")
                                withStyle(style = SpanStyle(color = ColorSecondary)) {
                                    append(text = stringResource(id = R.string.card_payment))
                                }
                                append(" ")
                                withStyle(style = SpanStyle(color = ColorSecondary)) {
                                    append(text = stringResource(id = R.string.account_transfer))
                                }
                            },
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

        ModalBottomSheetLayout(
            modifier = Modifier,
            sheetState = sheetState,
            sheetElevation = 8.dp,
            sheetContent = {
            StoreBottomSheet {
                onSelected()
            }
        }) {
        }

    }
}

@Composable
fun StoreBottomSheet(onSelected: () -> Unit) {
    Row(
        modifier = Modifier.background(Color.White)
            .fillMaxWidth().height(118.dp)
            .padding(20.dp),

        ) {
        Column(modifier = Modifier,) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(id = R.string.store_name),
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = Bold,
            )
            Row(  modifier=Modifier,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.phone_number),
                    fontSize = 14.sp,
                    color = ColorActiveButton,
                    fontWeight = Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "", fontSize = 14.sp, color = Color.Black,
                    fontWeight = Bold
                )

            }

        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 100.dp)
                .height(55.dp)
            ,
            onClick = { onSelected() },
            shape = RectangleShape,
        ) {
            Text(text = stringResource(id = R.string.select), fontWeight = Bold)
        }
    }

}


@Preview
@Composable
fun PreviewSearchStoreScreen() {
    KOIN_ANDROIDTheme {
     //   StoreList(ImmutableList.of(""))
        StoreBottomSheet(onSelected = {})
    }

}