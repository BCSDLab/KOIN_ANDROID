package `in`.koreatech.business.feature.forcrupdate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray1
import `in`.koreatech.business.ui.theme.Gray500
import `in`.koreatech.business.util.ext.navigateToPlayStore
import `in`.koreatech.koin.core.R

@Composable
fun ForceUpdateDialog(isShow: MutableState<Boolean> = remember { mutableStateOf(true) }) {
    val context = LocalContext.current
    if (isShow.value) {
        Dialog(
            onDismissRequest = { isShow.value = false }
        ) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color.White,
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.force_update_already_update),
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.force_update_dialog_content),
                        fontSize = 13.sp,
                        color = Gray1,
                        textAlign = TextAlign.Center,
                        modifier =
                        Modifier
                            .padding(horizontal = 32.dp)
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                    )

                    Row(
                        modifier =
                        Modifier
                            .padding(horizontal = 32.dp)
                            .padding(top = 16.dp, bottom = 24.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { isShow.value = false },
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, Gray500),
                            colors = ButtonDefaults.buttonColors(Color.White),
                            modifier =
                            Modifier
                                .weight(1f)
                                .wrapContentHeight()
                        ) {
                            Text(
                                text = stringResource(id = R.string.positive),
                                fontSize = 14.sp,
                                color = Gray500
                            )
                        }

                        Button(
                            onClick = {
                                isShow.value = false
                                context.navigateToPlayStore()
                            },
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(ColorPrimary),
                            modifier =
                            Modifier
                                .weight(1f)
                                .wrapContentHeight()
                        ) {
                            Text(
                                text = stringResource(id = R.string.force_update_go_store),
                                fontSize = 14.sp,
                                color = Color.White
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
fun PreviewForceUpdateDialog() {
    // ForceUpdateDialog()
}
