package `in`.koreatech.business.feature_signup.businessauth

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.feature_signup.dialog.BusinessAlertDialog
import `in`.koreatech.business.feature_signup.textfield.LinedTextField
import `in`.koreatech.business.ui.theme.ColorActiveButton
import `in`.koreatech.business.ui.theme.ColorDisabledButton
import `in`.koreatech.business.ui.theme.ColorHelper
import `in`.koreatech.business.ui.theme.Gray2
import `in`.koreatech.business.ui.theme.Gray3
import `in`.koreatech.business.ui.theme.KOIN_ANDROIDTheme
import `in`.koreatech.business.ui.theme.Orange

@Composable
fun BusinessAuthScreen(modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var storeName by remember { mutableStateOf("") }
    var storeNumber by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val openAlertDialog = remember { mutableStateOf(false) }
    val itemList: MutableList<String> = mutableListOf()

    Column(
        modifier = modifier,
    ) {
        IconButton(modifier = Modifier.padding(vertical = 24.dp), onClick = { }) {
            Icon(
                modifier = Modifier.padding(start = 10.dp),
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.back_icon),
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.master_sign_up),
                fontSize = 24.sp,
                fontWeight = Bold,
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    color = Orange, text = stringResource(id = R.string.business_auth)
                )
                Text(
                    color = Orange, text = stringResource(id = R.string.three_third)
                )
            }
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                drawLine(
                    color = Orange,
                    start = Offset(0f - 35, 0f),
                    end = Offset(size.width + 35, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinedTextField(
                value = name,
                onValueChange = { name = it },
                label = stringResource(id = R.string.master_name)
            )

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LinedTextField(
                    modifier = Modifier.width(197.dp),
                    value = storeName,
                    onValueChange = { storeName = it },
                    label = stringResource(id = R.string.enter_store_name)
                )
                Button(
                    modifier = Modifier,
                    onClick = { },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF175C8E),
                        contentColor = Color.White,
                    ),
                ) {
                    Text(text = stringResource(id = R.string.search_store))
                }
            }
            LinedTextField(
                value = storeNumber,
                onValueChange = { storeNumber = it },
                label = stringResource(id = R.string.business_registration_number)
            )
            LinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = stringResource(id = R.string.personal_contact)
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp),
            ) {

                if (itemList.isNotEmpty()) UploadFileList(itemList)
                else
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(125.dp)
                            .border(BorderStroke(1.dp, ColorHelper))
                            .clickable { },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.plus_square),
                            contentDescription = stringResource(id = R.string.upload_file_icon),
                            tint = Orange
                        )
                        Text(
                            text = stringResource(id = R.string.file_upload_prompt),
                            fontSize = 12.sp,
                            fontWeight = Bold,
                            color = Color.Black
                        )
                        Text(
                            text = stringResource(id = R.string.file_upload_instruction),
                            fontSize = 11.sp,
                            color = Gray2,
                        )
                    }

            }

            Spacer(modifier = Modifier.height(47.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorActiveButton,
                    disabledContainerColor = ColorDisabledButton,
                    contentColor = Color.White,
                    disabledContentColor = Color.White,
                ),
                onClick = { }) {
                Text(
                    text = stringResource(id = R.string.next),
                    fontSize = 15.sp,
                    color = Color.White,
                )

                BusinessAlertDialog(
                    onDismissRequest = { openAlertDialog.value = false },
                    onConfirmation = {
                        openAlertDialog.value = false
                    },
                    dialogTitle = stringResource(id = R.string.file_upload),
                    dialogText = stringResource(id = R.string.file_upload_requirements),
                    positiveButtonText = stringResource(id = R.string.select_file)
                )

            }


        }
    }
}

@Composable
fun UploadFileList(item: MutableList<String>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .border(BorderStroke(1.dp, ColorHelper)),
    ) {

        items(item.size) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.file_icon),
                    contentDescription = stringResource(id = R.string.file_icon),
                    tint = Gray3,
                )
                Text(text = "", fontSize = 15.sp, color = Gray3)
            }
            Spacer(modifier = Modifier.width(12.dp))
        }

    }
}
