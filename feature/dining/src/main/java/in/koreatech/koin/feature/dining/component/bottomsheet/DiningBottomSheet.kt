package `in`.koreatech.koin.feature.dining.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.feature.dining.component.switch.KoinSwitch
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.dining.R

@Composable
fun DiningBottomSheet(
    firstChecked: Boolean,
    secondChecked: Boolean,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onPositive: () -> Unit = {},
    onFirstValueChange: () -> Unit = {},
    onSecondValueChange: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = KoinTheme.colors.neutral0,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                )
            )
            .padding(
                start = 32.dp,
                end = 32.dp,
                top = 24.dp,
                bottom = 28.dp
            )
    ) {
        Text(
            text = stringResource(R.string.dining_notification_on_boarding),
            style = KoinTheme.typography.bold18
        )
        Spacer(Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.set_dining_sold_out_notification),
                    style = KoinTheme.typography.regular16
                )
                KoinSwitch(
                    checked = firstChecked,
                    onCheckedChange = {
                        onFirstValueChange()
                    }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.set_image_upload_notification),
                    style = KoinTheme.typography.regular16
                )
                KoinSwitch(
                    checked = secondChecked,
                    onCheckedChange = {
                        onSecondValueChange()
                    }
                )
            }
        }
        Column(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FilledButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.navigate_to_notification_setting),
                textStyle = KoinTheme.typography.medium15,
                contentPadding = PaddingValues(12.dp),
                onClick = onPositive
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = KoinTheme.shapes.extraSmall)
                    .clickable {
                        onDismiss()
                    }
                    .padding(12.dp),
                text = stringResource(R.string.close),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun DiningBottomSheetPreview() {
    DiningBottomSheet(
        firstChecked = true,
        secondChecked = false
    )
}
