package `in`.koreatech.business.feature.store.storedetail.dialog

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorActiveButton
import `in`.koreatech.business.ui.theme.ColorDisabledButton
import `in`.koreatech.business.ui.theme.ColorMinor
import `in`.koreatech.koin.domain.model.store.Store

@Composable
fun MyStoreSelectDialog(
    onClickCancel: () -> Unit = {},
    dialogVisibility: Boolean = true,
    storeList: List<Store> = emptyList(),
    selectStore: (Int) -> Unit = {}
) {
    if(dialogVisibility){
        Dialog(
            onDismissRequest = { onClickCancel() },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            )
        ) {
            Card(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                ),
            )
            {
                Column(
                    modifier = Modifier
                        .width(300.dp)
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Box(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_x),
                            contentDescription = "",
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 16.dp)
                                .clickable {
                                    onClickCancel()
                                }
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.select_store),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .heightIn(max = 200.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){
                        itemsIndexed(storeList){ index, store ->
                            Text(
                                modifier = Modifier
                                    .height(30.dp)
                                    .padding(horizontal = 40.dp)
                                    .clickable {
                                        selectStore(store.uid)
                                        onClickCancel()
                                    }
                                ,
                                text = "${index + 1}. ${store.name}",
                                fontSize = 15.sp,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewMyStoreSelectDialog() {
    MyStoreSelectDialog(
    )
}