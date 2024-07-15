package `in`.koreatech.business.feature.store.modifyinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.HoursNumberPicker
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailViewModel
import `in`.koreatech.business.ui.theme.Blue3
import `in`.koreatech.business.ui.theme.Gray10
import `in`.koreatech.koin.core.R
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun OperatingTimeSettingDialog(
    viewModel: ModifyInfoViewModel = hiltViewModel(),
    myStoreDetailViewModel: MyStoreDetailViewModel,
) {
    val state = viewModel.collectAsState().value
    val myStoreDetailState = myStoreDetailViewModel.collectAsState().value
    if (state.showDialog) {
        AlertDialog(modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
            onDismissRequest = viewModel::hideAlertDialog,
            text = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    HoursNumberPicker(modifier = Modifier.weight(1f),
                        dividersColor = MaterialTheme.colors.primary,
                        leadingZero = true,
                        value = FullHours(
                            state.dialogTimeState.openTime.hours,
                            state.dialogTimeState.openTime.minutes
                        ),
                        onValueChange = {
                            viewModel.onSettingStoreTime(
                                FullHours(it.hours, it.minutes),
                                FullHours(
                                    state.dialogTimeState.closeTime.hours,
                                    state.dialogTimeState.closeTime.minutes
                                )
                            )
                        },
                        minutesRange = (0..59 step 5),
                        hoursDivider = {
                            Text(
                                textAlign = TextAlign.Center, text = ":"
                            )
                        })
                    Text(
                        modifier = Modifier
                            .weight(0.3f)
                            .fillMaxHeight()
                            .wrapContentWidth(align = Alignment.CenterHorizontally),
                        text = " \n\n\n\n\n~",
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )

                    HoursNumberPicker(modifier = Modifier.weight(1f),
                        dividersColor = MaterialTheme.colors.primary,
                        leadingZero = true,
                        value = FullHours(
                            state.dialogTimeState.closeTime.hours,
                            state.dialogTimeState.closeTime.minutes
                        ),
                        onValueChange = {
                            viewModel.onSettingStoreTime(
                                FullHours(
                                    state.dialogTimeState.openTime.hours,
                                    state.dialogTimeState.openTime.minutes
                                ),
                                FullHours(it.hours, it.minutes),

                            )
                        },
                        minutesRange = (0..59 step 5),
                        hoursDivider = {
                            Text(
                                textAlign = TextAlign.Center, text = ":"
                            )
                        })
                }
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Blue3,
                    ),
                    onClick = viewModel::hideAlertDialog,
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
                ) {
                    Text(stringResource(id = R.string.positive))
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Gray10,
                    ),
                    onClick = viewModel::hideAlertDialog,
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
                ) {
                    Text(stringResource(id = R.string.close))
                }
            }
        )
    }
}
