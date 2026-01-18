package `in`.koreatech.koin.feature.lostandfound.ui.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType

@Composable
fun DetailFoundSwitch(
    lostOrFoundType: LostOrFoundType,
    isFound: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicText(
            text = if (lostOrFoundType == LostOrFoundType.LOST) stringResource(id = R.string.lost_and_found_lost_message) else stringResource(id = R.string.lost_and_found_found_message),
            style = KoinTheme.typography.regular12.copy(color = KoinTheme.colors.neutral500)
        )
        Spacer(modifier = Modifier.width(8.dp))
        LostAndFoundDetailCustomSwitch(
            checked = isFound,
            onCheckedChange = { onCheckedChange() },
            enabled = !isFound
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}
