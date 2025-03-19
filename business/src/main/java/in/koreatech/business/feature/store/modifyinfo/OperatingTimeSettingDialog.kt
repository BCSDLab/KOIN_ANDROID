package `in`.koreatech.business.feature.store.modifyinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import com.chargemap.compose.numberpicker.HoursNumberPicker
import `in`.koreatech.koin.core.R

@Composable
fun OperatingTimeSettingDialog(
    title: String = "",
    operatingTimeDialog: OperatingTimeDialog = OperatingTimeDialog()
) {
    var timeValue by remember { mutableStateOf<Hours>(FullHours(0, 0)) }

    if (operatingTimeDialog.showDialog) {
        Dialog(onDismissRequest = { operatingTimeDialog.closeDialog() }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                elevation = 8.dp
            ) {
                Column(
                    modifier =
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = title,
                        style = MaterialTheme.typography.h6,
                        fontSize = 20.sp
                    )

                    HoursNumberPicker(
                        modifier =
                        Modifier
                            .height(120.dp),
                        dividersColor = MaterialTheme.colors.primary,
                        leadingZero = true,
                        value = timeValue,
                        onValueChange = {
                            timeValue = it
                        },
                        minutesRange = (0..59 step 5),
                        hoursDivider = {
                            Text(
                                modifier = Modifier.size(24.dp),
                                textAlign = TextAlign.Center,
                                text = ":"
                            )
                        }
                    )
                    Row(
                        modifier =
                        Modifier
                            .align(Alignment.End)
                    ) {
                        Button(
                            modifier = Modifier.padding(end = 5.dp),
                            onClick = {
                                operatingTimeDialog.closeDialog()
                            }
                        ) {
                            Text(stringResource(id = R.string.cancel))
                        }
                        Button(
                            onClick = {
                                operatingTimeDialog.closeDialog()
                                operatingTimeDialog.onSettingStoreTime(Pair(timeValue, operatingTimeDialog.dayOfWeekIndex))
                            }
                        ) {
                            Text(stringResource(id = R.string.positive))
                        }
                    }
                }
            }
        }
    }
}
