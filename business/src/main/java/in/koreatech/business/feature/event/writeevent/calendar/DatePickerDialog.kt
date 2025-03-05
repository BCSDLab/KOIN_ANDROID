package `in`.koreatech.business.feature.event.writeevent.calendar

import android.widget.NumberPicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray3
import `in`.koreatech.business.ui.theme.Gray500

@Composable
fun DatePickerDialog(
    currentYear: Int,
    currentMonth: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
) {
    var selectedYear by remember { mutableStateOf(currentYear) }
    var selectedMonth by remember { mutableStateOf(currentMonth) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "${currentYear}년 ${selectedMonth}월",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray500,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    AndroidView(
                        factory = { context ->
                            NumberPicker(context).apply {
                                minValue = 2000
                                maxValue = 3000
                                value = selectedYear
                                setOnValueChangedListener { _, _, newVal ->
                                    selectedYear = newVal
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )

                    AndroidView(
                        factory = { context ->
                            NumberPicker(context).apply {
                                minValue = 1
                                maxValue = 12
                                value = selectedMonth
                                setOnValueChangedListener { _, _, newVal ->
                                    selectedMonth = newVal
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier =
                            Modifier
                                .wrapContentSize()
                                .padding(4.dp),
                        colors =
                            ButtonDefaults.outlinedButtonColors(
                                backgroundColor = Color.Transparent,
                            ),
                        border = BorderStroke(1.dp, Gray3),
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 30.dp).wrapContentSize(),
                            color = Gray3,
                        )
                    }

                    TextButton(
                        onClick = { onConfirm(selectedYear, selectedMonth) },
                        modifier =
                            Modifier
                                .wrapContentSize()
                                .padding(4.dp),
                        colors =
                            ButtonDefaults.textButtonColors(
                                backgroundColor = ColorPrimary,
                                contentColor = Color.White,
                            ),
                    ) {
                        Text(
                            text = stringResource(R.string.check),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 30.dp).wrapContentSize(),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DatePickerDialogPreview() {
    DatePickerDialog(
        currentYear = 2023,
        currentMonth = 1,
        onDismiss = {},
        onConfirm = { _, _ -> },
    )
}
