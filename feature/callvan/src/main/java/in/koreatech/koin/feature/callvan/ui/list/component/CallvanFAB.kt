package `in`.koreatech.koin.feature.callvan.ui.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFABDefaults

@Composable
fun CallvanFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = CallvanFABDefaults.windowInsets
) {
    Box(
        modifier = modifier
            .windowInsetsPadding(windowInsets)
            .wrapContentSize(Alignment.BottomEnd)
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = RebrandKoinTheme.colors.primary400,
                    shape = RoundedCornerShape(50)
                )
                .background(
                    color = RebrandKoinTheme.colors.neutral50,
                    shape = RoundedCornerShape(50)
                )
                .noRippleClickable(onClick = onClick)
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_write_fab),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
                Spacer(
                    modifier = Modifier.width(4.dp)
                )
                Text(
                    text = stringResource(R.string.write_btn),
                    style = RebrandKoinTheme.typography.medium16,
                    color = RebrandKoinTheme.colors.primary600
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanFABPreview() {
    RebrandKoinTheme {
        CallvanFAB(onClick = {})
    }
}
