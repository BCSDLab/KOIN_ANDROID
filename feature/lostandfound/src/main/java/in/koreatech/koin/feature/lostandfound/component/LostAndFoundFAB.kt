package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
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
                    color = KoinTheme.colors.neutral400,
                    shape = RoundedCornerShape(50)
                )
                .background(
                    color = KoinTheme.colors.neutral50,
                    shape = RoundedCornerShape(50)
                )
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .noRippleClickable {onClick()}
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.fab_write_text),
                    contentDescription = stringResource(R.string.write_btn)
                )
                Spacer(
                    modifier = Modifier.width(4.dp)
                )
                Text(
                    text = stringResource(R.string.write_btn),
                    style = KoinTheme.typography.medium16,
                    color = KoinTheme.colors.neutral600
                )
            }
        }
    }
}