package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun KoinOrdersResetChip(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = RebrandKoinTheme.colors.neutral400,
                spotColor = RebrandKoinTheme.colors.neutral500
            )
            .clip(RoundedCornerShape(24.dp))
            .background(RebrandKoinTheme.colors.neutral0)
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.orders_chip_reset),
            style = RebrandKoinTheme.typography.bold14,
            color = RebrandKoinTheme.colors.neutral500
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_process),
            contentDescription = "",
            tint = RebrandKoinTheme.colors.neutral500
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinOrdersResetChipPreview() {
    KoinOrdersResetChip()
}
