package `in`.koreatech.business.feature.store

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorDisabledButton
import `in`.koreatech.business.ui.theme.ColorMinor
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.Gray6


@Composable
fun OwnerStoreDialog(
    dialogTitle: String,
    dialogText: String,
    positiveButtonText: String,
    visibility: Boolean = false,
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (!visibility) {
        return
    }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .padding(7.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(vertical = 40.dp, horizontal = 14.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = dialogTitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(400),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 13.sp,
                    color = Color.Black,
                    text = dialogText,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row {
                    TextButton(
                        onClick = {
                            onDismissRequest()
                        },
                        border = BorderStroke(1.dp, Gray6),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .width(128.dp)
                            .height(48.dp),
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = stringResource(id = R.string.cancel),
                            color = ColorMinor,
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    TextButton(
                        onClick = {
                            onConfirmation()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = ColorSecondary,
                            disabledBackgroundColor = ColorDisabledButton,
                            contentColor = Color.White,
                            disabledContentColor = Color.White,
                        ),
                        border = BorderStroke(1.dp, ColorSecondary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .width(128.dp)
                            .height(48.dp),
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = positiveButtonText
                        )
                    }
                }
            }
        }
    }
}
