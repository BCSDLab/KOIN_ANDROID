package `in`.koreatech.koin.feature.article.ui.lostandfound.report.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.article.R

@Composable
fun LostAndFoundReportHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp)
    ) {
        Text(
            text = stringResource(id = R.string.report_header_title),
            style = KoinTheme.typography.bold18.copy(fontWeight = FontWeight.SemiBold),
            color = KoinTheme.colors.neutral800
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.report_header_description),
            style = KoinTheme.typography.regular14,
            color = Color(0xFF8E8E8E) // TODO: Replace after design system update
        )
    }
}
