package `in`.koreatech.koin.feature.store.notice.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun StoreNoticeEmpty(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_notice_empty),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Text(
            text = stringResource(R.string.store_notice_empty),
            style = RebrandKoinTheme.typography.bold18,
            color = RebrandKoinTheme.colors.primary500
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StoreNoticeEmptyPreview() {
    StoreNoticeEmpty()
}
