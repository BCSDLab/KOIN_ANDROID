package `in`.koreatech.koin.feature.lostandfound.ui.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.lostandfound.R

object LostAndFoundFABDefaults {
    val windowInsets: WindowInsets
        @Composable
        get() =
            WindowInsets.systemBars.only(
                WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
            )
}

@Composable
fun LostAndFoundFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    windowInsets: WindowInsets = LostAndFoundFABDefaults.windowInsets
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
                    color = RebrandKoinTheme.colors.primary500,
                    shape = RoundedCornerShape(50)
                )
                .background(
                    color = RebrandKoinTheme.colors.neutral0,
                    shape = RoundedCornerShape(50)
                )
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .noRippleClickable { onClick() }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.write_btn),
                    style = RebrandKoinTheme.typography.medium16,
                    color = RebrandKoinTheme.colors.primary500
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.fab_write_text),
                    contentDescription = stringResource(R.string.write_btn),
                    tint = RebrandKoinTheme.colors.primary500
                )
            }
        }
    }
}
