import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.event.writeevent.calendar.CalendarScreen
import `in`.koreatech.business.feature.event.writeevent.writeevent.WriteEventViewModel
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorTextFieldDescription
import `in`.koreatech.business.ui.theme.Gray10
import `in`.koreatech.business.ui.theme.Gray3
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarBottomDialog(
    viewModel: WriteEventViewModel = hiltViewModel(),
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
) {
    if (!showDialog) return

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var expandedMonth by remember { mutableStateOf(false) }
                    Text(
                        text = stringResource(id = R.string.setting_event_duration),
                        fontSize = 18.sp,
                        modifier = Modifier
                            .clickable { expandedMonth = true }
                    )
                }
                CalendarScreen(
                    viewModel = viewModel,
                )
                Row {
                    Button(
                        onClick = {
                            coroutineScope.launch { bottomSheetState.hide() }
                                .invokeOnCompletion { onDismiss() }
                        },
                        modifier = Modifier
                            .width(128.dp)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White
                        )
                    ) {
                        Text(text = stringResource(id = R.string.cancel),
                            color = ColorTextFieldDescription)
                    }
                    Spacer(modifier = Modifier.width(32.dp))
                    Button(
                        onClick = {
                            coroutineScope.launch { bottomSheetState.hide() }
                                .invokeOnCompletion { onDismiss() }
                        },
                        modifier = Modifier
                            .width(128.dp)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            backgroundColor = ColorPrimary
                        )
                    ) {
                        Text(text = stringResource(id = R.string.register))
                    }
                }
            }
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {}
}


@Preview(showBackground = true)
@Composable
fun PreviewCalendarBottomDialog() {
    CalendarBottomDialog(
        showDialog = true,
        onDismiss = {},
        onDateSelected = {},
    )
}