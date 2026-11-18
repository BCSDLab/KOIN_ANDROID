package `in`.koreatech.koin.feature.land.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
internal fun LandStatus(
    @StringRes messageRes: Int,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = RebrandKoinTheme.colors.primary500,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = stringResource(messageRes),
            style = RebrandKoinTheme.typography.regular14,
            color = RebrandKoinTheme.colors.neutral600,
            textAlign = TextAlign.Center
        )
    }
}
