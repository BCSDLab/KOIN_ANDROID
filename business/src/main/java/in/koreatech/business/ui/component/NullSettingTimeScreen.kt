package `in`.koreatech.business.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import `in`.koreatech.business.ui.component.button.SettingTimeButton
import `in`.koreatech.business.ui.theme.Gray3
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NullSettingTime(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
        ),
    updateIsSettingScreenState: (Boolean) -> Unit = {},
) {
    Column(
        modifier = modifier,
    ) {
        Divider(
            modifier =
                Modifier
                    .fillMaxWidth(),
            color = Gray3,
            thickness = 0.5.dp,
        )

        Text(
            modifier =
                Modifier
                    .padding(vertical = 135.dp)
                    .fillMaxWidth(),
            text = stringResource(R.string.setting_time_is_null),
            style = KoinTheme.typography.medium16,
            textAlign = TextAlign.Center,
        )

        Divider(
            modifier =
                Modifier
                    .fillMaxWidth(),
            color = Gray3,
            thickness = 0.5.dp,
        )

        Row(
            modifier =
                Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .clickable {
                        updateIsSettingScreenState(true)
                    },
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.add_setting_time),
                style = KoinTheme.typography.medium16,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(R.drawable.fi_plus),
                contentDescription = "플러스버튼",
            )
        }

        Divider(
            modifier =
                Modifier
                    .fillMaxWidth(),
            color = Gray3,
            thickness = 0.5.dp,
        )
    }

    SettingTimeButton(
        modifier = Modifier,
        onCancelButtonClicked = {
            updateIsSettingScreenState(false)
            coroutineScope.launch {
                sheetState.hide()
            }
        },
        onRegisterButtonClicked = {
            updateIsSettingScreenState(true)
        },
    )
}
