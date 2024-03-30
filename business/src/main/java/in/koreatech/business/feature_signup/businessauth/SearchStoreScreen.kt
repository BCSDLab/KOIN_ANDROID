@file:OptIn(ExperimentalMaterial3Api::class)

package `in`.koreatech.business.feature_signup.businessauth

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorActiveButton
import `in`.koreatech.business.ui.theme.ColorHelper
import `in`.koreatech.business.ui.theme.Gray2
import `in`.koreatech.business.ui.theme.Gray4
import `in`.koreatech.business.ui.theme.KOIN_ANDROIDTheme

@Composable
fun SearchStoreScreen(modifier: Modifier = Modifier) {
    var search by remember { mutableStateOf("") }
    val storeItems = mutableListOf<String>()

    Column(
        modifier = modifier,
    ) {
        IconButton(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { }
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

                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Gray4,
                    focusedLabelColor = Gray4,
                ),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_shop),
                        color = Gray2,
                        fontSize = 13.sp
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            modifier = Modifier.size(17.dp),
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "search"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            StoreList(storeItems)
        }
    }
}


@Composable
fun StoreList(item: MutableList<String>) {

    var showBottomSheet by remember { mutableStateOf(false) }
    var isButtonClicked by remember { mutableStateOf(false) }

    Scaffold() { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            items(item.size) { index ->

                Row(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth()
                        .height(59.dp)
                        .clickable {
                            showBottomSheet = true
                            isButtonClicked = true
                        }
                        .border(
                            BorderStroke(
                                width = if (isButtonClicked) 1.5.dp else 1.dp,
                                color = if (isButtonClicked) ColorActiveButton else ColorHelper
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
                                withStyle(style = SpanStyle(color = Color(0xFFF7941E))) {
                                    append(text = stringResource(id = R.string.delivery))
                                }
                                append(" ")
                                withStyle(style = SpanStyle(color = Color(0xFFF7941E))) {
                                    append(text = stringResource(id = R.string.card_payment))
                                }
                                append(" ")
                                withStyle(style = SpanStyle(color = Color(0xFFF7941E))) {
                                    append(text = stringResource(id = R.string.account_transfer))
                                }
                            },
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                dragHandle = null,
                onDismissRequest = {
                    showBottomSheet = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(138.dp)
                    .padding(contentPadding),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 23.dp),

                    ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = stringResource(id = R.string.store_name),
                            fontSize = 18.sp,
                            color = Color.Black,
                            fontWeight = Bold,
                        )
                        Row {
                            Text(
                                text = stringResource(id = R.string.phone_number),
                                fontSize = 14.sp,
                                color = Color(0xFF175C8E),
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
                            .width(195.dp)
                            .height(85.dp)
                            .padding(16.dp),
                        onClick = { },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF175C8E))
                    ) {
                        Text(text = stringResource(id = R.string.select), fontWeight = Bold)
                    }
                }

            }
        }
    }

}
